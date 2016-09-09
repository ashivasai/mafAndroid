package com.zambient.maf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zambient.Services.GetAskExpertData;
import com.zambient.Services.SubmitQuery;
import com.zambient.adapters.ExpertsListAdapter;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.AskExpertBean;
import com.zambient.dtos.MainAskExpertDto;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.Utils;

public class AskExpert extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Context context;
    ImageView bgimage;
    private Spinner spinner;
    private TextView submit,knowmore;
    private EditText query,mailid;
    private String selectedItem;
    private String expertMail,expertName,expertId;
    private ListView listview;
    MainAskExpertDto mainAskExpertDto;
    private LinearLayout asklayout;
    private Boolean enterIntoExpertsList;
    private TextView mTitleTextView;
    private String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ask_expert);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Utils.isNotNull(AppConstants.APP_COLOR))
            toolbar.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
*/
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        context =this;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        bgimage = (ImageView)findViewById(R.id.bgimage);
        spinner = (Spinner) findViewById(R.id.spinner);
        submit=(TextView) findViewById(R.id.submit);
        query=(EditText) findViewById(R.id.query);
        mailid=(EditText) findViewById(R.id.mailid);
        knowmore = (TextView) findViewById(R.id.knowmore);
        listview=(ListView) findViewById(R.id.listview);
        asklayout=(LinearLayout) findViewById(R.id.asklayout);
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
		
		FrameLayout titlebarlinear =(FrameLayout)findViewById(R.id.titlebarlinear);
		titlebarlinear.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
        ImageView appicon = (ImageView) findViewById(R.id.appicon);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
        ImageLoader imageloader = new ImageLoader(context, memoryCache);
        imageloader.displayImage(AppConstants.HTTP + pref.getString("appIcon", null), appicon);
        knowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterIntoExpertsList=true;
                asklayout.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                ExpertsListAdapter adapter = new ExpertsListAdapter(mainAskExpertDto.getAskExpertBean().getExpertsList(),context);
                listview.setAdapter(adapter);

            }
        });
        if (Utils.isConnectingToInternet(context)) {
        new GetAskExpertData(AskExpert.this).execute(AppConstants.GETASKEXPERTURL+"businessId="+pref.getString("bussinessId",null)+"&templateId="+pref.getString("templateId",null)+"&featureId=5&userId="+pref.getString("userId",null));
        }else{
            Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
        }
        }

    private void submitRequest(AskExpertBean askExpertBean) {
        if(!(selectedItem!=null && !selectedItem.trim().matches("Select Expert"))){
            Utils.toast("Please Select Expert",context);
        }else if(!(Utils.isNotNull(query.getText().toString()) && Utils.isNotNull(mailid.getText().toString()))){
            Utils.toast("Please fill mandatory details",context);
        }else if(!mailid.getText().toString().matches(EMAIL_REGEX)){
        	Utils.toast("Please Enter Valid email",context);
        }else{
            JSONObject reviewparams = new JSONObject();
            JSONObject review = new JSONObject();
            try {
                review.accumulate("expertId",expertId);
                review.accumulate("expertName",expertName);
                review.accumulate("expertEmail",expertMail);
                review.accumulate("appUserEmail", mailid.getText().toString());
                review.accumulate("mailText",query.getText().toString());

                reviewparams.accumulate("askQuestion", review);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("payload", reviewparams.toString());
            if (Utils.isConnectingToInternet(context)) {
            if(Utils.isNotNull(reviewparams.toString()))
                new SubmitQuery(AskExpert.this,context).execute(reviewparams.toString());
            }else{
                Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
            }
        }
    }

    public void serviceResponse(String response) {

        InputStream stream = new ByteArrayInputStream(response.getBytes());
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(stream);
        mainAskExpertDto = gson.fromJson(reader, MainAskExpertDto.class);
        if(mainAskExpertDto.getAskExpertBean().getBgImage()!=null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
            ImageLoader imageloader = new ImageLoader(context, memoryCache);
            imageloader.displayImage(AppConstants.HTTP + mainAskExpertDto.getAskExpertBean().getBgImage(), bgimage);
        }
        if(mainAskExpertDto.getAskExpertBean().getTitle()!=null){
        	mTitleTextView.setText(mainAskExpertDto.getAskExpertBean().getTitle());
        }
        if(mainAskExpertDto.getAskExpertBean().getExpertsList().size()!=0 && mainAskExpertDto.getAskExpertBean().getExpertsList()!=null){
            ArrayList<String> expertsList = new ArrayList<String>();
            expertsList.add("Select Expert");
            for(int i=0; i<mainAskExpertDto.getAskExpertBean().getExpertsList().size();i++){
                expertsList.add(mainAskExpertDto.getAskExpertBean().getExpertsList().get(i).getExpertName());

            }
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter dataAdapter = new ArrayAdapter(this, R.layout.spinner_item, expertsList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitRequest(mainAskExpertDto.getAskExpertBean());
                }
            });

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = parent.getItemAtPosition(position).toString();
        if(position!=0) {
            expertId = mainAskExpertDto.getAskExpertBean().getExpertsList().get(position - 1).getExpertId();
            expertMail = mainAskExpertDto.getAskExpertBean().getExpertsList().get(position - 1).getEmail();
            expertName = mainAskExpertDto.getAskExpertBean().getExpertsList().get(position - 1).getExpertName();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void submitServiceResponse(String result) {
        Utils.okButtonAlertDialog(result,context);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(enterIntoExpertsList!=null) {
            if (enterIntoExpertsList) {
                listview.setVisibility(View.GONE);
                asklayout.setVisibility(View.VISIBLE);

                enterIntoExpertsList = false;
            } else {
                finish();
            }
        }else {
            finish();
        }
    }
}
