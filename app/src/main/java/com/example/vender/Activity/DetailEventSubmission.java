package com.example.vender.Activity;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DetailEventSubmission extends AppCompatActivity {
    private TextView nama, venue, kota, tanggal, deskripsi, lowongan, min_fee, max_fee;
    private ImageView image;
    private Button cancel;
    private Toolbar toolbar;
    private ProgressBar loadingbar;
    private User user;
    private EventOffer eventOffer;
    private GlobalUser g = GlobalUser.getInstance();
    private static final String URL_CHANGEOFFERSTATUS = "https://venderapp.000webhostapp.com/changeofferstatus.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_eventsubmission);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cancel = (Button) findViewById(R.id.button_cancelsubmission);
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
        detailFromSubmission();

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
        Glide.with(DetailEventSubmission.this).load(eventOffer.getFoto()).asBitmap().placeholder(R.drawable.blankevent).centerCrop().into(this.image);
        this.min_fee.setText("Rp "+numberFormat(eventOffer.getMin_fee()));
        this.max_fee.setText(numberFormat(eventOffer.getMax_fee()));
    }

    public void detailFromSubmission(){
        if (eventOffer.getOffer_status().equals("waiting")){ //muncul button cancel
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventSubmission.this);
                    builder.setCancelable(true);
                    builder.setTitle("Membatalkan pengajuan");
                    builder.setMessage("Anda yakin?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    changeSubmissionStatus(eventOffer.getOffer_id(), "canceled");
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
            if (eventOffer.getOffer_status().equals("rejected")) inform = "Pengajuan ini telah ditolak";
            else if (eventOffer.getOffer_status().equals("accepted")) inform = "Pengajuan ini telah diterima";
            else if (eventOffer.getOffer_status().equals("canceled")) inform = "Anda telah membatalkan pengajuan ini";
            Toast.makeText(DetailEventSubmission.this, inform, Toast.LENGTH_SHORT).show();
        }
    }

    public void changeSubmissionStatus(final int offer_id, final String status){
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
                                Toast.makeText(DetailEventSubmission.this, "Berhasil membatalkan pengajuan", Toast.LENGTH_SHORT).show();
                                cancel.setVisibility(View.GONE);
                                loadingbar.setVisibility(View.GONE);

                            } else if (success.equals("0")) {
                                Toast.makeText(DetailEventSubmission.this, "Failed",
                                        Toast.LENGTH_SHORT).show();
                                loadingbar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailEventSubmission.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailEventSubmission.this, "Failed" + error.toString(),
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
}
