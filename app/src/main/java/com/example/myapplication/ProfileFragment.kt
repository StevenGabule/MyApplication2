package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    // Inflate the layout for this fragment
    val view =  inflater.inflate(R.layout.fragment_profile, container, false)
    val companySizeDropdown = view.findViewById<AutoCompleteTextView>(R.id.companySize_dropdown)

    val companies = resources.getStringArray(R.array.company)
    val adapter = ArrayAdapter(view.context, R.layout.list_item, companies)
    companySizeDropdown.setAdapter(adapter)

    val pref = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val name = pref?.getString("NAME","NO NAME")
    val email = pref?.getString("EMAIL","NO EMAIL")
    val contactNo = pref?.getString("CONTACT_NO","0")
    val location = pref?.getString("LOCATION","N/A")
    val company = pref?.getString("COMPANY","N/A")
    val companyType = pref?.getInt("COMPANY_TYPE",2)

    val nameEditText = view.findViewById<EditText>(R.id.client_name_EditText)
    val emailEditText = view.findViewById<EditText>(R.id.email_EditText)
    val contactNoEditText = view.findViewById<EditText>(R.id.contact_no_EditText)
    val locationEditText = view.findViewById<EditText>(R.id.location_EditText)
    val companyEditText = view.findViewById<EditText>(R.id.company_EditText)

    nameEditText.setText(name)
    emailEditText.setText(email)
    contactNoEditText.setText(contactNo)
    locationEditText.setText(location)
    companyEditText.setText(company)
    companySizeDropdown.setText(if (companyType == 1) "Individual" else "Company")
    return view
  }
}