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
import com.example.vender.Model.Talent;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EditTalentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Serializable {
    private User user;
    private Talent talent;
    private GlobalUser g = GlobalUser.getInstance();
    private Spinner spinnerkota;
    private EditText experience, talent_bio, min_fee, max_fee, instagram, youtube;
    private CheckBox penyanyi, pemainmusik, mc, dj, penari, fotografer, videografer, sulap, grup;
    private Button save;
    private Toolbar toolbar;
    private String kota, skill;
    private ProgressBar loading;
    private static final String URL_EDITTALENT = "https://venderapp.000webhostapp.com/edittalent.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittalent);

        user = g.getUser();
        Intent intent = getIntent();
        talent = (Talent) intent.getSerializableExtra("talent");

        talent_bio = (EditText) findViewById(R.id.talent_bio);
        experience = (EditText) findViewById(R.id.portofolio);
        min_fee = (EditText) findViewById(R.id.min_fee);
        max_fee = (EditText) findViewById(R.id.max_fee);
        instagram = (EditText) findViewById(R.id.instagram);
        youtube = (EditText) findViewById(R.id.youtube);
        loading = (ProgressBar) findViewById(R.id.loading);

        save = (Button) findViewById(R.id.button_save);
        penyanyi = (CheckBox) findViewById(R.id.checkBox_penyanyi);
        grup = (CheckBox) findViewById(R.id.checkBox_grup);
        pemainmusik = (CheckBox) findViewById(R.id.checkBox_pemainmusik);
        mc = (CheckBox) findViewById(R.id.checkBox_mc);
        penari = (CheckBox) findViewById(R.id.checkBox_penari);
        dj = (CheckBox) findViewById(R.id.checkBox_dj);
        fotografer = (CheckBox) findViewById(R.id.checkBox_fotografer);
        videografer = (CheckBox) findViewById(R.id.checkBox_videografer);
        sulap = (CheckBox) findViewById(R.id.checkBox_sulap);

        this.spinnerkota = (Spinner) findViewById(R.id.spinner_kota);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_kota, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerkota.setAdapter(adapter);
        this.spinnerkota.setOnItemSelectedListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setValues();

        toolbar.setTitle("Edit Talent");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
    }

    public void setValues(){
        this.talent_bio.setText(talent.getBio());
        this.experience.setText(talent.getExperience());
        this.min_fee.setText(talent.getMin_fee());
        this.max_fee.setText(talent.getMax_fee());
        this.instagram.setText(talent.getInstagram());
        this.youtube.setText(talent.getYoutube());
//        this.imageView.setImageResource(talent.);

        int index = 0;
        String[] arrkota = {"Surabaya", "Jakarta", "Semarang", "Medan"};
        for (int i = 0; i < arrkota.length; i++){
            if (talent.getKota().equals(arrkota[i])){
                index = i;
                break;
            }
        }
        this.spinnerkota.setSelection(index);
        this.skill = talent.getSkill();
        String[] skills = this.skill.split(",");

        for (int i = 0; i < skills.length; i++){
            String temp = skills[i];
            if (temp.equalsIgnoreCase("Penyanyi")) penyanyi.setChecked(true);
            else if (temp.equalsIgnoreCase("Pemain Musik")) pemainmusik.setChecked(true);
            else if (temp.equalsIgnoreCase("Penari")) penari.setChecked(true);
            else if (temp.equalsIgnoreCase("Fotografer")) fotografer.setChecked(true);
            else if (temp.equalsIgnoreCase("Videografer")) videografer.setChecked(true);
            else if (temp.equalsIgnoreCase("Band/Vocal Group")) grup.setChecked(true);
            else if (temp.equalsIgnoreCase("MC")) mc.setChecked(true);
            else if (temp.equalsIgnoreCase("DJ")) dj.setChecked(true);
            else if (temp.equalsIgnoreCase("Pesulap/Badut")) sulap.setChecked(true);
        }
    }

    public String checkLowongan(){
        skill = "";
        if (penyanyi.isChecked()){
            skill += penyanyi.getText().toString()+",";
        }
        if (grup.isChecked()){
            skill += grup.getText().toString()+",";
        }
        if (pemainmusik.isChecked()){
            skill += pemainmusik.getText().toString()+",";
        }
        if (mc.isChecked()){
            skill += mc.getText().toString()+",";
        }
        if (penari.isChecked()){
            skill += penari.getText().toString()+",";
        }
        if (dj.isChecked()){
            skill += dj.getText().toString()+",";
        }
        if (fotografer.isChecked()){
            skill += fotografer.getText().toString()+",";
        }
        if (videografer.isChecked()){
            skill += videografer.getText().toString()+",";
        }
        if (sulap.isChecked()){
            skill += sulap.getText().toString();
        }

        return skill;
    }

    public void edit(){
        loading.setVisibility(View.VISIBLE);
        skill = checkLowongan();
//        Toast.makeText(EditTalentActivity.this, skill, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDITTALENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(EditTalentActivity.this, "Saved",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(EditTalentActivity.this, HomeActivity.class);
                                talent.setSkill(skill);
                                talent.setKota(kota);
                                talent.setBio(talent_bio.getText().toString());
                                talent.setExperience(experience.getText().toString());
                                talent.setMin_fee(String.valueOf(min_fee.getText().toString()));
                                talent.setMax_fee(String.valueOf(max_fee.getText().toString()));
                                talent.setInstagram(instagram.getText().toString());
                                talent.setYoutube(youtube.getText().toString());
                                intent.putExtra("talent", talent);
                                startActivity(intent);

                            } else if (success.equals("0")) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(EditTalentActivity.this, "-Failed-",
                                        Toast.LENGTH_LONG).show();
//                                button_edit.setClickable(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(EditTalentActivity.this, "Failed" + e.toString(),
                                    Toast.LENGTH_LONG).show();
//                            button_edit.setClickable(true);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Toast.makeText(EditTalentActivity.this, "Failed Failed" + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(user.getId()));
                params.put("skill", skill);
                params.put("kota", kota);
                params.put("bio", talent_bio.getText().toString());
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
