package com.example.contactbook.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.repositories.implementations.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserProfileModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    suspend fun getUserById(email: String): User? = withContext(Dispatchers.IO) {
        return@withContext userRepository.getUserByEmail(email)
    }
}