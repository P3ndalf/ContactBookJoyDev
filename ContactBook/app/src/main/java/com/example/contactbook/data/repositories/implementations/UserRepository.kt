package com.example.contactbook.data.repositories.implementations

import com.example.contactbook.data.daos.UserDao
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.abstractions.IHashService
import java.util.*

class UserRepository(private val userDao: UserDao, private val hashService: IHashService) {

    suspend fun addUser(
        id: String, firstName: String, lastName: String, email: String, password: String
    ): Boolean {
        return if (isUserExists(email) != null) {
            false
        } else {
            val passwordHash = hashService.getHash(password, "SHA-256")
            val user = User(id, firstName, lastName, email, passwordHash)
            userDao.addUser(user)
            true
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    fun authenticateUser(email: String, password: String): User? {
        return userDao.authenticateUser(email, hashService.getHash(password, "SHA-256"))
    }

    private suspend fun isUserExists(email: String): User? {
        return userDao.isUserExists(email)
    }
}