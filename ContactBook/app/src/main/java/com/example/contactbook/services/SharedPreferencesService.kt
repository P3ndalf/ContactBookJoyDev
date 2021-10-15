package com.example.contactbook.services

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.contactbook.data.entities.User

class SharedPreferencesService(currentContext : Context, sharedPreferencesName : String) {

    private val currentContext : Context
    private val sharedPreferencesName : String
    var sharedPreferences : SharedPreferences

    init{
        this.currentContext = currentContext
        this.sharedPreferencesName = sharedPreferencesName
        this.sharedPreferences = currentContext.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    fun saveCurrentUserData(user : User?){
        var editor = sharedPreferences.edit()

        editor.apply{
            putString("Inserted id", user?.id)
        }.apply()

        Toast.makeText(currentContext, "Put", Toast.LENGTH_LONG).show()
    }

    fun deleteCurrentUserData(){
        var editor = sharedPreferences.edit()

        editor.apply{
            clear()
        }.apply()
    }

    fun loadCurrentUserId() : String {
        var savedString = sharedPreferences.getString("Inserted id", "")

        return if (savedString == null) "" else savedString
    }

    fun isAuthorized() : Boolean {
        return sharedPreferences.getString("Inserted id", "") != ""
    }

}