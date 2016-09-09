package com.zambient.Services;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.zambient.adapters.DoctorsListAdapter;
import com.zambient.dtos.DoctorsListDto;
import com.zambient.dtos.MainProductsListDto;
import com.zambient.maf.ProductsActivity;
import com.zambient.utils.URLConnection;
import com.zambient.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by zambient on 5/5/2016.
 */
public class GetProductsList extends AsyncTask<String,String,String> {
    ProgressDialog pDialog;
    ProductsActivity activity;
    public GetProductsList(ProductsActivity activity) {
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
        Log.d("productlistdata",data);

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
