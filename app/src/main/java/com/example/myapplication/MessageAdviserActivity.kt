package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.myapplication.adapter.MessageAdapter
import com.example.myapplication.data.MessageData

class MessageAdviserActivity : AppCompatActivity() {

  private lateinit var messageRecyclerView: RecyclerView
  private val url = "${Constant.PUBLIC_IP}/contact/customer"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_message_adviser)
    this.title = "Message"

    messageRecyclerView = findViewById(R.id.messagesRecycleView)
    val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)

    try {
      val list = ArrayList<MessageData>()
      val rq: RequestQueue = Volley.newRequestQueue(this)
      val jar = object : JsonArrayRequest(Method.GET, url, null, { response ->
        Log.d("API_MESSAGE_SUCCESS", "$response")
        for (x in 0 until response.length()) {
          list.add(
            MessageData(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getJSONObject("sendBy")["name"].toString(),
              response.getJSONObject(x).getJSONObject("sendBy")["_id"].toString(),
              response.getJSONObject(x).getString("receivedBy"),
              response.getJSONObject(x).getString("content"),
              response.getJSONObject(x).getBoolean("status"),
              response.getJSONObject(x).getString("readAt"),
              response.getJSONObject(x).getString("createdAt"),
              response.getJSONObject(x).getString("updatedAt")
            )
          )
        }
        val adp = MessageAdapter(this, list)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = adp
      }, { error ->
        Log.e("API_MESSAGE_FAILED", "$error.message")
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
      Log.e("API_MESSAGE_FAILED", "$e")
    }

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
        val i = Intent(this, AdviserHomeActivity::class.java)
        startActivity(i)
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }
}