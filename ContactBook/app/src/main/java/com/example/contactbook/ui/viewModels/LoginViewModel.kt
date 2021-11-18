package com.example.contactbook.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository : UserRepository) : ViewModel() {

    suspend fun getUserByEmail(email : String) : User? = withContext(Dispatchers.IO){
        return@withContext userRepository.getUserByEmail(email)
    }
}