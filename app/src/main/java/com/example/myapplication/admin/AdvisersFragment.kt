package com.example.myapplication.admin

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Constant
import com.example.myapplication.R
import com.example.myapplication.adapter.AdminAdvisersItemAdapter
import com.example.myapplication.data.AdminAdvisersItem

class AdvisersFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view =  inflater.inflate(R.layout.fragment_advisers, container, false)
    val rq: RequestQueue = Volley.newRequestQueue(view.context)
    val recycleAdminAdviserView = view.findViewById<RecyclerView>(R.id.adminAdviserRV)

    try {
      val pref = view.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
      val token = pref?.getString("TOKEN", null)
      val url = "${Constant.PUBLIC_IP}/admin/advisers"
      val list = ArrayList<AdminAdvisersItem>()
      val jar = object : JsonArrayRequest(Method.GET, url, null, { response ->
        for (x in 0 until response.length()) {
          list.add(
            AdminAdvisersItem(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getJSONObject("postedBy")["name"].toString(),
              response.getJSONObject(x).getString("course"),
              response.getJSONObject(x).getString("biography"),
              response.getJSONObject(x).getInt("employment_type"),
              response.getJSONObject(x).getString("school_at"),
              response.getJSONObject(x).getJSONObject("postedBy")["createdForHuman"].toString(),
              response.getJSONObject(x).getJSONObject("postedBy")["createdFormatted"].toString(),
              response.getJSONObject(x).getBoolean("adviser_status"),
              response.getJSONObject(x).getJSONObject("postedBy")["_id"].toString(),
              )
          )
        }
        val adp = AdminAdvisersItemAdapter(view.context, list)
        recycleAdminAdviserView.layoutManager = LinearLayoutManager(view.context)
        recycleAdminAdviserView.adapter = adp
      }, { error ->
        Log.e("API_ADVISER_FETCH", "$error.message")
        Toast.makeText(view.context, error.message, Toast.LENGTH_LONG).show()
      }) {
        override fun getHeaders(): MutableMap<String, String> {
          val params: MutableMap<String, String> = HashMap()
          params["Authorization"] = "Bearer $token"
          return params
        }
      }
      rq.add(jar)
    } catch (e: Exception) {
      Log.e("ADVISER_FRAGMENT_ERROR", "$e")
    }
    return view
  }
}