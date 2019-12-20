package com.example.vender.Activity;

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

import com.bumptech.glide.Glide;
import com.example.vender.GlobalUser;
import com.example.vender.Model.Event;
import com.example.vender.Model.MyJob;
import com.example.vender.Model.User;
import com.example.vender.R;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class DetailEventMyJob extends AppCompatActivity implements Serializable {
    private TextView nama, venue, kota, tanggal, deskripsi, lowongan, min_fee, max_fee;
    private ImageView image;
    private Button contact;
    private Toolbar toolbar;
    private MyJob myJob;
    private User user;
    private GlobalUser g = GlobalUser.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_eventmyjob);
        this.user = g.getUser();

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
        contact = (Button) findViewById(R.id.button_contact);

        toolbar.setTitle("Event Detail");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        myJob = (MyJob) intent.getSerializableExtra("myjob");
        setData(myJob);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Halo, saya ingin bertanya seputar event " + myJob.getNama_event();
                String number = myJob.getPhonenumber();
                String formattednumber = "62" + number.substring(1, number.length());
                try {
                    String url = "https://wa.me/" + formattednumber + "?text=" + URLEncoder.encode(text, "UTF-8");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);

                } catch (UnsupportedEncodingException e) {

                }
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

    public void setData(MyJob myJob){
        this.nama.setText(myJob.getNama_event());
        this.venue.setText(myJob.getVenue()+",");
        this.kota.setText(myJob.getKota());
        this.tanggal.setText(myJob.getTanggal_event());
        this.deskripsi.setText(myJob.getDeskripsi());
        String[] lowongan_ = myJob.getLowongan().split(",");
        String lowongan = "";
        for (int j = 0; j < lowongan_.length; j++){
            if (j == lowongan_.length-1){
                lowongan += lowongan_[j].substring(0, lowongan_[j].length());
            } else{
                lowongan += lowongan_[j]+", ";
            }
        }
        this.lowongan.setText(lowongan);
        Glide.with(DetailEventMyJob.this).load(myJob.getFoto()).asBitmap().placeholder(R.drawable.blankevent).centerCrop().into(this.image);
        this.min_fee.setText("Rp "+numberFormat(myJob.getMin_fee()));
        this.max_fee.setText(numberFormat(myJob.getMax_fee()));
    }
}
