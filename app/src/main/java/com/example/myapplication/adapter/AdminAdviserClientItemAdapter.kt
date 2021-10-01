package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.AdminAdviserClientsItem
import kotlinx.android.synthetic.main.item_row_clients_adviser_admin.view.*
import java.util.*

class AdminAdviserClientItemAdapter(var context: Context, var list: ArrayList<AdminAdviserClientsItem>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     val v:View = LayoutInflater.from(context).inflate(R.layout.item_row_clients_adviser_admin, parent, false)
    return AdviserClientItemHolder(v)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as AdviserClientItemHolder).bind(
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

  class AdviserClientItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
//      itemView.employmentTypeTV.text =  employment[employmentType]
    }
  }
}