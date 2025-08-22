import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slimlite.selfservekiosk.R

class CategoryAdapter(
    private val categories: List<Category>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(categoryName: String, position: Int)
    }

    private var selectedPosition = 0 // Track selected category

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

            // Change appearance based on selection
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