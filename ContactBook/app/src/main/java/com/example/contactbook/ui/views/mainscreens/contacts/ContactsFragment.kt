package com.example.contactbook.ui.views.mainscreens.contacts

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.databinding.FragmentContactsBinding
import com.example.contactbook.ui.viewModels.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var authorisedSharedPreferencesService: IAuthorisedSharedPreferencesService

    private val mContactViewModel: ContactViewModel by viewModels()

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
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            deleteContacts()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteContacts() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes") { _, _ ->
            mContactViewModel.deleteContacts(authorisedSharedPreferencesService.loadCurrentUser().id)
            findNavController().navigate(R.id.action_contactDetailFragment_to_contactsFragment)
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete all your contacts?")
    }
}