package com.example.hw9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Shipping.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Shipping#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shipping extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView shippintText;
    private String shippingInfo;
    private JSONObject ship;
    private TextView shipInfoLabel;

    private OnFragmentInteractionListener mListener;

    public Shipping(String shippingInfo) {
        this.shippingInfo = shippingInfo;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Shipping.
     */
    // TODO: Rename and change types and number of parameters
    public static Shipping newInstance(String param1, String param2, String shippingInfo) {
        Shipping fragment = new Shipping(shippingInfo);
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
        return inflater.inflate(R.layout.fragment_shipping, container, false);
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        this.shippintText = view.findViewById(R.id.shippingText);
        this.shipInfoLabel = view.findViewById(R.id.shippingInformation);
        try {
            this.ship = new JSONObject(shippingInfo);
        } catch (JSONException err) {
            err.printStackTrace();
        }
        String structure = "";
        JSONArray shipArray = ship.names();
        for (int i = 0; i < shipArray.length(); i++) {
            try {
                if (!shipArray.getString(i).equals("shippingServiceCost")) {
                    structure += "<p>&#8226;<b>" + shipArray.getString(i) +"</b>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    String value = ship.getString(shipArray.getString(i)).replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
                    if (value.equals("false")) {
                        value = "No";
                    } else if (value.equals("true")){
                        value = "Yes";
                    }
                    structure += value + "</p><br>";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        this.shippintText.setText(Html.fromHtml(structure));
        if (shipArray.length() == 0) {
            this.shipInfoLabel.setVisibility(View.GONE);
            this.shippintText.setVisibility(View.GONE);
        }






    }

}
