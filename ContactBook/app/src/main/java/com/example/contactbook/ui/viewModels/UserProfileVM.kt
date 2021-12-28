package com.example.contactbook.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contactbook.data.repositories.implementations.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileVM @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {

    val name = MutableLiveData("")

    val lastName = MutableLiveData("")

    val email = MutableLiveData("")

    fun setData(_name: String, _lastName:String, _email:String){
        name.value = _name
        lastName.value = _lastName
        email.value = _email
    }
}