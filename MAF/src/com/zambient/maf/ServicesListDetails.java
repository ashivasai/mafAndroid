package com.zambient.maf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zambient.constants.AppConstants;
import com.zambient.dtos.ServiceListBeanDto;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.Utils;

public class ServicesListDetails extends AppCompatActivity {
	private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_services_list_details);
        ServiceListBeanDto doctorBeanListDto = (ServiceListBeanDto) getIntent().getSerializableExtra("selecteddata");
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(Utils.isNotNull(AppConstants.APP_COLOR))
            toolbar.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
*/
        ImageView doctorimg = (ImageView)findViewById(R.id.doctorimg);
        TextView doctorname = (TextView) findViewById(R.id.doctorname);
        TextView doctordesc = (TextView) findViewById(R.id.doctordesc);
        context =this;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        TextView mTitleTextView = (TextView) findViewById(R.id.title_text);
        mTitleTextView.setText(doctorBeanListDto.title);
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

        if(doctorBeanListDto!=null){

            if(doctorBeanListDto.image!=null){
                final int mmaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int mcacheSize = mmaxMemory / 8;
                LruCache<String, Bitmap> mmemoryCache = new LruCache<String, Bitmap>(mcacheSize);
                ImageLoader mimageloader = new ImageLoader(this, mmemoryCache);
                mimageloader.displayImage(AppConstants.HTTP + doctorBeanListDto.image, doctorimg);
            }

            if(doctorBeanListDto.description!=null){
                doctordesc.setText(doctorBeanListDto.description);
            }
            if(doctorBeanListDto.title!=null){
                doctorname.setText(doctorBeanListDto.title);
            }



        }




    }

}