package com.example.contactbook.services.abstractions

import com.example.contactbook.data.Model.UserModel

interface IAuthorizedUserSharedPreferencesService {

    fun saveCurrentUserData(user : UserModel)

    fun deleteCurrentUserData()

    fun loadCurrentUser() : UserModel

    fun isAuthorized() : Boolean
}