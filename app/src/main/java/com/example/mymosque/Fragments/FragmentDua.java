package com.example.mymosque.Fragments;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymosque.Adapter.AdapterDuas;
import com.example.mymosque.HttpHandler;
import com.example.mymosque.Model.DuaList;
import com.example.mymosque.Model.MosqueData;
import com.example.mymosque.R;
import com.example.mymosque.RV_FindMasajid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.constraint.Constraints.TAG;

public class FragmentDua extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    ArrayList<DuaList> duaListArrayList ;
    DuaList duaList;
    String Next_;
    AdapterDuas recyclerViewAdapter ;
    EditText search_masjid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dua, container, false);

        //<For Toolbar>
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Duas");
        //</For Toolbar>




        recyclerView = (RecyclerView)v.findViewById(R.id.RV_DUAS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if(!Next_.equals("null")) {
                        new GetUpdateList().execute();

                    }else{
                        Toast.makeText(getActivity(),"List End " , Toast.LENGTH_LONG).show();
                    }


                }
            }
        });

        duaListArrayList = new ArrayList<>();

        search_masjid = (EditText) v.findViewById(R.id.edit_txt_masjid);

        search_masjid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (recyclerViewAdapter != null) {
                    filter(editable.toString());
                }
            }
        });



        new GetDuas().execute();



        return v;
    }//End onCreateView Method



    private class GetDuas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting  Duas  List");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response


            String jsonStr = sh.makeServiceCall("http://masjidi.co.uk/api/Duas");
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray DATA_ = jsonObj.getJSONArray("data");
                    JSONObject LINKS = jsonObj.getJSONObject("links");



                    String First_ = LINKS.getString("first");
                    String Last_ = LINKS.getString("last");
                    Next_ = LINKS.getString("next");


                    Log.e("Data", "doInBackground: data [] "+DATA_);
                    Log.e("Links", "doInBackground: links {} "+LINKS);
                    Log.e("First", "doInBackground: links:> first "+First_);
                    Log.e("Last", "doInBackground: links:> last "+Last_);
                    Log.e("Next", "doInBackground: links:> next "+Next_);




                    // looping through All Data
                    for (int i = 0; i < DATA_.length(); i++) {

                        duaList = new DuaList();

                        JSONObject c = DATA_.getJSONObject(i);

                        duaList.setD_id(c.getInt("d_id"));
                        duaList.setName(c.getString("name"));
                        duaList.setFile_path(c.getString("file_path"));
                        duaList.setFile_name(c.getString("file_name"));
                        duaList.setDuration(c.getInt("duration"));
                        duaList.setIs_active(c.getString("is_active"));
                        duaList.setTimestamp(c.getString("timestamp"));
                        duaList.setDiscription(c.getString("discription"));
                        duaList.setDiscriptionArbic(c.getString("discriptionArbic"));

                        // adding data HashMap into list
                        duaListArrayList.add(duaList);
                    }
                } catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
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


            //  Updating parsed JSON data into ListRecyclerView

            recyclerViewAdapter = new AdapterDuas(getActivity(),duaListArrayList);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();
        }

    }




    private class GetUpdateList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting  Duas  List");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response

        if(Next_ != null){

            String jsonStr = sh.makeServiceCall(Next_);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray DATA_ = jsonObj.getJSONArray("data");
                    JSONObject LINKS = jsonObj.getJSONObject("links");



                    String First_ = LINKS.getString("first");
                    String Last_ = LINKS.getString("last");
                    Next_ = LINKS.getString("next");


                    Log.e("Data", "doInBackground: data [] "+DATA_);
                    Log.e("Links", "doInBackground: links {} "+LINKS);
                    Log.e("First", "doInBackground: links:> first "+First_);
                    Log.e("Last", "doInBackground: links:> last "+Last_);
                    Log.e("Next", "doInBackground: links:> next "+Next_);




                    // looping through All Data
                    for (int i = 0; i < DATA_.length(); i++) {

                        duaList = new DuaList();

                        JSONObject c = DATA_.getJSONObject(i);

                        duaList.setD_id(c.getInt("d_id"));
                        duaList.setName(c.getString("name"));
                        duaList.setFile_path(c.getString("file_path"));
                        duaList.setFile_name(c.getString("file_name"));
                        duaList.setDuration(c.getInt("duration"));
                        duaList.setIs_active(c.getString("is_active"));
                        duaList.setTimestamp(c.getString("timestamp"));
                        duaList.setDiscription(c.getString("discription"));
                        duaList.setDiscriptionArbic(c.getString("discriptionArbic"));

                        // adding data HashMap into list
                        duaListArrayList.add(duaList);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (final JSONException e) {
                    Log.e("ok", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  Toast.makeText(getActivity(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(getActivity(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                    }
                });

            }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
        }

    }

    private void filter(String text) {
        ArrayList<DuaList> filteredList = new ArrayList<>();

        for (DuaList item : duaListArrayList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        recyclerViewAdapter.filterList(filteredList);
    }

}
