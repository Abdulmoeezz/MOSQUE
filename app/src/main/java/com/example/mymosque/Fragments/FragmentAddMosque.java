package com.example.mymosque.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Api.API;
import com.example.mymosque.Api.APIUrl;
import com.example.mymosque.Model.Message;
import com.example.mymosque.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentAddMosque extends Fragment {

    View v;
    Button  AddMosqueBTN;
    EditText Majidame,Personbudy,PersonCOntact;
    ProgressDialog ProDialog;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);











    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_mosque, container, false);

        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add Mosque");



        ProDialog=new ProgressDialog(getActivity());
        ProDialog.setMessage("Thank For  Your Request  \nSending Your Request");
        ProDialog.setCancelable(false);
        Majidame =v.findViewById(R.id.EDT_MasajidName_);
        Personbudy=v.findViewById(R.id.EDT_YourName_);
        PersonCOntact=v.findViewById(R.id.EDT_Contact_);
        AddMosqueBTN=v.findViewById(R.id.BTN_Submit_);

        //</For Toolbar>

AddMosqueBTN.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        checkValidation();


    }
});


        return v;
    }//End onCreateView Method


    public void checkValidation(){
        ProDialog.show();

        final String masjidname = Majidame.getText().toString().trim();
        final String person = Personbudy.getText().toString().trim();
        final String contact = PersonCOntact.getText().toString().trim();

        if (masjidname.isEmpty()) {
            ProDialog.dismiss();

            Majidame.setError("fill this");
            Majidame.requestFocus();
            return;
        }



        if (person.isEmpty()) {
            ProDialog.dismiss();
            Personbudy.setError("fill this");
            Personbudy.requestFocus();
            return;
        }


        if (contact.isEmpty()) {
            ProDialog.dismiss();
            PersonCOntact.setError("fill this");
            PersonCOntact.requestFocus();
            return;
        }

        Toast.makeText(getActivity(),"Your Mosque Request Sent to the Admin", Toast.LENGTH_SHORT).show();
        Majidame.setText("");
        Personbudy.setText("");
        PersonCOntact.setText("");
        Sendfeedback(person,masjidname,contact);


    }





    public  void  Sendfeedback(String Name,String MasajidName,String Contact){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API service = retrofit.create(API.class);

        Call<String> call = service.addmosque(Name,MasajidName,Contact);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                ProDialog.dismiss();
                Toast.makeText(getActivity(),"Your MosQue Request Sent it",Toast.LENGTH_LONG).show();
                Majidame.setText("");
                Personbudy.setText("");
                PersonCOntact.setText("");


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


                Toast.makeText(getActivity(),"Server Problem Contact to System Support",Toast.LENGTH_LONG).show();


            }
        });






        //  Toast.makeText(mContext,"Application Function Not Called",Toast.LENGTH_LONG).show();



    }//End of Function












}


