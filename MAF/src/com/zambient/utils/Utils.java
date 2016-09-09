package com.zambient.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zambient on 2/20/2016.
 */
public class Utils {


    public static void toast(final String s, final Context context) {

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            }
        });
    }
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }



        public static void CopyStream(InputStream is, OutputStream os) {
            final int buffer_size = 1024;
            try {
                byte[] bytes = new byte[buffer_size];
                for (;;) {
                    int count = is.read(bytes, 0, buffer_size);
                    if (count == -1)
                        break;
                    os.write(bytes, 0, count);
                }
            } catch (Exception ex) {
            }
        }

    public static Boolean isNotNull(String str){
        if(str==null || str=="" || str.trim().length()<=0){
            return false;
        }else
            return true;
    }

    public static Boolean isNotNull(Boolean bln){
        if(bln==null || bln.equals("") || bln.equals(" ")){
            return false;
        }else
            return true;
    }

    public static Boolean isNotNull(Long lng){
        if(lng==null || lng.equals("") || lng.equals(" ")){
            return false;
        }else
            return true;
    }

    public static void okButtonAlertDialog(String message,Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", null);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

}
