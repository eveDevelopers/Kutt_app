package com.example.root.kutt_app_i;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {
    SignInButton sign_in;
    Switch notification;
    TextView name;
    ImageView close,signout;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    TextView mStatusTextView;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_account);
        sign_in = findViewById(R.id.sign_in);
        notification = findViewById(R.id.notification);
        close = findViewById(R.id.close);
        signout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat_Light.ttf");
        name.setTypeface(custom_font1);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("124232395859-suevi2hljk37mgvm03vts6lrh1eedfmd.apps.googleusercontent.com")
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        SharedPreferences not = getSharedPreferences("notif",MODE_PRIVATE);
        final SharedPreferences.Editor editor = not.edit();
        if(isMyServiceRunning(TheService.class) || isMyServiceRunning(SensorService.class) || not.getInt("enable",1)==1) {
            notification.setChecked(true);
        }else {
            notification.setChecked(false);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   editor.putInt("enable",1);
                   editor.apply();
                   /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       getApplicationContext().startForegroundService(new Intent(getApplicationContext(), TheService.class));
                   }else {
                       getApplicationContext().startService(new Intent(getApplicationContext(), SensorService.class));
                   }*/
                   getApplicationContext().startService(new Intent(getApplicationContext(), SensorService.class));
                   getApplicationContext().startService(new Intent(getApplicationContext(), TheService.class));
                   //ServiceJob.enqueueWork(getApplicationContext(),getIntent());
               }else {
                   editor.putInt("enable",0);
                   editor.apply();
                   //if(isMyServiceRunning(SensorService.class)) {
                       getApplicationContext().stopService(new Intent(getApplicationContext(), SensorService.class));
                   //}

                   getApplicationContext().stopService(new Intent(getApplicationContext(), TheService.class));

                   Toast.makeText(Account.this,"Service Stopped!",Toast.LENGTH_SHORT).show();
               }
           }
       });

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            final String idToken = account.getIdToken();
            updateUI(account);


            final RequestQueue requestQueue = Volley.newRequestQueue(Account.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(Account.this,response,Toast.LENGTH_LONG).show();
                            /*String link = "http://kutt.fossgect.club/"+response;
                            cut.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            sharel.setVisibility(View.VISIBLE);
                            shlink.setText(link);**/
                            requestQueue.stop();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           /* cut.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);*/
                            Toast.makeText(Account.this,error.toString(), Toast.LENGTH_SHORT).show();
                            error.printStackTrace();

                            requestQueue.stop();
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idTocken",idToken );
                    params.put("url","http://ex.com");
                    return params;
                }
            };
            requestQueue.add(stringRequest);





        } catch (ApiException e) {

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    private void updateUI(@Nullable GoogleSignInAccount account) {

        if(account!=null) {
            sign_in.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            name.setText(account.getDisplayName());
            signout.setVisibility(View.VISIBLE);



        }
        else
        {
            sign_in.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
            signout.setVisibility(View.GONE);
            name.setText("");
        }


    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
}
