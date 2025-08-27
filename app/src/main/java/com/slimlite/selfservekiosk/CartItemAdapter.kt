package com.slimlite.selfservekiosk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartItemAdapter(private val items: List<MenuItem>,
    private val readOnly: Boolean = false,
    private val onCartUpdated: (() -> Unit)? = null
) : RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.itemName)
        val price = view.findViewById<TextView>(R.id.itemPrice)
        val quantity = view.findViewById<TextView>(R.id.itemQuantity)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)
        val btnRemove = view.findViewById<Button>(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.price.text = "Rp ${item.price}"
        holder.quantity.text = "x${item.quantity}"

        if (readOnly) {
            holder.btnAdd.visibility = View.GONE
            holder.btnRemove.visibility = View.GONE
        } else {
            holder.btnAdd.setOnClickListener {
                item.quantity++
                notifyItemChanged(position)
                onCartUpdated?.invoke()
            }
            holder.btnRemove.setOnClickListener {
                if (item.quantity > 0) {
                    item.quantity--
                    notifyItemChanged(position)
                    onCartUpdated?.invoke()
                }
            }
        }
    }

    override fun getItemCount() = items.size
}

