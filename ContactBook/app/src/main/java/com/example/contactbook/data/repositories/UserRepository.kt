package com.example.contactbook.data.repositories

import com.example.contactbook.data.daos.UserDao
import com.example.contactbook.data.entities.User
import java.util.*

class UserRepository(private val userDao: UserDao) {

    suspend fun addUser(
        firstName: String, lastName: String, email: String, password: String
    ) {
        val id = UUID.randomUUID().toString()
        val passwordHash = password
        val user = User(id,  firstName, lastName, email, passwordHash)
        userDao.addUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun isUserExists(email: String): Boolean {
        return userDao.isUserExists(email)
    }
}