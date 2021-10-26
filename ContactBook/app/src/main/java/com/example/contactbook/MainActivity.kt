package com.example.contactbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationBarView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fragmentContainerView = supportFragmentManager.findFragmentById(R.id.fragmentMainContainerView) as NavHostFragment
        val navigationController = fragmentContainerView.navController
        val appBarConfig = AppBarConfiguration(setOf(R.id.settingsFragment,R.id.contactsFragment,R.id.userProfileFragment))
        setupActionBarWithNavController(navigationController,appBarConfig)
        bottomNavigationBarView.setupWithNavController(navigationController)

    }
}