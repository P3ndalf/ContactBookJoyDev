package com.example.contactbook.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.repositories.UserRepository
import com.example.contactbook.data.services.InputValidationService
import com.example.contactbook.data.services.abstractions.IInputValidationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val inputValidationService: InputValidationService
) :
    ViewModel() {


    suspend fun isUserExists(email: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext userRepository.isUserExists(email)
    }

    suspend fun addUser(
        firstName: String, lastName: String, email: String,  password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(firstName, lastName, email, password)
        }
    }

    fun checkInputValidation(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmedPassword: String
    ): Array<Boolean> {
        return inputValidationService.registerInputValidation(
            firstName,
            lastName,
            email,
            password,
            confirmedPassword
        )
    }
}