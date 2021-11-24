package com.example.contactbook.ui.views.mainscreens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.databinding.FragmentContactDetailBinding
import com.example.contactbook.ui.viewModels.ContactDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactDetailFragment : Fragment() {
    private lateinit var binding: FragmentContactDetailBinding
    private val args : ContactDetailFragmentArgs by navArgs()

    private val mContactDetailViewModel : ContactDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        lifecycleScope.launch(Dispatchers.Main){
            val contact = mContactDetailViewModel.getContact(args.transferContactId)
            with(binding){
                contactNameTV.text = contact.contactName
                phoneNumberTV.text = contact.phoneNumber
                instagramTV.text = contact.instagram
                genderTV.text = contact.gender
            }
        }

        return binding.root
    }


}