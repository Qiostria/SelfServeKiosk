package com.slimlite.selfservekiosk

import CartBottomSheetFragment
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Category(
    val name: String,
    var isSelected: Boolean = false
)


data class MenuItem(
    val name: String,
    val category: String,
    val price: Double,
    val description: String = "",
    val imageResId: Int = 0,
    var quantity: Int = 1
)

private val cartItems = mutableListOf<MenuItem>()


class CategoryAdapter(
    private val categories: List<Category>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(categoryName: String, position: Int)
    }

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, position == selectedPosition)
    }

    override fun getItemCount(): Int = categories.size

    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.categoryName)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedPosition(position)
                    listener.onCategoryClick(categories[position].name, position)
                }
            }
        }

        fun bind(category: Category, isSelected: Boolean) {
            categoryName.text = category.name


            //HW: Add more color variations on color.xml
            if (isSelected) {
                itemView.setBackgroundResource(R.drawable.selected_category_bg)
                categoryName.setTextColor(Color.WHITE)
            } else {
                itemView.setBackgroundResource(R.drawable.default_category_bg)
                categoryName.setTextColor(Color.BLACK)
            }
        }
    }
}

class OrderMenuActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener {
    private lateinit var menuGrid: GridView
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categories: MutableList<Category>
    private lateinit var allMenuItems: MutableList<MenuItem>
    private lateinit var filteredMenuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_menu)

        val sharedPref = getSharedPreferences("KioskPrefs", MODE_PRIVATE)
        val orderType = sharedPref.getString("order_type", "No Type Selected")
        val orderTypeTextView = findViewById<TextView>(R.id.OrderType)
        orderTypeTextView.text = orderType


        initViews()
        setupCategories()
        setupMenuItems()
        setupRecyclerView()
        setupCancelButton()
        filterMenuByCategory("All")

        val cartButton = findViewById<Button>(R.id.btnCart)
        cartButton.isEnabled = CartManager.cartItems.isNotEmpty()

        cartButton.setOnClickListener {
            val bottomSheet = CartBottomSheetFragment.newInstance(CartManager.cartItems)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    private fun initViews() {
        menuGrid = findViewById(R.id.menuGrid)
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)
    }

    private fun setupCategories() {
        categories = mutableListOf<Category>().apply {
            add(Category("All"))
            add(Category("Appetizers"))
            add(Category("Main Course"))
            add(Category("Desserts"))
            add(Category("Beverages"))
            add(Category("Snacks"))
        }
    }

    private fun setupMenuItems() {
        allMenuItems = mutableListOf<MenuItem>().apply {
            // Appetizers
            add(MenuItem("Caesar Salad", "Appetizers", 8.99, "Fresh romaine lettuce with caesar dressing",R.drawable.caesar_salad))
            add(MenuItem("Spring Rolls", "Appetizers", 6.99, "Crispy vegetable spring rolls",R.drawable.spring_rolls))
            add(MenuItem("Chicken Wings", "Appetizers", 9.99, "Buffalo style chicken wings",R.drawable.grilled_chicken))

            // Main Course
            add(MenuItem("Grilled Chicken", "Main Course", 15.99, "Herb-seasoned grilled chicken breast",R.drawable.grilled_chicken))
            add(MenuItem("Beef Steak", "Main Course", 24.99, "Premium cut beef steak",R.drawable.beef_steak))
            add(MenuItem("Fish & Chips", "Main Course", 12.99, "Beer battered fish with fries",R.drawable.fish_and_chips))
            add(MenuItem("Pasta Carbonara", "Main Course", 13.99, "Creamy pasta with bacon",R.drawable.spaghetti_carbonara))

            // Desserts
            add(MenuItem("Chocolate Cake", "Desserts", 6.99, "Rich chocolate layer cake",R.drawable.chocolate_cake))
            add(MenuItem("Ice Cream", "Desserts", 4.99, "Vanilla ice cream with toppings",R.drawable.ice_cream))
            add(MenuItem("Cheesecake", "Desserts", 7.99, "New York style cheesecake",R.drawable.chocolate_cake))

            // Beverages
            add(MenuItem("Coffee", "Beverages", 3.99, "Freshly brewed coffee",R.drawable.coffee))
            add(MenuItem("Orange Juice", "Beverages", 2.99, "Fresh squeezed orange juice",R.drawable.orange_juice))
            add(MenuItem("Soft Drinks", "Beverages", 1.99, "Coke, Pepsi, Sprite",R.drawable.soft_drink))

            // Snacks
            add(MenuItem("French Fries", "Snacks", 4.99, "Crispy golden fries",R.drawable.french_fries))
            add(MenuItem("Onion Rings", "Snacks", 5.99, "Beer battered onion rings",R.drawable.onion_rings))
        }

        filteredMenuItems = allMenuItems.toMutableList()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(categories, this)
        categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderMenuActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter

            addItemDecoration(object : RecyclerView.ItemDecoration() {
                private val space = 16
                override fun getItemOffsets(outRect: android.graphics.Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    val position = parent.getChildAdapterPosition(view)
                    outRect.right = space
                    if (position == 0) {
                        outRect.left = space
                    }
                }
            })
        }
    }


    private fun setupCancelButton() {
        val cancelButton = findViewById<Button>(R.id.btnCancelOrder)
        cancelButton.setOnClickListener {
            showCancelConfirmation()
        }
    }

    override fun onCategoryClick(categoryName: String, position: Int) {
        filterMenuByCategory(categoryName)
    }

    private fun filterMenuByCategory(categoryName: String) {
        filteredMenuItems.clear()

        if (categoryName == "All") {
            filteredMenuItems.addAll(allMenuItems)
        } else {
            filteredMenuItems.addAll(
                allMenuItems.filter { it.category == categoryName }
            )
        }

        updateMenuGrid()
    }

    private fun updateMenuGrid() {
        menuGrid.adapter = MenuItemAdapter(this, filteredMenuItems) { selectedItem ->

            val existingItem = CartManager.cartItems.find { it.name == selectedItem.name }
            if (existingItem != null) {
                existingItem.quantity += 1
            } else {
                val newItem = selectedItem.copy(quantity = 1)
                CartManager.cartItems.add(newItem)
            }

            val cartButton = findViewById<Button>(R.id.btnCart)
            cartButton.isEnabled = true
        }
    }


    private fun showCancelConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Cancel Order")
            .setMessage("Are you sure you want to cancel order?")
            .setPositiveButton("Yes") { _, _ ->
                getSharedPreferences("KioskPrefs", MODE_PRIVATE).edit().clear().apply()
                val intent = Intent(this, OrderTypeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}