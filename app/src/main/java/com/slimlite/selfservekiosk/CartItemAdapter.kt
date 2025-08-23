package com.slimlite.selfservekiosk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartItemAdapter(private val items: List<MenuItem>) : RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(android.R.id.text1)
        val itemPrice: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return CartViewHolder(view)
    }
// chnge 1
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = String.format("$%.2f", item.price)
    }

    override fun getItemCount(): Int = items.size
}