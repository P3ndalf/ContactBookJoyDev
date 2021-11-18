package com.example.contactbook.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.contactbook.data.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM userTable WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("Select 1 FROM userTable WHERE EXISTS (SELECT * FROM userTable WHERE email = :email )")
    suspend fun isUserExists(email: String): Boolean
}