package com.example.contactbook.data.repositories

import androidx.lifecycle.LiveData
import com.example.contactbook.data.daos.IUserDao
import com.example.contactbook.data.entities.User

class UserRepository(private val userDao : IUserDao) {

    suspend fun addUser(user : User){
        userDao.addUser(user)
    }

    fun authenticateUser(email : String, password : String) : LiveData<User>{
        return userDao.authenticateUser(email, password)
    }


    val getUsers : LiveData<List<User>> = userDao.getUsers()
}