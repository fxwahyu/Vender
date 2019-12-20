package com.example.vender.Activity;

import android.content.Intent;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.vender.Fragment.FragmentFind;
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

public class MainActivity extends AppCompatActivity implements Serializable {
    private EditText email, password;
    private ProgressBar loadingbar;
    private Button login, register;
    private SharedPreference preference;
    private User user_loggedin;
    private GlobalUser g = GlobalUser.getInstance();
    private static String URL_LOGIN = "https://venderapp.000webhostapp.com/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        preference = new SharedPreference(this);
        if (preference.isLoggedIn()){
            user_loggedin = new User();
            user_loggedin = preference.getUser();
            g.setUser(preference.getUser());
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else{
            user_loggedin = new User();

            email = (EditText) findViewById(R.id.email);
            password = (EditText) findViewById(R.id.password);
            login = (Button) findViewById(R.id.loginButton);
            register = (Button) findViewById(R.id.registerButton);
            loadingbar = (ProgressBar) findViewById(R.id.loadingbar);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getEmail = email.getText().toString().trim();
                    String getPassword = password.getText().toString().trim();

                    boolean enter = true;
                    if (getEmail.length() == 0) {
                        email.setError("Email kosong");
                        email.requestFocus();
                        enter = false;
                    } else if (getPassword.length() == 0) {
                        password.setError("Password kosong");
                        password.requestFocus();
                        enter = false;
                    }

                    if (enter) {
                        try{
                            login(getEmail, getPassword);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    } else{
                        Toast.makeText(MainActivity.this, "Email/password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register();
                }
            });

        }

    }

    public void login(final String email, final String password) throws JSONException {
            this.loadingbar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");

                        if (success.equals("1")) {
                            loadingbar.setVisibility(View.GONE);

                            JSONObject object = jsonArray.getJSONObject(0);

                            int id = Integer.parseInt(object.getString("id").trim());
                            user_loggedin = new User(object.getString("nama"), email, password, object.getString("photo"), id, object.getString("istalent"), object.getString("phone_number"));
                            g.setUser(user_loggedin);
                            preference = new SharedPreference(MainActivity.this);
                            preference.setUser(user_loggedin);
                            preference.setIsLoggedIn(true);

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                            intent.putExtra("user", user_loggedin);
                            startActivity(intent);


                        } else if (success.equals("-1")){
                            loadingbar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Akun belum terdaftar", Toast.LENGTH_SHORT).show();
                        }
                        else if (success.equals("0")){
                            Toast.makeText(MainActivity.this, "Password Salah", Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Login gagal" + e.toString(), Toast.LENGTH_SHORT).show();
                        loadingbar.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Login gagal" + error.toString(), Toast.LENGTH_SHORT).show();
                    loadingbar.setVisibility(View.GONE);
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }

    public void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public User getUser(){
        return this.user_loggedin;
    }

}
