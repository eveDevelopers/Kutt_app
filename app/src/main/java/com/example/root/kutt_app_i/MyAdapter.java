package com.example.root.kutt_app_i;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by RahulKMohan on 07-01-2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private List<ListenItem> listenItems;


    Context context;
   // HashMap<String,Integer> h;

    public MyAdapter(List<ListenItem> listenItems, Context context) {
        this.listenItems = listenItems;
        this.context = context;

    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listen, parent, false);
        return new ViewHolder(v, context, listenItems);
    }


    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {

        final ListenItem listen = listenItems.get(position);
        final String link = listen.getLink();
        holder.Name.setText(link);
        /*for(String i: h.keySet()){
            if(link.contains(i)){
                Picasso.get().load(h.get(i)).resize(75,75).into(holder.web);
                break;
            }
        }*/
        Picasso.get().load(R.drawable.not_fav).resize(30,30).into(holder.del);
        Picasso.get().load(R.drawable.fav).resize(30,30).into(holder.fav);
       // final byte[] icon_T = listen.getIcon();
        Picasso.get().load(R.drawable.share).resize(30,30).into(holder.share);
        if(listen.getStar()==0){
            holder.fav.setVisibility(View.GONE);
            holder.del.setVisibility(View.VISIBLE);
        }
        else {
            holder.fav.setVisibility(View.VISIBLE);
            holder.del.setVisibility(View.GONE);
        }
        if( context instanceof Application){
            holder.del.setVisibility(View.GONE);
            holder.fav.setVisibility(View.GONE);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Dis.class);
                i.putExtra("link",link);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
       /* holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Clipboard_Utils.copyToClipboard(context,link);
                return false;
            }
        });*/
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDb = new DatabaseHelper(context);
                String link = listen.getLink();
                myDb.updateData(link);
                Toast.makeText(context,"Added to favorites",Toast.LENGTH_SHORT).show();
                holder.del.setVisibility(View.GONE);
                holder.fav.setVisibility(View.VISIBLE);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.share.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
                if(link.length()>=27) {
                    if (!link.substring(0, 26).equals("http://kutt.fossgect.club/")) {
                        final RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                        holder.share.setVisibility(View.VISIBLE);
                                        holder.progressBar.setVisibility(View.GONE);
                                        context.startActivity(i);
                                        Toast.makeText(context, "Sharing shortened link", Toast.LENGTH_SHORT).show();
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
                                        holder.share.setVisibility(View.VISIBLE);
                                        holder.progressBar.setVisibility(View.GONE);
                                        context.startActivity(i);
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
                        holder.share.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.GONE);
                        context.startActivity(i);
                    }
                }else {
                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                    holder.share.setVisibility(View.VISIBLE);
                                    holder.progressBar.setVisibility(View.GONE);
                                    context.startActivity(i);
                                    Toast.makeText(context, "Sharing shortened link", Toast.LENGTH_SHORT).show();
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
                                    holder.share.setVisibility(View.VISIBLE);
                                    holder.progressBar.setVisibility(View.GONE);
                                    context.startActivity(i);
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
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDb = new DatabaseHelper(context);
                String link = listen.getLink();
                myDb.updateNormal(link);
                holder.fav.setVisibility(View.GONE);
                holder.del.setVisibility(View.VISIBLE);
                Toast.makeText(context,"Removed from favorites",Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listenItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Name,title;
        public LinearLayout linearLayout,confirm,exp,card;
        private ImageView del,share,web,fav;
        //private CardView card;
        private String linkc,link;
        private WebView mWebview;
        ProgressBar progressBar;
        //final ListenItem listen = listenItems.get(position);

        public ViewHolder(final View ItemView, Context context, List<ListenItem> listenItems) {
            super(ItemView);
            card = ItemView.findViewById(R.id.card);
            fav = ItemView.findViewById(R.id.fav);
            share = ItemView.findViewById(R.id.share);
            del = ItemView.findViewById(R.id.del);
            progressBar = ItemView.findViewById(R.id.progressBar);
            Name = ItemView.findViewById(R.id.textViewName);
            linearLayout=ItemView.findViewById(R.id.linearlayout);
            /*h = new HashMap<>();
            h.put("play.google",R.drawable.google_play);
            h.put("www.google",R.drawable.google);
            h.put("facebook.com",R.drawable.facebook);
            h.put("youtube.com",R.drawable.youtube);
            h.put("youtu.be",R.drawable.youtube);
            h.put("instagram.com",R.drawable.instagram);
            h.put("twitter.com",R.drawable.twitter);
            h.put("fb.com",R.drawable.facebook);
            h.put("amazon.com",R.drawable.amazon);
            h.put("amazon.in",R.drawable.amazon);
            h.put("google.co.in",R.drawable.google);
            h.put("medium.com",R.drawable.medium);
            h.put("wikipedia.org",R.drawable.wikipedia);
            h.put("stackoverflow.com",R.drawable.stackoverflow);
            h.put("flipkart.com",R.drawable.flipkart);*/

        }




    }
}