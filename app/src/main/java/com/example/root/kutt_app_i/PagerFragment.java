package com.example.root.kutt_app_i;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

public class PagerFragment extends Fragment {


    private static final String KEY_POS = "key_pos";
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

    ImageView imageView,imageView2;

    public static PagerFragment newInstance(int position, int page_id ) {
        Bundle args = new Bundle();
        args.putInt(KEY_POS, position);
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
            this.pos = bundle.getInt(KEY_POS);
            this.page_id = bundle.getInt(KEY_PAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_page, container, false);
        this.titleView = view.findViewById(R.id.txt_title);
        this.icon_view = view.findViewById(R.id.icon);
        this.link_view = view.findViewById(R.id.link);
        this.mWebview = view.findViewById(R.id.webview);
        this.res = myDb.preview_data(pos);
        res.moveToNext();
        this.title_text = res.getString(1);
        this.icon_array = res.getBlob(0);
        this.link = res.getString(2);
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
        view.setBackgroundColor(Color.WHITE);
        return view;
    }


}