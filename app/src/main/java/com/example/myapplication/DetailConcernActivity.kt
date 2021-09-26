package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class DetailConcernActivity : AppCompatActivity() {
  private val getConcernByAdviser: String = "${Constant.PUBLIC_IP}/get-concern-by-adviser"
  private lateinit var fileNameImage: ImageView
  private lateinit var titleTextView: TextView
  private lateinit var descriptionTextView: TextView
  private lateinit var postedByTextView: TextView

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail_concern)
    val id = intent.getStringExtra("concernId")
    val requestQueue = Volley.newRequestQueue(this)
    titleTextView = findViewById(R.id.titleTextView)
    postedByTextView = findViewById(R.id.postedByTextView)
    descriptionTextView = findViewById(R.id.descriptionTextView)
    fileNameImage = findViewById(R.id.fileNameImageView)
    val request =
      JsonObjectRequest(Request.Method.GET, "$getConcernByAdviser/$id", null, { response ->
        println("response is: $response")
        titleTextView.text = response.getString("title").toString()
        descriptionTextView.text = response.getString("description").toString()
        postedByTextView.text =
          "Posted by: " + response.getJSONObject("postedBy")["name"].toString()
        Picasso.get().load(response.getString("concern_file").toString()).into(fileNameImage);
      }, {
        println("error is: $it")
      }
      )
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