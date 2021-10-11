package com.example.contactbook.data.services

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.contactbook.data.entities.User

class SharedPreferencesService(fragment : Fragment, sharedPreferencesName : String) {

    private val fragment : Fragment
    private val sharedPreferencesName : String
    init{
        this.fragment = fragment
        this.sharedPreferencesName = sharedPreferencesName
    }

    public fun saveCurrentUserData(user : User?){
        var sharedPreferences = fragment.requireActivity().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        editor.apply{
            putString("Inserted id", user?.id)
        }.apply()

        Toast.makeText(fragment.requireContext(), "Put", Toast.LENGTH_LONG).show()
    }

    public fun loadCurrentUserId() : String {
        var sharedPreferences = fragment.requireActivity().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        var savedString = sharedPreferences.getString("Inserted id", "")

        return if (savedString == null) "" else savedString
    }

}