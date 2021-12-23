package com.example.contactbook.ui.views.mainscreens.contacts.add

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.databinding.FragmentAddContactBinding
import com.example.contactbook.ui.viewModels.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.util.*

@AndroidEntryPoint
class AddContactFragment : Fragment() {
    private lateinit var binding: FragmentAddContactBinding
    private lateinit var authorisedSharedPreferencesService: IAuthorisedSharedPreferencesService

    private val mContactViewModel: ContactViewModel by viewModels()

    private var inputValidationFlags: Array<Boolean> = Array(2) { true }

    private var entityImagePath: String? = null
    private val EXTERNAL_STORAGE_PERMISSION = 1
    private val PHOTO_PICKER_CODE = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddContactBinding.inflate(inflater, container, false)

        with(binding) {
            cancelBtn.setOnClickListener {
                findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
            }
            authorisedSharedPreferencesService = AuthorisedSharedPreferencesService(
                requireActivity().getSharedPreferences(
                    "AuthorizedUser",
                    Context.MODE_PRIVATE
                )
            )
            changeAvatar.setOnClickListener {
                if (checkReadGalleryPermission()) {
                    choosePhoto()
                } else {
                    requestReadStoragePermission()
                }
            }

            addBtn.setOnClickListener {
                addContact()
            }
        }
        return binding.root
    }

    private fun addContact() {
        var ownerId = authorisedSharedPreferencesService.loadCurrentUser().id

        with(binding) {
            inputValidationFlags = mContactViewModel.checkInputValidation(
                nameET.text.toString(), phoneNumberET.text.toString()
            )
            if (!inputValidationFlags.contains(false)) {
                lifecycleScope.launch(Dispatchers.Main) {
                    if (mContactViewModel.addContact(
                            nameET.text.toString(),
                            phoneNumberET.text.toString(),
                            Calendar.getInstance().timeInMillis,
                            getGender(),
                            instagramET.text.toString(),
                            entityImagePath,
                            ownerId
                        )
                    ) {
                        findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
                    } else {
                        inputValidationFlags = Array(inputValidationFlags.size) { false }
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.ContactExistsToast),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                changeLayoutValidity()
            }
        }
    }

    private fun changeLayoutValidity() {
        with(binding) {
            if (!inputValidationFlags[0]) {
                nameError.isErrorEnabled = true
                nameError.error = "Enter correct name"
            } else {
                nameError.isErrorEnabled = false
                nameError.error = null
            }
            if (!inputValidationFlags[1]) {
                phoneNumberError.isErrorEnabled = true
                phoneNumberError.error = "Enter correct phone number"
            } else {
                phoneNumberError.isErrorEnabled = false
                phoneNumberError.error = null
            }
            if (!inputValidationFlags[2]) {
                instagramError.isErrorEnabled = true
                instagramError.error = "Enter correct instagram id  "
            } else {
                instagramError.isErrorEnabled = false
                instagramError.error = null
            }
        }
    }

    private suspend fun getGender(): String {
        var gender = when (binding.genderRG.checkedRadioButtonId) {
            binding.maleRB.id -> {
                getString(R.string.male)
            }
            binding.femaleRB.id -> {
                getString(R.string.female)
            }
            else -> "Others"
        }
        return gender
    }

    private fun choosePhoto() {
        val builder = AlertDialog.Builder(requireContext())
        val options: Array<String> =
            arrayOf(getString(R.string.chooseImage), getString(R.string.deny))

        builder.setTitle(getString(R.string.chooseImage))

        builder.setItems(options) { dialog, item ->
            if (options[item].equals(getString(R.string.chooseImage))) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            } else if (options[item].equals(getString(R.string.deny))) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun requestReadStoragePermission() {
        val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(
            requireActivity(),
            permission,
            EXTERNAL_STORAGE_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto()
            } else {
                Toast.makeText(requireContext(), getString(R.string.denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PHOTO_PICKER_CODE) {
                val selectedImage: Uri = data?.data!!
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor =
                    requireActivity().contentResolver.query(
                        selectedImage,
                        filePath,
                        null,
                        null,
                        null
                    )
                        ?: throw NullPointerException("Cursor is null")

                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePath[0])
                val picturePath = cursor.getString(columnIndex)

                cursor.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                entityImagePath = picturePath
                binding.imageView.setImageBitmap(thumbnail)
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.addExternalContactCanceledToast),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkReadGalleryPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

}