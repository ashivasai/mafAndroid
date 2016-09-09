package com.zambient.maf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zambient.Services.GetGoToCartDetails;
import com.zambient.Services.GetProductsList;
import com.zambient.adapters.ProductsListAdapter;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.MainGoToCartDto;
import com.zambient.dtos.MainProductsListDto;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.Utils;

public class ProductsActivity extends AppCompatActivity {
    GridView gridView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_products_list);
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
        gridView = (GridView)findViewById(R.id.doctorslistGrid);
        context =this;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        TextView mTitleTextView = (TextView) findViewById(R.id.title_text);
        mTitleTextView.setText("Products");
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
		LinearLayout priceLinear = (LinearLayout)findViewById(R.id.priceLinear);
		priceLinear.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
		priceLinear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProductsActivity.this, GoToCartActivity.class);
	            //i.putExtras(sendBundle);
	            startActivity(i); 
				
			}
		});
        ImageView appicon = (ImageView) findViewById(R.id.appicon);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
        ImageLoader imageloader = new ImageLoader(context, memoryCache);
        imageloader.displayImage(AppConstants.HTTP + pref.getString("appIcon", null), appicon);
        Log.d("GETPRODUCTSLISTURL",AppConstants.GETPRODUCTSLISTURL);
        if (Utils.isConnectingToInternet(context)) {
        new GetProductsList(ProductsActivity.this).execute(AppConstants.GETPRODUCTSLISTURL+"businessId="+pref.getString("bussinessId",null)+"&templateId="+pref.getString("templateId",null)+"&featureId=7&userId="+pref.getString("userId",null));
        new GetGoToCartDetails(ProductsActivity.this).execute(AppConstants.GETGOTOCARTURL+"businessId="+pref.getString("bussinessId",null)+"&userId="+pref.getString("userId",null));
        
        }else{
            Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
        }
        }

    public void serviceResponse(String response) {
        InputStream stream = new ByteArrayInputStream(response.getBytes());
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(stream);
        MainProductsListDto mainProductsListDto = gson.fromJson(reader, MainProductsListDto.class);
        Log.d("size", mainProductsListDto.getAndroidProductsList().getProductList().size() + "");
        ProductsListAdapter adapter= new ProductsListAdapter(mainProductsListDto.getAndroidProductsList().getProductList(),context);
        gridView.setAdapter(adapter);
    }

	public void serviceResponseGoToCart(String response) {
		InputStream stream = new ByteArrayInputStream(response.getBytes());
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(stream);
        MainGoToCartDto mainGoToCartDto = gson.fromJson(reader, MainGoToCartDto.class);
		if(mainGoToCartDto!=null)
			setDataForCart(mainGoToCartDto);
	}

	private void setDataForCart(MainGoToCartDto mainGoToCartDto) {
		TextView nocartItems = (TextView)findViewById(R.id.nocartItems);
		TextView cartAmt = (TextView)findViewById(R.id.cartAmt);
		if(mainGoToCartDto.getNoOfItems()!=null)
		nocartItems.setText(mainGoToCartDto.getNoOfItems());
		if(mainGoToCartDto.getTotalCost()!=null)
			cartAmt.setText("Rs. "+mainGoToCartDto.getTotalCost());
		
	}
}
