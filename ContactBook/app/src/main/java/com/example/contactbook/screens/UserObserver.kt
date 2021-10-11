package com.example.contactbook.screens

import android.widget.Adapter
import com.example.contactbook.data.entities.User

class UserObserver :  {

    public var user : User? = null

    public fun setData(user : User){
        this.user = user
    }

}