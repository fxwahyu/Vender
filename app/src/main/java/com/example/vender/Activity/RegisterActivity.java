package com.example.vender.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.example.vender.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements Serializable {

    private static final String URL_REGIST = "https://venderapp.000webhostapp.com/register.php";
    private EditText nama, email, password, rpassword, phonenumber;
    private Button register;
    private ProgressBar loadingbar2;
    private User user_loggedin;
    private Toolbar toolbar;
    GlobalUser g = GlobalUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        nama = (EditText) findViewById(R.id.nama_register);
        email = (EditText) findViewById(R.id.email_register);
        phonenumber = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password_register);
        rpassword = (EditText) findViewById(R.id.passwordrepeat_register);
        register = (Button) findViewById(R.id.register);
        loadingbar2 = (ProgressBar) findViewById(R.id.loadingbar2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean enter = true;
                String getnama = nama.getText().toString();
                String getemail = email.getText().toString().trim();
                String getphonenumner = phonenumber.getText().toString().trim();
                String getpassword = password.getText().toString().trim();
                String getrpassword = rpassword.getText().toString().trim();

                if (!getemail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    email.setError("Invalid Email Address");
                    enter = false;
                }
                if (getpassword.length() < 6){
                    password.setError("Password minimal 6 karakter");
                    enter = false;
                }
                if (getphonenumner.isEmpty()){
                    enter = false;
                    phonenumber.setError("Nomor kosong");
                }
                if (getnama.isEmpty()) {
                    nama.setError("Nama kosong");
                    enter = false;
                } else if (getemail.isEmpty()) {
                    email.setError("Email kosong");
                    enter = false;
                } else if (getpassword.isEmpty()) {
                    password.setError("Password kosong");
                    enter = false;
                } else if (getrpassword.isEmpty()) {
                    rpassword.setError("Password kosoong");
                    enter = false;
                } else if (!getrpassword.equals(getpassword)) {
                    rpassword.setError("Password tidak sesuai");
                    enter = false;
                }


                if (enter) try {
                    register(getnama, getemail, getphonenumner, getpassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void register(final String getnama, final String getemail, final String phonenumber, final String getpassword) throws JSONException {
        loadingbar2.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf(
                            "{"), response.lastIndexOf("}") + 1));
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("register");


                    if (success.equals("1")) {
                        loadingbar2.setVisibility(View.GONE);
                        JSONObject object = jsonArray.getJSONObject(0);
                        int id = Integer.parseInt(object.getString("id").trim());
                        user_loggedin = new User(getnama, getemail, getpassword, null, id, "0", phonenumber);
                        g.setUser(user_loggedin);
                        SharedPreference preference = new SharedPreference(RegisterActivity.this);
                        preference.setUser(user_loggedin);
                        preference.setIsLoggedIn(true);

//                        Toast.makeText(RegisterActivity.this, "Register Succeed, ID : "+id,
//                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.putExtra("user", user_loggedin);
                        startActivity(intent);

                    } else if (success.equals("0")) {
                        Toast.makeText(RegisterActivity.this, "Register Failed",
                                Toast.LENGTH_SHORT).show();
                        loadingbar2.setVisibility(View.GONE);
                    } else if (success.equals("-1")) {
                        Toast.makeText(RegisterActivity.this, "Account already registered",
                                Toast.LENGTH_SHORT).show(); loadingbar2.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Register failed" + e.toString(),
                            Toast.LENGTH_SHORT).show();
                    loadingbar2.setVisibility(View.GONE);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Register Failed" + error.toString(),
                        Toast.LENGTH_SHORT).show();
                loadingbar2.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", getnama);
                params.put("email", getemail);
                params.put("phone_number", phonenumber);
                params.put("password", getpassword);
                //this. yg di return adalah params, bukan super.getParams() wkwkkw.
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
