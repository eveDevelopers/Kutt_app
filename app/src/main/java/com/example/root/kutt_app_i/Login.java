package com.example.root.kutt_app_i;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText username,password;
    TextView submit,test,login_text;
    ProgressBar progressBar;
    FloatingActionButton signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.user_id);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.login);
        signup = findViewById(R.id.sign_up);
        login_text = findViewById(R.id.login_text);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat_Light.ttf");
        login_text.setTypeface(custom_font1);
        CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
        progressBar = findViewById(R.id.progressBar);
        test = findViewById(R.id.test);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,SignUp.class);
                startActivity(i);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                final String uname = username.getText().toString();
                final String pass = password.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/login/",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String  status = jsonObject.getString("status");
                                    String text = jsonObject.getString("text");
                                    Toast.makeText(Login.this,text,Toast.LENGTH_SHORT).show();
                                    if(status.equals("200")){
                                       // finish();
                                    }
                                }catch (JSONException e){
                                    Toast.makeText(Login.this,"Error Logging In"+e,Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                                progressBar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);

                                //Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Login.this, "Something went wrong,try again later"+error, Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);

                            }

                        }) {
                    @Override
                    protected Map<String,String> getParams() {
                        Map<String,String> params = new HashMap<>();
                        params.put("username", uname);
                        params.put("password",pass);
                        return params;
                    }
                };
                final RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                requestQueue.add(stringRequest);
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://kutt.fossgect.club/get_statistics/",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String text = jsonObject.getString("data");
                                    Toast.makeText(Login.this,text,Toast.LENGTH_SHORT).show();
                                }catch (JSONException e){
                                    Toast.makeText(Login.this,"Error Logging In"+e,Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                                Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Login.this, "Something went wrong,try again later"+error, Toast.LENGTH_SHORT).show();
                                error.printStackTrace();

                            }

                        }) {
                    @Override
                    protected Map<String,String> getParams() {
                        Map<String,String> params = new HashMap<>();
                        params.put("username",username.getText().toString());
                        return params;
                    }
                };
                final RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                requestQueue.add(stringRequest);
            }

        });
    }
}
