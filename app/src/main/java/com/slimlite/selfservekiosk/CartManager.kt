package com.slimlite.selfservekiosk

object CartManager {
    val cartItems = mutableListOf<MenuItem>()

    fun clearCart() {
        cartItems.clear()
    }
}