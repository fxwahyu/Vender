package com.example.vender.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.vender.Adapter.Adapter_JobOffer;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.EventOffer;
import com.example.vender.Model.Offer;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentOffer_JobOffer extends Fragment {
    private List<EventOffer> eventOfferList;
    private View view;
    private static String URL_JOBOFFERLIST = "https://venderapp.000webhostapp.com/jobofferlist.php";
    private GlobalUser g = GlobalUser.getInstance();
    private User user;
    private TextView kosong;
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_offer_joboffer, container, false);
        user = g.getUser();
        eventOfferList = new ArrayList<>();
        kosong = (TextView) view.findViewById(R.id.kosong);
        rv = (RecyclerView) view.findViewById(R.id.rv_joboffer);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_JOBOFFERLIST, new Response.Listener<String>() {
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
                            String venue = object.getString("venue");
                            String tanggal_event = object.getString("tanggal_event");
                            String foto = object.getString("foto");
                            String status = object.getString("status");
                            String lowongan = object.getString("lowongan");
                            String min_fee = object.getString("min_fee");
                            String max_fee = object.getString("max_fee");
                            int user_id = object.getInt("user_id");
                            int offer_id = object.getInt("offer_id");
                            String phonenumber = object.getString("phone_number");
//                            System.out.println(foto);

                            EventOffer eventOffer = new EventOffer(id, user_id, offer_id, nama_event, deskripsi, kota, venue, tanggal_event, foto, status, lowongan, min_fee, max_fee,phonenumber, "waiting");
                            eventOfferList.add(eventOffer);
                        }

                        RecyclerView myrv = (RecyclerView) view.findViewById(R.id.rv_joboffer);
                        Adapter_JobOffer myAdapter = new Adapter_JobOffer(getActivity(), eventOfferList);
                        myrv.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        myrv.setAdapter(myAdapter);

                    } else if (success.equals("0")){
                        rv.setVisibility(View.GONE);
                        kosong.setVisibility(View.VISIBLE);
//                        Toast.makeText(view.getContext(), "Belum ada tawaran", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_talent", String.valueOf(user.getId()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        return view;
    }
}
