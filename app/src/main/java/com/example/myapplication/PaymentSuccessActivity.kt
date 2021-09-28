package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PaymentSuccessActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_payment_success)
    this.title = "Payment Status"
    val returnToMainButton = findViewById<Button>(R.id.returnButton)

    returnToMainButton.setOnClickListener {
      val intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
      finish()
    }
  }
}