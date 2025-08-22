package com.slimlite.selfservekiosk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CheckoutActivity : AppCompatActivity() {
    private var selectedPayment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val cartItems = CartManager.cartItems
        val recyclerView = findViewById<RecyclerView>(R.id.checkoutRecyclerView)
        val totalText = findViewById<TextView>(R.id.checkoutTotal)
        val confirmButton = findViewById<Button>(R.id.btnConfirmOrder)
        confirmButton.setOnClickListener {
            // Clear SharedPreferences if needed
            getSharedPreferences("KioskPrefs", MODE_PRIVATE).edit().clear().apply()

            // Optionally clear cart
            CartManager.cartItems.clear()

            getSharedPreferences("KioskPrefs", MODE_PRIVATE).edit().clear().apply()
            val intent = Intent(this, OrderTypeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartItemAdapter(cartItems)

        val total = cartItems.sumOf { it.price }
        totalText.text = String.format("Total: $%.2f", total)

        val btnCash = findViewById<Button>(R.id.btnCash)
        val btnQRIS = findViewById<Button>(R.id.btnQRIS)
        val btnDebit = findViewById<Button>(R.id.btnDebit)

        val paymentButtons = listOf(btnCash, btnQRIS, btnDebit)



        fun highlightSelected(selected: Button) {
            paymentButtons.forEach { it.setBackgroundTintList(null) }
            selected.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.orange))
        }

        btnCash.setOnClickListener {
            selectedPayment = "Cash"
            highlightSelected(btnCash)
        }

        btnQRIS.setOnClickListener {
            selectedPayment = "QRIS"
            highlightSelected(btnQRIS)
        }

        btnDebit.setOnClickListener {
            selectedPayment = "Debit"
            highlightSelected(btnDebit)
        }

        findViewById<Button>(R.id.btnConfirmOrder).setOnClickListener {
            if (selectedPayment == null) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with checkout logic
                Toast.makeText(this, "Order confirmed with $selectedPayment", Toast.LENGTH_LONG)
                    .show()
                CartManager.clearCart()
                getSharedPreferences("KioskPrefs", MODE_PRIVATE).edit().remove("order_type").apply()
                val intent = Intent(this, OrderTypeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
