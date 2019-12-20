package com.example.vender.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.TalentOffer;
import com.example.vender.Model.Talent;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailTalentActivity extends AppCompatActivity {

    private TextView name, rating, numberofjobs, kota, min_fee, max_fee, skills, bio, experience, instagram, youtube;
    private Button send_offer, response, cancel, contact;
    private ImageView profile_photo;
    private Toolbar toolbar;
    private GlobalUser g = GlobalUser.getInstance();
    private User user;
    private TalentOffer talentOffer;
    private Talent talent;
    private List<Event> myEventList;
    private List<TalentOffer> talentOfferList;
    private ProgressBar loadingbar;
    private static final String URL_DETAILTALENT = "https://venderapp.000webhostapp" +
            ".com/detailtalent.php";
    private static final String URL_MYACTIVEEVENTLIST = "https://venderapp.000webhostapp.com/myactiveeventlist.php";
    private static final String URL_SENDOFFER = "https://venderapp.000webhostapp.com/sendoffer.php";
    private static final String URL_CHANGEOFFERSTATUS = "https://venderapp.000webhostapp.com/changeofferstatus.php";
    private static final String URL_INPUTJOB = "https://venderapp.000webhostapp.com/inputjob.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = g.getUser();
        setContentView(R.layout.detail_talent);

        myEventList = new ArrayList<>();
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
        send_offer = (Button) findViewById(R.id.send_offer);
        response = (Button) findViewById(R.id.button_response);
        cancel = (Button) findViewById(R.id.button_cancelsubmission);
        contact = (Button) findViewById(R.id.button_contact);
        loadingbar = (ProgressBar) findViewById(R.id.loading);

        Intent i = getIntent();
        final String from = i.getExtras().getString("dari");
        talent = (Talent) i.getSerializableExtra("talent");
        talentOffer = (TalentOffer) i.getSerializableExtra("talentoffer");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Talent");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (from.equals("talentlist") || from.equals("workedtalent")){
            String foto = i.getExtras().getString("foto");
            setData(talent, foto);
            if  (from.equals("talentlist")) detailFromList();
            else if (from.equals("workedtalent")) detailFromWorkedTalent(i.getExtras().getString("phonenumber"));

        } else if (from.equals("offer") || from.equals("submission")){
            setData(talentOffer);
            if (from.equals("offer")) detailFromOffer();
            else if (from.equals("submission")) detailFromSubmission();
        }

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                if (from.equals("offer") || from.equals("submission")){
                    url = "https://instagram.com/"+talentOffer.getInstagram();
                } else{
                    url = "https://instagram.com/"+talent.getInstagram();
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                if (from.equals("offer") || from.equals("submission")){
                    url = talentOffer.getYoutube();
                } else{
                    url = talent.getYoutube();
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    public String numberFormat(String fee){
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        int rupiah = Integer.parseInt(fee);
        String format = decimalFormat.format(rupiah);
        System.out.println(format);
        return format;
    }

    public void setData(Talent talent, String foto) {
        if (talent.getRating() == 0){
            this.rating.setText(String.valueOf(talent.getRating()));
        } else{
            Double rating_ = Double.valueOf(talent.getRating()/talent.getNumberofjobs());
            this.rating.setText(String.valueOf(rating_));
        }

        Glide.with(this)
                .load(foto)
                .placeholder(R.drawable.blankprofile)
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

    public void setData(TalentOffer talentOffer){
        if (talentOffer.getRating() == 0){
            this.rating.setText(String.valueOf(talentOffer.getRating()));
        } else{
            Double rating_ = Double.valueOf(talentOffer.getRating()/talentOffer.getNumberofjobs());
            this.rating.setText(String.valueOf(rating_));
        }
        Glide.with(this)
                .load(talentOffer.getFoto())
                .placeholder(R.drawable.blankprofile)
                .into(this.profile_photo);
        this.name.setText(talentOffer.getNamatalent());
        this.numberofjobs.setText("("+String.valueOf(talentOffer.getNumberofjobs())+" jobs done)");
        this.kota.setText(talentOffer.getKota());
        this.min_fee.setText("Rp "+ numberFormat(talentOffer.getMin_fee()));
        this.max_fee.setText(numberFormat(talentOffer.getMax_fee()));
        String[] skill_ = talentOffer.getSkill().split(",");
        String skill = "";
        for (int j = 0; j < skill_.length; j++){
            if (j == skill_.length-1){
                skill += skill_[j].substring(0, skill_[j].length());
            } else{
                skill += skill_[j]+", ";
            }
        }
        this.skills.setText(skill);
        this.bio.setText(talentOffer.getBio());
        this.experience.setText(talentOffer.getExperience());
        if (talentOffer.getInstagram() == null || talentOffer.getInstagram().equals("")){
            instagram.setVisibility(View.GONE);
        }
        if (talentOffer.getYoutube() == null || talentOffer.getYoutube().equals("")){
            youtube.setVisibility(View.GONE);
        }
    }

    public void detailFromWorkedTalent(final String number){
        System.out.println(number);
        contact.setVisibility(View.VISIBLE);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                String formattednumber = "62"+number.substring(1, number.length());
//                try{
                    String url = "https://wa.me/"+formattednumber+"?text=";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
//
//                } catch (UnsupportedEncodingException e){
//
//                }
            }
        });
    }

    public void detailFromList(){
        if (talent.getId() != this.user.getId()){
            takeEvent();
            send_offer.setVisibility(View.VISIBLE);
            send_offer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        Toast.makeText(DetailTalentActivity.this, String.valueOf(myEventList.size()), Toast.LENGTH_SHORT).show();
                    if (myEventList.size() > 0){
                        chooseEvent();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailTalentActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Gagal");
                        builder.setMessage("Anda belum memiliki event yang akan diselenggarakan, silahkan membuat event terlebih dahulu");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        AlertDialog showdialog = builder.create();
                        showdialog.show();
                    }
                }
            });
        }

    }

    public void detailFromOffer(){
        final String nama = name.getText().toString();
        response.setVisibility(View.VISIBLE);
        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] menu = {"Accept Offer", "Refuse Offer", "Send Message"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DetailTalentActivity.this);
                builder.setTitle("Choose Action");
                builder.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Hire");
                            builder.setMessage("Apakah anda yakin untuk mempekerjakan talent "+nama+" pada event anda?"); //kurang nama event
                            builder.setPositiveButton("Confirm",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            changeOfferStatus(talentOffer.getOfferid(), "accepted");
                                        }
                                    });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            android.support.v7.app.AlertDialog showdialog = builder.create();
                            showdialog.show();
                        } else if (which == 1){
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Menolak permintaan");
                            builder.setMessage("Apakah anda yakin untuk menolak talent "+nama+"?");
                            builder.setPositiveButton("Confirm",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            changeOfferStatus(talentOffer.getOfferid(), "rejected");
                                        }
                                    });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            android.support.v7.app.AlertDialog showdialog = builder.create();
                            showdialog.show();
                        } else if (which == 2){
                            String text = "Hai "+talentOffer.getNamatalent()+", saya admin dari event "+talentOffer.getNamaevent();
                            String number = talentOffer.getPhonenumber();
                            String formattednumber = "62"+number.substring(1, number.length());
                            try{
                                String url = "https://wa.me/"+formattednumber+"?text=" + URLEncoder.encode(text, "UTF-8");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);

                            } catch (UnsupportedEncodingException e){

                            }
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void detailFromSubmission(){
        if (talentOffer.getOfferstatus().equals("waiting")){ //muncul button cancel
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Membatalkan pengajuan");
                    builder.setMessage("Anda yakin?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    changeSubmissionStatus(talentOffer.getOfferid(), "canceled");
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    android.support.v7.app.AlertDialog showdialog = builder.create();
                    showdialog.show();
                }
            });
        } else{
            String inform = "";
            if (talentOffer.getOfferstatus().equals("rejected")) inform = "Pengajuan ini telah ditolak";
            else if (talentOffer.getOfferstatus().equals("accepted")) inform = "Pengajuan ini telah diterima";
            else if (talentOffer.getOfferstatus().equals("canceled")) inform = "Anda telah membatalkan pengajuan ini";
            Toast.makeText(DetailTalentActivity.this, inform, Toast.LENGTH_SHORT).show();
        }
    }

    public void changeSubmissionStatus(final int offer_id, final String offer_status){
//        loadingbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGEOFFERSTATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(DetailTalentActivity.this, "Berhasil membatalkan pengajuan", Toast.LENGTH_SHORT).show();
                                cancel.setVisibility(View.GONE);
//                                loadingbar.setVisibility(View.GONE);

                            } else if (success.equals("0")) {
                                Toast.makeText(DetailTalentActivity.this, "Failed",
                                        Toast.LENGTH_SHORT).show();
//                                loadingbar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTalentActivity.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
//                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailTalentActivity.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
//                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("offer_id", String.valueOf(offer_id));
                params.put("status", offer_status);
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void chooseEvent(){
        String[] event = new String[myEventList.size()];
        for (int i = 0; i < myEventList.size(); i++){
            event[i] = myEventList.get(i).getNama_event();
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DetailTalentActivity.this);
        builder.setTitle("Pilih Event");
        builder.setItems(event, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int chosen) {
                final String namaevent = myEventList.get(chosen).getNama_event();
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Mengajukan Penawaran");
                builder.setMessage("Apakah anda yakin mengajukan penawaran kepada " +name.getText().toString()+" sebagai pengisi acara pada " + namaevent+ "?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int id = myEventList.get(chosen).getId();
//                                Toast.makeText(DetailTalentActivity.this, String.valueOf(id)+String.valueOf(talentid), Toast.LENGTH_SHORT).show();
                                sendOffer(id);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                android.support.v7.app.AlertDialog showdialog = builder.create();
                showdialog.show();
            }
        });
        builder.show();

    }

    public void takeEvent(){
//        loadingbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MYACTIVEEVENTLIST,
                new Response.Listener<String>() {
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
                                    myEventList.add(eventItem);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTalentActivity.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
//                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailTalentActivity.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
//                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getId()));
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void sendOffer(final int eventid) {
        loadingbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SENDOFFER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) { //berhasil input data
                                loadingbar.setVisibility(View.GONE);
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Berhasil!");
                                builder.setMessage("Lihat progress pengajuan anda pada halaman Submission > Submission to Talent");
                                builder.setPositiveButton("Confirm",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(DetailTalentActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                android.support.v7.app.AlertDialog showdialog = builder.create();
                                showdialog.show();

                            } else if (success.equals("-1")) { //offer sedang waiting atau accepted
                                loadingbar.setVisibility(View.GONE);
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Gagal!");
                                builder.setMessage("Penawaran sudah pernah dilakukan");
                                builder.setPositiveButton("Confirm",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                android.support.v7.app.AlertDialog showdialog = builder.create();
                                showdialog.show();
                            } else if (success.equals("0")) { //offer sedang waiting atau accepted
                                loadingbar.setVisibility(View.GONE);
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Gagal!");
                                builder.setMessage("");
                                builder.setPositiveButton("Confirm",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                android.support.v7.app.AlertDialog showdialog = builder.create();
                                showdialog.show();
                            }
                        } catch (JSONException e) {
                            loadingbar.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(DetailTalentActivity.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
//                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingbar.setVisibility(View.GONE);
                Toast.makeText(DetailTalentActivity.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
//                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(eventid));
                params.put("talent_id", String.valueOf(talent.getId()));
                params.put("status", "waiting");
                params.put("dari", "event");
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void changeOfferStatus(final int offerid, final String status){
        loadingbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGEOFFERSTATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                loadingbar.setVisibility(View.GONE);
                                if (status.equals("accepted")){
                                    inputNewRating(talentOffer.getEventid(), talentOffer.getTalentid(), 0);
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Berhasil!");
                                    builder.setMessage("Lihat daftar talent yang telah anda terima pada halaman My Event");
                                    builder.setPositiveButton("Confirm",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(DetailTalentActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                    android.support.v7.app.AlertDialog showdialog = builder.create();
                                    showdialog.show();

                                } else if (status.equals("rejected")){
                                    loadingbar.setVisibility(View.GONE);
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailTalentActivity.this);
                                    builder.setCancelable(true);
                                    builder.setMessage("Anda akan menolak penawaran dari talent ini");
                                    builder.setPositiveButton("Confirm",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(DetailTalentActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    android.support.v7.app.AlertDialog showdialog = builder.create();
                                    showdialog.show();
//
                                }

                            } else if (success.equals("0")) {
                                loadingbar.setVisibility(View.GONE);
                                Toast.makeText(DetailTalentActivity.this, "Failed",
                                        Toast.LENGTH_SHORT).show();
//                                loadingbar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            loadingbar.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(DetailTalentActivity.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
//                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingbar.setVisibility(View.GONE);
                Toast.makeText(DetailTalentActivity.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
//                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("offer_id", String.valueOf(offerid));
                params.put("status", status);
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void inputNewRating(final int eventid, final int talentid, final int rating){
        loadingbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INPUTJOB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
//                                Toast.makeText(DetailTalentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);

                            } else if (success.equals("0")) {
                                Toast.makeText(DetailTalentActivity.this, "Failed",
                                        Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTalentActivity.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailTalentActivity.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(eventid));
                params.put("talent_id", String.valueOf(talentid));
                params.put("rating", String.valueOf(rating));
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
