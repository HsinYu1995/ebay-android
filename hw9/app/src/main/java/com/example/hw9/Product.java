package com.example.hw9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Product.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList image;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout;
    private ImageView imageView;
    private OnFragmentInteractionListener mListener;
    private ImageView redirect;
    private String itemTitleConst;
    private String priceConst;
    private String shipCostConst;
    private HashMap<String, String> specifications;
    private boolean productFeatures;
    private String subtitle;
    private String brandValue;
    private TextView productHtml;
    private TextView brandPart;
    private ImageView productFeacturesLabel;
    private ImageView wrench;
    private TextView specificationText;
    private View secondLine;
    private TextView productFeactureTitle;
    private View firstLine;
    private TextView specificationTitle;
    private LinearLayout brandLayout;



    public Product(ArrayList input, String itemTitle, String price, String shipCost, HashMap<String, String> specification, String subtitle, boolean productFeatures, String brand) {
        this.image = input;
        this.itemTitleConst = itemTitle;
        this.priceConst = price;
        this.shipCostConst = shipCost;
        this.specifications = specification;
        this.subtitle = subtitle;
        this.productFeatures = productFeatures;
        this.brandValue = brand;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Product.
     */
    // TODO: Rename and change types and number of parameters
    public static Product newInstance(String param1, String param2, ArrayList image, String itemTitle, String price, String shipCost, HashMap specifics,
                                      String subtitle, boolean productFeatures, String brand) {
        Product fragment = new Product(image,itemTitle, price, shipCost, specifics, subtitle, productFeatures, brand);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.horizontalScrollView = view.findViewById(R.id.hscrollview);
        this.linearLayout = view.findViewById(R.id.productScroll);
        this.imageView = view.findViewById(R.id.image);
        this.productHtml = view.findViewById(R.id.productHtml);
        this.brandPart = view.findViewById(R.id.brandPart);
        this.productFeacturesLabel = view.findViewById(R.id.productFeatures);
        this.productFeactureTitle = view.findViewById(R.id.textFeatures);
        this.wrench = view.findViewById(R.id.wrench);
        this.specificationText = view.findViewById(R.id.specificationText);
        this.specificationTitle = view.findViewById(R.id.specification);
        this.firstLine = view.findViewById(R.id.firstLine);
        this.secondLine = view.findViewById(R.id.secondLine);
        this.brandLayout = view.findViewById(R.id.brandLayout);




        for (int i = 0; i < image.size(); i++) {
            ImageView newImage = new ImageView(getContext());
            newImage.setAdjustViewBounds(true);
            System.out.println(imageView); // this is null
            System.out.println(this);
            Glide.with(this).asBitmap().load(image.get(i)).into(newImage);

            this.linearLayout.addView(newImage);
            System.out.println(image.get(i));
        }
        String structure = "";
        String addShipCost = "";
        if (shipCostConst.equals("0.0")) {
            addShipCost = "Free Shipping";
        } else {
            addShipCost += " ships for $ " + shipCostConst;
        }
        structure += "<p><big><font color=\"#000\">" + this.itemTitleConst + "</font></big><br><font color=\"#32CD32\">" + "$" + priceConst + "</font></br>&nbsp;&nbsp;" + addShipCost + "</p>";
        productHtml.setText(Html.fromHtml(structure));
        System.out.println("product feacture" + productFeatures);
        System.out.println("brand value" + brandValue);
        if (!productFeatures && brandValue.equals("")) {
            /*
            brandPart.setVisibility(View.GONE);
            productFeacturesLabel.setVisibility(View.GONE);
            this.productFeactureTitle.setVisibility(View.GONE);
            firstLine.setVisibility(View.GONE);

             */
            brandLayout.setVisibility(View.GONE);
        } else {
            String structure2 = "";
            structure2 += "";
            if (!subtitle.equals("")) {
                structure2 += "<p><b>Subtitle</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + subtitle + "</p>";
            }
            if (!brandValue.equals("")) {
                structure2 += "<p><b>Brand</b>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + brandValue + "</p>";
            }

            brandPart.setText(Html.fromHtml(structure2));
            System.out.println("structure2: " + structure2);
            brandPart.setVisibility(View.VISIBLE);
            productFeacturesLabel.setVisibility(View.VISIBLE);
            firstLine.setVisibility(View.VISIBLE);
        }
        System.out.println("specifications size: " + specifications.size());
        if (specifications.size() == 0) {
            secondLine.setVisibility(View.GONE);
            this.wrench.setVisibility(View.GONE);
            this.specificationText.setVisibility(View.GONE);
            this.specificationTitle.setVisibility(View.GONE);
        } else {
            secondLine.setVisibility(View.VISIBLE);
            this.wrench.setVisibility(View.VISIBLE);
            this.specificationText.setVisibility(View.VISIBLE);
            String structure3 = "";
            for (Map.Entry<String, String> entry : specifications.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                structure3 += "<p>&#8226;" + value;


            }
            specificationText.setText(Html.fromHtml(structure3));
        }


        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
}
