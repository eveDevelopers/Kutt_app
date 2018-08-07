package com.example.root.kutt_app_i;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagerFragment extends Fragment {


    private static final String KEY_LINK = "key_link";
    private static final String KEY_POS = "key_pos";
    private static final String KEY_TITLE = "key_title";
    private static final String KEY_ICON = "key_icon";
    private static final String KEY_PAGE_ID = "key_page_id";
    private static final String KEY_FAV = "fav_int";
    private String link;
    private int pos;
    private TextView titleView,link_view;
    Boolean got_icon,got_tittle;
    private ImageView icon_view;
    private int page_id;
    DatabaseHelper myDb;
    Cursor res;
    int fav_int;
    String title_text,test_link;
    byte[] icon_array;
    WebView mWebview;
    LinearLayout link_layout,deleted_link;
    ImageView fav,not_fav,share,delete;
    ProgressBar progressBar;
    CardView present_link;


    public static PagerFragment newInstance(ListenItem item, int page_id ) {
        Bundle args = new Bundle();
       // List_item = item;
        args.putInt(KEY_POS,item.getPos_in_db());
        args.putString(KEY_LINK, item.getLink());
        args.putString(KEY_TITLE,item.getTitle());
        args.putByteArray(KEY_ICON,item.getIcon());
        args.putInt(KEY_PAGE_ID, page_id);
        args.putInt(KEY_FAV,item.getStar());
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.pos = bundle.getInt(KEY_POS);
            this.page_id = bundle.getInt(KEY_PAGE_ID);
            this.link = bundle.getString(KEY_LINK);
            this.icon_array = bundle.getByteArray(KEY_ICON);
            this.title_text = bundle.getString(KEY_TITLE);
            this.fav_int = bundle.getInt(KEY_FAV);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_page, container, false);
        this.titleView = view.findViewById(R.id.txt_title);
        this.icon_view = view.findViewById(R.id.icon);
        this.link_view = view.findViewById(R.id.link);
        this.mWebview = view.findViewById(R.id.webview);
        this.link_layout = view.findViewById(R.id.linear_link);
        this.fav = view.findViewById(R.id.fav);
        this.not_fav = view.findViewById(R.id.not_fav);
        this.share = view.findViewById(R.id.share);
        this.delete = view.findViewById(R.id.delete);
        this.progressBar = view.findViewById(R.id.progressBar);
        this.present_link = view.findViewById(R.id.cardview1);
        this.deleted_link = view.findViewById(R.id.deleted_link);


        this.res = myDb.preview_data(this.pos);
        //this.test_link = res.getString(0);
        if(this.res.getCount() == 0){
            deleted_link.setVisibility(View.VISIBLE);
            present_link.setVisibility(View.GONE);
        }else {
            int star = myDb.getStar(this.pos);
            if(star == 1){
                fav.setVisibility(View.VISIBLE);
                not_fav.setVisibility(View.GONE);
            }else {
                fav.setVisibility(View.GONE);
                not_fav.setVisibility(View.VISIBLE);
            }
            this.link_view.setText(this.link);
            deleted_link.setVisibility(View.GONE);
            present_link.setVisibility(View.VISIBLE);
            if(this.title_text == null ||  this.icon_array == null || this.title_text.equals("Web page not available")) {
                got_icon = false;
                got_tittle = false;
                if (mWebview != null) {
                    mWebview.setWebViewClient(new WebViewClient());
                    mWebview.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView view, int newProgress) {
                            super.onProgressChanged(view, newProgress);

                            // Your custom code.
                        }

                        @Override
                        public void onReceivedTitle(WebView view, String title) {
                            super.onReceivedTitle(view, title);
                            if (title.length() > 30) {
                                titleView.setText(title.substring(0, 27) + "...");
                            } else {
                                titleView.setText(title);
                            }
                            new DatabaseHelper(getActivity()).add_title(title,link);
                            got_tittle = true;
                            if (got_tittle && got_icon) {
                                mWebview.setTag(null);
                                mWebview.clearHistory();
                                mWebview.removeAllViews();
                                mWebview.clearView();
                                mWebview.destroy();
                                mWebview = null;
                            }
                        }

                        @Override
                        public void onReceivedIcon(WebView view, Bitmap icon) {
                            super.onReceivedIcon(view, icon);
                            //icon_view.setImageBitmap(icon);
                            Glide.with(getActivity()).load(icon).into(icon_view);
                            got_icon = true;
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            icon.compress(Bitmap.CompressFormat.PNG, 0, stream);
                            new DatabaseHelper(getActivity()).add_icon(stream.toByteArray(),link);
                            if (got_tittle && got_icon) {
                                mWebview.setTag(null);
                                mWebview.clearHistory();
                                mWebview.removeAllViews();
                                mWebview.clearView();
                                mWebview.destroy();
                                mWebview = null;
                            }
                        }
                    });
                    this.mWebview.loadUrl(link);
                }
            }else {
                if (this.title_text.length() > 30) {
                    this.titleView.setText(this.title_text.substring(0, 27) + "...");
                } else {
                    this.titleView.setText(this.title_text);
                }
                if(this.icon_array != null) {
                    //this.icon_view.setImageBitmap(BitmapFactory.decodeByteArray(this.icon_array, 0, this.icon_array.length));
                    Glide.with(getActivity()).load(this.icon_array).into(this.icon_view);
                }

            }
        }





        link_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(link);
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

                intentBuilder.setShowTitle(true);
                intentBuilder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.white));
                intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getActivity(), R.color.white));
                intentBuilder.setStartAnimations(getActivity(), R.anim.zoom_enter, R.anim.zoom_exit);
                intentBuilder.setExitAnimations(getActivity(), android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

                CustomTabsIntent customTabsIntent = intentBuilder.build();
                customTabsIntent.launchUrl(getActivity(), uri);
            }
        });
        not_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateData(link);
                fav.setVisibility(View.VISIBLE);
                not_fav.setVisibility(View.GONE);
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateNormal(link);
                fav.setVisibility(View.GONE);
                not_fav.setVisibility(View.VISIBLE);
            }
        });
        this.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if(link.length()>=27) {
                    if (!link.substring(0, 26).equals("http://kutt.fossgect.club/")) {
                        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        response = "http://kutt.fossgect.club/" + response;
                                        Intent i = new Intent();
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, response);
                                        i.setType("text/plain");
                                        share.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(i);
                                        Toast.makeText(getActivity(), "Sharing shortened link", Toast.LENGTH_SHORT).show();
                                        requestQueue.stop();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Intent i = new Intent();
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, link);
                                        i.setType("text/plain");
                                        share.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(i);
                                        error.printStackTrace();
                                        requestQueue.stop();
                                    }

                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("url", link);
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                    } else {
                        Intent i = new Intent();
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setAction(Intent.ACTION_SEND);
                        i.putExtra(Intent.EXTRA_TEXT, link);
                        i.setType("text/plain");
                        share.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        startActivity(i);
                    }
                }else {
                    final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    response = "http://kutt.fossgect.club/" + response;
                                    Intent i = new Intent();
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setAction(Intent.ACTION_SEND);
                                    i.putExtra(Intent.EXTRA_TEXT, response);
                                    i.setType("text/plain");
                                    share.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(i);
                                    Toast.makeText(getActivity(), "Sharing shortened link", Toast.LENGTH_SHORT).show();
                                    requestQueue.stop();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Intent i = new Intent();
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setAction(Intent.ACTION_SEND);
                                    i.putExtra(Intent.EXTRA_TEXT, link);
                                    i.setType("text/plain");
                                    share.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(i);
                                    error.printStackTrace();
                                    requestQueue.stop();
                                }

                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("url", link);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });
        this.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete!");
                builder.setMessage("Are you sure ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.deletelink(link);
                        deleted_link.setVisibility(View.VISIBLE);
                        present_link.setVisibility(View.GONE);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        return view;
    }


}