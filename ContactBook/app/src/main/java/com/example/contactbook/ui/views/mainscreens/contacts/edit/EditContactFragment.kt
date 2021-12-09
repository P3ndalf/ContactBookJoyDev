package com.example.contactbook.ui.views.mainscreens.contacts.edit

import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contactbook.R
import com.example.contactbook.databinding.FragmentEditContactBinding

import com.example.contactbook.ui.viewModels.ContactViewModel
import com.example.contactbook.ui.views.mainscreens.contacts.detail.ContactDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.content.Intent

import android.provider.MediaStore

import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract
import android.widget.Toast
import java.io.File
import android.graphics.BitmapFactory

import android.graphics.Bitmap

import android.R.attr.data
import java.lang.NullPointerException


@AndroidEntryPoint
class EditContactFragment : Fragment() {
    private lateinit var binding: FragmentEditContactBinding
    private val args: ContactDetailFragmentArgs by navArgs()

    private val mContactViewModel: ContactViewModel by viewModels()

    private var inputValidationFlags: Array<Boolean> = Array(2) { true }

    private var editImagePath : String? = null

    private val EXTERNAL_STORAGE_PERMISSION = 1
    private val PHOTO_PICKER_CODE = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditContactBinding.inflate(inflater, container, false)

        binding.changeAvatar.setOnClickListener {
            if (checkReadGalleryPermission()) {
                choosePhoto()
            } else {
                requestReadStoragePermission()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            val contact = mContactViewModel.getContact(args.transferContactId)
            with(binding) {
                contactNameTV.setText(contact.contactName)
                phoneNumberTV.setText(contact.phoneNumber)
                instagramTV.setText(contact.instagram)
                when(contact.gender){
                    getString(R.string.male) -> maleRB.isChecked = true
                    getString(R.string.female) -> femaleRB.isChecked = true
                    getString(R.string.others) -> othersRB.isChecked = true
                }
                if(contact.picturePath != null){
                    imageView.setImageBitmap(BitmapFactory.decodeFile(contact.picturePath))
                }
            }
        }

        binding.editBtn.setOnClickListener {
            editContact()
        }

        binding.cancelBtn.setOnClickListener {
            val action =
                EditContactFragmentDirections.actionEditContactFragmentToContactDetailFragment(args.transferContactId)
            findNavController().navigate(action)
        }
        return binding.root
    }

    private fun choosePhoto() {
        val builder = AlertDialog.Builder(requireContext())
        val options: Array<String> = arrayOf(getString(R.string.chooseImage), getString(R.string.deny))

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
                Toast.makeText(requireContext(), getString(R.string.denied), Toast.LENGTH_SHORT).show()
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
                    requireActivity().contentResolver.query(selectedImage, filePath, null, null, null)
                        ?: throw NullPointerException("Cursor is null")
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePath[0])
                val picturePath = cursor.getString(columnIndex)
                editImagePath = picturePath
                cursor.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
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

    private fun changeLayoutValidity() {
        with(binding) {
            if (!inputValidationFlags[0]) {
                nameError.isErrorEnabled = true
                nameError.error = getString(R.string.contactNameError)
            } else {
                nameError.isErrorEnabled = false
                nameError.error = null
            }
            if (!inputValidationFlags[1]) {
                phoneNumberError.isErrorEnabled = true
                phoneNumberError.error = getString(R.string.contactPhoneNumberError)
            } else {
                phoneNumberError.isErrorEnabled = false
                phoneNumberError.error = null
            }
        }
    }

    private fun editContact() {
        with(binding) {
            inputValidationFlags = mContactViewModel.checkInputValidation(
                contactNameTV.text.toString(),
                phoneNumberTV.text.toString()
            )

            lifecycleScope.launch(Dispatchers.Main) {
                val contact = mContactViewModel.getContact(args.transferContactId)
                contact.contactName = contactNameTV.text.toString()
                contact.instagram = instagramTV.text.toString()
                contact.phoneNumber = phoneNumberTV.text.toString()
                contact.gender = getGender()
                contact.picturePath = editImagePath
                if (!inputValidationFlags.contains(false)) {
                    mContactViewModel.editContact(
                        contact
                    )
                    val action =
                        EditContactFragmentDirections.actionEditContactFragmentToContactDetailFragment(
                            args.transferContactId
                        )
                    findNavController().navigate(action)
                } else {
                    changeLayoutValidity()
                }
            }
        }
    }
}