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
import android.support.v7.widget.Toolbar;
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

public class EditEventActivity extends AppCompatActivity implements Serializable, AdapterView.OnItemSelectedListener {
    private Event event;
    private TextView nama_event, venue, deskripsi_event, label_tanggal_event, min_fee, max_fee;
    private Button button_tanggal_event, add_image, button_edit;
    private Spinner spinner_kota;
    private CheckBox checkBox_penyanyi, checkBox_grup, checkBox_pemainmusik, checkBox_mc, checkBox_penari, checkBox_dj, checkBox_fotografer, checkBox_videografer, checkBox_sulap;
    private ImageView image_tn, image_tn2;
    private String kota, tanggal_event, tanggal_deadline, lowongan = "";
    private static final String URL_EDITEVENT = "https://venderapp.000webhostapp.com/editevent.php";
    private Toolbar toolbar;
    private User user;
    private GlobalUser g = GlobalUser.getInstance();
    private int PICK_IMAGE_REQUEST = 1, bitmap_size = 60, changed = 0;
    private Bitmap bitmap, decoded;
    private ProgressBar loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editevent);
        Intent i = getIntent();
        event = (Event) i.getSerializableExtra("event");
        this.user = g.getUser();

        this.nama_event = (TextView) findViewById(R.id.edit_nama_event);
        this.venue = (TextView) findViewById(R.id.edit_venue_event);
        this.deskripsi_event = (TextView) findViewById(R.id.edit_deskripsi_event);
        this.label_tanggal_event = (TextView) findViewById(R.id.edit_label_tanggal_event);
        this.min_fee = (TextView) findViewById(R.id.edit_min_fee);
        this.max_fee = (TextView) findViewById(R.id.edit_max_fee);
        this.image_tn = (ImageView) findViewById(R.id.edit_image_tn);
        this.image_tn2 = (ImageView) findViewById(R.id.edit_image_tn2);
        this.loading = (ProgressBar) findViewById(R.id.loading);

        this.spinner_kota = (Spinner) findViewById(R.id.edit_spinner_kota);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_kota, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner_kota.setAdapter(adapter);
        this.spinner_kota.setOnItemSelectedListener(this);

        this.button_tanggal_event = (Button) findViewById(R.id.edit_button_tanggal_event);
        this.button_tanggal_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tanggal_event = dayOfMonth + "/" + month + "/" + year;
                        label_tanggal_event.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        this.button_edit = (Button) findViewById(R.id.button_save);
        this.button_edit.setOnClickListener(new View.OnClickListener() {
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

                    if (success == true) createEvent();
                } catch (JSONException e){
                    Toast.makeText(EditEventActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
        this.image_tn = (ImageView) findViewById(R.id.edit_image_tn);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setValues();

        toolbar.setTitle("Edit Event");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.add_image = (Button) findViewById(R.id.edit_button_addimage);
        this.add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoser();
            }
        });
    }

    public void setValues(){
        this.nama_event.setText(event.getNama_event());
        this.venue.setText(event.getVenue());
        this.deskripsi_event.setText(event.getDeskripsi());
        this.label_tanggal_event.setText(event.getTanggal_event());
        this.min_fee.setText(event.getMin_fee());
        this.max_fee.setText(event.getMax_fee());
        this.tanggal_event = event.getTanggal_event();
        Glide.with(this)
                .load(event.getFoto())
                .into(this.image_tn);
        int index = 0;
        String[] arrkota = {"Surabaya", "Jakarta", "Semarang", "Medan"};
        for (int i = 0; i < arrkota.length; i++){
            if (event.getKota().equals(arrkota[i])){
                index = i;
                break;
            }
        }

        this.spinner_kota.setSelection(index);
        setChecked();
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
        image_tn2.setImageBitmap(decoded);
        image_tn.setVisibility(View.GONE);
        image_tn2.setVisibility(View.VISIBLE);
        changed = 1;
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

    public void setChecked(){
        this.lowongan = event.getLowongan();
        String[] lowongan = this.lowongan.split(",");

        for (int i = 0; i < lowongan.length; i++){
            String temp = lowongan[i];
            if (temp.equalsIgnoreCase("Penyanyi")) checkBox_penyanyi.setChecked(true);
            else if (temp.equalsIgnoreCase("Pemain Musik")) checkBox_pemainmusik.setChecked(true);
            else if (temp.equalsIgnoreCase("Penari")) checkBox_penari.setChecked(true);
            else if (temp.equalsIgnoreCase("Fotografer")) checkBox_fotografer.setChecked(true);
            else if (temp.equalsIgnoreCase("Videografer")) checkBox_videografer.setChecked(true);
            else if (temp.equalsIgnoreCase("Band/Vocal Group")) checkBox_grup.setChecked(true);
            else if (temp.equalsIgnoreCase("MC")) checkBox_mc.setChecked(true);
            else if (temp.equalsIgnoreCase("DJ")) checkBox_dj.setChecked(true);
            else if (temp.equalsIgnoreCase("Pesulap/Badut")) checkBox_sulap.setChecked(true);
        }

    }
    public String checkLowongan(){
        lowongan = "";
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
        loading.setVisibility(View.VISIBLE);
        button_edit.setClickable(false);
        lowongan = checkLowongan();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDITEVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                                    "{"), response.lastIndexOf("}") + 1));
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(EditEventActivity.this, "Saved",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(EditEventActivity.this, HomeActivity.class);
//                                intent.putExtra("user", user);
                                startActivity(intent);

                            } else if (success.equals("0")) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(EditEventActivity.this, "Failed",
                                        Toast.LENGTH_LONG).show();
                                System.out.println("ganti tanpa foto failed");
                                button_edit.setClickable(true);
                            } else if (success.equals("2")){
                                loading.setVisibility(View.GONE);
                                System.out.println("ganti foto success");
                                Intent intent = new Intent(EditEventActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else if (success.equals("-1")){
                                loading.setVisibility(View.GONE);
                                System.out.println("ganti foto failed"+jsonObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditEventActivity.this, "Failed" + e.toString(),
                                    Toast.LENGTH_LONG).show();
                            button_edit.setClickable(true);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Toast.makeText(EditEventActivity.this, "Failed Failed" + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(event.getId()));
                params.put("nama_event", nama_event.getText().toString());
                params.put("deskripsi", deskripsi_event.getText().toString());
                params.put("kota", kota);
                params.put("venue", venue.getText().toString());
                params.put("tanggal_event", tanggal_event);
                params.put("status", "recruiting");
                params.put("lowongan", lowongan);
                params.put("user_id", String.valueOf(event.getUser_id()));
                params.put("min_fee", min_fee.getText().toString());
                params.put("max_fee", max_fee.getText().toString());
                if (changed == 0) params.put("foto", "");
                else params.put("foto", getStringImage(decoded));
                params.put("changed", String.valueOf(changed));
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
