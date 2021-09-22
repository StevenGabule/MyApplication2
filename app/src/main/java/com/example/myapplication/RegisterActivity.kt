package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    this.title = "AgriConnect - Create an account"
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    val nameEditText = findViewById<EditText>(R.id.client_name_EditText)
    val emailEditText = findViewById<EditText>(R.id.email_EditText)
    val contactNoEditText = findViewById<EditText>(R.id.contact_no_EditText)
    val passwordEditText = findViewById<EditText>(R.id.password_EditText)
    val confirmPasswordEditText = findViewById<EditText>(R.id.confirm_password_EditText)
    val companyEditText = findViewById<EditText>(R.id.company_EditText)
    val companySizeDropdown = findViewById<AutoCompleteTextView>(R.id.companySize_dropdown)
    val locationEditText = findViewById<EditText>(R.id.location_EditText)
    val submitButton = findViewById<Button>(R.id.submit_buttonView)

    val companies = resources.getStringArray(R.array.company)
    val adapter = ArrayAdapter(this, R.layout.list_item, companies)
    companySizeDropdown.setAdapter(adapter)

    submitButton.setOnClickListener {
      if (passwordEditText.text.toString() != confirmPasswordEditText.text.toString()) {
        Toast.makeText(this, "Password mismatch!", Toast.LENGTH_LONG).show()
      } else {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val contactNo = contactNoEditText.text.toString()
        val password = passwordEditText.text.toString()
        val company = companyEditText.text.toString()
        val location = locationEditText.text.toString()
        val companySize = companySizeDropdown.text.toString()

        val url = "${Constant.PUBLIC_IP}/register"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()

        try {
          postData.put("name", name)
          postData.put("email", email)
          postData.put("contact_no", contactNo)
          postData.put("password", password)
          postData.put("company", company)
          postData.put("company_type", if (companySize == "individual") 1 else 2)
          postData.put("location", location)
          postData.put("user_type", 2)
        } catch (e: JSONException) {
          e.printStackTrace()
        }

        val jsonObjectRequest =
          JsonObjectRequest(
            Request.Method.POST, url, postData,
            { response ->
              Log.d("API_REGISTER", "$response")
              val i = Intent(this, LoginActivity::class.java)
              startActivity(i)
              finish()
            }) { error ->
            Log.e("API_ERROR_REGISTER", "$error")
            Toast.makeText(
              this.applicationContext,
              "Oops, Something goes wrong! kindly check your entry!",
              Toast.LENGTH_LONG
            ).show()
          }

        requestQueue.add(jsonObjectRequest)
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      finish()
    }
    return super.onOptionsItemSelected(item)
  }
}