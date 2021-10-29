package com.example.contactbook.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val users : LiveData<List<User>>

    private val repository : UserRepository

    init {
        repository = UserRepository(application)
        users = repository.users
    }

    fun addUser(user : User){
        viewModelScope.launch(Dispatchers.IO){
            repository.addUser(user)
        }
    }

    fun authenticateUser(email : String, password : String) : LiveData<User> {
        return repository.authenticateUser(email, password)
    }

    fun getUserById(id : String) : LiveData<User>{
        return repository.getUserById(id)
    }
}