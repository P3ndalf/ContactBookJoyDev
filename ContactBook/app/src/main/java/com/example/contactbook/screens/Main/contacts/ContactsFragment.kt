package com.example.contactbook.screens.Main.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.data.viewModels.ContactViewModel
import com.example.contactbook.databinding.FragmentContactsBinding
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var authorizedUserSharedPreferencesService: IAuthorizedUserSharedPreferencesService


    private lateinit var mContactViewModel : ContactViewModel
    private lateinit var adapter: ContactsAdapter
    private lateinit var contactsRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)

        mContactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactsRecyclerView = binding.contactListRecyclerView
        adapter = ContactsAdapter()

        contactsRecyclerView.adapter = adapter
        contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity())

        binding.addContactBtn.setOnClickListener{
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }

        mContactViewModel.getContacts(authorizedUserSharedPreferencesService.loadCurrentUser().id).
            observe(viewLifecycleOwner, Observer{ contacts ->
                adapter.setData(contacts)
            })
        return binding.root
    }

}