package com.example.contactbook.services

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.contactbook.data.Model.UserModel
import com.example.contactbook.data.entities.User

class AuthorizedUserSharedPreferencesService(currentContext : Context, sharedPreferencesName : String) {

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
            putString("AuthorizedUser id", user?.id)
            putString("AuthorizedUser name", user?.firstName)
            putString("AuthorizedUser lastname", user?.lastName)
            putString("AuthorizedUser email", user?.email)
        }.apply()

        Toast.makeText(currentContext, "Put", Toast.LENGTH_LONG).show()
    }

    fun deleteCurrentUserData(){
        var editor = sharedPreferences.edit()

        editor.apply{
            clear()
        }.apply()
    }

    fun loadCurrentUser() : UserModel {
        var userId = sharedPreferences.getString("AuthorizedUser id", "").toString()
        var userName = sharedPreferences.getString("AuthorizedUser name", "").toString()
        var userLastName = sharedPreferences.getString("AuthorizedUser lastname", "").toString()
        var userEmail = sharedPreferences.getString("AuthorizedUser email", "").toString()

        return UserModel(userId,userName,userLastName,userEmail)
    }

    fun isAuthorized() : Boolean {
        return sharedPreferences.getString("AuthorizedUser id", "") != ""
    }

}