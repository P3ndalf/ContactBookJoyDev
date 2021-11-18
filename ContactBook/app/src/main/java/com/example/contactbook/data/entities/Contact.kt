package com.example.contactbook.data.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "contactsTable",
    foreignKeys = [ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("ownerId"),
            onDelete = CASCADE
    )]
)
data class Contact (
    @PrimaryKey
    @NonNull
    var id : String,

    var contactName : String,

    var phoneNumber : String,

    var birthday : Long,

    var gender : Boolean,

    var instagram : String,

    var ownerId : String
)