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

public class DetailEventList extends AppCompatActivity {
    private TextView nama, venue, kota, tanggal, deskripsi, lowongan, min_fee, max_fee;
    private ImageView image;
    private Button sendoffer;
    private Toolbar toolbar;
    private ProgressBar loadingbar;
    private Event event;
    private User user;
    private GlobalUser g = GlobalUser.getInstance();
    private static final String URL_SENDOFFER = "https://venderapp.000webhostapp.com/sendoffer.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_eventlist);
        this.user = g.getUser();
        event = new Event();

        nama = (TextView) findViewById(R.id.nama_event_detail);
        venue = (TextView) findViewById(R.id.venue_detail);
        kota = (TextView) findViewById(R.id.kota_detail);
        tanggal = (TextView) findViewById(R.id.tanggal_detail);
        deskripsi = (TextView) findViewById(R.id.deskripsi_detail);
        lowongan = (TextView) findViewById(R.id.lowongan_detail);
        min_fee = (TextView) findViewById(R.id.min_fee_detail);
        max_fee = (TextView) findViewById(R.id.max_fee_detail);
        image = (ImageView) findViewById(R.id.event_tn_detail);
        sendoffer = (Button) findViewById(R.id.button_sendOffer);
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
        event = (Event) intent.getSerializableExtra("event");
        setData(event);
        detailFromList();

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
        Glide.with(DetailEventList.this).load(event.getFoto()).asBitmap().placeholder(R.drawable.blankevent).centerCrop().into(this.image);
        this.min_fee.setText("Rp "+numberFormat(event.getMin_fee()));
        this.max_fee.setText(numberFormat(event.getMax_fee()));
    }

    public void detailFromList(){
        if (user.getId() != event.getUser_id()) //jika event tersebut bukan milik user
        {
            if (user.getIstalent().equals("1")){ //jika user adalah talent
                sendoffer.setVisibility(View.VISIBLE);
                sendoffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventList.this);
                        builder.setCancelable(true);
                        builder.setTitle("Mengajukan Penawaran");
                        builder.setMessage("Anda akan mengirimkan penawaran sebagai talent pada event "+ nama.getText().toString());
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendoffertoevent(event.getId(), user.getId());
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
            } else{ //jika user bukan talent
                sendoffer.setVisibility(View.VISIBLE);
                sendoffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventList.this);
                        builder.setCancelable(true);
                        builder.setTitle("Gagal");
                        builder.setMessage("Anda belum terdaftar sebagai talent, silahkan mendaftar pada halaman My Talent Profile");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        android.support.v7.app.AlertDialog showdialog = builder.create();
                        showdialog.show();
                    }
                });
            }
        }
    }

    public void sendoffertoevent(final int eventid, final int userid){
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
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventList.this);
                                builder.setCancelable(true);
                                builder.setTitle("Berhasil!");
                                builder.setMessage("Lihat progress pengajuan anda pada halaman Submission > Submission to Event");
                                builder.setPositiveButton("Confirm",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(DetailEventList.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                android.support.v7.app.AlertDialog showdialog = builder.create();
                                showdialog.show();

                            } else if (success.equals("-1")) { //offer sedang waiting atau accepted
                                loadingbar.setVisibility(View.GONE);
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventList.this);
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
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailEventList.this);
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
                            Toast.makeText(DetailEventList.this, "failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
//                            loadingbar.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailEventList.this, "Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
                loadingbar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(eventid));
                params.put("talent_id", String.valueOf(userid));
                params.put("status", "waiting");
                params.put("dari", "talent");
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
