package com.example.contactbook.services

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.contactbook.data.Model.UserModel
import com.example.contactbook.data.entities.User
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService

class AuthorizedUserSharedPreferencesService(private val currentContext : Context) : IAuthorizedUserSharedPreferencesService{

    private val sharedPreferencesName : String = "AuthorizedUser"
    var sharedPreferences : SharedPreferences = currentContext.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    override fun saveCurrentUserData(user : UserModel){
        var editor = sharedPreferences.edit()

        editor.apply{
            putString("AuthorizedUser id", user.id)
            putString("AuthorizedUser firstName", user.firstName)
            putString("AuthorizedUser lastname", user.lastName)
            putString("AuthorizedUser email", user.email)
        }.apply()
    }

    override fun deleteCurrentUserData(){
        var editor = sharedPreferences.edit()

        editor.apply{
            clear()
        }.apply()
    }

    override fun loadCurrentUser() : UserModel {
        var userId = sharedPreferences.getString("AuthorizedUser id", "").toString()
        var userName = sharedPreferences.getString("AuthorizedUser firstName", "").toString()
        var userLastName = sharedPreferences.getString("AuthorizedUser lastname", "").toString()
        var userEmail = sharedPreferences.getString("AuthorizedUser email", "").toString()

        return UserModel(userId,userName,userLastName,userEmail)
    }

    override fun isAuthorized() : Boolean {
        return sharedPreferences.getString("AuthorizedUser id", "") != ""
    }

}