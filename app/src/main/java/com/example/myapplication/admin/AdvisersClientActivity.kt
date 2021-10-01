package com.example.myapplication.admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Constant
import com.example.myapplication.R
import com.example.myapplication.adapter.AdminClientItemAdapter
import com.example.myapplication.data.AdminClientsItem

class AdvisersClientActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_advisers_client)

    val rq: RequestQueue = Volley.newRequestQueue(this)
    val recycleAdminClientView = this.findViewById<RecyclerView>(R.id.adminClientAdviserRV)
    val adviserId = intent.getStringExtra("adviserId").toString()
    val name = intent.getStringExtra("name").toString()

    this.title = "$name - Customers"

    val pref = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)

    try {

      val url = "${Constant.PUBLIC_IP}/admin/advisers/client/$adviserId"
      val list = ArrayList<AdminClientsItem>()
      val jar = object : JsonArrayRequest(Method.GET, url, null, { response ->
        for (x in 0 until response.length()) {
          list.add(
            AdminClientsItem(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getString("avatar"),
              response.getJSONObject(x).getString("name"),
              response.getJSONObject(x).getString("createdForHuman"),
              response.getJSONObject(x).getString("createdFormatted"),
              response.getJSONObject(x).getString("location"),
              response.getJSONObject(x).getString("company"),
              response.getJSONObject(x).getString("contact_no"),
            )
          )
        }
        val adp = AdminClientItemAdapter(this, list)
        recycleAdminClientView.layoutManager = LinearLayoutManager(this)
        recycleAdminClientView.adapter = adp
      }, { error ->
        Log.e("API_CLIENT_FETCH", "$error.message")
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
      Log.e("API_CLIENT_ERROR", "$e")
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