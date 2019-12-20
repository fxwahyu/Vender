package com.example.vender.Fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.vender.Activity.EditProfile;
import com.example.vender.Activity.MainActivity;
import com.example.vender.Activity.MyEventActivity;
import com.example.vender.Activity.MyJobActivity;
import com.example.vender.Activity.MyTalentProfileActivity;
import com.example.vender.Activity.TalentRegisterActivity;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Talent;
import com.example.vender.Model.User;
import com.example.vender.R;
import com.example.vender.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment implements Serializable {
    private TextView nama, email;
    private ImageView profile_photo;
    private View view;
    private User user;
    private Button myEvent, signout, mytalentprofile, edit, my_job;
    private GlobalUser g = GlobalUser.getInstance();
    private static final String URL_TALENTPROFILE = "https://venderapp.000webhostapp.com/talentprofile.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.user = g.getUser();

        nama = (TextView) view.findViewById(R.id.profile_name);
        email = (TextView) view.findViewById(R.id.email);
        profile_photo = (ImageView) view.findViewById(R.id.profile_photo);
        Glide.with(this)
                .load(user.getFoto())
                .placeholder(R.drawable.blankprofile)
                .into(this.profile_photo);

        nama.setText(user.getNama());
        email.setText(user.getEmail());

        myEvent = (Button) view.findViewById(R.id.my_event);
        myEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MyEventActivity.class);
//                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        signout = (Button) view.findViewById(R.id.sign_out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                SharedPreference preference = new SharedPreference(view.getContext());
                preference.setIsLoggedIn(false);
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        mytalentprofile = (Button) view.findViewById(R.id.button_talent_profile);
        mytalentprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeData();
            }
        });

        edit = (Button) view.findViewById(R.id.button_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        my_job = (Button) view.findViewById(R.id.my_job);
        my_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MyJobActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void takeData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TALENTPROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("talent");
                    if (success.equals("1")) {

                        JSONObject object = jsonArray.getJSONObject(0);
                        Talent talent = new Talent();
                        talent.setId(user.getId());
                        talent.setNama(object.getString("nama"));
                        talent.setSkill(object.getString("skill"));
                        talent.setBio(object.getString("bio"));
                        talent.setKota(object.getString("kota"));
                        talent.setMin_fee(object.getString("min_fee"));
                        talent.setMax_fee(object.getString("max_fee"));
                        talent.setExperience(object.getString("experience"));
                        talent.setRating(Integer.parseInt(object.getString("rating")));
                        talent.setNumberofjobs(Integer.parseInt(object.getString("numberofjobs")));
                        talent.setInstagram(object.getString("instagram"));
                        talent.setYoutube(object.getString("youtube"));
                        String foto = object.getString("foto");
                        Intent intent = new Intent(view.getContext(), MyTalentProfileActivity.class);
                        intent.putExtra("foto", foto);
                        intent.putExtra("talent", talent);
                        startActivity(intent);

                    }
                    else if (success.equals("0")){
                        Intent intent = new Intent(view.getContext(), MyTalentProfileActivity.class);
                        startActivity(intent);
//                        Toast.makeText(view.getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "Failed to load data" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Failed to load data" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(user.getId()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }
}
