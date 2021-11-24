package com.example.contactbook.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.data.repositories.implementations.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactDetailViewModel  @Inject constructor(
    private val contactRepository: ContactRepository): ViewModel() {

        suspend fun getContact(id : String) : Contact = withContext(Dispatchers.IO) {
            return@withContext contactRepository.getContact(id)
        }
}