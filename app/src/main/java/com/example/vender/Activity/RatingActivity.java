package com.example.vender.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vender.Adapter.Adapter_RatingList;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.Jobs;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Jobs jobs;
    private List<Jobs> jobsList;
    private User user;
    private Event event;
    private GlobalUser g = GlobalUser.getInstance();
    private static final String URL_RATINGLIST = "https://venderapp.000webhostapp.com/ratinglist.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_rating);
        user = g.getUser();
        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("event");
        jobsList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Give Rating");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        takeData();
    }

    public void takeData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RATINGLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("jobs");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int eventid = object.getInt("eventid");
                                    int talentid = object.getInt("talentid");
                                    String nama = object.getString("nama");
                                    int rating_ = object.getInt("rating");
                                    String foto = object.getString("foto");

                                    jobs = new Jobs(foto, nama, eventid, talentid, rating_);
                                    jobsList.add(jobs);
                                }

                                RecyclerView myrv = (RecyclerView) findViewById(R.id.rv_rating);
                                Adapter_RatingList myAdapter = new Adapter_RatingList(RatingActivity.this, jobsList);
                                myrv.setLayoutManager(new GridLayoutManager(RatingActivity.this,1));
                                myAdapter.notifyDataSetChanged();
                                myrv.setAdapter(myAdapter);

                            } else if (success.equals("0")) {
                                Toast.makeText(RatingActivity.this, "Kosong",
                                        Toast.LENGTH_SHORT).show();
//                                loadingbar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RatingActivity.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
//                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RatingActivity.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
//                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(event.getId()));
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
