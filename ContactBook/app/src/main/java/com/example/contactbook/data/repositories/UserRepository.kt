package com.example.contactbook.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.contactbook.data.ApplicationDatabase
import com.example.contactbook.data.entities.User

class UserRepository(private val application: Application) {

    private  val userDao = ApplicationDatabase.getDatabase(application).userDao()

    suspend fun addUser(user : User){
        userDao.addUser(user)
    }

     fun  authenticateUser(email : String, password : String) : LiveData<User>{
        return userDao.authenticateUser(email, password)
    }

    fun getUserById(id : String) : LiveData<User> {
        return userDao.getUserById(id)
    }

    val users : LiveData<List<User>> = userDao.getUsers()
}