package com.example.myapplication.admin

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Constant
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class DashboardActivity : AppCompatActivity() {
  private val url: String = "${Constant.PUBLIC_IP}/admin/reports"
  private lateinit var customerTV: TextView
  private lateinit var adviserTV: TextView
  private lateinit var concernTV: TextView
  private lateinit var subscriptionTV: TextView
  private lateinit var oneTimeTV: TextView
  private lateinit var yearlyTV: TextView
  private lateinit var monthlyTV: TextView


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dashboard)

    customerTV = findViewById(R.id.customerCountTV)
    adviserTV = findViewById(R.id.AdviserCountTV)
    concernTV = findViewById(R.id.ConcernsCountTV)
    subscriptionTV = findViewById(R.id.SubscribersCountTV)
    oneTimeTV = findViewById(R.id.onetimeTV)
    monthlyTV = findViewById(R.id.monthlyTV)
    yearlyTV = findViewById(R.id.yearlyTV)

    this.title = "DASHBOARD"

    val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)
    val requestQueue = Volley.newRequestQueue(this)
    val request = @SuppressLint("SetTextI18n")
    object : JsonObjectRequest(Method.GET, url, null, { response ->
      println("response is: $response")
      subscriptionTV.text = response.getString("subscribers").toString() + " JOINED"
      adviserTV.text = response.getString("advisers").toString() + " ACTIVE"
      concernTV.text = response.getString("concerns").toString() + " POSTED"
      customerTV.text = response.getString("clients").toString() + " CONNECTED"
      oneTimeTV.text = response.getString("oneTime").toString() + " ITEM"
      monthlyTV.text = response.getString("monthly").toString() + " ITEM"
      yearlyTV.text = response.getString("yearly").toString() + " ITEM"
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