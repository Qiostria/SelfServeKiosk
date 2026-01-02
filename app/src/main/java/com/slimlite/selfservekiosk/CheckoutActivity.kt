package com.slimlite.selfservekiosk

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

// HW: FIX EDITABLE CHECKOUT BUG
// HW: FIX MISSING CHECKOUT PRICE ON FIRST LAUNCH
// HW: GET SOME SLEEP


class CheckoutActivity : AppCompatActivity() {
    private var selectedPayment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        var total = 0.0

        val cartItems = CartManager.cartItems
        val recyclerView = findViewById<RecyclerView>(R.id.checkoutRecyclerView)
        val totalText = findViewById<TextView>(R.id.checkoutTotal)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CheckoutItemAdapter(cartItems)

        total = cartItems.sumOf { it.price }
        totalText.text = formatRupiah(total)

        val btnCash = findViewById<Button>(R.id.btnCash)
        val btnQRIS = findViewById<Button>(R.id.btnQRIS)
        val btnDebit = findViewById<Button>(R.id.btnDebit)

        val paymentButtons = listOf(btnCash, btnQRIS, btnDebit)



        fun highlightSelected(selected: Button) {
            paymentButtons.forEach { it.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA"))) }
            //paymentButtons.forEach { it.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA"))) }
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

        val confirmButton = findViewById<Button>(R.id.btnConfirmOrder)

        confirmButton.setOnClickListener {
            when (selectedPayment) {
                null -> {
                    Toast.makeText(
                        this,
                        "Please select a payment method",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                "QRIS" -> {
                    val intent = Intent(this, QrisActivity::class.java)
                    startActivity(intent)
                }

                else -> {
                    val trxNumber = generateTransactionNumber()
                    showFinishDialog(trxNumber)
                }
            }
        }

    }

    private fun showFinishDialog(tnumber: String) {
        AlertDialog.Builder(this)
            .setTitle("Transaction Succesfull")
            .setMessage("Succesfully paid with $selectedPayment\n" +
                    "Your Transaction number\n" +
                    "$tnumber")
            .setPositiveButton("Confirm") { _, _ ->
                CartManager.clearCart()
                getSharedPreferences("KioskPrefs", MODE_PRIVATE).edit().remove("order_type").apply()
                val intent = Intent(this, OrderTypeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun generateTransactionNumber(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        val random = Random.nextInt(100, 999)

        return "TRX-$timestamp-$random"
    }

    private fun formatRupiah(value: Double): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(value)
    }
    override fun onResume() {
        super.onResume()
        updateTotal()
    }
    private fun updateTotal() {
        val total = CartManager.cartItems.sumOf { it.price * it.quantity }
        val totalText = findViewById<TextView>(R.id.checkoutTotal)
        totalText.text = formatRupiah(total)
    }


}
