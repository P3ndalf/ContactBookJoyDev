package com.example.contactbook.ui.views.mainscreens.contacts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.ui.viewModels.ContactViewModel
import com.example.contactbook.databinding.FragmentContactsBinding
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.ui.views.mainscreens.contacts.ContactsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {
    private lateinit var binding : FragmentContactsBinding
    private lateinit var authorisedSharedPreferencesService: IAuthorisedSharedPreferencesService

    private val mContactViewModel : ContactViewModel  by viewModels()

    private lateinit var contactsRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)

        contactsRecyclerView = binding.contactListRecyclerView

        val adapter = ContactsAdapter()

        contactsRecyclerView.adapter = adapter
        contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        authorisedSharedPreferencesService = AuthorisedSharedPreferencesService(
            requireActivity().getSharedPreferences(
                "AuthorizedUser",
                Context.MODE_PRIVATE
            )
        )

        binding.addContactBtn.setOnClickListener{
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }

        mContactViewModel.getContacts(authorisedSharedPreferencesService.loadCurrentUser().id).
        observe(viewLifecycleOwner, { contacts ->
            adapter.setData(contacts)
        })

        return binding.root
    }

}