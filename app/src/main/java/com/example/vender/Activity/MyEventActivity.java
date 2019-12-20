package com.example.vender.Activity;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vender.Adapter.Adapter_MyEvent;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyEventActivity extends AppCompatActivity implements Serializable {
    private List<Event> event_list;
    private static String URL_MYEVENT = "https://venderapp.000webhostapp.com/myeventlist.php";
    private static final String URL_CHANGEEVENTSTATUS = "https://venderapp.000webhostapp.com/changeeventstatus.php";
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private GlobalUser g = GlobalUser.getInstance();
    private User user;
    private RecyclerView rv;
    private TextView kosong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myevent);
        user = g.getUser();
        rv = (RecyclerView) findViewById(R.id.rv_eventlist);
        kosong = (TextView) findViewById(R.id.kosong);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.createEvent);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyEventActivity.this, CreateEventActivity.class);
//                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        event_list = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Event");
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MYEVENT, new Response.Listener<String>() {
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

                            Event eventItem = new Event(id, user_id, nama_event, deskripsi, kota, venue, tanggal_event,
                                    foto, status, lowongan, min_fee, max_fee);
                            event_list.add(eventItem);
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date c = Calendar.getInstance().getTime();
                        String formattedDate = sdf.format(c);
                        String eventdate = "";
                        Date now, date;

                        for (int i = 0; i < event_list.size(); i++) {
                            eventdate = event_list.get(i).getTanggal_event();

                            try {
                                date = sdf.parse(eventdate);
                                now = sdf.parse(formattedDate);
                                if (!event_list.get(i).getStatus().equals("done")){
                                    if (now.after(date)){ //tanggal melewati tanggal event
                                        event_list.get(i).setStatus("done");
                                        changeEventStatus(event_list.get(i).getId());
                                    }
                                }
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        RecyclerView myrv = (RecyclerView) findViewById(R.id.rv_eventlist);
                        Adapter_MyEvent myAdapter = new Adapter_MyEvent(MyEventActivity.this, event_list);
                        myrv.setLayoutManager(new GridLayoutManager(MyEventActivity.this,1));
                        myrv.setAdapter(myAdapter);

                    } else if (success.equals("0")){
                        rv.setVisibility(View.GONE);
                        kosong.setVisibility(View.VISIBLE);
//                        Toast.makeText(MyEventActivity.this, "Belum ada event", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MyEventActivity.this, "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyEventActivity.this, "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getId()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MyEventActivity.this);
        requestQueue.add(stringRequest);


    }

    public void changeEventStatus(final int id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGEEVENTSTATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                            } else if (success.equals("0")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyEventActivity.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
//                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyEventActivity.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
//                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(id));
                params.put("status", "done");
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
