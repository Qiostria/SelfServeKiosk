import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.slimlite.selfservekiosk.CartItemAdapter
import com.slimlite.selfservekiosk.CartManager
import com.slimlite.selfservekiosk.CheckoutActivity
import com.slimlite.selfservekiosk.MenuItem
import com.slimlite.selfservekiosk.R
import java.text.NumberFormat
import java.util.Locale

class CartBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(cartItems: List<MenuItem>): CartBottomSheetFragment {
            val fragment = CartBottomSheetFragment()
            val args = Bundle()
            args.putSerializable("cart", ArrayList(cartItems))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cartList = CartManager.cartItems


        val recyclerView = view.findViewById<RecyclerView>(R.id.cartRecyclerView)
        val totalText = view.findViewById<TextView>(R.id.cartTotal)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fun updateTotal() {
            val total = cartList.sumOf { it.price * it.quantity }
            totalText.text = "Total: ${formatRupiah(total)}"
        }

        val adapter = CartItemAdapter(
            items = CartManager.cartItems,
            readOnly = false
        ) {
            updateTotal()
        }


        recyclerView.adapter = adapter

        // initial total
        updateTotal()

        view.findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
            dismiss()
        }
    }

    private fun formatRupiah(value: Double): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(value)
    }
}
