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
import com.example.myapplication.adapter.ConcernItemAdviserAdapter
import com.example.myapplication.data.ConcernItemAdviser

class AdviserHomeActivity : AppCompatActivity() {
  private val url = "${Constant.PUBLIC_IP}/advisers/fetch-adviser-concerns"
  private lateinit var recycleConcern: RecyclerView

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