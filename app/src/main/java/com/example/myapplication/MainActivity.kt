package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

    navController = findNavController(R.id.navHostFragment)
    val appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.concernsFragment,
        R.id.postedFragment,
        R.id.profileFragment,
        R.id.adviserFragment,
      )
    )

    bottomNavigationView.setupWithNavController(navController)
    setupActionBarWithNavController(navController, appBarConfiguration)

  }

  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp() || super.onSupportNavigateUp()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.idLogout -> {
        val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.remove("IS_LOGIN")
        editor.apply()
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }
}