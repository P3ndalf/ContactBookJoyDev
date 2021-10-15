package com.example.contactbook.data.services

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.contactbook.data.entities.User

class SharedPreferencesService(currentContext : Context, sharedPreferencesName : String) {

    private val currentContext : Context
    private val sharedPreferencesName : String

    init{
        this.currentContext = currentContext
        this.sharedPreferencesName = sharedPreferencesName
    }

    fun saveCurrentUserData(user : User?){
        var sharedPreferences = currentContext.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        editor.apply{
            putString("Inserted id", user?.id)
        }.apply()

        Toast.makeText(currentContext, "Put", Toast.LENGTH_LONG).show()
    }

    fun loadCurrentUserId() : String {
        var sharedPreferences = currentContext.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        var savedString = sharedPreferences.getString("Inserted id", "")

        return if (savedString == null) "" else savedString
    }

    fun isAuthorized() : Boolean {
        var sharedPreferences = currentContext.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.getString("Inserted id", "") != ""
    }

}