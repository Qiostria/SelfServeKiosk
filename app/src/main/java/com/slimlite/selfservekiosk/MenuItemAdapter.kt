package com.slimlite.selfservekiosk

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.Locale

class MenuItemAdapter(
    private val context: Context,
    private val items: List<MenuItem>,
    private val onItemSelected: (MenuItem) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): MenuItem = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
//        val btnAddToCart = Button(context).apply {
//            text = "Add to Cart"
//            setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
//            setTextColor(Color.WHITE)
//            setPadding(8, 8, 8, 8)
//            layoutParams = LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            ).apply {
//                topMargin = 8
//            }
//
//            setOnClickListener {
//                CartManager.cartItems.add(item)
//                Toast.makeText(context, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
//            }
//        }

        val itemLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
            layoutParams = AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val imageView = ImageView(context).apply {
            setImageResource(
                if (item.imageResId != 0) item.imageResId else R.drawable.placeholder
            )
            layoutParams = LinearLayout.LayoutParams(180, 180).apply {
                gravity = Gravity.CENTER
                bottomMargin = 8
            }
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        val nameText = TextView(context).apply {
            text = item.name
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        val priceText = TextView(context).apply {
            text = formatRupiah(item.price)
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(Color.parseColor("#F5A62E"))
        }

        val descriptionText = TextView(context).apply {
            text = item.description
            textSize = 12f
            gravity = Gravity.CENTER
            maxLines = 2
        }

        itemLayout.addView(imageView)
        itemLayout.addView(nameText)
        itemLayout.addView(priceText)
        if (item.description.isNotEmpty()) {
            itemLayout.addView(descriptionText)
        }
        //itemLayout.addView(btnAddToCart)
        itemLayout.setOnClickListener {
            Toast.makeText(context, "Selected: ${item.name}", Toast.LENGTH_SHORT).show()
            onItemSelected(item)
        }

        return itemLayout
    }
    private fun formatRupiah(value: Double): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(value)
    }

}
