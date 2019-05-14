package com.example.mymosque.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.AdapterRVFavorite;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.MainActivity;
import com.example.mymosque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class FragmentNearestJummah extends Fragment {


    View v;
    Spinner Spinner;
    Button  SearchButton_;
    EditText EDT_Town_city_,EDT_PostalCode_;
    String PostalCode,City,Jummah;







    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);










    }//end of onCreate method





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_nearest_jummah, container, false);

        //For Toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Nearest Jummah");
        //For Toolbar


/*

        SharedPreferences postalCode = getActivity().getSharedPreferences("PostalCodePass", MODE_PRIVATE);
        PostalCode=postalCode.getString("PostalCode","");
*/


        SharedPreferences CityEd = getActivity().getSharedPreferences("CityPass", MODE_PRIVATE);
        City=CityEd.getString("city","");



       // Spinner=(Spinner) v.findViewById(R.id.JummahSpinner_);
        SearchButton_=v.findViewById(R.id.BTN_Search_);
        EDT_Town_city_=v.findViewById(R.id.EDT_Town_city_);
        EDT_PostalCode_=v.findViewById(R.id.EDT_PostalCode_);

        try{

            EDT_Town_city_.setText(City);
            PostalCode=EDT_PostalCode_.getText().toString().trim();
            //EDT_PostalCode_.setText(PostalCode);




        }catch (Exception ex){ex.printStackTrace();}





  /*      EDT_PostalCode_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new PostalCodeFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area, myFragment).addToBackStack(null).commit();


            }
        });*/








        EDT_Town_city_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new CityFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area, myFragment).addToBackStack(null).commit();



            }
        });




        String[] buy = new String[]{
                "First Jummah",
                "Second Jummah"};


    /*    final List<String> List = new ArrayList<>(Arrays.asList(buy));
        SpinnerMethod(Spinner,List);*/



        SearchButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(EDT_PostalCode_.getText().toString().trim().equals("") && City.equals("")){

                    Toast.makeText(getActivity(),"Please Fill First Postal Code or City",Toast.LENGTH_SHORT).show();


                }else {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("PassNearestMosqueData", MODE_PRIVATE).edit();
                    editor.putString("PostalCode",EDT_PostalCode_.getText().toString().trim());
                    editor.putString("City",City);
                    editor.putString("Jummah",Jummah);
                    editor.apply();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new ResultNearestJummah()).commit();

                }

            }
        });





        return v;
    }//End onCreateView Method



    public void SpinnerMethod(Spinner spinner, List plantsList){

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_items,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_items);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){

                    if(selectedItemText.equals("First Jummah")){

                            Toast.makeText(getActivity(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                            Jummah="true";

                    }if(selectedItemText.equals("Second Jummah")){

                        Toast.makeText(getActivity(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                        Jummah="false";
                    }



                }//end of postion
            }//end of method spinner

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    }//enof SpinnerMethod
    public void  Alert(Context context){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Sorry Adminstrator Not Provide A Google Api  Paid Key!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.Screen_Area,new FragmentHome()).commit();


                    }
                });

        builder1.setNegativeButton(
                "",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();







    }


}
