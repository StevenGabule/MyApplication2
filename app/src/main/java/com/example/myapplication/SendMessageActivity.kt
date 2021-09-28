package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class SendMessageActivity : AppCompatActivity() {
  private lateinit var userId: String
  private val url = "${Constant.PUBLIC_IP}/contact/customer"

  private lateinit var messageTextInputEditText: TextInputEditText
  private lateinit var sendMessageButton: MaterialButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_send_message)
    userId = intent.getStringExtra("userId").toString()

    this.title = "Send a message"
    messageTextInputEditText = findViewById(R.id.messageTextInputEditText)
    sendMessageButton = findViewById(R.id.sendMessageButton)

    val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)

    sendMessageButton.setOnClickListener {
      val rq: RequestQueue = Volley.newRequestQueue(this)
      val postData = JSONObject()
      val content = messageTextInputEditText.text.toString()
      try {
        postData.put("receivedBy", userId)
        postData.put("content", content)

        val jar = object : JsonObjectRequest(Method.POST, url, postData, { response ->
          Log.d("API_CONTACT_SUCCESS", "$response")
          finish()
        }, { error ->
          Log.e("API_CONTACT_ERROR", "$error.message")
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
        Log.e("API_CONTACT_ERROR", "$e")
      }
    }

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      finish()
    }
    return super.onOptionsItemSelected(item)
  }
}