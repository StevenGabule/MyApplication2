package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.admin.AdminActivity
import com.example.myapplication.data.ClientInfo
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
  private var userType = 2
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    this.title = "AgriConnect - Login"
    val loginButton = findViewById<Button>(R.id.login_Button)
    val registerButton = findViewById<Button>(R.id.register_ButtonView)
    val emailTextView = findViewById<TextView>(R.id.email_EditText)
    val passwordTextView = findViewById<TextView>(R.id.password_EditText)

    val pref = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val checkIfLogin = pref.getBoolean("IS_LOGIN", false);
    val userTypeLogin = pref.getInt("USER_TYPE", 2)

    if (checkIfLogin) {
      if (userTypeLogin == 2) {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
      } else if (userTypeLogin == 3) {
        val i = Intent(this, AdminActivity::class.java)
        startActivity(i)
        finish()
      } else {
        val i = Intent(this, AdviserHomeActivity::class.java)
        startActivity(i)
        finish()
      }
    }

    loginButton.setOnClickListener {
      val email = emailTextView.text.toString()
      val password = passwordTextView.text.toString()
      val url = "${Constant.PUBLIC_IP}/login"
      val requestQueue = Volley.newRequestQueue(this)
      val postData = JSONObject()

      try {
        postData.put("username", email)
        postData.put("password", password)
      } catch (e: JSONException) {
        e.printStackTrace()
      }

      val jsonObjectRequest =
        JsonObjectRequest(Request.Method.POST, url, postData,
          { response ->
            Log.d("API_LOGIN", "$response")
            val editor = pref.edit()

            ClientInfo.id = response.getJSONObject("user")["_id"].toString()
            ClientInfo.name = response.getJSONObject("user")["name"].toString()
            ClientInfo.email = response.getJSONObject("user")["email"].toString()
            ClientInfo.userType = response.getJSONObject("user")["user_type"].toString().toInt()
            userType = response.getJSONObject("user")["user_type"].toString().toInt()
            ClientInfo.token = response.getString("token").toString()
            ClientInfo.createdAt = response.getJSONObject("user")["createdAt"].toString()
            ClientInfo.updatedAt = response.getJSONObject("user")["updatedAt"].toString()

            editor.apply {
              editor.putBoolean("IS_LOGIN", true)
              editor.putInt(
                "USER_TYPE",
                response.getJSONObject("user")["user_type"].toString().toInt()
              )
              editor.putString("NAME", response.getJSONObject("user")["name"].toString())
              editor.putString("EMAIL", response.getJSONObject("user")["email"].toString())
              editor.putString(
                "CONTACT_NO",
                response.getJSONObject("user")["contact_no"].toString()
              )
              editor.putString("LOCATION", response.getJSONObject("user")["location"].toString())
              editor.putString("COMPANY", response.getJSONObject("user")["company"].toString())
              editor.putInt(
                "COMPANY_TYPE",
                response.getJSONObject("user")["company_type"].toString().toInt()
              )
              editor.putString("TOKEN", response.getString("token").toString())
              editor.putString("CREATED_AT", response.getJSONObject("user")["createdAt"].toString())
              editor.putString("UPDATED_AT", response.getJSONObject("user")["updatedAt"].toString())
            }.apply()

            if (userType == 2) {
              // display the customer
              val i = Intent(this, MainActivity::class.java)
              startActivity(i)
              finish()
            } else if (userTypeLogin == 3) {
              val i = Intent(this, AdminActivity::class.java)
              startActivity(i)
              finish()
            } else {
              // display the adviser
              val i = Intent(this, AdviserHomeActivity::class.java)
              startActivity(i)
              finish()
            }

          }) { error ->
          Log.e("API_ERROR_LOGIN", "$error")
          Toast.makeText(
            this.applicationContext,
            "Email or password didn't match in our record. Please try again!",
            Toast.LENGTH_LONG
          ).show()
        }

      requestQueue.add(jsonObjectRequest)
    }

    registerButton.setOnClickListener {
      val i = Intent(this, RegisterActivity::class.java)
      startActivity(i)
    }
  }
}