package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText( this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT ).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity + 1;
        displayQuantity( quantity );
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 0) {
            // Show an error message as a toast
            Toast.makeText( this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT ).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity - 1;
        displayQuantity( quantity );
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Get user's name
        EditText nameField = findViewById( R.id.name_field );
        Editable nameEditable = nameField.getText();
        String name = nameEditable.toString();

        // Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckBox = findViewById( R.id.whipped_cream_checkbox );
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the user wants chocolate topping
        CheckBox chocolateCheckBox = findViewById( R.id.chocolate_checkbox );
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // Calculate the price
        int price = calculatePrice( hasWhippedCream, hasChocolate );

        // Display the order summary on the screen
        String message = createOrderSummary( name, price, hasWhippedCream, hasChocolate );

        // Use an intent to launch an email app.
        // Send the order summary in the email body.
        Intent intent = new Intent( Intent.ACTION_SENDTO );
        intent.setData( Uri.parse( "mailto:" ) ); // only email apps should handle this
        intent.putExtra( Intent.EXTRA_SUBJECT, "Just Java order for " + name );
        intent.putExtra( Intent.EXTRA_TEXT, message );

        if (intent.resolveActivity( getPackageManager() ) != null) {
            startActivity( intent );
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream is whether or not we should include whipped cream topping in the price
     * @param addChocolate    is whether or not we should include chocolate topping in the price
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        // First calculate the price of one cup of coffee
        int basePrice = 5;

        // If the user wants whipped cream, add $1 per cup
        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }

        // If the user wants chocolate, add $2 per cup
        if (addChocolate) {
            basePrice = basePrice + 2;
        }

        // Calculate the total order price by multiplying by the quantity
        return quantity * basePrice;
    }

    /**
     * Create summary of the order.
     *
     * @param name            on the order
     * @param price           of the order
     * @param addWhippedCream is whether or not to add whipped cream to the coffee
     * @param addChocolate    is whether or not to add chocolate to the coffee
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream,
                                      boolean addChocolate) {
        String priceMessage = "Name:" + name;
        priceMessage += "\nAdd Whipped cream?" + addWhippedCream;
        priceMessage += "\nAdd Chocolate?" + addChocolate;
        priceMessage += "\nQuantity:" + quantity;
        priceMessage += "\nTotal: $" + price;
        NumberFormat.getCurrencyInstance().format( price );
        priceMessage += "\nThank you!";
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = findViewById(
                R.id.quantity_text_view );
        quantityTextView.setText( "" + numberOfCoffees );
    }
}


