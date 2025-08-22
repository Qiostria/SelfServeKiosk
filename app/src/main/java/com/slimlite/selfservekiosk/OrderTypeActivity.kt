package com.slimlite.selfservekiosk


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OrderTypeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_type)

        val sharedPref = getSharedPreferences("KioskPrefs", MODE_PRIVATE)

        findViewById<Button>(R.id.btnTakeOut).setOnClickListener {
            sharedPref.edit().putString("order_type", "Take Out").apply()
            startActivity(Intent(this, OrderMenuActivity::class.java))
        }

        findViewById<Button>(R.id.btnDineIn).setOnClickListener {
            sharedPref.edit().putString("order_type", "Dine In").apply()
            startActivity(Intent(this, OrderMenuActivity::class.java))
        }

//        findViewById<Button>(R.id.btnEdit).setOnClickListener {
//            sharedPref.edit().putString("order_type", "Edit").apply()
//            startActivity(Intent(this, OrderMenuActivity::class.java))
//        }
    }
}