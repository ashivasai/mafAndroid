package com.zambient.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.zambient.constants.AppConstants;
import com.zambient.maf.AppointmentActivity;
import com.zambient.maf.RestaurantAppointmentActivity;
import com.zambient.maf.ReviewsActivity;
import com.zambient.utils.ServiceHandler;

/**
 * Created by zambient on 5/9/2016.
 */
public class SubmitRestaurantAppointment extends AsyncTask<String, Void, String> {
    private ProgressDialog pDialog;
    private RestaurantAppointmentActivity activity;
    private Context context;

    public SubmitRestaurantAppointment(RestaurantAppointmentActivity restaurantAppointmentActivity, Context context) {
        this.activity=restaurantAppointmentActivity;
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
        SharedPreferences pref = activity.getSharedPreferences("MyPref", activity.MODE_PRIVATE);
        String response = sh.makeServiceCallWithPayLoad(AppConstants.RESERVETABLEURL+"businessId="+pref.getString("bussinessId",null)+"&templateId="+pref.getString("templateId",null)+"&featureId=12&userId="+pref.getString("userId",null), params[0]);
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            activity.submitServiceResponse(result);
        }
    }
}
