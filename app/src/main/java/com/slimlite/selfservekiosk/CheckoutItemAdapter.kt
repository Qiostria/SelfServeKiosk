package com.slimlite.selfservekiosk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CheckoutItemAdapter(private val items: List<MenuItem>) :
    RecyclerView.Adapter<CheckoutItemAdapter.CheckoutViewHolder>() {

    inner class CheckoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkout, parent, false)
        return CheckoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = "${item.name} x${item.quantity}"
        holder.itemPrice.text = String.format("$%.2f", item.price * item.quantity)
    }

    override fun getItemCount(): Int = items.size
}

