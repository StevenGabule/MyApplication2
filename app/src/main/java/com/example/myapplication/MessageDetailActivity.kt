package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class MessageDetailActivity : AppCompatActivity() {
  private lateinit var id: String
  private lateinit var sendBy: String
  private lateinit var name: String
  private lateinit var receivedBy: String
  private lateinit var content: String

  private lateinit var messageTextInputEditText: TextInputEditText
  private lateinit var sendMessageButton: MaterialButton
  private lateinit var nameTextView: TextView
  private lateinit var contentTextView: TextView

  private val urlPut = "${Constant.PUBLIC_IP}/contact/customer"
  private val urlPost = "${Constant.PUBLIC_IP}/contact/customer"
  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_message_detail)

    messageTextInputEditText = findViewById(R.id.messageTextInputEditText)
    sendMessageButton = findViewById(R.id.sendMessageButton)
    nameTextView = findViewById(R.id.nameTextView)
    contentTextView = findViewById(R.id.contentTextView)

    val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)

    id = intent.getStringExtra("id").toString()
    sendBy = intent.getStringExtra("sendBy").toString()
    name = intent.getStringExtra("name").toString()
    receivedBy = intent.getStringExtra("receivedBy").toString()
    content = intent.getStringExtra("content").toString()

    contentTextView.text = content
    nameTextView.text = "Sent By $name"

    // update contact when user view the message
    try {
      val rq: RequestQueue = Volley.newRequestQueue(this)
      val jar = object : JsonObjectRequest(Method.PUT, "$urlPut/$id", null, { response ->
        Log.d("API_CONTACT_PUT_SUCCESS", "$response")
      }, { error ->
        Log.e("API_CONTACT_PUT_ERROR", "$error.message")
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
      Log.e("API_CONTACT_PUT_ERROR", "$e")
    }

    sendMessageButton.setOnClickListener {
      val rq: RequestQueue = Volley.newRequestQueue(this)
      val postData = JSONObject()
      val content = messageTextInputEditText.text.toString()
      try {
        postData.put("receivedBy", sendBy)
        postData.put("content", content)

        val jar = object : JsonObjectRequest(Method.POST, urlPost, postData, { response ->
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