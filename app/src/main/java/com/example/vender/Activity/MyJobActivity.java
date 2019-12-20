package com.example.vender.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.vender.Adapter.Adapter_MyEvent;
import com.example.vender.Adapter.Adapter_MyJob;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.MyJob;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyJobActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private static final String URL_MYJOBLIST = "https://venderapp.000webhostapp.com/myjoblist.php";
    private User user;
    private GlobalUser g = GlobalUser.getInstance();
    private RecyclerView rv;
    private TextView kosong;
    private List<MyJob> myJobs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myjob);
        user = g.getUser();
        myJobs = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv_myjob);
        kosong = (TextView) findViewById(R.id.kosong);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Job");
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MYJOBLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("event");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("id");
                            String nama_event = object.getString("nama_event");
                            String deskripsi = object.getString("deskripsi");
                            String kota = object.getString("kota");
                            String venue = object   .getString("venue");
                            String tanggal_event = object.getString("tanggal_event");
                            String foto = object.getString("foto");
                            String status = object.getString("status");
                            String lowongan = object.getString("lowongan");
                            String min_fee = object.getString("min_fee");
                            String max_fee = object.getString("max_fee");
                            int user_id = object.getInt("user_id");
                            int rating_ = object.getInt("rating");
                            String phonenumber = object.getString("phonenumber");

                            MyJob item = new MyJob(id, user_id, nama_event, deskripsi, kota, venue, tanggal_event,
                                    foto, status, lowongan, min_fee, max_fee, rating_, phonenumber);
                            myJobs.add(item);
                        }

                        RecyclerView myrv = (RecyclerView) findViewById(R.id.rv_myjob);
                        Adapter_MyJob myAdapter = new Adapter_MyJob(MyJobActivity.this, myJobs);
                        myrv.setLayoutManager(new LinearLayoutManager(MyJobActivity.this));
                        myrv.setAdapter(myAdapter);

                    } else if (success.equals("0")){
                        rv.setVisibility(View.GONE);
                        kosong.setVisibility(View.VISIBLE);
//                        Toast.makeText(MyJobActivity.this, "Belum ada pekerjaan", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MyJobActivity.this, "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyJobActivity.this, "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getId()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MyJobActivity.this);
        requestQueue.add(stringRequest);

    }
}
