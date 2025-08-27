package com.slimlite.selfservekiosk

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addItem(menuItem: MenuItem) {
        val existingItem = cartItems.find { it.menuItem.id == menuItem.id }
        if (existingItem != null) {
            existingItem.quantity++   // increment if already in cart
        } else {
            cartItems.add(CartItem(menuItem, 1))
        }
    }

    fun removeItem(menuItem: MenuItem) {
        val existingItem = cartItems.find { it.menuItem.id == menuItem.id }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--  // decrement
            } else {
                cartItems.remove(existingItem) // remove if last one
            }
        }
    }

    fun getCartItems(): List<CartItem> {
        return cartItems.toList()  // return a copy to avoid accidental modification
    }

    fun getTotal(): Double {
        return cartItems.sumOf { it.menuItem.price * it.quantity }
    }

    fun clearCart() {
        cartItems.clear()
    }
}
