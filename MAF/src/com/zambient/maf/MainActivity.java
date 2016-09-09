package com.zambient.maf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;







import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.AppDetailsBean;
import com.zambient.dtos.BusinessDetails;
import com.zambient.dtos.FeaturesList;
import com.zambient.dtos.HomeDetailsDto;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.URLConnection;
import com.zambient.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private Context context;
    LinearLayout landinglinear;
    TextView mTitleTextView;
    ImageView bgimage;
    private BusinessDetails mhomeDetails;
    //private Toolbar toolbar;
    private ImageView appicon;
    //private ImageView toolbar_logo;
    private FrameLayout titlebarlinear;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_landing_main);
    	mTitleTextView = (TextView) findViewById(R.id.title_text);
		appicon = (ImageView) findViewById(R.id.appicon);
		titlebarlinear =(FrameLayout)findViewById(R.id.titlebarlinear);
		
      /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mTitleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView toolbar_logo = (ImageView) toolbar.findViewById(R.id.toolbar_logo);
        appicon = (ImageView) toolbar.findViewById(R.id.appicon);
        toolbar_logo = (ImageView) toolbar.findViewById(R.id.toolbar_logo);


        toolbar_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
       /* ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(0, 0, 0, 0);
		mActionBar.setDisplayOptions(mActionBar.getDisplayOptions()| ActionBar.DISPLAY_SHOW_CUSTOM);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		appicon = (ImageView) mCustomView.findViewById(R.id.appicon);
		titlebarlinear =(LinearLayout)mCustomView.findViewById(R.id.titlebarlinear);
		mCustomView.setLayoutParams(params);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);*/
        context =this;
        landinglinear = (LinearLayout)findViewById(R.id.landinglinear);
        bgimage = (ImageView)findViewById(R.id.bgimage);
        Log.d("GETHOMEDETAILSURL", AppConstants.GETHOMEDETAILSURL);
        if (Utils.isConnectingToInternet(context)) {
            try {
                new GetHomeData().execute(AppConstants.GETHOMEDETAILSURL + "appcode=" + getProperty("appcode", context) + "&email=" + AppConstants.CLIENT_EMAIL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Please check Network Connection")
        	       .setCancelable(false)
        	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   finish();
        	               System.exit(0);
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
        }
    }

    private class GetHomeData extends AsyncTask<String,String,HomeDetailsDto>{
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected HomeDetailsDto doInBackground(String... params) {
            String data = null;
          try {
                URLConnection http = new URLConnection();
                data = http.readUrl(params[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("homedata",data);
            InputStream stream = new ByteArrayInputStream(data.getBytes());
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(stream);
            return gson.fromJson(reader, HomeDetailsDto.class);
        }

        @Override
        protected void onPostExecute(HomeDetailsDto homeDetailsDto) {
            super.onPostExecute(homeDetailsDto);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(homeDetailsDto!=null){
            if(homeDetailsDto.appDetailsBean.businessDetails!=null)
            getHomeDatails(homeDetailsDto.appDetailsBean.businessDetails);
            //Utils.toast(homeDetailsDto.appDetailsBean.featuresList.get(1).displayTitle + "", MainActivity.this);
            setBackgroundimgandTitle(homeDetailsDto.appDetailsBean);
            storeSharedPreferences(homeDetailsDto);

            for (int i=0;i<homeDetailsDto.appDetailsBean.featuresList.size();i++){

                createFeatureListView(homeDetailsDto.appDetailsBean.featuresList.get(i),context);
            }
            }
        }
        
    }

    private void storeSharedPreferences(HomeDetailsDto homeDetailsDto) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //Utils.toast(homeDetailsDto.appDetailsBean.businessId+"", context);
        editor.putString("bussinessId", String.valueOf(homeDetailsDto.appDetailsBean.businessId));
        editor.putString("templateId", String.valueOf(homeDetailsDto.appDetailsBean.templateId));
        editor.putString("userId", String.valueOf(homeDetailsDto.appDetailsBean.appuserId));
        editor.putString("appCode", String.valueOf(homeDetailsDto.appDetailsBean.appCode));
        editor.putString("appIcon", String.valueOf(homeDetailsDto.appDetailsBean.businessDetails.getAppicon()));
        editor.commit();
    }


    private void getHomeDatails(BusinessDetails homeDetails){

        mhomeDetails=homeDetails;
    }
    private void setBackgroundimgandTitle(AppDetailsBean appDetailsBean){
        if(appDetailsBean.businessDetails.getAppName()!=null){
            mTitleTextView.setText(appDetailsBean.businessDetails.getAppName());
        }
        if(appDetailsBean.buttonColor!=null){
            //toolbar.setBackgroundColor(Color.parseColor(appDetailsBean.buttonColor));
        	titlebarlinear.setBackgroundColor(Color.parseColor(appDetailsBean.buttonColor));
            AppConstants.APP_COLOR=appDetailsBean.buttonColor;
        }
        if(appDetailsBean.businessDetails.getAppicon()!=null){
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
            ImageLoader imageloader = new ImageLoader(context, memoryCache);
            imageloader.displayImage(AppConstants.HTTP+appDetailsBean.businessDetails.getAppicon(),appicon);

        }
      if(appDetailsBean.businessDetails.getBgImage()!=null) {
           final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
            ImageLoader imageloader = new ImageLoader(context, memoryCache);
            imageloader.displayImage(AppConstants.HTTP + appDetailsBean.businessDetails.getBgImage(), bgimage);

        }
    }


    private void createFeatureListView(final FeaturesList featuresList, final Context context) {
        LinearLayout parent = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        parent.setPadding(60, 5, 60, 5);
        params.setMargins(0, 10, 0, 0);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        parent.setBackgroundResource(R.drawable.homebutton);
        GradientDrawable drawable = (GradientDrawable) parent.getBackground();
        drawable.setColor(Color.parseColor(featuresList.color));
        TextView tv  = new TextView(this);
        TableRow.LayoutParams tparams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        tv.setGravity(Gravity.CENTER);
        tv.setText(featuresList.displayTitle);
        tv.setTextSize(20);
        tv.setLayoutParams(tparams);
        tparams.setMargins(0, 10, 0, 10);
        tv.setTextColor(getResources().getColor(R.color.white));
        if (tv.getParent() != null)
            ((ViewGroup) tv.getParent()).removeView(tv);
        parent.addView(tv);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.toast(featuresList.featureId + "", context);
                startActivityfromHome(featuresList.featureId);
            }
        });
        if (parent.getParent() != null)
            ((ViewGroup) parent.getParent()).removeView(parent);

        landinglinear.addView(parent);
    }


    private void startActivityfromHome(Integer featureId) {

        if(featureId!=null){
            if(featureId==1){
                startHomeActivity();
            }else if(featureId==3){
                startServicesActivity();
            }else if(featureId==4){
                startAppointmentActivity();
            }else if(featureId==5) {
                startAskExpertActivity();
            }else if(featureId==11){
                startDoctorsListActivity();
            }else if(featureId==7) {
                startProductsListActivity();
            }else if(featureId==8){
                startContactUSActivity();
            }else if(featureId==9) {
                startPromotionListActivity();
            }else if(featureId==10) {
                startStoreLocatorActivity();
            } else if(featureId==2){
                startReviewsActivity();
            }else if(featureId==12){
                startRestauAppointmentActivity();
            }else if(featureId==13) {
                startMenuListActivity();
            }else{
                Utils.toast("otherfeatureid",context);
            }

        }

    }

    private void startMenuListActivity() {
    	Intent intent = new Intent(context,MenuListActivity.class);
        startActivity(intent);
		
	}


	private void startRestauAppointmentActivity() {
    	Intent intent = new Intent(context,RestaurantAppointmentActivity.class);
        startActivity(intent);
		
	}


	private void startAskExpertActivity() {
        Intent intent = new Intent(context,AskExpert.class);
        startActivity(intent);
    }

    private void startStoreLocatorActivity() {
        Intent intent = new Intent(context,StoreLocatorActivity.class);
        startActivity(intent);
    }

    private void startReviewsActivity() {
        Intent intent = new Intent(context,ReviewsActivity.class);
        startActivity(intent);
    }

    private void startContactUSActivity() {
        Intent intent = new Intent(context,ContactUsActivity.class);
        startActivity(intent);
    }

    private void startDoctorsListActivity() {
        Intent intent = new Intent(context,DoctorsListActivity.class);
        startActivity(intent);
    }

    private void startAppointmentActivity() {
        Intent intent = new Intent(context,AppointmentActivity.class);
        startActivity(intent);
    }

    private void startServicesActivity() {
        Intent intent = new Intent(context,ServicesActivity.class);
        startActivity(intent);
    }
    private void startProductsListActivity() {
        Intent intent = new Intent(context,ProductsActivity.class);
        startActivity(intent);
    }
    private void startPromotionListActivity() {
        Intent intent = new Intent(context,PromotionsActivity.class);
        startActivity(intent);
    }

    private void startHomeActivity() {
        /* public String bgImage; */
       Intent intent = new Intent(context,HomeActivity.class);
       startActivity(intent);
    }


    @Override
    public void onBackPressed() {
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    	Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        //System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("appcode.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
