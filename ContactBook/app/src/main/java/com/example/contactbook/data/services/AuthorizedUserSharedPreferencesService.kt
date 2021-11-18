package com.example.contactbook.data.services

import android.content.SharedPreferences
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.abstractions.IAuthorizedUserSharedPreferencesService

class AuthorizedUserSharedPreferencesService(private var sharedPreferences: SharedPreferences) :
    IAuthorizedUserSharedPreferencesService {


    override fun saveCurrentUserData(user: User) {
        var editor = sharedPreferences.edit()

        editor.apply {
            putString("AuthorizedUser id", user.id)
            putString("AuthorizedUser firstName", user.firstName)
            putString("AuthorizedUser lastname", user.lastName)
            putString("AuthorizedUser email", user.email)
        }.apply()
    }

    override fun deleteCurrentUserData() {
        var editor = sharedPreferences.edit()

        editor.apply {
            clear()
        }.apply()
    }

    override fun loadCurrentUser(): User {
        val userId = sharedPreferences.getString("AuthorizedUser id", "").toString()
        val userName = sharedPreferences.getString("AuthorizedUser firstName", "").toString()
        val userLastName = sharedPreferences.getString("AuthorizedUser lastname", "").toString()
        val userEmail = sharedPreferences.getString("AuthorizedUser email", "").toString()

        return User(userId, userName, userLastName, userEmail, "")
    }

    override fun isAuthorized(): Boolean {
        return sharedPreferences.getString("AuthorizedUser email", "") != ""
    }
}