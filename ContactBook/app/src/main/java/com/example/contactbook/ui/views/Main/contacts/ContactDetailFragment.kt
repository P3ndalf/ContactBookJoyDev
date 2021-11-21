package com.example.contactbook.ui.views.Main.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.contactbook.databinding.FragmentContactDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactDetailFragment : Fragment() {
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)

        return binding.root
    }


}