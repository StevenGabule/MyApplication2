package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.AdminSubscriberItem
import kotlinx.android.synthetic.main.item_row_subscribers_admin.view.*
import java.util.*

class AdminSubscribersItemAdapter(var context: Context, var list: ArrayList<AdminSubscriberItem>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     val v:View = LayoutInflater.from(context).inflate(R.layout.item_row_subscribers_admin, parent, false)
    return SubscriberItemHolder(v)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as SubscriberItemHolder).bind(
      list[position].id,
      list[position].name,
      list[position].subscriptionType,
      list[position].email,
      list[position].contactNo,
      list[position].subscriptionStart,
      list[position].subscriptionEnd,
      list[position].createdForHuman)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  class SubscriberItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val subscription = arrayOf("", "One time Payment", "PHP 958.0 / Per Month", "PHP 10,034.0 / Per Year")

    @SuppressLint("SetTextI18n")
    fun bind(
      id: String,
      name: String,
      subscriptionType: Int,
      email: String,
      contactNo: String,
      subscriptionStart: String,
      subscriptionEnd: String,
      createdForHuman: String
    ) {
      itemView.subscriptionNameTV.text = "$name ${subscription[subscriptionType]}".toUpperCase()
      itemView.subscriberContactTV.text = "$email | $contactNo"
      itemView.StartedDateTV.text = "Starting: $subscriptionStart"
      itemView.endDateTV.text = "Ended: $subscriptionEnd"
//      itemView.employmentTypeTV.text =  employment[employmentType]
    }
  }
}