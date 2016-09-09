package com.zambient.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.zambient.constants.AppConstants;
import com.zambient.maf.AppointmentActivity;
import com.zambient.maf.ReviewsActivity;
import com.zambient.utils.ServiceHandler;
import com.zambient.utils.Utils;

/**
 * Created by zambient on 5/9/2016.
 */
public class AddToCart extends AsyncTask<String, Void, String> {
    private ProgressDialog pDialog;
    
    private Context context;

    public AddToCart(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {
        ServiceHandler sh = new ServiceHandler();
        String response = sh.makeServiceCallWithPayLoad(AppConstants.ADDTOCARTURL, params[0]);
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            Utils.okButtonAlertDialog(result, context);
        }
    }
}
