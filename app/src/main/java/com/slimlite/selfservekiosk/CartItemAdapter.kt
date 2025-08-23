package com.slimlite.selfservekiosk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartItemAdapter(
    private val items: MutableList<MenuItem>,
    private val onCartUpdated: (List<MenuItem>) -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val btnIncrease: Button = view.findViewById(R.id.btnIncrease)
        val btnDecrease: Button = view.findViewById(R.id.btnDecrease)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        holder.itemName.text = item.name
        holder.itemPrice.text = String.format("$%.2f", item.price * item.quantity)
        holder.tvQuantity.text = item.quantity.toString()

        holder.btnIncrease.setOnClickListener {
            item.quantity++
            notifyItemChanged(position)
            onCartUpdated(items)
        }

        holder.btnDecrease.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                notifyItemChanged(position)
            } else {
                // Remove item completely if quantity = 0
                items.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, items.size)
            }
            onCartUpdated(items)
        }
    }

    override fun getItemCount(): Int = items.size
}
