package com.example.vender.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.bumptech.glide.Glide;
import com.example.vender.Adapter.Adapter_EventList;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.Talent;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MyTalentProfileActivity extends AppCompatActivity implements Serializable {
    private TextView name, rating, numberofjobs, kota, min_fee, max_fee, skills, bio, experience, instagram, youtube;
    private Button edit, talent_register;
    private ImageView profile_photo;
    private Toolbar toolbar;
    private GlobalUser g = GlobalUser.getInstance();
    private User user;
    private Talent talent;
    private String foto;
    private static final String URL_TALENTPROFILE = "https://venderapp.000webhostapp.com/talentprofile.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = g.getUser();
//        Toast.makeText(MyTalentProfileActivity.this, String.valueOf(user.getId()), Toast.LENGTH_SHORT).show();

        if (user.getIstalent().equals("1")) {
            setContentView(R.layout.activity_talentprofile);
            Intent intent = getIntent();
            talent = (Talent) intent.getSerializableExtra("talent");
            foto = intent.getExtras().getString("foto");

            name = (TextView) findViewById(R.id.profile_name);
            rating = (TextView) findViewById(R.id.profile_rating);
            numberofjobs = (TextView) findViewById(R.id.profile_numberofjobs);
            kota = (TextView) findViewById(R.id.profile_kota);
            min_fee = (TextView) findViewById(R.id.min_fee);
            max_fee = (TextView) findViewById(R.id.max_fee);
            skills = (TextView) findViewById(R.id.profile_skills);
            bio = (TextView) findViewById(R.id.profile_bio);
            experience = (TextView) findViewById(R.id.profile_portofolio);
            instagram = (TextView) findViewById(R.id.instagram);
            youtube = (TextView) findViewById(R.id.youtube);
            profile_photo = (ImageView) findViewById(R.id.profile_photo);
            edit = (Button) findViewById(R.id.profile_edit);
            edit.setVisibility(View.VISIBLE);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("My Talent Profile");
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setData();

            instagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     String url = "https://instagram.com/"+talent.getInstagram();
                     Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                     startActivity(browserIntent);
                }
            });

            youtube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = talent.getYoutube();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MyTalentProfileActivity.this, EditTalentActivity.class);
                    i.putExtra("talent", talent);
                    startActivity(i);
                }
            });

        }
        else{
            setContentView(R.layout.talent_empty);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("My Talent Profile");
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            talent_register = (Button) findViewById(R.id.button_talent_register);
            talent_register.setVisibility(View.VISIBLE);
            talent_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyTalentProfileActivity.this, TalentRegisterActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    public String numberFormat(String fee){
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        int rupiah = Integer.parseInt(fee);
        String format = decimalFormat.format(rupiah);
        System.out.println(format);
        return format;
    }

    public void setData(){
        if (talent.getRating() == 0){
            this.rating.setText(String.valueOf(talent.getRating()));
        } else{
            Double rating_ = Double.valueOf(talent.getRating()/talent.getNumberofjobs());
            this.rating.setText(String.valueOf(rating_));
        }
        Glide.with(this)
                .load(foto)
                .into(this.profile_photo);
        this.name.setText(talent.getNama());
        this.numberofjobs.setText("("+String.valueOf(talent.getNumberofjobs()+" jobs done)"));
        this.kota.setText(talent.getKota());
        this.min_fee.setText("Rp "+numberFormat(talent.getMin_fee()));
        this.max_fee.setText(numberFormat(talent.getMax_fee()));
        String[] skill_ = talent.getSkill().split(",");
        String skill = "";
        for (int j = 0; j < skill_.length; j++){
            if (j == skill_.length-1){
                skill += skill_[j].substring(0, skill_[j].length());
            } else{
                skill += skill_[j]+", ";
            }
        }
        this.skills.setText(skill);
        this.bio.setText(talent.getBio());
        this.experience.setText(talent.getExperience());
        if (talent.getInstagram() == null || talent.getInstagram().equals("")) instagram.setVisibility(View.GONE);
        if (talent.getYoutube() == null || talent.getYoutube().equals("")) youtube.setVisibility(View.GONE);
    }

}
