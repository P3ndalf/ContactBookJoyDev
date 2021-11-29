package com.example.contactbook.ui.views.mainscreens.contacts

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.databinding.FragmentContactsBinding
import com.example.contactbook.ui.viewModels.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var authorisedSharedPreferencesService: IAuthorisedSharedPreferencesService

    private val mContactViewModel: ContactsViewModel by viewModels()

    private lateinit var contactsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)

        contactsRecyclerView = binding.contactListRecyclerView

        val adapter = ContactsAdapter { contact ->
            run {
                val action =
                    ContactsFragmentDirections.actionContactsFragmentToContactDetailFragment(contact.id)
                findNavController().navigate(action)
            }

        }

        contactsRecyclerView.adapter = adapter
        contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        authorisedSharedPreferencesService = AuthorisedSharedPreferencesService(
            requireActivity().getSharedPreferences(
                "AuthorizedUser",
                Context.MODE_PRIVATE
            )
        )

        binding.addContactBtn.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }

        mContactViewModel.getContacts(authorisedSharedPreferencesService.loadCurrentUser().id)
            .observe(viewLifecycleOwner, { contacts ->
                adapter.setData(contacts)
            })
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_contacts_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            deleteContacts()
        } else if (item.itemId == R.id.addExternalContactBtn) {
            addExternalContact()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addExternalContact() {
        if (checkContactPermission()) {
            getExternalContact()
        } else {
            requestContactPermission()
        }
    }

    private fun deleteContacts() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes") { _, _ ->
            mContactViewModel.deleteContacts(authorisedSharedPreferencesService.loadCurrentUser().id)
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete all your contacts?")
        builder.show()
    }

    private val CONTACT_PERMISSION_CODE = 1
    private val CONTACT_PICK_CODE = 2

    private fun getExternalContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PICK_CODE)
    }

    private fun checkContactPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactPermission() {
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
        ActivityCompat.requestPermissions(requireActivity(), permission, CONTACT_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getExternalContact()
            } else {
                Toast.makeText(requireContext(), "Denied...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == CONTACT_PICK_CODE) {
                val cursor: Cursor
                val cursor2: Cursor?

                val uri = data!!.data

                cursor = requireActivity().contentResolver.query(uri!!, null, null, null, null)!!
                if (cursor.moveToFirst()) {
                    val contactId =
                        cursor.getString((cursor.getColumnIndex(ContactsContract.Contacts._ID)))
                    val contactName =
                        cursor.getString((cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                    val isPhoneExist =
                        cursor.getString((cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
                    var contactPhone: String = ""

                    if (isPhoneExist.toInt() == 1) {
                        cursor2 = requireActivity().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                            null,
                            null
                        )
                        if(cursor2!!.moveToFirst()) {
                            contactPhone =
                                cursor2.getString((cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
                            mContactViewModel.addExternalContact(
                                contactName,
                                contactPhone,
                                authorisedSharedPreferencesService.loadCurrentUser().id
                            )
                        }
                        cursor2.close()
                    }
                    cursor.close()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()
        }
    }
}