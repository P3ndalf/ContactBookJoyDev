package com.example.contactbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contactbook.data.daos.IUserDao
import com.example.contactbook.data.entities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {

    abstract  fun userDao() : IUserDao
    companion object{
        @Volatile
        private var INSTANCE:UserDatabase? = null

        fun getDatabase(context : Context) : UserDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return  tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "userDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}