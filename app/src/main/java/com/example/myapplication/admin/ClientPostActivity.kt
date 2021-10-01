package com.example.myapplication.admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Constant
import com.example.myapplication.R
import com.example.myapplication.adapter.AdminClientPostItemAdapter
import com.example.myapplication.data.AdminClientPostsItem

class ClientPostActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_client_post)
    val rq: RequestQueue = Volley.newRequestQueue(this)
    val clientPostRv = findViewById<RecyclerView>(R.id.adminClientPostedRV)

    val id = intent.getStringExtra("id").toString()
    this.title = "ALL POSTED"
    try {
      val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
      val token = pref?.getString("TOKEN", null)
      val url = "${Constant.PUBLIC_IP}/admin/customer/all/$id"
      val list = ArrayList<AdminClientPostsItem>()
      val jar = object : JsonArrayRequest(Method.GET, url, null, { response ->
        for (x in 0 until response.length()) {
          list.add(
            AdminClientPostsItem(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getString("title"),
//              response.getJSONObject(x).getString("description"),
//              response.getJSONObject(x).getJSONObject("postedBy")["name"].toString(),
//              response.getJSONObject(x).getInt("status"),
//              response.getJSONObject(x).getJSONObject("assignBy")["name"].toString(),
//              response.getJSONObject(x).getString("concern_file"),
//              response.getJSONObject(x).getString("createdForHuman"),
//              response.getJSONObject(x).getJSONObject("postedBy")["_id"].toString(),
            )
          )
        }
        val adp = AdminClientPostItemAdapter(this, list)
        clientPostRv.layoutManager = LinearLayoutManager(this)
        clientPostRv.adapter = adp
      }, { error ->
        Log.e("API_POSTED_FETCH", "$error.message")
      }) {
        override fun getHeaders(): MutableMap<String, String> {
          val params: MutableMap<String, String> = HashMap()
          params["Authorization"] = "Bearer $token"
          return params
        }
      }
      rq.add(jar)
    } catch (e: Exception) {
      Log.e("POSTED_FRAGMENT_ERROR", "$e")
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