package com.example.contactbook.di

import android.content.Context
import androidx.room.Room
import com.example.contactbook.data.ApplicationDatabase
import com.example.contactbook.data.daos.ContactDao
import com.example.contactbook.data.daos.UserDao
import com.example.contactbook.data.repositories.implementations.ContactRepository
import com.example.contactbook.data.repositories.implementations.UserRepository
import com.example.contactbook.data.services.HashService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class Startup {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ApplicationDatabase::class.java,
        "applicationDatabase"
    ).build()

    @Provides
    @Singleton
    fun provideUserDao(database: ApplicationDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideContactDao(database: ApplicationDatabase) = database.contactDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, hashService : HashService) =
        UserRepository(userDao, hashService)

    @Provides
    @Singleton
    fun provideContactRepository(contactDao: ContactDao) = ContactRepository(contactDao)
}