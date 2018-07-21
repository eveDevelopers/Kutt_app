package com.example.root.kutt_app_i;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {


    DatabaseHelper myDb;
    TextView data,shlink,Bartitle;
    ImageView save,cut,copy,sharelink,close,qr;
    String text;
    LinearLayout got,sharel;
    ProgressBar progressBar;
    SurfaceView cameraView;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;
    public static  final int PERMISSION_REQUEST=200;
    public  static final int REQUEST_CODE=100;
    ObjectAnimator animator;
    View scannerLayout;
    View scannerBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main2);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDb = new DatabaseHelper(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
       // String manufacturer = "xiaomi";
        /*if(manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            //this will open auto start screen where user can enable permission for your app2
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        }*/

       //show_text = (Button) findViewById(R.id.show_text);//
       // Intent intent = new Intent(MainActivity.this,TheService.class);
       // startService(intent);
        Bartitle = findViewById(R.id.toolbar_title);
        qr=findViewById(R.id.qr);
        //profile = findViewById(R.id.profile);
        //got = findViewById(R.id.button);
        close = findViewById(R.id.close);
        sharel = findViewById(R.id.shortl);
        copy = findViewById(R.id.copy);
        progressBar = findViewById(R.id.progressBar);
        sharelink = findViewById(R.id.sharelink);
        data = findViewById(R.id.clipboard_data);
        save = findViewById(R.id.save);
        save.setVisibility(View.GONE);
        shlink = findViewById(R.id.shortlink);
        sharel.setVisibility(View.GONE);
        cut = findViewById(R.id.cut);
        cut.setVisibility(View.GONE);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String receivedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                receiveData(receivedText);// Handle text being sent
            }
        }else {
            get_data();
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sharelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = shlink.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clipboard_Utils.copyToClipboard(MainActivity.this,shlink.getText().toString());
            }
        });
        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.length()>=27) {
                    if (!text.substring(0, 26).equals("http://kutt.fossgect.club/")) {
                        cut.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String link = "http://kutt.fossgect.club/"+response;
                                        cut.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        sharel.setVisibility(View.VISIBLE);
                                        shlink.setText(link);
                                        requestQueue.stop();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        cut.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, "Something went wrong,try again later", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();

                                        requestQueue.stop();
                                    }

                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("url", text);
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                    } else {
                        Toast.makeText(MainActivity.this, "This link can't be shortened further", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    cut.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String link = "http://kutt.fossgect.club/"+response;
                                    cut.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    sharel.setVisibility(View.VISIBLE);
                                    shlink.setText(link);
                                    requestQueue.stop();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    cut.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Something went wrong,try again later", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();

                                    requestQueue.stop();
                                }

                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("url", text);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });
       //clipboardData = (TextView) findViewById(R.id.clipboard_data);//
       //get_data();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });





    }
    @Override
    public void onResume(){
        super.onResume();
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,QrActivity.class);
                startActivityForResult(i,REQUEST_CODE);
            }
        });
        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                text = Clipboard_Utils.getDataFromClipboard(MainActivity.this);
                if(text.length()>=27) {
                    if (!text.substring(0, 26).equals("http://kutt.fossgect.club/")) {
                        sharel.setVisibility(View.GONE);
                    }
                }else {
                    sharel.setVisibility(View.GONE);
                }
                get_data();

            }
        });

    }
    public void get_data(){
        text = Clipboard_Utils.getDataFromClipboard(MainActivity.this);
        String[] text1 = text.split(":");
        if (!text.equals("")) {
            if (text1[0].equals("http") || text1[0].equals("https")) {
                if(text.length() > 62) {
                    data.setText(text.substring(0, 59) + "...");
                }else {
                    data.setText(text);
                }

                save.setVisibility(View.VISIBLE);
                cut.setVisibility(View.VISIBLE);
            } else {
                data.setText("Clipboard doesn't contain a valid link");
            }
        }


    }
    public  void receiveData(String text2){
        String[] text1 = text2.split(":");
        if (!text2.equals("")) {
            if (text1[0].equals("http") || text1[0].equals("https")) {
                if(text2.length() > 62) {
                    data.setText(text2.substring(0, 59) + "...");
                }else {
                    data.setText(text2);
                }
                text = text2;
                save.setVisibility(View.VISIBLE);
                cut.setVisibility(View.VISIBLE);
            } else {
                data.setText("Not a valid link");
            }
        }

    }
    public void insertData(){
        String[] text1 = text.split(":");
        if (!text.equals("")) {
            if (text1[0].equals("http") || text1[0].equals("https")) {
                boolean isInserted = myDb.insertData(text);
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Link saved", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MainActivity.this, "Not a legal link", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(MainActivity.this, "Clipboard is empty.", Toast.LENGTH_SHORT).show();
        }
        //myDb.close();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data1) {
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){

            if(data1!=null){

                final Barcode barcode =data1.getParcelableExtra("barcode");
                text = barcode.displayValue;
                data.post(new Runnable() {
                    @Override
                    public void run() {
                        data.setText(text);
                        receiveData(text);
                    }
                });

            }

        }
    }


}