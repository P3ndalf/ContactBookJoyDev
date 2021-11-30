package com.example.contactbook.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.repositories.implementations.UserRepository
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

    suspend fun addUser(
        userId: String, firstName: String, lastName: String, email: String, password: String
    ) : Boolean = withContext(Dispatchers.IO) {
        userRepository.addUser(userId, firstName, lastName, email, password)
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