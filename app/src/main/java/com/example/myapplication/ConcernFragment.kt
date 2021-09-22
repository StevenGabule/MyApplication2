package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.adapter.ConcernItemAdapter
import com.example.myapplication.data.ConcernItem
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class ConcernFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val url = "${Constant.PUBLIC_IP}/concerns"
    val list = ArrayList<ConcernItem>()
    val view = inflater.inflate(R.layout.fragment_concern, container, false)
    val rq: RequestQueue = Volley.newRequestQueue(view.context)
    val recycleConcern = view.findViewById<RecyclerView>(R.id.concern_recyclerview)
    val concernNewButton = view.findViewById<ExtendedFloatingActionButton>(R.id.addConcern_floatingActionButton)

    try {
      val jar = JsonArrayRequest(Request.Method.GET, url, null, { response ->
//        Log.d("API_FETCH_SUCCESS", "$response")
        for (x in 0 until response.length()) {
          list.add(
            ConcernItem(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getString("title"),
              response.getJSONObject(x).getString("description"),
              response.getJSONObject(x).getJSONObject("postedBy")["name"].toString(),
              response.getJSONObject(x).getInt("status")
            )
          )
        }
        val adp = ConcernItemAdapter(view.context, list)
        recycleConcern.layoutManager = LinearLayoutManager(view.context)
        recycleConcern.adapter = adp
      }, { error ->
        Log.e("API_CONCERN_FETCH", "$error.message")
        Toast.makeText(view.context, error.message, Toast.LENGTH_LONG).show()
      })
      rq.add(jar)
    } catch (e: Exception) {
      Log.e("CONCERN_FRAGMENT_ERROR", "$e")
    }

    recycleConcern.addOnScrollListener (object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
          concernNewButton.hide()
        } else {
          concernNewButton.show()
        }
      }
    })

    concernNewButton.setOnClickListener {
      val i = Intent(view.context, ConcernNewActivity::class.java)
      startActivity(i)
    }

    return view
  }
}