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
 * {@link Selling.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Selling#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Selling extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView sellerInformation;
    private TextView informationText;
    private TextView policy;
    private TextView policyText;
    private JSONObject seller;
    private JSONObject returnPolicy;

    private OnFragmentInteractionListener mListener;

    public Selling(JSONObject seller, JSONObject returnPolicy) {
        this.seller = seller;
        this.returnPolicy = returnPolicy;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Selling.
     */
    // TODO: Rename and change types and number of parameters
    public static Selling newInstance(String param1, String param2, JSONObject seller, JSONObject returnPolicy) {
        Selling fragment = new Selling(seller, returnPolicy);
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
        return inflater.inflate(R.layout.fragment_selling, container, false);
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
        this.sellerInformation = view.findViewById(R.id.sellerInformation);
        this.informationText = view.findViewById(R.id.informationText);
        this.policy = view.findViewById(R.id.policy);
        this.policyText = view.findViewById(R.id.policyText);
        JSONArray sellerArray = null;
        String structure = "";
        if (this.seller != null) {
            sellerArray = this.seller.names();
            if (sellerArray.length() == 0) {
                this.sellerInformation.setVisibility(View.GONE);
                this.informationText.setVisibility(View.GONE);
            }
            for(int i = 0; i < sellerArray.length(); i++) {
                try {
                    String key = sellerArray.getString(i);
                    structure += "<p>&#8226;<b>" + key;
                    structure += "</b>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + seller.getString(key);
                    structure += "</p><br>";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.sellerInformation.setVisibility(View.GONE);
            this.informationText.setVisibility(View.GONE);
        }
        structure += "<p>______________________________________________________________<p>";
        this.informationText.setText(Html.fromHtml(structure));
        JSONArray returnPolicyArray = null;
        String structure2 = "";
        if (this.returnPolicy != null) {
            returnPolicyArray = this.returnPolicy.names();
            if (returnPolicyArray.length() == 0) {
                this.policy.setVisibility(View.GONE);
                this.policyText.setVisibility(View.GONE);
            }
            for (int i = 0; i < returnPolicyArray.length(); i ++) {
                try {
                    String key = returnPolicyArray.getString(i);
                    System.out.println(key);
                    structure2 += "<p>&#8226;<b>" + key;
                    structure2 += "</b>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + returnPolicy.getString(key);
                    structure2 += "</p><br>";

                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
            this.policyText.setText(Html.fromHtml(structure2));
        } else {
            this.policy.setVisibility(View.GONE);
            this.policyText.setVisibility(View.GONE);
        }


    }

}
