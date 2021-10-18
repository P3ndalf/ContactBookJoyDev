package com.example.contactbook.data.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
data class User (
    @PrimaryKey
    @NonNull
    val id : String,

    val firstName : String,

    val lastName : String,

    @NonNull
    val email : String,

    @NonNull
    val passwordHash : String,
)