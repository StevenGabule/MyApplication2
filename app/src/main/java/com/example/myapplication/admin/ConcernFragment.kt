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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.Constant
import com.example.myapplication.R
import com.example.myapplication.adapter.AdminConcernItemAdapter
import com.example.myapplication.adapter.ConcernItemAdapter
import com.example.myapplication.data.AdminConcernItem

class ConcernFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_concern, container, false)
    val rq: RequestQueue = Volley.newRequestQueue(view.context)
    val recycleConcern = view.findViewById<RecyclerView>(R.id.adminConcernRV)

    try {
      val pref = view.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
      val token = pref?.getString("TOKEN", null)
      val url = "${Constant.PUBLIC_IP}/admin/concerns/posted"
      val list = ArrayList<AdminConcernItem>()
      val jar = object : JsonArrayRequest(Method.GET, url, null, { response ->
        for (x in 0 until response.length()) {
          list.add(
            AdminConcernItem(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getString("title"),
              response.getJSONObject(x).getString("description"),
              response.getJSONObject(x).getJSONObject("postedBy")["name"].toString(),
              response.getJSONObject(x).getInt("status"),
              response.getJSONObject(x).getJSONObject("assignBy")["name"].toString(),
              response.getJSONObject(x).getString("concern_file"),
              response.getJSONObject(x).getString("createdForHuman"),
              response.getJSONObject(x).getJSONObject("postedBy")["_id"].toString(),
            )
          )
        }
        val adp = AdminConcernItemAdapter(view.context, list)
        recycleConcern.layoutManager = LinearLayoutManager(view.context)
        recycleConcern.adapter = adp
      }, { error ->
        Log.e("API_CONCERN_FETCH", "$error.message")
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

    return view
  }
}