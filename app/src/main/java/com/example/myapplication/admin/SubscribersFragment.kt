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
import com.example.myapplication.adapter.AdminSubscribersItemAdapter
import com.example.myapplication.data.AdminAdvisersItem
import com.example.myapplication.data.AdminSubscriberItem


class SubscribersFragment : Fragment() {


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view =  inflater.inflate(R.layout.fragment_subscribers, container, false)
    val rq: RequestQueue = Volley.newRequestQueue(view.context)
    val recycleAdminSubscriberView = view.findViewById<RecyclerView>(R.id.adminSubscriberRV)

    try {
      val pref = view.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
      val token = pref?.getString("TOKEN", null)
      val url = "${Constant.PUBLIC_IP}/admin/subscribers"
      val list = ArrayList<AdminSubscriberItem>()
      val jar = object : JsonArrayRequest(Method.GET, url, null, { response ->
        for (x in 0 until response.length()) {
          list.add(
            AdminSubscriberItem(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getJSONObject("userId")["name"].toString(),
              response.getJSONObject(x).getInt("subscriptionType"),
              response.getJSONObject(x).getJSONObject("userId")["email"].toString(),
              response.getJSONObject(x).getJSONObject("userId")["contact_no"].toString(),
              response.getJSONObject(x).getString("subscriptionStart"),
              response.getJSONObject(x).getString("subscriptionEnd"),
              response.getJSONObject(x).getString("createdForHuman"),
            )
          )
        }
        val adp = AdminSubscribersItemAdapter(view.context, list)
        recycleAdminSubscriberView.layoutManager = LinearLayoutManager(view.context)
        recycleAdminSubscriberView.adapter = adp
      }, { error ->
        Log.e("API_SUBSCRIBERS_FETCH", "$error.message")
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
      Log.e("SUBSCRIBERS_FRAG_ERROR", "$e")
    }

    return view
  }


}