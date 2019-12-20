package com.example.vender.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vender.Adapter.Adapter_EventList;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.Talent;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRelated extends AppCompatActivity {
    private List<Event> event_list;
    private static String URL_EVENTRELATED = "https://venderapp.000webhostapp.com/eventrelated.php";
    private RecyclerView rv;
    private TextView kosong;
    private User user;
    private GlobalUser g = GlobalUser.getInstance();
    private Talent talent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_find_event);
        user = g.getUser();
        Intent intent = getIntent();
        talent = (Talent) intent.getSerializableExtra("talent");
        event_list = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv_eventlist);
        kosong = (TextView) findViewById(R.id.kosong);
        takeData();

    }

    public void takeData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EVENTRELATED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("event");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("id");
                            String nama_event = object.getString("nama_event");
                            String deskripsi = object.getString("deskripsi");
                            String kota = object.getString("kota");
                            String venue = object.getString("venue");
                            String tanggal_event = object.getString("tanggal_event");
                            String foto = object.getString("foto");
                            String status = object.getString("status");
                            String lowongan = object.getString("lowongan");
                            String min_fee = object.getString("min_fee");
                            String max_fee = object.getString("max_fee");
                            int user_id = object.getInt("user_id");

                            Event eventItem = new Event(id, user_id, nama_event, deskripsi, kota, venue, tanggal_event, foto, status, lowongan, min_fee, max_fee);
                            event_list.add(eventItem);
                        }

                        RecyclerView myrv = (RecyclerView) findViewById(R.id.rv_eventlist);
                        Adapter_EventList myAdapter = new Adapter_EventList(EventRelated.this, event_list);
                        myrv.setLayoutManager(new GridLayoutManager(EventRelated.this, 1));
                        myrv.setAdapter(myAdapter);

                    } else if (success.equals("0")) {
                        rv.setVisibility(View.GONE);
                        kosong.setVisibility(View.VISIBLE);
//                        Toast.makeText(view.getContext(), "Belum ada event", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EventRelated.this, "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EventRelated.this, "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(user.getId()));
                return params;
            }
        };;

        RequestQueue requestQueue = Volley.newRequestQueue(EventRelated.this);
        requestQueue.add(stringRequest);
    }
}


