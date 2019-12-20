package com.example.vender.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.example.vender.GlobalUser;
import com.example.vender.Model.User;
import com.example.vender.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Serializable {
    User user ;
    private TextView nama_event, venue, deskripsi_event, label_tanggal_event, min_fee, max_fee;
    private Button button_tanggal_event, add_image, button_create_event;
    private Spinner spinner_kota;
    private CheckBox checkBox_penyanyi, checkBox_grup, checkBox_pemainmusik, checkBox_mc, checkBox_penari, checkBox_dj, checkBox_fotografer, checkBox_videografer, checkBox_sulap;
    private ImageView image_tn;
    private String kota, tanggal_event = "", tanggal_deadline, lowongan = "";
    private static final String URL_CREATEEVENT = "https://venderapp.000webhostapp.com/createevent.php";
    private Toolbar toolbar;
    private int PICK_IMAGE_REQUEST = 1, bitmap_size = 60, photochosen = 0;
    private Bitmap bitmap, decoded;
    private ProgressBar progressBar;
    GlobalUser g = GlobalUser.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        user = g.getUser();

        this.nama_event = (TextView) findViewById(R.id.nama_event);
        this.venue = (TextView) findViewById(R.id.venue_event);
        this.deskripsi_event = (TextView) findViewById(R.id.deskripsi_event);
        this.label_tanggal_event = (TextView) findViewById(R.id.label_tanggal_event);
        this.min_fee = (TextView) findViewById(R.id.min_fee);
        this.max_fee = (TextView) findViewById(R.id.max_fee);
        this.progressBar = (ProgressBar) findViewById(R.id.loading);

        this.spinner_kota = (Spinner) findViewById(R.id.spinner_kota);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_kota, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner_kota.setAdapter(adapter);
        this.spinner_kota.setOnItemSelectedListener(this);

        this.button_tanggal_event = (Button) findViewById(R.id.button_tanggal_event);
        this.button_tanggal_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tanggal_event = dayOfMonth + "/" + (month+1) + "/" + year;
                        label_tanggal_event.setText(tanggal_event);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        this.button_create_event = (Button) findViewById(R.id.button_create_event);
        this.button_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    boolean success = true;
                    if (nama_event.getText().toString().length() == 0){
                        nama_event.setError("Tidak boleh kosong");
                        success = false;
                    }
                    if (venue.getText().toString().length() == 0){
                        venue.setError("Tidak boleh kosong");
                        success = false;
                    }
                    if (deskripsi_event.getText().toString().length() == 0){
                        deskripsi_event.setError("Tidak boleh kosong");
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
                    if (tanggal_event.equals("")){
                        label_tanggal_event.setError("Pilih tanggal");
                        success = false;
                    }
                    if (photochosen == 0){
                        add_image.setError("Upload gambar event");
                        success = false;
                    }

                    if (success == true) createEvent();
                } catch (JSONException e){
                    Toast.makeText(CreateEventActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        this.checkBox_penyanyi = (CheckBox) findViewById(R.id.checkBox_penyanyi);
        this.checkBox_grup = (CheckBox) findViewById(R.id.checkBox_grup);
        this.checkBox_pemainmusik = (CheckBox) findViewById(R.id.checkBox_pemainmusik);
        this.checkBox_mc = (CheckBox) findViewById(R.id.checkBox_mc);
        this.checkBox_penari = (CheckBox) findViewById(R.id.checkBox_penari);
        this.checkBox_dj = (CheckBox) findViewById(R.id.checkBox_dj);
        this.checkBox_fotografer = (CheckBox) findViewById(R.id.checkBox_fotografer);
        this.checkBox_videografer = (CheckBox) findViewById(R.id.checkBox_videografer);
        this.checkBox_sulap = (CheckBox) findViewById(R.id.checkBox_sulap);
        this.image_tn = (ImageView) findViewById(R.id.image_tn);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Create Event");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_image = (Button) findViewById(R.id.button_addimage);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoser();
            }
        });
    }

    public void showFileChoser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setToImageView(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        image_tn.setImageBitmap(decoded);
        photochosen = 1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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

    public String checkLowongan(){
        if (checkBox_penyanyi.isChecked()){
            lowongan += checkBox_penyanyi.getText().toString()+",";
        }
        if (checkBox_grup.isChecked()){
            lowongan += checkBox_grup.getText().toString()+",";
        }
        if (checkBox_pemainmusik.isChecked()){
            lowongan += checkBox_pemainmusik.getText().toString()+",";
        }
        if (checkBox_mc.isChecked()){
            lowongan += checkBox_mc.getText().toString()+",";
        }
        if (checkBox_penari.isChecked()){
            lowongan += checkBox_penari.getText().toString()+",";
        }
        if (checkBox_dj.isChecked()){
            lowongan += checkBox_dj.getText().toString()+",";
        }
        if (checkBox_fotografer.isChecked()){
            lowongan += checkBox_fotografer.getText().toString()+",";
        }
        if (checkBox_videografer.isChecked()){
            lowongan += checkBox_videografer.getText().toString()+",";
        }
        if (checkBox_sulap.isChecked()){
            lowongan += checkBox_sulap.getText().toString();
        }

        return lowongan;
    }

    public void createEvent() throws JSONException {
        this.progressBar.setVisibility(View.VISIBLE);
        button_create_event.setClickable(false);
        lowongan = checkLowongan();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CREATEEVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(CreateEventActivity.this, "Event Created",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(CreateEventActivity.this, HomeActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);

                            } else if (success.equals("0")) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(CreateEventActivity.this, "Register Failed",
                                        Toast.LENGTH_SHORT).show();
                                button_create_event.setClickable(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateEventActivity.this, "Register failed" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            button_create_event.setClickable(true);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateEventActivity.this, "Register Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_event", nama_event.getText().toString());
                params.put("deskripsi", deskripsi_event.getText().toString());
                params.put("kota", kota);
                params.put("venue", venue.getText().toString());
                params.put("tanggal_event", tanggal_event);
                params.put("status", "recruiting");
                params.put("lowongan", lowongan);
                params.put("foto", getStringImage(decoded));
                params.put("user_id", String.valueOf(user.getId()));
                params.put("min_fee", min_fee.getText().toString());
                params.put("max_fee", max_fee.getText().toString());
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
