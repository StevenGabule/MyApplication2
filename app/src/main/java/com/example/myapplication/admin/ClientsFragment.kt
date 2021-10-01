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
import com.example.myapplication.adapter.AdminClientItemAdapter
import com.example.myapplication.data.AdminAdvisersItem
import com.example.myapplication.data.AdminClientsItem

class ClientsFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view =  inflater.inflate(R.layout.fragment_clients, container, false)
    val rq: RequestQueue = Volley.newRequestQueue(view.context)
    val recycleAdminClientView = view.findViewById<RecyclerView>(R.id.adminClientRV)

    try {
      val pref = view.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
      val token = pref?.getString("TOKEN", null)
      val url = "${Constant.PUBLIC_IP}/admin/users/account"
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
        val adp = AdminClientItemAdapter(view.context, list)
        recycleAdminClientView.layoutManager = LinearLayoutManager(view.context)
        recycleAdminClientView.adapter = adp
      }, { error ->
        Log.e("API_CLIENT_FETCH", "$error.message")
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
      Log.e("API_CLIENT_ERROR", "$e")
    }
    return view
  }
}