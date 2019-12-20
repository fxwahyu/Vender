package com.example.vender.Activity;

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
import android.widget.Button;
import android.widget.EditText;
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
import com.example.vender.Model.User;
import com.example.vender.R;
import com.example.vender.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private User user, user_loggedin;
    private GlobalUser g = GlobalUser.getInstance();
    private EditText nama, email, password, rpassword, phonenumber;
    private ImageView profilephoto, profilephoto2;
    private Button save, changephoto;
    private Toolbar toolbar;
    private static final String URL_EDITPROFILE = "https://venderapp.000webhostapp.com/editprofile.php";
    private int PICK_IMAGE_REQUEST = 1, bitmap_size = 60;
    private Bitmap bitmap, decoded;
    private int changed = 0;
    private ProgressBar loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);
        user = g.getUser();

        nama = (EditText) findViewById(R.id.nama);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.changepassword);
        rpassword = (EditText) findViewById(R.id.rpassword);
        phonenumber = (EditText) findViewById(R.id.phone_number);
        profilephoto = (ImageView) findViewById(R.id.profilephoto);
        profilephoto2 = (ImageView) findViewById(R.id.profilephoto2);
        changephoto = (Button) findViewById(R.id.button_changeimage);
        loading = (ProgressBar) findViewById(R.id.loading);
        save = (Button) findViewById(R.id.save);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nama.setText(user.getNama());
        email.setText(user.getEmail());
        phonenumber.setText(user.getPhone_number());
        email.setEnabled(false);
        Glide.with(this)
                .load(user.getFoto())
                .placeholder(R.drawable.blankprofile)
                .into(this.profilephoto);

        changephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoser();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nama.getText().toString();
                String pass = password.getText().toString().trim();
                String rpass = rpassword.getText().toString().trim();

                if (pass.isEmpty()){ //tidak mengubah pass
                    pass = "-1";
                    save(name, pass);
                } else{
                    if (rpass.isEmpty()){
                        rpassword.setError("Masukkan password");
                    } else{
                        if (rpass.equals(pass)){
                            save(name, pass);
                        } else{
                            rpassword.setError("Password tidak sama");
                        }
                    }
                }
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
        profilephoto2.setImageBitmap(decoded);
        profilephoto2.setVisibility(View.VISIBLE);
        profilephoto.setVisibility(View.GONE);
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

    public void save(final String nama, final String password){
        loading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDITPROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    String success = jsonObject.getString("success");


                    if (success.equals("1") || success.equals("2") || success.equals("3") || success.equals("4")) {
//                        loadingbar.setVisibility(View.GONE);

                        if (success.equals("1") || success.equals("3")) user_loggedin = new User(nama, user.getEmail(), password, user.getFoto(), user.getId(), user.getIstalent(), phonenumber.getText().toString().trim());
                        else {
                            String foto = jsonObject.getString("foto");
                            user_loggedin = new User(nama, user.getEmail(), password, foto, user.getId(), user.getIstalent(), phonenumber.getText().toString().trim());
                        }
                        loading.setVisibility(View.GONE);
                        g.setUser(user_loggedin);
                        SharedPreference preference = new SharedPreference(EditProfile.this);
                        preference.setUser(user_loggedin);
                        preference.setIsLoggedIn(true);

                        Toast.makeText(EditProfile.this, "Saved", Toast.LENGTH_SHORT).show();
                        System.out.println(success);
                        Intent intent = new Intent(EditProfile.this, HomeActivity.class);
//                            intent.putExtra("user", user_loggedin);
                        startActivity(intent);


                    } else if (success.equals("0") || success.equals("-1")){
                        loading.setVisibility(View.GONE);
                        System.out.println(success);
                        Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
//                        loadingbar.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    loading.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toast.makeText(EditProfile.this, "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
//                    loadingbar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Toast.makeText(EditProfile.this, "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
//                loadingbar.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(user.getId()));
                params.put("nama", nama);
                params.put("password", password);
                params.put("phonenumber", phonenumber.getText().toString().trim());
                if (changed == 0) params.put("foto", "");
                else params.put("foto", getStringImage(decoded));
                params.put("changed", String.valueOf(changed));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
