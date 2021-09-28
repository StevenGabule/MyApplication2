package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessaging;

class MainActivity : AppCompatActivity() {
  private lateinit var navController: NavController
  private val TAG = "PushNotification"
  private val CHANNEL_ID = "101"


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    createNotificationChannel()
    getToken()

    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

    navController = findNavController(R.id.navHostFragment)
    val appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.concernsFragment,
        R.id.messageFragment,
        R.id.profileFragment,
        R.id.adviserFragment,
      )
    )

    bottomNavigationView.setupWithNavController(navController)
    setupActionBarWithNavController(navController, appBarConfiguration)

  }

  private fun getToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task -> //If task is failed then
      if (!task.isSuccessful) {
        Log.d(TAG, "onComplete: Failed to get the Token")
      }

      //Token
      val token = task.result
      Log.d(TAG, "onComplete: $token")
    }
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name: CharSequence = "firebaseNotificationChannel"
      val description = "Receive Firebase notification"
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(CHANNEL_ID, name, importance)
      channel.description = description
      val notificationManager = getSystemService(
        NotificationManager::class.java
      )
      notificationManager.createNotificationChannel(channel)
    }
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