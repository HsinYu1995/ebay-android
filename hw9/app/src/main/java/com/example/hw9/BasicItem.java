package com.example.hw9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

public class BasicItem extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String numberOfItems;
    private String toServerURL;
    private String displayKeyword;
    private RequestQueue myqueue;
    private TextView displayTitle;
    private String[] priceArray;
    private String[] conditionArray;
    private String[] itemTitleArray;
    private String[] imageArray;
    private String[] idArray;
    private String[] freeShippingArray;
    private CardView card;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String[] expeditedShippingArray;
    private String[] shippingTypeArray;
    private String[] shipToLocationArray;
    private String[] oneDayShippingArray;
    private String[] handlingTimeArray;
    private String[] fromWhere;
    private String[] itemURL;
    private TextView noResult;
    private JSONObject [] shippingInfo;
    private TextView loadingText;
    private LinearLayout reloadingItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("on create");
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Search Result");
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        setContentView(R.layout.activity_basic_item);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        Intent intent = getIntent();
        this.displayKeyword = intent.getStringExtra("keywords");
        this.toServerURL = intent.getStringExtra("url");
        this.progressBar = findViewById(R.id.loadingSingle);
        this.loadingText = findViewById(R.id.searching);
        this.card = findViewById(R.id.card);
        displayTitle = findViewById(R.id.displayTitle);
        this.myqueue = Volley.newRequestQueue(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        this.noResult = findViewById(R.id.noResult);
        this.reloadingItem = findViewById(R.id.reloadingItem);
        getData();

    }
    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, toServerURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("here");
                try {
                    swipeRefreshLayout.setRefreshing(true);
                    String totalEntries = response.get("totalEntries").toString();
                    if (totalEntries.equals("0")) {
                        noResult.setVisibility(View.VISIBLE);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, "No Result", duration);
                        toast.show();
                        reloadingItem.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                    int arraySize = Integer.parseInt(response.get("correctItem").toString());
                    numberOfItems = Integer.toString(arraySize);
                    String[] imageURL = new String[arraySize];
                    String[] itemTitle = new String[arraySize];
                    String[] condition = new String[arraySize];
                    String[] topRate = new String[arraySize];
                    String[] freeShipping = new String[arraySize];
                    String[] price = new String[arraySize];
                    String[] itemId = new String[arraySize];
                    String[] expedited = new String[arraySize];
                    String[] shippingType = new String[arraySize];
                    String[] shipToLocation = new String[arraySize];
                    String[] oneDayShipping = new String[arraySize];
                    String[] handlingTime = new String[arraySize];
                    itemURL = new String[arraySize];
                    fromWhere = new String[arraySize];
                    shippingInfo = new JSONObject[arraySize];


                    for (int i = 0; i < arraySize; i++) {
                        System.out.println(response.getJSONObject("totalItem").getJSONObject(Integer.toString(i)));
                        JSONObject eachItem = response.getJSONObject("totalItem").getJSONObject(Integer.toString(i));
                        imageURL[i] = eachItem.get("galleryURL").toString();
                        itemTitle[i] = eachItem.get("title").toString();
                        condition[i] = eachItem.get("conditionDisplayName").toString();
                        topRate[i] = eachItem.get("topRatedListing").toString();
                        freeShipping[i] = eachItem.get("shippingServiceCost").toString();
                        price[i] = eachItem.get("convertedCurrentPrice").toString();
                        itemId[i] = eachItem.get("itemId").toString(); // for singleItem search
                        itemURL[i] = eachItem.get("viewItemURL").toString();
                        shippingInfo[i] = eachItem.getJSONObject("shippingInfo");


                    }
                    priceArray = price;
                    conditionArray = condition;
                    System.out.println(conditionArray.toString());
                    itemTitleArray = itemTitle;
                    imageArray = imageURL;
                    idArray = itemId;
                    freeShippingArray = freeShipping;
                    expeditedShippingArray = expedited;
                    shippingTypeArray = shippingType;
                    shipToLocationArray = shipToLocation;
                    oneDayShippingArray = oneDayShipping;
                    handlingTimeArray = handlingTime;
                    reloadingItem.setVisibility(View.GONE);
                    String display = "<p>Showing " + "<font color=\"#091AFA\">" + numberOfItems + "</font>"  + " results for " +
                            "<font color=\"#091AFA\">" + displayKeyword + "</font></p>";
                    displayTitle.setVisibility(View.VISIBLE);
                    displayTitle.setText(Html.fromHtml(display));
                    RecyclerView recyclerView = findViewById(R.id.basicItem);
                    BasicResult basic = new BasicResult(BasicItem.this, conditionArray, priceArray, itemTitleArray,
                            imageArray, idArray, freeShippingArray, shippingInfo, itemURL, topRate);
                    recyclerView.setAdapter(basic);
                    swipeRefreshLayout.setRefreshing(false);


                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);

                error.printStackTrace();
            }
        });
        myqueue.add(jsonObjectRequest);


    }
    @Override
    public void onRefresh() {
        getData();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
