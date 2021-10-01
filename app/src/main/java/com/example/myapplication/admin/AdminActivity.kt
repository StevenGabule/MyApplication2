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
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {
  private lateinit var adminNavController: NavController
  private lateinit var adminBottomNavigationView: BottomNavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_admin)

    adminBottomNavigationView = findViewById(R.id.adminBottomNavigationView)
    adminNavController = findNavController(R.id.navAdminHostFragment)

    val appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.adminHomeFragment,
        R.id.adminAdvisersFragment,
        R.id.adminClientFragment,
        R.id.adminSubscribersFragment,
      )
    )

    adminBottomNavigationView.setupWithNavController(adminNavController)
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

      R.id.idDashboard -> {
        val i = Intent(this, DashboardActivity::class.java)
        startActivity(i)
      }
    }
    return super.onOptionsItemSelected(item)
  }
}