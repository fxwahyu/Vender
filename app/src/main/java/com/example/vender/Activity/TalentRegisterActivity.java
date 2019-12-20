package com.example.vender.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vender.GlobalUser;
import com.example.vender.Model.User;
import com.example.vender.R;
import com.example.vender.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TalentRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Serializable {
    private EditText bio, experience, min_fee, max_fee, image_file, instagram, youtube;
    private Button button_talent_register;
    private Spinner spinner_kota;
    private CheckBox checkBox_penyanyi, checkBox_grup, checkBox_pemainmusik, checkBox_mc, checkBox_penari, checkBox_dj, checkBox_fotografer, checkBox_videografer, checkBox_sulap;
    private ImageView image_tn;
    private static final String URL_TALENTREGISTER = "https://venderapp.000webhostapp.com/talentregister.php";
    private Toolbar toolbar;
    private String kota, skill = "";
    private GlobalUser g = GlobalUser.getInstance();
    private User user;
    private ProgressBar loading;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talent_register);
        user = g.getUser();

        bio = (EditText) findViewById(R.id.talent_bio);
        experience = (EditText) findViewById(R.id.portofolio);
        min_fee = (EditText) findViewById(R.id.min_fee);
        max_fee = (EditText) findViewById(R.id.max_fee);
        instagram = (EditText) findViewById(R.id.instagram);
        youtube = (EditText) findViewById(R.id.youtube);

        button_talent_register = (Button) findViewById(R.id.button_talent_register);
        checkBox_penyanyi = (CheckBox) findViewById(R.id.checkBox_penyanyi);
        checkBox_grup = (CheckBox) findViewById(R.id.checkBox_grup);
        checkBox_pemainmusik = (CheckBox) findViewById(R.id.checkBox_pemainmusik);
        checkBox_mc = (CheckBox) findViewById(R.id.checkBox_mc);
        checkBox_penari = (CheckBox) findViewById(R.id.checkBox_penari);
        checkBox_dj = (CheckBox) findViewById(R.id.checkBox_dj);
        checkBox_fotografer = (CheckBox) findViewById(R.id.checkBox_fotografer);
        checkBox_videografer = (CheckBox) findViewById(R.id.checkBox_videografer);
        checkBox_sulap = (CheckBox) findViewById(R.id.checkBox_sulap);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        loading = (ProgressBar) findViewById(R.id.loading);

        toolbar.setTitle("Talent Registration");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.spinner_kota = (Spinner) findViewById(R.id.spinner_kota);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_kota, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner_kota.setAdapter(adapter);
        this.spinner_kota.setOnItemSelectedListener(this);

        image_tn = (ImageView) findViewById(R.id.image_tn);

        button_talent_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = true;
                if (bio.getText().toString().length() == 0){
                    bio.setError("Tidak boleh kosong");
                    success = false;
                }
                if (min_fee.getText().toString().length() == 0){
                    min_fee.setError("Tidak boleh kosong");
                    success = false;
                }
                if (max_fee.getText().toString().length() == 0){
                    max_fee.setError("Tidak boleh kosong");
                    success = false;
                }

                if (success == true) talentRegister();
            }
        });
    }

    public void talentRegister(){
        loading.setVisibility(View.VISIBLE);
        button_talent_register.setClickable(false);
        skill = checkSkill();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TALENTREGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(TalentRegisterActivity.this, "Registration Success",
                                        Toast.LENGTH_SHORT).show();
                                User user_loggedin = new User(user.getNama(), user.getEmail(), user.getPassword(), user.getFoto(), user.getId(), "1", user.getPhone_number());
                                g.setUser(user_loggedin);
                                SharedPreference preference = new SharedPreference(TalentRegisterActivity.this);
                                preference.setUser(user_loggedin);
                                preference.setIsLoggedIn(true);

                                Intent intent = new Intent(TalentRegisterActivity.this, HomeActivity.class);
                                startActivity(intent);

                            } else if (success.equals("0")) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(TalentRegisterActivity.this, "Register Failed",
                                        Toast.LENGTH_SHORT).show();
                                button_talent_register.setClickable(true);
                            } else if (success.equals("-1")) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(TalentRegisterActivity.this, "Register Failed -1",
                                        Toast.LENGTH_SHORT).show();
                                button_talent_register.setClickable(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(TalentRegisterActivity.this, "Register failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            button_talent_register.setClickable(true);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Toast.makeText(TalentRegisterActivity.this, "Register Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(user.getId()));
                params.put("skill", skill);
                params.put("kota", kota);
                params.put("bio", bio.getText().toString());
                params.put("experience", experience.getText().toString());
                params.put("min_fee", min_fee.getText().toString());
                params.put("max_fee", max_fee.getText().toString());
                params.put("instagram", instagram.getText().toString());
                params.put("youtube", youtube.getText().toString());
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String checkSkill(){
        if (checkBox_penyanyi.isChecked()){
            skill += checkBox_penyanyi.getText().toString()+",";
        }
        if (checkBox_grup.isChecked()){
            skill += checkBox_grup.getText().toString()+",";
        }
        if (checkBox_pemainmusik.isChecked()){
            skill += checkBox_pemainmusik.getText().toString()+",";
        }
        if (checkBox_mc.isChecked()){
            skill += checkBox_mc.getText().toString()+",";
        }
        if (checkBox_penari.isChecked()){
            skill += checkBox_penari.getText().toString()+",";
        }
        if (checkBox_dj.isChecked()){
            skill += checkBox_dj.getText().toString()+",";
        }
        if (checkBox_fotografer.isChecked()){
            skill += checkBox_fotografer.getText().toString()+",";
        }
        if (checkBox_videografer.isChecked()){
            skill += checkBox_videografer.getText().toString()+",";
        }
        if (checkBox_sulap.isChecked()){
            skill += checkBox_sulap.getText().toString();
        }

        return skill;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0){
            this.kota = "Surabaya";
        } else if (position == 1){
            this.kota = "Jakarta";
        } else if (position == 2) {
            this.kota = "Semarang";
        } else if (position == 3){
            this.kota = "Medan";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
