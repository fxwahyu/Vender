package com.example.vender.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.vender.Adapter.Adapter_Hire;
import com.example.vender.Adapter.Adapter_Recruiting;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Talent;
import com.example.vender.Model.TalentOffer;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentSubmissionRecruiting extends Fragment {
    private View view;
    private List<TalentOffer> talentOfferList;
    private User user;
    private GlobalUser g = GlobalUser.getInstance();
    private static String URL_RECRUITINGLIST = "https://venderapp.000webhostapp.com/recruitinglist.php";
    private RecyclerView rv;
    private TextView kosong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_submission_recruiting, container, false);
        talentOfferList = new ArrayList<>();
        user = g.getUser();
        rv = (RecyclerView) view.findViewById(R.id.rv_recruiting);
        kosong = (TextView) view.findViewById(R.id.kosong);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RECRUITINGLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("talent");


                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            int offerid = object.getInt("offerid");
                            int eventid = object.getInt("eventid");
                            String offer_status = object.getString("offer_status");
                            String namaevent = object.getString("namaevent");
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

                            TalentOffer talentOffer = new TalentOffer(offerid, eventid, talentid, rating, numberofjobs, namaevent, namatalent, phonenumber, skill, bio, kota, min_fee, max_fee, experience, offer_status, instagram, youtube, foto);
                            talentOfferList.add(talentOffer);
                        }

                        RecyclerView myrv = (RecyclerView) view.findViewById(R.id.rv_recruiting);
                        Adapter_Recruiting myAdapter = new Adapter_Recruiting(getActivity(), talentOfferList);
                        myrv.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        myrv.setAdapter(myAdapter);

                    } else if (success.equals("0")){
                        rv.setVisibility(View.GONE);
                        kosong.setVisibility(View.VISIBLE);
//                        Toast.makeText(view.getContext(), "Kosong", Toast.LENGTH_SHORT).show();
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
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(user.getId()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        return view;

    }
}
