package com.example.vender.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vender.Adapter.Adapter_Recruiting;
import com.example.vender.Adapter.Adapter_WorkedTalentList;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.TalentOffer;
import com.example.vender.Model.User;
import com.example.vender.Model.WorkedTalent;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkedTalentActivity extends AppCompatActivity implements Serializable {
    private GlobalUser g = GlobalUser.getInstance();
    private User user;
    private RecyclerView rv;
    private TextView kosong;
    private Toolbar toolbar;
    private Event event;
    private List<WorkedTalent> workedTalents;
    private final static String URL_WORKEDTALENT = "https://venderapp.000webhostapp.com/workedtalent.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_workedtalent);
        user = g.getUser();
        rv = (RecyclerView) findViewById(R.id.rv_workedtalent);
        kosong = (TextView) findViewById(R.id.kosong);
        event = new Event();
        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("event");
        workedTalents = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Talent List");
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_WORKEDTALENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("talent");


                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            int talentid = object.getInt("talentid");
                            String namatalent = object.getString("namatalent");
                            String phonenumber = object.getString("phone_number");
                            String skill = object.getString("skill");
                            String bio = object.getString("bio");
                            String kota = object.getString("kota");
                            String min_fee = object.getString("min_fee");
                            String max_fee = object.getString("max_fee");
                            String experience = object.getString("experience");
                            int rating = object.getInt("rating");
                            int numberofjobs = object.getInt("numberofjobs");
                            String instagram = object.getString("instagram");
                            String youtube = object.getString("youtube");
                            String foto = object.getString("foto");

                            WorkedTalent workedTalent = new WorkedTalent(talentid, rating, numberofjobs, namatalent, phonenumber, skill, bio, kota, min_fee, max_fee, experience, instagram, youtube, foto);
                            workedTalents.add(workedTalent);
                        }

                        RecyclerView myrv = (RecyclerView) findViewById(R.id.rv_workedtalent);
                        Adapter_WorkedTalentList myAdapter = new Adapter_WorkedTalentList(WorkedTalentActivity.this, workedTalents);
                        myrv.setLayoutManager(new GridLayoutManager(WorkedTalentActivity.this,1));
                        myrv.setAdapter(myAdapter);

                    } else if (success.equals("0")){
                        rv.setVisibility(View.GONE);
                        kosong.setVisibility(View.VISIBLE);
//                        Toast.makeText(view.getContext(), "Kosong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(WorkedTalentActivity.this, "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WorkedTalentActivity.this, "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(event.getId()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(WorkedTalentActivity.this);
        requestQueue.add(stringRequest);
    }
}
