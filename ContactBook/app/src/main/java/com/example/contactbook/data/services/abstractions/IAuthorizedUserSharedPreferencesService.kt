package com.example.contactbook.data.services.abstractions

import com.example.contactbook.data.entities.User

interface IAuthorizedUserSharedPreferencesService {
    fun saveCurrentUserData(user: User)

    fun deleteCurrentUserData()

    fun loadCurrentUser(): User

    fun isAuthorized(): Boolean
}