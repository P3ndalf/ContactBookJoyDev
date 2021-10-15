package com.example.contactbook.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.contactbook.data.UserDatabase
import com.example.contactbook.data.daos.IUserDao
import com.example.contactbook.data.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

class UserRepository(private val application: Application) {

    private  val userDao = UserDatabase.getDatabase(application).userDao()

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