package com.example.myapplication.admin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.LoginActivity
import com.example.myapplication.R

class AdminActivity : AppCompatActivity() {
  private lateinit var adminNavController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_admin)

    adminNavController = findNavController(R.id.navAdminHostFragment)
    val appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.adminConcernsFragment,
        R.id.adminAdvisersFragment,
        R.id.adminClientFragment,
        R.id.adminSubscribersFragment,
      )
    )

    setupActionBarWithNavController(adminNavController, appBarConfiguration)
  }

  override fun onSupportNavigateUp(): Boolean {
    return adminNavController.navigateUp() || super.onSupportNavigateUp()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu_admin, menu)
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