package com.example.hw9;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SingleItem extends AppCompatActivity implements Product.OnFragmentInteractionListener, Selling.OnFragmentInteractionListener, Shipping.OnFragmentInteractionListener {
    private ProgressBar progressBar ;
    private TextView loadingMessage;
    private String itemId;
    private static String toServerOrg = "https://hw9server-281910.wl.r.appspot.com/singleItem?id=";
    private RequestQueue myqueue;
    private ArrayList<String> pictureURLlist;
    private JSONObject item;
    private ViewPager viewPager;
    private Product product;
    private Shipping shipping;
    private Selling selling;
    private TabLayout tabLayout;
    private String itemTitle;
    private String price;
    private String shipCost;
    private String itemTitleFromOrg;
    private String subtitleName;
    private String fromWhere;
    private ImageView redirect;
    private String itemURL;
    private TextView itemName;
    private String shippingInfo;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        this.progressBar = findViewById(R.id.loadingSingle);
        this.loadingMessage = findViewById(R.id.loadingMessage);
        this.viewPager = findViewById(R.id.pager);
        progressBar.setVisibility(View.GONE);
        loadingMessage.setVisibility(View.GONE);
        Intent intent = getIntent();
        this.itemId = intent.getStringExtra("id");
        this.price = intent.getStringExtra("price");
        this.shipCost = intent.getStringExtra("shipCost");
        this.shippingInfo = intent.getStringExtra("shippingInfo");
        /*
        this.expeditedShipping = intent.getStringExtra("expeditedShipping");
        this.shippingType = intent.getStringExtra("shippingType");
        this.shipToLocation = intent.getStringExtra("shipToLocation");
        this.oneDayShipping = intent.getStringExtra("oneDayShipping");
        this.handlingTime = intent.getStringExtra("handlingTime");
        */

        this.itemTitleFromOrg = intent.getStringExtra("itemTitle");
        this.itemURL = intent.getStringExtra("itemURL");
        this.itemName = findViewById(R.id.itemName);
        this.redirect = findViewById(R.id.redirect);
        this.itemName.setText(itemTitleFromOrg);


        getSupportActionBar().setTitle("");
        //this.fromWhere = intent.getStringExtra("fromWhere");
        String toServerURL = toServerOrg + itemId;
        progressBar.setVisibility(View.VISIBLE);
        loadingMessage.setVisibility(View.VISIBLE);
        myqueue = Volley.newRequestQueue(this);
        System.out.println(toServerURL);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        pictureURLlist = new ArrayList<>();
        toServer(toServerURL);
        System.out.println(toServerURL);








    }
    private void toServer(String id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("SingleItem get response");
                try {
                    System.out.println(id);
                    item = response.getJSONObject("Item");
                    String redirectURL = item.getString("ViewItemURLForNaturalSearch");
                    redirect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent anotherPage = new Intent("android.intent.action.VIEW", Uri.parse(redirectURL));
                            startActivity(anotherPage);

                        }
                    });
                    itemName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent anotherPage = new Intent("android.intent.action.VIEW", Uri.parse(redirectURL));
                            startActivity(anotherPage);
                        }
                    });
                    if (item.has("PictureURL")) {
                        for (int i = 0; i < item.getJSONArray("PictureURL").length(); i++) {
                            pictureURLlist.add(item.getJSONArray("PictureURL").getString(i));
                        }
                    }
                    JSONObject itemSpecifies = null;
                    if (item.has("Title")) {
                        itemTitle = item.getString("Title");
                        System.out.println("itemTitle: " + itemTitle);
                    }
                    if (item.has("ItemSpecifics")) {
                        itemSpecifies = item.getJSONObject("ItemSpecifics");
                    }




                    JSONArray nameValueList;
                    boolean brand = true;
                    String brandValue = "";
                    HashMap<String, String> specifies = new HashMap<>();
                    if (itemSpecifies != null) {
                        nameValueList = itemSpecifies.getJSONArray("NameValueList");
                        if (nameValueList.length() != 0) {
                            for (int i = 0; i < nameValueList.length(); i++) {
                                JSONObject obj = nameValueList.getJSONObject(i);
                                if (i == 0 && !nameValueList.getJSONObject(i).getString("Name").equals("Brand")) {
                                    brand = false;
                                } else if (i == 0 && nameValueList.getJSONObject(i).getString("Name").equals("Brand")) {
                                    brandValue = nameValueList.getJSONObject(i).getJSONArray("Value").getString(0);
                                }
                                if (i != 0) {
                                    String name = nameValueList.getJSONObject(i).getString("Name");
                                    String value = nameValueList.getJSONObject(i).getJSONArray("Value").getString(0);
                                    if (specifies.size() < 5) {
                                        specifies.put(name, value);
                                    }

                                }
                            }
                        }
                    } else {
                        brand = false; // no itemSpecifics means no brand part
                    }
                    boolean productFeatures = false;
                    if (item.has("Subtitle") || brand) {
                        productFeatures = true;
                        if (item.has("Subtitle")) {
                            subtitleName = item.getString("Subtitle");
                        } else {
                            subtitleName = "";
                        }

                    }
                    System.out.println("itemTitleOrg: " + itemTitleFromOrg);
                    JSONObject returnPolicy = null;
                    if (item.has("ReturnPolicy")) {
                        returnPolicy = item.getJSONObject("ReturnPolicy");
                    }
                    JSONObject seller = null;
                    if (item.has("Seller")) {
                        seller = item.getJSONObject("Seller");
                    }


                    progressBar.setVisibility(View.GONE);
                    loadingMessage.setVisibility(View.GONE);
                    //
                    product = new Product(pictureURLlist, itemTitle, price, shipCost, specifies, subtitleName, productFeatures, brandValue);
                    shipping = new Shipping(shippingInfo);
                    selling = new Selling(seller, returnPolicy);
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
                    viewPagerAdapter.addFragment(product, "PRODUCT");
                    viewPagerAdapter.addFragment(selling, "SELLING INFO");
                    viewPagerAdapter.addFragment(shipping, "SHIPPING");


                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.getTabAt(0).setIcon(R.drawable.information_variant_selected);

                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_seller);

                    tabLayout.getTabAt(2).setIcon(R.drawable.truck_delivery_selected);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        myqueue.add(jsonObjectRequest);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();
        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }
        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
    // method to return back without getting null reference
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
