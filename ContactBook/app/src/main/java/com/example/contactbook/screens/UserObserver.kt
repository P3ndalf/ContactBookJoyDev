package com.example.contactbook.screens

import android.widget.Adapter
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.contactbook.data.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class UserObserver {

    public var user : User? = null

    public suspend fun setData(_user : User?) {
        withContext(Dispatchers.IO){
            user = _user
        }
    }
}