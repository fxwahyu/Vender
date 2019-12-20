package com.example.vender.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.vender.Model.EventOffer;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DetailEventOffer extends AppCompatActivity {
    private TextView nama, venue, kota, tanggal, deskripsi, lowongan, min_fee, max_fee;
    private ImageView image;
    private Button response;
    private Toolbar toolbar;
    private ProgressBar loadingbar;
    private User user;
    private EventOffer eventOffer;
    private GlobalUser g = GlobalUser.getInstance();
    private static final String URL_CHANGEOFFERSTATUS = "https://venderapp.000webhostapp.com/changeofferstatus.php";
    private static final String URL_INPUTJOB = "https://venderapp.000webhostapp.com/inputjob.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_eventoffer);
        this.user = g.getUser();
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
        response = (Button) findViewById(R.id.button_response);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        loadingbar = (ProgressBar) findViewById(R.id.loading);

        toolbar.setTitle("Event Detail");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        eventOffer = (EventOffer) intent.getSerializableExtra("eventoffer");
        setData(eventOffer);
        detailFromOffer();

    }

    public String numberFormat(String fee){
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        int rupiah = Integer.parseInt(fee);
        String format = decimalFormat.format(rupiah);
        System.out.println(format);
        return format;
    }

    public void setData(EventOffer eventOffer){
        this.nama.setText(eventOffer.getNama_event());
        this.venue.setText(eventOffer.getVenue()+",");
        this.kota.setText(eventOffer.getKota());
        this.tanggal.setText(eventOffer.getTanggal_event());
        this.deskripsi.setText(eventOffer.getDeskripsi());
        String[] lowongan_ = eventOffer.getLowongan().split(",");
        String lowongan = "";
        for (int j = 0; j < lowongan_.length; j++){
            if (j == lowongan_.length-1){
                lowongan += lowongan_[j].substring(0, lowongan_[j].length());
            } else{
                lowongan += lowongan_[j]+", ";
            }
        }
        this.lowongan.setText(lowongan);
        Glide.with(DetailEventOffer.this).load(eventOffer.getFoto()).asBitmap().placeholder(R.drawable.blankevent).centerCrop().into(this.image);
        this.min_fee.setText("Rp "+numberFormat(eventOffer.getMin_fee()));
        this.max_fee.setText(numberFormat(eventOffer.getMax_fee()));
    }

    public void detailFromOffer() {
        response.setVisibility(View.VISIBLE);
        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] menu = {"Accept Offer", "Refuse Offer", "Send Message"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DetailEventOffer.this);
                builder.setTitle("Choose Action");
                builder.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventOffer.this);
                            builder.setCancelable(true);
                            builder.setTitle("Menerima pekerjaan");
                            builder.setMessage("Anda yakin?");
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    changeOfferStatus(eventOffer.getOffer_id(), "accepted");
                                }
                            });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            android.support.v7.app.AlertDialog showdialog = builder.create();
                            showdialog.show();
                        } else if (which == 1) {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventOffer.this);
                            builder.setCancelable(true);
                            builder.setTitle("Menolak pekerjaan");
                            builder.setMessage("Anda yakin?");
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    changeOfferStatus(eventOffer.getOffer_id(), "rejected");
                                }
                            });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            android.support.v7.app.AlertDialog showdialog = builder.create();
                            showdialog.show();
                        } else if (which == 2) {
                            String text = "Halo, saya ingin bertanya seputar perekrutan pada event " + eventOffer.getNama_event();
                            String number = eventOffer.getPhonenumber();
                            String formattednumber = "62" + number.substring(1, number.length());
                            try {
                                String url = "https://wa.me/" + formattednumber + "?text=" + URLEncoder.encode(text, "UTF-8");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);

                            } catch (UnsupportedEncodingException e) {

                            }
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void changeOfferStatus(final int offer_id, final String status){
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
                                if (status.equals("accepted")){
                                    inputNewJob(eventOffer.getId(), user.getId(), 0);
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventOffer.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Berhasil!");
                                    builder.setMessage("Lihat daftar pekerjaan anda pada halaman My Job");
                                    builder.setPositiveButton("Confirm",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(DetailEventOffer.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                    android.support.v7.app.AlertDialog showdialog = builder.create();
                                    showdialog.show();

                                } else if (status.equals("rejected")){
                                    loadingbar.setVisibility(View.GONE);
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventOffer.this);
                                    builder.setMessage("Pekerjaan berhasil ditolak");
                                    builder.setPositiveButton("Confirm",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(DetailEventOffer.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                    android.support.v7.app.AlertDialog showdialog = builder.create();
                                    showdialog.show();
//                                    Toast.makeText(DetailEventOffer.this, "Berhasil menolak pekerjaan",
//                                            Toast.LENGTH_SHORT).show();
                                }

                            } else if (success.equals("0")) {
                                Toast.makeText(DetailEventOffer.this, "Failed",
                                        Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailEventOffer.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailEventOffer.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("offer_id", String.valueOf(offer_id));
                params.put("status", status);
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void inputNewJob(final int eventid, final int talentid, final int rating){
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
//                                Toast.makeText(DetailEventOffer.this, "Success", Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);

                            } else if (success.equals("0")) {
//                                Toast.makeText(DetailEventOffer.this, "Failed",
//                                        Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailEventOffer.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailEventOffer.this, "Failed" + error.toString(),
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
