package com.example.android.justkotlin



import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat


/**
 * This app displays an order form to order coffee.
 */
class MainActivity : AppCompatActivity() {

    var quantity = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * This method is called when the order button is clicked.
     */
    fun submitOrder(view: View?) {
        // Get name of the user
        val nameField = findViewById<EditText>(R.id.name_field)
        val name = nameField.text.toString()

        // Figure out if the user wants whipped cream topping
        val whippedCreamCheckBox = findViewById<CheckBox>(R.id.whipped_cream_checkbox)
        val hasWhippedCream = whippedCreamCheckBox.isChecked

        // Figure out if the user wants chocolate topping
        val chocolateCheckBox = findViewById<CheckBox>(R.id.chocolate_checkbox)
        val hasChocolate = chocolateCheckBox.isChecked

        // Calculate the price
        val price = calculatePrice(hasWhippedCream, hasChocolate)
        val priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate)

        // Display the order summary on the screen
        displayMessage(priceMessage)

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name))
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No app available to handle Intent", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream is whether or not user wants whipped cream topping
     * @param addChocolate is whether or not user wants chocolate topping
     * @return total price
     */
    private fun calculatePrice(addWhippedCream: Boolean, addChocolate: Boolean) : Int {
        // Base price of one cup of coffee.
        var pricePerCup = 5

        // Add $1 if user wants whipped cream topping.
        if (addWhippedCream) pricePerCup += 1

        // Add $2 if user wants chocolate topping.
        if (addChocolate) pricePerCup += 2

        // Calculate total price by multiplying price per cup with quantity.
        return quantity * pricePerCup
    }

    /**
     * Creates summary of the order.
     *
     * @param name of the user.
     * @param priceOfOrder price of the order
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not user wants chocolate topping
     * @return message containing the summary
     */
    private fun createOrderSummary(name: String, priceOfOrder : Int, addWhippedCream: Boolean, addChocolate: Boolean) : String {
        return getString(R.string.order_summary_name, name) +
                "\n${getString(R.string.order_summary_whipped_cream, addWhippedCream)}" +
                "\n${getString(R.string.order_summary_chocolate, addChocolate)}" +
                "\n${getString(R.string.order_summary_quantity, quantity)}" +
                "\n${getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(priceOfOrder))}" +
                "\n${getString(R.string.thank_you)}"
    }

    /**
     * This method displays the given text on the screen.
     */
    private fun displayMessage(message: String) {
        val orderSummaryTextView = findViewById<TextView>(R.id.order_summary_text_view)
        orderSummaryTextView.text = message
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private fun displayQuantity(number: Int) {
        val quantityTextView =
            findViewById<View>(R.id.quantity_text_view) as TextView
        quantityTextView.text = "" + number
    }

    /**
     * This method is called when the plus button is clicked.
     */
    fun increment(view: View?) {
        if (quantity == 100){
            // show error message
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show()
            return
        }
        quantity++
        displayQuantity(quantity)
    }

    /**
     * This method is called when the minus button is clicked.
     */
    fun decrement(view: View?) {
        if (quantity == 1) {
            // show error message
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show()
            return
        }
        quantity--
        displayQuantity(quantity)
    }




}