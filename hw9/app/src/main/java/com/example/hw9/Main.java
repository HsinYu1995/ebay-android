package com.example.hw9;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public class Main extends AppCompatActivity {
    private Spinner spinner;
    private LinearLayout linearLayout;
    private Button search;
    private Button clear;
    private EditText keywordInput;
    private EditText min;
    private EditText max;
    private CheckBox newCheck;
    private CheckBox usedCheck;
    private CheckBox unspecifiedCheck;
    private TextView errorKeyword;
    private TextView errorValue;
    private TextView test;
    private String generalURL = "";
    private String singleURL = "";
    private boolean errorHappened = false;
    private static final String server = "https://hw9server-281910.wl.r.appspot.com/hw9?";
    private RequestQueue myqueue;


    private final String[] CHOICE = {"Best Match", "Price: highest first","Price + shipping: Highest first", "Price + shipping: Lowest first"};

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.match);
        ArrayAdapter<String> sort = new ArrayAdapter<>(Main.this, android.R.layout.simple_spinner_dropdown_item, CHOICE);
        spinner.setAdapter(sort);
        // setting up and initialize
        search = findViewById(R.id.search);
        clear = findViewById(R.id.clear);
        myqueue = Volley.newRequestQueue(this);
        searchFunction();
        clearfunction();














    }
    private void searchFunction() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                generalURL = "";
                System.out.println(v);
                keywordInput = findViewById(R.id.keywordInput);
                min = findViewById(R.id.min);
                max = findViewById(R.id.max);
                newCheck = findViewById(R.id.newCheck);
                usedCheck = findViewById(R.id.used);
                unspecifiedCheck = findViewById(R.id.unspecified);
                errorKeyword = findViewById(R.id.errorKeyword);
                errorValue = findViewById(R.id.errorPrice);
                String getKeyword = keywordInput.getText().toString();
                String getMin = min.getText().toString();
                String getMax = max.getText().toString();
                int count = 0; // itemFilter index
                int duration = Toast.LENGTH_SHORT;
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Please fix all fields with errors", duration);
                if (getKeyword.equals("")) {
                    errorKeyword.setVisibility(View.VISIBLE);
                    errorHappened = true;

                    toast.show();
                } else {
                    errorHappened = false;
                }
                // to check the price range



                if (!getMin.equals("") && !getMax.equals("")) {
                    double minValue = Double.valueOf(getMin);
                    double maxValue = Double.valueOf(getMax);
                    if (minValue < 0 || maxValue < 0 || minValue > maxValue) {
                        errorValue.setVisibility(View.VISIBLE);
                        errorHappened = true;
                        toast.show();
                    }
                } else if (!getMin.equals("")) {
                    if (Double.valueOf(getMin) < 0) {
                        errorValue.setVisibility(View.VISIBLE);
                        errorHappened = true;
                        toast.show();
                    }
                } else if (!getMax.equals("")){
                    if (Double.valueOf(getMax) < 0) {
                        errorValue.setVisibility(View.VISIBLE);
                        errorHappened = true;
                        toast.show();

                    }
                } else {
                    errorValue.setVisibility(View.GONE);
                }
                // end the function if errors happen
                if (errorHappened) {
                    return;
                }
                errorValue.setVisibility(View.GONE);
                errorKeyword.setVisibility(View.GONE);

                generalURL += "keywords=";
                String noSpaceKeyword = getKeyword.replace(" ", "%20");
                generalURL += noSpaceKeyword;
                generalURL += "&sortOrder=";
                String selectedSort = spinner.getSelectedItem().toString();
                if (selectedSort.equals("Best Match")) {
                    generalURL += "BestMatch&";
                } else if (selectedSort.equals("Price: highest first")) {
                    generalURL += "CurrentPriceHighest&";
                } else if (selectedSort.equals("Price + shipping: Highest first")) {
                    generalURL += "PricePlusShippingHighest&";
                } else {
                    generalURL += "PricePlusShippingLowest&";
                }
                // Price Range part
                if (!getMin.equals("")) {
                    generalURL += "itemFilter(" + Integer.toString(count) + ").name=MinPrice&";
                    generalURL += "itemFilter(" + Integer.toString(count) + ").value=" + getMin;
                    generalURL += "&itemFilter(" + Integer.toString(count)+ ").paramName=Currency&" +
                            "itemFilter(" + Integer.toString(count) + ").paramValue=USD&";
                    count++;
                }
                if (!getMax.equals("")) {
                    generalURL += "itemFilter(" + Integer.toString(count) + ").name=MaxPrice&";
                    generalURL += "itemFilter(" + Integer.toString(count) + ").value=" + getMax;
                    generalURL += "&itemFilter(" + Integer.toString(count)+ ").paramName=Currency&" +
                            "itemFilter(" + Integer.toString(count) + ").paramValue=USD&";
                    count++;
                }
                if (newCheck.isChecked() || usedCheck.isChecked() || unspecifiedCheck.isChecked()) {
                    generalURL += "itemFilter(" + Integer.toString(count) + ").name=Condition&";
                }
                int conditionIndex = 0;
                if (newCheck.isChecked()) {
                    generalURL += "itemFilter(" + Integer.toString(count) + ").value(" + Integer.toString(conditionIndex) + ")=New&";
                    conditionIndex++;
                }
                if (usedCheck.isChecked()) {
                    generalURL += "itemFilter(" + Integer.toString(count) + ").value(" + Integer.toString(conditionIndex) + ")=Used&";
                    conditionIndex++;
                }
                if (unspecifiedCheck.isChecked()) {
                    System.out.println("in the if unspecificied");
                    generalURL += "itemFilter(" + Integer.toString(count) + ").value(" + Integer.toString(conditionIndex) + ")=Unspecified&";
                }
                //httpGetRequest = new HttpGetRequest(generalURL);
                String toServer = server + generalURL;
                System.out.println("toServer" + toServer);
                Intent intent = new Intent(v.getContext(), BasicItem.class);
                intent.putExtra("keywords", getKeyword);
                intent.putExtra("url", toServer);

                startActivity(intent);

            }
        });
    }
    private void clearfunction() {
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }
}
