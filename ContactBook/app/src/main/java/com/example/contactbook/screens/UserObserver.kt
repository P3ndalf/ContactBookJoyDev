package com.example.contactbook.screens

import com.example.contactbook.data.entities.User

class UserObserver {
    var user : User? = null

    public fun setData(user : User){
        this.user = user
    }

}