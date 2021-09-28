package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.adapter.ConcernItemAdviserAdapter
import com.example.myapplication.data.ConcernItemAdviser
import com.google.firebase.messaging.FirebaseMessaging

class AdviserHomeActivity : AppCompatActivity() {
  private val url = "${Constant.PUBLIC_IP}/advisers/fetch-adviser-concerns"
  private lateinit var recycleConcern: RecyclerView

  private val TAG = "PushNotification"
  private val CHANNEL_ID = "101"


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


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home_adviser)
    this.title = "Home"

    val list = ArrayList<ConcernItemAdviser>()
    val rq: RequestQueue = Volley.newRequestQueue(this)
     recycleConcern = findViewById(R.id.adviser_concern_recyclerview)

    val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)
    try {
      val jar = object : JsonArrayRequest(Method.GET, url, null, { response ->
        Log.d("API_FETCH_SUCCESS", "$response")
        for (x in 0 until response.length()) {
          list.add(
            ConcernItemAdviser(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getString("title"),
              response.getJSONObject(x).getString("description"),
              response.getJSONObject(x).getJSONObject("postedBy")["name"].toString(),
              response.getJSONObject(x).getInt("status"),
              response.getJSONObject(x).getString("concern_file"),
            )
          )
        }
        val adp = ConcernItemAdviserAdapter(this, list)
        recycleConcern.layoutManager = LinearLayoutManager(this)
        recycleConcern.adapter = adp
      }, { error ->
        Log.e("API_CONCERN_FETCH", "$error.message")
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
      }) {
        override fun getHeaders(): MutableMap<String, String> {
          val params: MutableMap<String, String> = HashMap()
          params["Authorization"] = "Bearer $token"
          return params
        }
      }
      rq.add(jar)
    } catch (e: Exception) {
      Log.e("CONCERN_FRAGMENT_ERROR", "$e")
    }

    createNotificationChannel()
    getToken()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu_adviser, menu)
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

      R.id.idMessage -> {
        val i = Intent(this, MessageAdviserActivity::class.java)
        startActivity(i)
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }
}