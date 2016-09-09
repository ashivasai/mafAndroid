package com.zambient.maf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zambient.adapters.ServicesListAdapter;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.ServicesListDto;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.URLConnection;
import com.zambient.utils.Utils;

public class ServicesActivity extends AppCompatActivity {
    private Context context;
    GridView serviceslistGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_services);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(Utils.isNotNull(AppConstants.APP_COLOR))
            toolbar.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));*/

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        serviceslistGrid = (GridView)findViewById(R.id.serviceslistGrid);
        context =this;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        TextView mTitleTextView = (TextView) findViewById(R.id.title_text);
        mTitleTextView.setText("Our Services");
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		});
		
		FrameLayout titlebarlinear =(FrameLayout)findViewById(R.id.titlebarlinear);
		titlebarlinear.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
        ImageView appicon = (ImageView) findViewById(R.id.appicon);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
        ImageLoader imageloader = new ImageLoader(context, memoryCache);
        imageloader.displayImage(AppConstants.HTTP + pref.getString("appIcon", null), appicon);
        if (Utils.isConnectingToInternet(context)) {
         new GetServicesList().execute(AppConstants.GETSERVICESLISTURL+"businessId="+pref.getString("bussinessId",null)+"&templateId="+pref.getString("templateId",null)+"&featureId=3&userId="+pref.getString("userId",null));
        }else{
            Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
        }
        }

    private class GetServicesList extends AsyncTask<String,String,ServicesListDto> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected ServicesListDto doInBackground(String... params) {
            String data = null;
            try {
                URLConnection http = new URLConnection();
                data = http.readUrl(params[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("listdata",data);
            InputStream stream = new ByteArrayInputStream(data.getBytes());
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(stream);
            return gson.fromJson(reader, ServicesListDto.class);
        }

        @Override
        protected void onPostExecute(ServicesListDto servicesListDto) {
            super.onPostExecute(servicesListDto);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(servicesListDto!=null){
                ServicesListAdapter servicesListAdapter= new ServicesListAdapter(servicesListDto,context);
                serviceslistGrid.setAdapter(servicesListAdapter);

            }

        }
    }

}
