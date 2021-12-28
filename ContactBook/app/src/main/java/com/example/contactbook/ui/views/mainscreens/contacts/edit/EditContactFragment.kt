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
import android.widget.Spinner
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.example.contactbook.databinding.FragmentContactDetailBinding
import com.example.contactbook.ui.viewModels.EditContactVM
import java.io.IOException
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class EditContactFragment : Fragment(R.layout.fragment_edit_contact) {
    private lateinit var binding: FragmentEditContactBinding
    private val args: ContactDetailFragmentArgs by navArgs()

    private val viewModel: EditContactVM by viewModels()

    private var inputValidationFlags: Array<Boolean> = Array(2) { true }

    private var editImagePath: String? = null

    private val EXTERNAL_STORAGE_PERMISSION = 1
    private val PHOTO_PICKER_CODE = 2
    private val REQUEST_IMAGE_CAPTURE = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_contact, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        viewModel.setData(args.transferContactId)

        binding.changeAvatar.setOnClickListener {
            if (checkReadGalleryPermission()) {
                choosePhoto()
            } else {
                requestReadStoragePermission()
            }
        }

        viewModel.gender.observe(viewLifecycleOwner, { gender ->
            when (gender) {
                    getString(R.string.male) -> binding.maleRB.isChecked = true
                    getString(R.string.female) -> binding.femaleRB.isChecked = true
                    getString(R.string.others) -> binding.othersRB.isChecked = true
            }
        })

        binding.cancelBtn.setOnClickListener {
            val action =
                EditContactFragmentDirections.actionEditContactFragmentToContactDetailFragment(args.transferContactId)
            findNavController().navigate(action)
        }

        binding.editBtn.setOnClickListener {
            editContact()
        }
    }

    private fun editContact() {
        inputValidationFlags = viewModel.checkInputValidation(
            binding.contactNameTV.text.toString(),
            binding.phoneNumberTV.text.toString()
        )
        if (!inputValidationFlags.contains(false)) {
            with(binding) {
                viewModel.name.value = contactNameTV.text.toString()
                viewModel.phoneNumber.value = phoneNumberTV.text.toString()
                viewModel.instagram.value = instagramTV.text.toString()
                if (editImagePath != null) viewModel.picturePath.value = editImagePath.toString()
                viewModel.gender.value = getGender()
            }
            viewModel.editContact(
                args.transferContactId
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

    private fun choosePhoto() {
        val builder = AlertDialog.Builder(requireContext())
        val options: Array<String> = arrayOf(
            getString(R.string.chooseImage),
            getString(R.string.makeImageNow),
            getString(R.string.deny)
        )

        builder.setTitle(getString(R.string.chooseImage))

        builder.setItems(options) { dialog, item ->
            if (options[item].equals(getString(R.string.chooseImage))) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            } else if (options[item].equals(getString(R.string.deny))) {
                dialog.dismiss()
            } else if (options[item].equals(getString(R.string.makeImageNow))) {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
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
                editImagePath = picturePath
                cursor.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                binding.imageView.setImageBitmap(thumbnail)
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                /*Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ takePictureIntent ->
                    takePictureIntent.resolveActivity(requireContext().packageManager).also{
                        val file = saveImage()
                        file.also {
                            val uri = FileProvider.getUriForFile(
                                requireContext(),
                                "Pictures",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        }
                    }
                }*/
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                    val f = saveImage()
                    mediaScanIntent.data = Uri.fromFile(f)
                    requireActivity().sendBroadcast(mediaScanIntent)
                }
                binding.imageView.setImageBitmap(imageBitmap)
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

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun saveImage(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            editImagePath = absolutePath
        }
    }

    private fun getGender(): String {
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


}