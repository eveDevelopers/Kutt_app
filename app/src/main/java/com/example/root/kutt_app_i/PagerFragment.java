package com.example.root.kutt_app_i;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PagerFragment extends Fragment {


    private static final String KEY_LINK = "key_link";
    private static final String KEY_TITLE = "key_title";
    private static final String KEY_ICON = "key_icon";
    private static final String KEY_PAGE_ID = "key_page_id";
    private String link;
    private int pos;
    private TextView titleView,link_view;
    Boolean got_icon,got_tittle;
    private ImageView icon_view;
    private int page_id;
    DatabaseHelper myDb;
    Cursor res;
    String title_text;
    byte[] icon_array;
    WebView mWebview;
    LinearLayout link_layout;


    public static PagerFragment newInstance(ListenItem item, int page_id ) {
        Bundle args = new Bundle();
       // List_item = item;
        args.putString(KEY_LINK, item.getLink());
        args.putString(KEY_TITLE,item.getTitle());
        args.putByteArray(KEY_ICON,item.getIcon());
        args.putInt(KEY_PAGE_ID, page_id);
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
            this.page_id = bundle.getInt(KEY_PAGE_ID);
            this.link = bundle.getString(KEY_LINK);
            this.icon_array = bundle.getByteArray(KEY_ICON);
            this.title_text = bundle.getString(KEY_TITLE);

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
        link_view.setText(link);

        if(title_text == null ||  icon_array == null || title_text.equals("Web page not available")) {
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

       // this.imageView = view.findViewById(R.id.imageView);

        //this.imageView2 = view.findViewById(R.id.imageView2);
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),GraphActivity.class);
                startActivity(i);
            }
        });


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),WebActivity.class);
                startActivity(i);
            }
        });*/
        //view.setBackgroundColor(Color.WHITE);
        return view;
    }


}