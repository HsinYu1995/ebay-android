package com.example.hw9;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class BasicResult extends RecyclerView.Adapter<BasicResult.MyViewHolder> {
    private String[] conditionArray;
    private String[] priceArray;
    private String[] itemTitleArray;
    private String[] imageViewArray;
    private Context context;
    private String[] idArray;
    private String[] freeShippingArray;
    private String[] itemURL;
    private JSONObject[] shippingInfo;
    private String[] topRate;





    BasicResult(Context text, String[] cond, String[] price, String[] itemTitle, String[] imageURL, String[] id, String[] freeShipping, JSONObject[] shippingInfo,
                String[] itemURL, String[] topRate) {
        this.conditionArray = cond;
        this.priceArray = price;
        this.itemTitleArray = itemTitle;
        this.imageViewArray = imageURL;
        this.context = text;
        this.idArray = id;
        this.freeShippingArray = freeShipping; //shipping cost
        this.shippingInfo = shippingInfo;
        this.itemURL = itemURL;
        this.topRate = topRate;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_basic_result, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        System.out.println("on bind");
        if (!imageViewArray[position].equals("no")) {
            Glide.with(context).asBitmap().load(imageViewArray[position]).into(holder.image);
        }
        // maybe need to assign default?
        String price = "<p><font color=\"#32CD32\"><b>$" + priceArray[position] + "</b></font></p>";
        holder.price.setText(Html.fromHtml(price));
        String conditionHTML = "";
        System.out.println(conditionArray[position]);
        if (!conditionArray[position].equals("N/A")) {
            holder.condition.setText(conditionArray[position]);
        } else {
            holder.condition.setText("N/A");

        }



        holder.title.setText(itemTitleArray[position]);
        if (freeShippingArray[position].equals("0.0")) {
            holder.shippingCost.setText("Free Shipping");
        } else {
            String cost = "Ships for $" + freeShippingArray[position];
            holder.shippingCost.setText(cost);
        }
        if (topRate[position].equals("true")) {
            holder.topRateLabel.setVisibility(View.VISIBLE);
        } else {
            holder.topRateLabel.setVisibility(View.GONE);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SingleItem.class);
                intent.putExtra("id", idArray[position]);
                intent.putExtra("price", priceArray[position]);
                intent.putExtra("shipCost", freeShippingArray[position]);
                intent.putExtra("itemTitle", itemTitleArray[position]);
                intent.putExtra("shippingInfo", shippingInfo[position].toString());
                intent.putExtra("itemURL", itemURL[position]);
                v.getContext().startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return conditionArray.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView shippingCost;
        TextView condition;
        TextView price;
        TextView title;
        ImageView image;
        CardView card;
        TextView topRateLabel;
        public MyViewHolder(View v) {
            super(v);
            shippingCost = v.findViewById(R.id.shippingCost);
            condition = v.findViewById(R.id.condition);
            price = v.findViewById(R.id.price);
            title = v.findViewById(R.id.itemTitle);
            this.image = v.findViewById(R.id.imageView);
            this.card = v.findViewById(R.id.card);
            this.topRateLabel = v.findViewById(R.id.topRate);


        }
    }


}
