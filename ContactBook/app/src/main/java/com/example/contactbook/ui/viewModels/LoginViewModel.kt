package com.example.contactbook.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.repositories.implementations.UserRepository
import com.example.contactbook.data.services.InputValidationService
import com.example.contactbook.data.services.abstractions.IInputValidationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


    private val inputValidationService: IInputValidationService = InputValidationService()

    suspend fun authenticateUser(email: String, password: String): User? =
        withContext(Dispatchers.IO) {
            userRepository.authenticateUser(email, password)
        }

    fun checkInputValidation(
        email: String,
        password: String,
    ): Array<Boolean> {
        return inputValidationService.loginInputValidation(
            email,
            password,
        )
    }
}