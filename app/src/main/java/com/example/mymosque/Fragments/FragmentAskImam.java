package com.example.mymosque.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Adapter.AdapterAskimamRV;
import com.example.mymosque.AdapterRVFavorite;
import com.example.mymosque.Api.API;
import com.example.mymosque.Api.APIUrl;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.Model.AskImam;
import com.example.mymosque.Model.PrimaryMosque;
import com.example.mymosque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class FragmentAskImam extends Fragment {


    View v;
    EditText Question;
    Button   Send;
    String PM_MosqueID,UserID;

    ArrayList<HashMap<String, String>> AnswersList ;
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    public RelativeLayout Layout_noMsg ;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);











    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ask_imam, container, false);



        SharedPreferences prefss = getActivity().getSharedPreferences("GetPrimaryMosque", MODE_PRIVATE);
        PM_MosqueID =    prefss.getString("PM_ID", null);

        SharedPreferences prefs = getActivity().getSharedPreferences("My_SP", MODE_PRIVATE);
        UserID = String.valueOf(prefs.getInt("ID", 0));

        Layout_noMsg = v.findViewById(R.id.Layout_noMsg);


        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Ask Imam");
        //</For Toolbar>

        Question =(EditText) v.findViewById(R.id.msgEDT);
        Send= v.findViewById(R.id.BTN_SEND);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                 SetMsg( Integer.parseInt(PM_MosqueID),  Integer.parseInt(UserID),String.valueOf(Question.getText()));
                 hideKeyboard(getActivity());
                 Question.setText("");
                 new GetingAnswersList().execute();


            }
        });




        new GetingAnswersList().execute();


        return v;
    }//End onCreateView Method

    public  void  SetMsg(int M,int U,String Msg){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API service = retrofit.create(API.class);

        Call<ArrayList<AskImam>> call = service.AskImam(M,U,Msg);

        call.enqueue(new Callback<ArrayList<AskImam>>() {
            @Override
            public void onResponse(Call<ArrayList<AskImam>> call, Response<ArrayList<AskImam>> response) {


                //Toast.makeText(getActivity(),"Question # "+response.body().getQuestiion(),Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(),"Your Question To Imam Has been Sent",Toast.LENGTH_LONG).show();





            }

            @Override
            public void onFailure(Call<ArrayList<AskImam>> call, Throwable t) {


                Toast.makeText(getActivity(),"Server Problem Contact to System Support",Toast.LENGTH_LONG).show();


            }
        });






        //  Toast.makeText(mContext,"Application Function Not Called",Toast.LENGTH_LONG).show();



    }//End of Function




    private class GetingAnswersList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Your Answer List");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            AnswersList  = new ArrayList<>();
            AnswersList.clear();
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/ask/"+PM_MosqueID+"/"+UserID);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray json = new JSONArray(jsonStr);

                    // looping through All Data
                    for (int i = 0; i < json.length(); i++) {



                        JSONObject e = json.getJSONObject(i);



                        String questiion = e.getString("questiion");
                        String anwer = e.getString("anwer");


                        // tmp hash map for single Data Array
                        HashMap<String, String> data_Hashmap = new HashMap<>();

                        // adding each child node to HashMap key => value

                        data_Hashmap.put("questiion_", questiion);
                        data_Hashmap.put("answer", anwer);





                        AnswersList.add(data_Hashmap);



                    }
                } catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

                recyclerView = (RecyclerView) v.findViewById(R.id.RV_Questions_Answers);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);

                //  Updating parsed JSON data into ListRecyclerView
            Collections.reverse(AnswersList);
                AdapterAskimamRV adapter = new AdapterAskimamRV(getActivity(), AnswersList);
                if(adapter.getItemCount() != 0){
                    Layout_noMsg.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


        }

    }


    //FOR HIDE KEYBOARD
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
