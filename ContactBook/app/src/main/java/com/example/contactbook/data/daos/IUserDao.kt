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

    @Query("SELECT * FROM  userTable")
    fun getUsers() : LiveData<List<User>>

    @Query("SELECT * FROM userTable WHERE email = :email AND passwordHash = :passwordHash ")
    fun authenticateUser(email : String, passwordHash : String) : LiveData<User>

    @Query("SELECT * FROM userTable WHERE id = :id")
    fun getUserById(id : String) : LiveData<User>
}