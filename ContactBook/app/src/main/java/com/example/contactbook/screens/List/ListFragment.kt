package com.example.contactbook.screens.List

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.databinding.FragmentListBinding

class ListFragment: Fragment() {
    private lateinit var mUserViewModel : UserViewModel

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = FragmentListBinding.inflate(inflater, container, false)

        val adapter = ListAdapter()
        val recyclerView = binding.recycleview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mUserViewModel.users.observe(viewLifecycleOwner, Observer{ users ->
            adapter.setData(users)
        })
        return binding.root
    }

}