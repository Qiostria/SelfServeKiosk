package com.slimlite.selfservekiosk

import android.view.MenuItem
import java.io.Serializable

data class CartItem(
    val menuItem: MenuItem,
    var quantity: Int
) : Serializable
