package com.cswala.cswala.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cswala.cswala.Adapters.LatestNewsAdapter;
import com.cswala.cswala.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class LatestNewsFragment extends Fragment {

    public JSONArray results_arr;
    private RecyclerView recyclerView;
    LatestNewsAdapter recyclerViewAdapter;
    RequestQueue queue;
    private ProgressBar progressBar;

    public LatestNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_latest_news, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_hf);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        load_hackfeed();

        progressBar = view.findViewById(R.id.progressBar_hf);
        progressBar.setVisibility(View.VISIBLE);

        return view;
    }

    public void load_hackfeed() {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(requireContext());
        String url = "https://api.nytimes.com/svc/topstories/v2/technology.json?api-key=cOqDp0g8yPJ1oDHi64TB3YPm74L2LiJV";

        // Request a string response from the provided URL.
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("json success 1", "json api success");
                        //JSON Request Success
                        try {
                            results_arr = response.getJSONArray("results");
                            recyclerProcess();

                            //progressBar.setVisibility(View.INVISIBLE);

                        } catch (Exception e) {
                            Log.d("json error", "json api error");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("json fail", "json api fail");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void recyclerProcess(){
        //setup adapter
        if(results_arr==null)
            Log.d("results_arr json fail", "results_arr json fail");
        else {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerViewAdapter = new LatestNewsAdapter(getActivity(), results_arr);
            recyclerView.setAdapter(recyclerViewAdapter);

        }
    }

}