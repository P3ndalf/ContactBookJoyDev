package com.example.contactbook.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.contactbook.data.entities.User

@Dao
interface IUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user : User)

    @Query("SELECT * FROM  userTable ORDER BY id ASC")
    fun getUsers() : LiveData<List<User>>

    @Query("SELECT * FROM userTable WHERE email = :email AND password = :password ")
    fun authenticateUser(email : String, password : String) : LiveData<User>
}