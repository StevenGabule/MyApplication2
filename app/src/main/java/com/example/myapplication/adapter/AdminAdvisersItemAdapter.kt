package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailConcernActivity
import com.example.myapplication.R
import com.example.myapplication.admin.AdvisersClientActivity
import com.example.myapplication.data.AdminAdvisersItem
import kotlinx.android.synthetic.main.item_row_advisers_admin.view.*
import java.util.*

class AdminAdvisersItemAdapter(var context: Context, var list: ArrayList<AdminAdvisersItem>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     val v:View = LayoutInflater.from(context).inflate(R.layout.item_row_advisers_admin, parent, false)
    return AdminAdviserItemHolder(v)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as AdminAdviserItemHolder).bind(
      list[position].id,
      list[position].name,
      list[position].course,
      list[position].bio,
      list[position].employmentType,
      list[position].schoolAt,
      list[position].CreatedForHuman,
      list[position].CreatedFormatted,
      list[position].userStatus,
      list[position].adviserId,
    )
  }

  override fun getItemCount(): Int {
    return list.size
  }

  class AdminAdviserItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val employment = arrayOf("", "FullTime", "PartTime", "Freelance", "Contract", "Seasonal")
    @SuppressLint("SetTextI18n")
    fun bind(
      id: String,
      name: String,
      course: String,
      bio: String,
      employmentType: Int,
      schoolAt: String,
      createdForHuman: String,
      createdFormatted: String,
      userStatus: Boolean,
      adviserId: String,
    ) {
      itemView.adviserNameTV.text = "${name.toUpperCase()} | ${schoolAt.toUpperCase()}"
      itemView.bioTV.text = bio
      itemView.courseTV.text = course
      itemView.employmentTypeTV.text =  employment[employmentType]

      itemView.setOnClickListener {
        val obj = Intent(itemView.context, AdvisersClientActivity::class.java)
        obj.putExtra("adviserId", adviserId)
        obj.putExtra("name", name)
        itemView.context.startActivity(obj)
      }
    }
  }
}