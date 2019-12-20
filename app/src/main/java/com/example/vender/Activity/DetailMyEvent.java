package com.example.vender.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.EventOffer;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class DetailMyEvent extends AppCompatActivity implements Serializable {
    private TextView nama, venue, kota, tanggal, deskripsi, lowongan, min_fee, max_fee, statuslabel, statusview, daftar_talent;
    private ImageView image;
    private Button edit, giverating;
    private Toolbar toolbar;
    private ProgressBar loadingbar;
    private Event event;
    private User user;
    private EventOffer eventOffer;
    private GlobalUser g = GlobalUser.getInstance();
    private static final String URL_CHANGEEVENTSTATUS = "https://venderapp.000webhostapp.com/changeeventstatus.php";
    private static final String URL_CHANGEOFFERSTATUS = "https://venderapp.000webhostapp.com/changeofferstatus.php";
    private static final String URL_SENDOFFER = "https://venderapp.000webhostapp.com/sendoffer.php";
    private static final String URL_INPUTJOB = "https://venderapp.000webhostapp.com/inputjob.php";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_myevent);
        this.user = g.getUser();
        event = new Event();
        eventOffer = new EventOffer();

        nama = (TextView) findViewById(R.id.nama_event_detail);
        venue = (TextView) findViewById(R.id.venue_detail);
        kota = (TextView) findViewById(R.id.kota_detail);
        tanggal = (TextView) findViewById(R.id.tanggal_detail);
        deskripsi = (TextView) findViewById(R.id.deskripsi_detail);
        lowongan = (TextView) findViewById(R.id.lowongan_detail);
        min_fee = (TextView) findViewById(R.id.min_fee_detail);
        max_fee = (TextView) findViewById(R.id.max_fee_detail);
        image = (ImageView) findViewById(R.id.event_tn_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edit = (Button) findViewById(R.id.button_edit);
        giverating = (Button) findViewById(R.id.button_giverating);
        loadingbar = (ProgressBar) findViewById(R.id.loading);
        daftar_talent = (TextView) findViewById(R.id.daftar_talent);
        statusview = (TextView) findViewById(R.id.status);
        statuslabel = (TextView) findViewById(R.id.statuslabel);

        toolbar.setTitle("Event Detail");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });

        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("event");
        setData(event);

        detailFromMyEvent();
    }

    public String numberFormat(String fee){
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        int rupiah = Integer.parseInt(fee);
        String format = decimalFormat.format(rupiah);
        System.out.println(format);
        return format;
    }

    public void setData(Event event){
        this.nama.setText(event.getNama_event());
        this.venue.setText(event.getVenue()+",");
        this.kota.setText(event.getKota());
        this.tanggal.setText(event.getTanggal_event());
        this.deskripsi.setText(event.getDeskripsi());
        String[] lowongan_ = event.getLowongan().split(",");
        String lowongan = "";
        for (int j = 0; j < lowongan_.length; j++){
            if (j == lowongan_.length-1){
                lowongan += lowongan_[j].substring(0, lowongan_[j].length());
            } else{
                lowongan += lowongan_[j]+", ";
            }
        }
        this.lowongan.setText(lowongan);
        Glide.with(DetailMyEvent.this).load(event.getFoto()).asBitmap().placeholder(R.drawable.blankevent).centerCrop().into(this.image);
        this.min_fee.setText("Rp "+numberFormat(event.getMin_fee()));
        this.max_fee.setText(numberFormat(event.getMax_fee()));
        statusview.setText(event.getStatus());
        if (event.getStatus().equals("recruiting")) statusview.setTextColor(getResources().getColor(R.color.active));
        else if (event.getStatus().equals("stopped")) statusview.setTextColor(getResources().getColor(R.color.nonactive));
        else statusview.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void detailFromMyEvent(){
        if (!event.getStatus().equals("done")){
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailMyEvent.this, EditEventActivity.class);
                    i.putExtra("event", event);
                    startActivity(i);
                }
            });
            statusview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailMyEvent.this);
                    builder.setCancelable(true);
                    if (event.getStatus().equalsIgnoreCase("stopped")){
                        builder.setTitle("Mengaktifkan Event");
                        builder.setMessage("Anda akan mengaktifkan kembali event ini dan dapat menerima penawaran dari talent, apakah anda yakin?");
                    } else if (event.getStatus().equalsIgnoreCase("recruiting")){
                        builder.setTitle("Nonaktifkan Event");
                        builder.setMessage("Anda akan memberhentikan pengiklanan event ini dan tidak akan menerima penawaran dari talent, apakah anda yakin?");
                    }
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (event.getStatus().equalsIgnoreCase("recruiting")){
                                        changeEventStatus("stopped");
                                    } else if (event.getStatus().equalsIgnoreCase("stopped")){
                                        changeEventStatus("recruiting");
                                    }
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
            daftar_talent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailMyEvent.this, WorkedTalentActivity.class);
                    intent.putExtra("event", event);
                    startActivity(intent);
                }
            });
        } else{
            daftar_talent.setVisibility(View.GONE);
            giverating.setVisibility(View.VISIBLE);
            giverating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailMyEvent.this, RatingActivity.class);
                    intent.putExtra("event", event);
                    startActivity(intent);
                }
            });
        }
    }

    public void changeEventStatus(final String status){
        loadingbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGEEVENTSTATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(DetailMyEvent.this, "Berhasil mengubah status event!",
                                        Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);
                                event.setStatus(status);
                                if (event.getStatus().equalsIgnoreCase("recruiting")){
                                    statusview.setTextColor(getResources().getColor(R.color.active));
                                    statusview.setText("recruiting");
                                } else if (event.getStatus().equalsIgnoreCase("stopped")){
                                    statusview.setTextColor(getResources().getColor(R.color.nonactive));
                                    statusview.setText("stopped");
                                }

                            } else if (success.equals("0")) {
                                Toast.makeText(DetailMyEvent.this, "Failed",
                                        Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailMyEvent.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailMyEvent.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(event.getId()));
                params.put("status", status);
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}


