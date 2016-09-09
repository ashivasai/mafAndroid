package com.zambient.Services;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.zambient.maf.AskExpert;
import com.zambient.maf.HomeActivity;
import com.zambient.utils.URLConnection;
import com.zambient.utils.Utils;

/**
 * Created by zambient on 5/5/2016.
 */
public class GetAboutUsData extends AsyncTask<String,String,String> {
    ProgressDialog pDialog;
    HomeActivity activity;
    public GetAboutUsData(HomeActivity activity) {
            this.activity=activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        pDialog.show();

    }
    @Override
    protected String doInBackground(String... params) {
        String data = null;
        try {
            URLConnection http = new URLConnection();
            data = http.readUrl(params[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        Log.d("listdata",data);

        return data;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (pDialog.isShowing())
            pDialog.dismiss();
        if(Utils.isNotNull(response)){
           activity.serviceResponse(response);

        }

    }
}
