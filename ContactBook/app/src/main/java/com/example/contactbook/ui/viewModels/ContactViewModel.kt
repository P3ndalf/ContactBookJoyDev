package com.example.contactbook.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.data.repositories.implementations.ContactRepository
import com.example.contactbook.data.services.InputValidationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val inputValidationService: InputValidationService
) : ViewModel() {

    val name = MutableLiveData("")

    val phoneNumber = MutableLiveData("")

    val birthday = MutableLiveData("")

    val gender = MutableLiveData("")

    val instagram = MutableLiveData("")

    val picturePath = MutableLiveData("")

    suspend fun addContact(
        name: String,
        phoneNumber: String,
        birthday: Long,
        gender: String,
        instagram: String,
        picturePath: String?,
        ownerId: String
    ): Boolean = withContext(Dispatchers.IO) {
        contactRepository.addContact(
            name,
            phoneNumber,
            birthday,
            gender,
            instagram,
            picturePath,
            ownerId
        )
    }

    fun getContact(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val contact =
                contactRepository.findContact(id) ?: throw Exception("Contact null exception")
            setData(
                contact.contactName,
                contact.phoneNumber,
                contact.birthday,
                contact.gender,
                contact.instagram,
                contact.picturePath
            )
        }
    }

    fun checkInputValidation(name: String, phone: String): Array<Boolean> {
        return inputValidationService.addContactInputValidation(name, phone)
    }

    fun deleteContact(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            contactRepository.deleteContact(id)
        }
    }

    fun setData(
        _name: String,
        _phoneNumber: String,
        _birthday: Long,
        _gender: String,
        _instagram: String,
        _picturePath: String?,
    ) {
        name.value = _name
        phoneNumber.value = _phoneNumber
        birthday.value = _birthday.toString()
        gender.value = _gender
        instagram.value = _instagram
        picturePath.value = _picturePath
    }
}