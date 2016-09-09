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
import com.zambient.dtos.ProductList;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.Utils;

public class ProductsListDetails extends AppCompatActivity {
	private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_products_list_details);
        ProductList doctorBeanListDto = (ProductList) getIntent().getSerializableExtra("selecteddata");
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(Utils.isNotNull(AppConstants.APP_COLOR))
            toolbar.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));*/
      
        ImageView doctorimg = (ImageView)findViewById(R.id.doctorimg);
        TextView doctorname = (TextView) findViewById(R.id.doctorname);
        TextView doctorqualification = (TextView) findViewById(R.id.doctorqualification);
        TextView specification = (TextView) findViewById(R.id.specification);
        TextView doctorspecification = (TextView) findViewById(R.id.doctorspecification);
        TextView doctordesc = (TextView) findViewById(R.id.doctordesc);
        context =this;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        TextView mTitleTextView = (TextView) findViewById(R.id.title_text);
        if(doctorBeanListDto.getTitle()!=null)
        	mTitleTextView.setText(doctorBeanListDto.getTitle());
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

            if(doctorBeanListDto.getImage1()!=null){
                final int mmaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int mcacheSize = mmaxMemory / 8;
                LruCache<String, Bitmap> mmemoryCache = new LruCache<String, Bitmap>(mcacheSize);
                ImageLoader mimageloader = new ImageLoader(this, mmemoryCache);
                mimageloader.displayImage(AppConstants.HTTP+doctorBeanListDto.getImage1(),doctorimg);
            }
            if(doctorBeanListDto.getTitle()!=null){
                doctorname.setText(doctorBeanListDto.getTitle());
            }
            if(doctorBeanListDto.getPrice()!=null){
                doctorqualification.setText(doctorBeanListDto.getPrice());
            }
            if(doctorBeanListDto.getSpecification()!=null){
                specification.setText(doctorBeanListDto.getSpecification());
            }
            if(doctorBeanListDto.getMetric()!=null){
                doctorspecification.setText(doctorBeanListDto.getMetric());
            }
            if(doctorBeanListDto.getDescription()!=null){
                doctordesc.setText(doctorBeanListDto.getDescription());
            }



        }




    }

}
