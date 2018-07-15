package com.example.root.kutt_app_i;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class Clipboard_Utils {

    /**
     * Copy Data to Clipboard Methods
     **/

    //First Check SDK version because SDK below honeycomb has different method and above honeycomb is different
    public static void copyToClipboard(Context mContext,
                                       String data) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            copyToClipboardHoneyLess(mContext, data);
        } else {
            copyToClipboardForHoney(mContext, data);
        }
        //Toast.makeText(mContext, "Link copied to Clipboard.", Toast.LENGTH_SHORT).show();
    }

    //For Honeycomb and above devices
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void copyToClipboardForHoney(Context mContext,
                                                String data) {
        ClipboardManager clipboard = (ClipboardManager) mContext
                .getSystemService(Context.CLIPBOARD_SERVICE);//Get Clipboard Manager
        ClipData clip = ClipData.newPlainText(
                "clipboard data ", data);//Save plain text data to clip data
        clipboard.setPrimaryClip(clip);//set clip data as primary clip
        Toast.makeText(mContext,"Link Copied to Clipboard",Toast.LENGTH_SHORT).show();
    }

    //For below Honeycomb devices
    @SuppressWarnings("deprecation")
    private static void copyToClipboardHoneyLess(Context mContext,
                                                 String data) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext
                .getSystemService(Context.CLIPBOARD_SERVICE);//get Clipboard manager
        clipboard.setText(data);//set text to clipboard
    }


    /**
     * Get Data from Clipboard Methods
     **/

    //First Check SDK version because SDK below honeycomb has different method and above honeycomb is different
    public static String getDataFromClipboard(Context mContext) {
        String text = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            text = getClipboardDataHoneyLess(mContext);
        } else {
            text = getClipboardDataForHoney(mContext);
        }
        //Toast.makeText(mContext, "Data pasted from Clipboard.", Toast.LENGTH_SHORT).show();
        return text;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static String getClipboardDataForHoney(Context mContext) {
        ClipboardManager clipboard = (ClipboardManager) mContext
                .getSystemService(Context.CLIPBOARD_SERVICE);//get Clipboard manager
        if (clipboard.hasPrimaryClip())
        {
            ClipData data = clipboard.getPrimaryClip();
            if (data.getItemCount() > 0)
            {
                CharSequence text = data.getItemAt(0).coerceToText(mContext);
                if (text != null)
                {
                    return text.toString();
                }else {
                    return "";
                }
            }else {
                return "";
            }
        }else {
            return "";
        }



    }

    @SuppressWarnings("deprecation")
    private static String getClipboardDataHoneyLess(Context mContext) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext
                .getSystemService(Context.CLIPBOARD_SERVICE);//get Clipboard manager
        return clipboard.getText().toString();

    }

}
