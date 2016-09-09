package com.zambient.maf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.zambient.Services.GetContactDetails;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.ContactUsBean;
import com.zambient.dtos.MainContactUsDetailsDto;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.Utils;


public class ContactUsActivity extends AppCompatActivity {

    private GoogleMap googleMap;

    private TextView maild,phoneno,address;
    private ImageView bgimage;
    private Context context;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_contact_us);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(Utils.isNotNull(AppConstants.APP_COLOR))
            toolbar.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
*/
        maild= (TextView)findViewById(R.id.maild);
        phoneno= (TextView)findViewById(R.id.phoneno);
        address= (TextView)findViewById(R.id.address);
        bgimage=(ImageView)findViewById(R.id.bgimage);
        context=this;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        mTitleTextView = (TextView) findViewById(R.id.title_text);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		});
       /* phoneno.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+phoneno.getText().toString()));
				startActivity(callIntent);
				
			}
		});*/
		FrameLayout titlebarlinear =(FrameLayout)findViewById(R.id.titlebarlinear);
		titlebarlinear.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
        ImageView appicon = (ImageView) findViewById(R.id.appicon);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
        ImageLoader imageloader = new ImageLoader(context, memoryCache);
        imageloader.displayImage(AppConstants.HTTP + pref.getString("appIcon", null), appicon);
   
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        if (Utils.isConnectingToInternet(context)) {
        new GetContactDetails(ContactUsActivity.this).execute(AppConstants.GETCONTACTUSDETAILSURL+"businessId="+pref.getString("bussinessId",null)+"&templateId="+pref.getString("templateId",null)+"&featureId=8&userId="+pref.getString("userId",null));
        }else{
            Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
        }
            try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serviceResponse(String response) {
        InputStream stream = new ByteArrayInputStream(response.getBytes());
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(stream);
        MainContactUsDetailsDto contactUsDetailsDto = gson.fromJson(reader, MainContactUsDetailsDto.class);
        Log.d("data", contactUsDetailsDto.getContactUsBean().getEmail());
        if(contactUsDetailsDto.getContactUsBean()!=null)
        setTextData(contactUsDetailsDto.getContactUsBean());
    }

    private void setTextData(ContactUsBean contactUsBean){
        if(Utils.isNotNull(contactUsBean.getEmail()))
            maild.setText(contactUsBean.getEmail());
        if(Utils.isNotNull(contactUsBean.getMobileNumber()))
            phoneno.setText(contactUsBean.getMobileNumber());
        if(Utils.isNotNull(contactUsBean.getAddressLine1()) && Utils.isNotNull(contactUsBean.getAddressLine2())) {
            address.setText(contactUsBean.getAddressLine1()+","+contactUsBean.getAddressLine2());
            if(getLocationFromAddress(contactUsBean.getAddressLine2())!=null){
            Barcode.GeoPoint gp = getLocationFromAddress(contactUsBean.getAddressLine2());
            LatLng latlng = new LatLng(gp.lat , gp.lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(latlng).title(contactUsBean.getMessage()));
            }


        }
        if(contactUsBean.getImage()!=null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
            ImageLoader imageloader = new ImageLoader(context, memoryCache);
            imageloader.displayImage(AppConstants.HTTP + contactUsBean.getImage(), bgimage);
        }
        if(contactUsBean.getTitle()!=null){
        	mTitleTextView.setText(contactUsBean.getTitle());
        }

    }

    public Barcode.GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            if(address.size()>0){
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new Barcode.GeoPoint(1,location.getLatitude(),location.getLongitude());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }



}
