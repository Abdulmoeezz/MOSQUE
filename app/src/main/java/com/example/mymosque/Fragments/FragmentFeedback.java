package com.example.mymosque.Fragments;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Api.API;
import com.example.mymosque.Api.APIUrl;
import com.example.mymosque.Model.Favourite;
import com.example.mymosque.Model.Message;
import com.example.mymosque.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class FragmentFeedback  extends Fragment {

       View v;
       EditText Feedback,Contact_,Name;
       Button   SendFeedBack;
       String msg,contacts,Name_;
       int UserID;
       ProgressDialog ProDialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);










    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_feedback, container, false);
        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("FeedBack");
        //</For Toolbar>

        ProDialog=new ProgressDialog(getActivity());
        ProDialog.setMessage("Thank For  Your Feedback  \nSending Your Feedback");
        ProDialog.setCancelable(false);


        SharedPreferences UserPerfs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID = UserPerfs.getInt("ID", 0);




        Feedback=v.findViewById(R.id.EDT_Feedback_);
           Name=v.findViewById(R.id.EDT_name_);
           Contact_=v.findViewById(R.id.EDT_personContact_);
           SendFeedBack=v.findViewById(R.id.btn_submit);
           msg= String.valueOf(Feedback.getText());
           contacts= String.valueOf(Contact_.getText());
           Name_= String.valueOf(Name.getText());


           SendFeedBack.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   checkValidation();


               }
           });









        return v;
    }//End onCreateView Method

    public  void  Sendfeedback(int Id,String Message,String contact,String name){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API service = retrofit.create(API.class);

        Call<Message> call = service.Feedback(Id,Message,contact,name);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                ProDialog.dismiss();
                Toast.makeText(getActivity(),"Your Feedback Sent it",Toast.LENGTH_LONG).show();
                Feedback.setText("");
                Contact_.setText("");
                Name.setText("");


            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {


                Toast.makeText(getActivity(),"Server Problem Contact to System Support",Toast.LENGTH_LONG).show();


            }
        });






        //  Toast.makeText(mContext,"Application Function Not Called",Toast.LENGTH_LONG).show();



    }//End of Function
    public void checkValidation(){

        msg = Feedback.getText().toString().trim();
        contacts = Contact_.getText().toString().trim();
        Name_ = Name.getText().toString().trim();

        if (msg.isEmpty()) {
            Feedback.setError("fill this");
            Feedback.requestFocus();
            return;
        }



        if (contacts.isEmpty()) {
            Contact_.setError("fill this");
            Contact_.requestFocus();
            return;
        }


        if (Name_.isEmpty()) {
            Name.setError("fill this");
            Name.requestFocus();
            return;
        }


        ProDialog.show();
        Sendfeedback(UserID,msg,contacts,Name_);
        Toast.makeText(getActivity(),"Your Mosque Request Sent to the Admin", Toast.LENGTH_SHORT).show();
        Feedback.setText("");
        Contact_.setText("");
        Name.setText("");

    }






}
