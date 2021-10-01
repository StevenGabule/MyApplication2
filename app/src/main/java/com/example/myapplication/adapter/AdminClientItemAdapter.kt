package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.admin.ClientPostActivity
import com.example.myapplication.admin.ConcernDetailActivity
import com.example.myapplication.data.AdminClientsItem
import kotlinx.android.synthetic.main.item_row_clients_admin.view.*
import java.util.*

class AdminClientItemAdapter(var context: Context, var list: ArrayList<AdminClientsItem>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     val v:View = LayoutInflater.from(context).inflate(R.layout.item_row_clients_admin, parent, false)
    return ClientItemHolder(v)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as ClientItemHolder).bind(
      list[position].id,
      list[position].avatar,
      list[position].name,
      list[position].createdForHuman,
      list[position].createdFormatted,
      list[position].location,
      list[position].company,
      list[position].contactNo)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  class ClientItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @SuppressLint("SetTextI18n")
    fun bind(
      id: String,
      avatar: String,
      name: String,
      createdForHuman: String,
      createdFormatted: String,
      location: String,
      company: String,
      contactNo: String
    ) {
      itemView.NameTV.text = "${name.toUpperCase()}"
      itemView.descriptionTV.text = "${location}, ${company}".toUpperCase()
      itemView.contactNoTV.text = contactNo

      itemView.setOnClickListener {
        val obj = Intent(itemView.context, ClientPostActivity::class.java)
        obj.putExtra("id", id)
        itemView.context.startActivity(obj)
      }
//      itemView.employmentTypeTV.text =  employment[employmentType]
    }
  }
}