package com.example.contactbook.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.contactbook.data.entities.Contact

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addContact(contact : Contact)

    @Query("SELECT * FROM contactsTable WHERE ownerId = :ownerId")
    fun getContacts(ownerId : String) : LiveData<List<Contact>>

    @Query("SELECT * FROM contactsTable WHERE id = :id")
    fun getContact(id : String) : Contact
}