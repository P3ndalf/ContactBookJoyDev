package com.example.contactbook.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserProfileModel @Inject constructor(private val userRepository : UserRepository) : ViewModel(){
    suspend fun getUserById(email : String) : User? = withContext(Dispatchers.IO){
        return@withContext userRepository.getUserByEmail(email)
    }
}