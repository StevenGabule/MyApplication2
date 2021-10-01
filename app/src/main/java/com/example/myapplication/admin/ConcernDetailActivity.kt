package com.example.myapplication.admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Constant
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class ConcernDetailActivity : AppCompatActivity() {
  private val url: String = "${Constant.PUBLIC_IP}/admin/concerns/posted"
  private lateinit var fileNameImage: ImageView
  private lateinit var titleTextView: TextView
  private lateinit var descriptionTextView: TextView
  private lateinit var postedByTextView: TextView
  private lateinit var sendMessageButton: Button
  private lateinit var id: String
  private lateinit var name: String
  private lateinit var userId: String
  private lateinit var postedinfo: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_concern_detail)

    id = intent.getStringExtra("concernId").toString()
    name = intent.getStringExtra("title").toString()

    this.title = name.toUpperCase()

    titleTextView = findViewById(R.id.titleTextView)
    postedByTextView = findViewById(R.id.postedByTextView)
    descriptionTextView = findViewById(R.id.descriptionTextView)
    fileNameImage = findViewById(R.id.fileNameImageView)
    sendMessageButton = findViewById(R.id.sendMessageButton)

    val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)
    val requestQueue = Volley.newRequestQueue(this)
    val request = object : JsonObjectRequest(Method.GET, "$url/$id", null, { response ->
      println("response is: $response")
      titleTextView.text = response.getString("title").toString().toUpperCase()
      descriptionTextView.text = response.getString("description").toString()
      postedinfo = "Posted by: ${response.getJSONObject("postedBy")["name"]} on ${response.getString("createdFormatted")}, ${response.getString("createdForHuman")}".toUpperCase()
      postedByTextView.text = postedinfo
      userId = response.getJSONObject("postedBy")["_id"].toString()
      Picasso.get().load("${Constant.STATIC_IP}/concern/${userId}/${response.getString("concern_file")}").into(fileNameImage)
    }, {
      println("error is: $it")
    }) {
      override fun getHeaders(): MutableMap<String, String> {
        val params: MutableMap<String, String> = HashMap()
        params["Authorization"] = "Bearer $token"
        return params
      }
    }
    requestQueue.add(request)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      finish()
    }
    return super.onOptionsItemSelected(item)
  }
}