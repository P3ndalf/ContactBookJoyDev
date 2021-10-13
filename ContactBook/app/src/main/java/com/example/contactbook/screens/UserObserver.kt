package com.example.contactbook.screens

import android.widget.Adapter
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.contactbook.data.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class UserObserver{

    public var user : User? = null

    public fun setData(user : User){
        this.user = user
    }

}