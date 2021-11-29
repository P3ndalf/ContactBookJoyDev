package com.example.contactbook.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.contactbook.data.entities.Contact

@Dao
interface  ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addContact(contact : Contact)

    @Query("SELECT * FROM contactsTable WHERE ownerId = :ownerId")
    fun getContacts(ownerId : String) : LiveData<List<Contact>>

    @Query("SELECT * FROM contactsTable WHERE id = :id")
    fun getContact(id : String) : Contact


    @Query("DELETE FROM contactsTable WHERE ownerId = :ownerId")
    fun deleteContacts(ownerId : String)

    @Query("DELETE FROM contactsTable WHERE id = :id")
    fun deleteContact(id : String)

    @Update
    suspend fun editContact(contact: Contact)

}