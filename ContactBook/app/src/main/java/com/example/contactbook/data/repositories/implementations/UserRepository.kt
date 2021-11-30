package com.example.contactbook.data.repositories.implementations

import com.example.contactbook.data.daos.UserDao
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.abstractions.IHashService

class UserRepository(private val userDao: UserDao, private val hashService: IHashService) {

    suspend fun addUser(
        id: String, firstName: String, lastName: String, email: String, password: String
    ): Boolean {
        return if (findUser(email) != null) {
            false
        } else {
            val passwordHash = hashService.getSha256Hash(password)
            val user = User(id, firstName, lastName, email, passwordHash)
            userDao.addUser(user)
            true
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    fun authenticateUser(email: String, password: String): User? {
        return userDao.authenticateUser(email, hashService.getSha256Hash(password))
    }

    private suspend fun findUser(email: String): User? {
        return userDao.findUser(email)
    }
}