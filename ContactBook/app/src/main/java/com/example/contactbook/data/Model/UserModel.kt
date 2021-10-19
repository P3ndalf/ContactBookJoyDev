package com.example.contactbook.data.Model

import com.example.contactbook.data.entities.User

class UserModel(var id : String, var firstName : String, var lastName : String, var email : String) {

     fun setData(_user : User) {
            id = _user.id
            firstName = _user.firstName
            lastName = _user.lastName
            email = _user.email
     }

    fun isEmpty() : Boolean {
        if(id == null) return false
        return true
    }
}