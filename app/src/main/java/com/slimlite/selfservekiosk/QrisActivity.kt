package com.slimlite.selfservekiosk

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class QrisActivity : AppCompatActivity() {

    private lateinit var btnSelesai: Button
    private lateinit var btnBatal: Button
    private var totalPay: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qris)

        btnSelesai = findViewById(R.id.btnSelesai)
        btnBatal = findViewById(R.id.btnBatal)

        val cartItems = CartManager.cartItems
        totalPay = cartItems.sumOf { it.price }

        val total = findViewById<TextView>(R.id.tvTotalNominal)
        total.text = formatRupiah(totalPay)

        startCountdown()

        btnBatal.setOnClickListener {
            showCancelDialog()
        }

        btnSelesai.setOnClickListener {
            val trxNumber = generateTransactionNumber()

            showFinishDialog(trxNumber.toString())
        }

    }

    private fun startCountdown() {
        var timeLeft = 5

        btnSelesai.isEnabled = false
        btnSelesai.setBackgroundColor(
            ContextCompat.getColor(this@QrisActivity, R.color.gray)
        )

        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                btnSelesai.text = "Finish Transaction ($timeLeft)"
                timeLeft--
            }

            override fun onFinish() {
                btnSelesai.text = "Finish Transaction"
                btnSelesai.isEnabled = true
                btnSelesai.setBackgroundColor(
                    ContextCompat.getColor(this@QrisActivity, R.color.green)
                )
            }
        }

        timer.start()
    }


    private fun generateTransactionNumber(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        val random = Random.nextInt(100, 999)

        return "TRX-$timestamp-$random"
    }

    private fun showCancelDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cancel Payment")
            .setMessage("Are you sure want to cancel the payment?")
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showFinishDialog(tnumber: String) {
        AlertDialog.Builder(this)
            .setTitle("Transaction Succesfull")
            .setMessage("Your Transaction number\n" +
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

    private fun formatRupiah(value: Double): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(value)
    }
    override fun onResume() {
        super.onResume()
        totalPay = CartManager.cartItems.sumOf { it.price * it.quantity }
        val total = findViewById<TextView>(R.id.tvTotalNominal)
        total.text = formatRupiah(totalPay)
    }

}
