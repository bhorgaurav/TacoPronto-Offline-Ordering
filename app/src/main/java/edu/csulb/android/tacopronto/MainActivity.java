package edu.csulb.android.tacopronto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 23;
    private float totalPrice = 0f;
    boolean fillingsSelected = false;
    private StringBuilder sb = new StringBuilder();
    public static ArrayList<Item> fillings, beverages, sizes, tortillas;

    private Button buttonPlaceOrder;
    private LinearLayout fillings1, fillings2, beverages1, beverages2;
    private RadioGroup radioGroupSize, radioGroupTortilla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlaceOrder = (Button) findViewById(R.id.button_place_order);
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                sb.setLength(0);
                totalPrice = 0f;
                sb.append("TACOPRONTO ORDER");
                sb.append("\n");

                // Collect values and form SMS
                setSize();
                setTortilla();
                setFillings();
                setBeverage();

                if (fillingsSelected) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {

                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    } else {
                        sendSMS();
                    }
                }


            }
        });

        radioGroupSize = (RadioGroup) findViewById(R.id.radio_group_size);
        radioGroupSize.check(R.id.radio_button_medium);

        radioGroupTortilla = (RadioGroup) findViewById(R.id.radio_group_tortilla);
        radioGroupTortilla.check(R.id.radio_button_corn);

        fillings1 = (LinearLayout) findViewById(R.id.linear_layout_fillings1);
        fillings2 = (LinearLayout) findViewById(R.id.linear_layout_fillings2);
        beverages1 = (LinearLayout) findViewById(R.id.linear_layout_beverages1);
        beverages2 = (LinearLayout) findViewById(R.id.linear_layout_beverages2);
    }

    private void sendSMS() {

        sb.append("\n");
        sb.append("Price: $").append(String.valueOf(totalPrice));

        System.out.println(sb.toString());
        
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("5623503109", null, sb.toString(), null, null);

        Snackbar.make(findViewById(android.R.id.content), "Order placed! Your Total: " + totalPrice, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.BLUE).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    sendSMS();
                } else {
                    Toast.makeText(this, "Permission is required.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void setSize() {
        int count = radioGroupSize.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton button = (RadioButton) radioGroupSize.getChildAt(i);
            if (button.isChecked()) {
                for (Item item : sizes) {
                    if (item.getName().equalsIgnoreCase(button.getText().toString())) {
                        totalPrice += item.getPrice();
                        sb.append("Size: " + item.getName());
                    }
                }
            }
        }
    }

    public void setTortilla() {
        int count = radioGroupTortilla.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton button = (RadioButton) radioGroupTortilla.getChildAt(i);
            if (button.isChecked()) {
                for (Item item : tortillas) {
                    if (item.getName().equalsIgnoreCase(button.getText().toString())) {
                        totalPrice += item.getPrice();
                        sb.append("\n");
                        sb.append("Tortilla: " + item.getName());
                    }
                }
            }
        }
    }

    public void setFillings() {

        String allFillings = "";

        int count = fillings1.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckBox button = (CheckBox) fillings1.getChildAt(i);
            if (button.isChecked()) {
                fillingsSelected = true;
                for (Item item : fillings) {
                    if (item.getName().equalsIgnoreCase(button.getText().toString())) {
                        totalPrice += item.getPrice();
                        allFillings += item.getName() + " ";
                    }
                }
            }
        }

        count = fillings2.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckBox button = (CheckBox) fillings2.getChildAt(i);
            if (button.isChecked()) {
                fillingsSelected = true;
                for (Item item : fillings) {
                    if (item.getName().equalsIgnoreCase(button.getText().toString())) {
                        totalPrice += item.getPrice();
                        allFillings += item.getName() + " ";
                    }
                }
            }
        }

        if (fillingsSelected) {
            sb.append("\n");
            sb.append("Fillings: ").append(allFillings);
        } else {
            Toast.makeText(this, "Please select at least one filling.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setBeverage() {

        boolean beveragesSelected = false;
        String allBeverages = "";

        int count = beverages1.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckBox button = (CheckBox) beverages1.getChildAt(i);
            if (button.isChecked()) {
                beveragesSelected = true;
                for (Item item : beverages) {
                    if (item.getName().equalsIgnoreCase(button.getText().toString())) {
                        totalPrice += item.getPrice();
                        allBeverages += item.getName() + " ";
                    }
                }
            }
        }

        count = beverages2.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckBox button = (CheckBox) beverages2.getChildAt(i);
            if (button.isChecked()) {
                beveragesSelected = true;
                for (Item item : beverages) {
                    if (item.getName().equalsIgnoreCase(button.getText().toString())) {
                        totalPrice += item.getPrice();
                        allBeverages += item.getName() + " ";
                    }
                }
            }
        }

        if (beveragesSelected) {
            sb.append("\n");
            sb.append("Beverages: ").append(allBeverages);
        }
    }

    static {
        sizes = new ArrayList<>();
        sizes.add(new Item("Small", 3));
        sizes.add(new Item("Medium", 4));
        sizes.add(new Item("Large", 5));

        tortillas = new ArrayList<>();
        tortillas.add(new Item("Corn", 0.5f));
        tortillas.add(new Item("Flour", 0.8f));

        fillings = new ArrayList<>();
        fillings.add(new Item("Beef", 1.1f));
        fillings.add(new Item("Chicken", 1.2f));
        fillings.add(new Item("White Fish", 1.3f));
        fillings.add(new Item("Cheese", 1.4f));
        fillings.add(new Item("Sea Food", 1.5f));
        fillings.add(new Item("Rice", 1.6f));
        fillings.add(new Item("Beans", 1.7f));
        fillings.add(new Item("Pico de Gallo", 1.8f));
        fillings.add(new Item("Guacamole", 1.9f));
        fillings.add(new Item("LBT", 2));

        beverages = new ArrayList<>();
        beverages.add(new Item("Soda", 1));
        beverages.add(new Item("Margarita", 1.1f));
        beverages.add(new Item("Tequilla", 1.2f));
        beverages.add(new Item("Cerveza", 1.3f));
    }
}
