package com.example.contactbook.ui.views.mainscreens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.databinding.FragmentEditContactBinding

class EditContactFragment : Fragment() {
    private var _binding: FragmentEditContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditContactBinding.inflate(inflater, container, false)


        binding.cancelBtn.setOnClickListener{
            findNavController().navigate(R.id.action_editContactFragment_to_contactDetailFragment)
        }
        return binding.root
    }
}