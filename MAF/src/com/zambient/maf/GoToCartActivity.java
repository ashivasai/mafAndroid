package com.zambient.maf;

import com.zambient.adapters.GoToCartListAdapter;
import com.zambient.adapters.ProductsListAdapter;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.MainGoToCartDto;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

public class GoToCartActivity extends Activity {
	private MainGoToCartDto mainGoToCartDto;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_go_to_cart);
		context=this;
		init();
	}

	private void init() {
		FrameLayout titlebarlinear =(FrameLayout)findViewById(R.id.titlebarlinear);
		titlebarlinear.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
		LinearLayout placeOrderLinear = (LinearLayout)findViewById(R.id.placeOrderLinear);
		placeOrderLinear.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));
		
		mainGoToCartDto= (MainGoToCartDto) getIntent().getSerializableExtra("cartDetails");
		setData();
	}

	private void setData() {
		ListView cartItems = (ListView)findViewById(R.id.cartItems);
		 GoToCartListAdapter adapter= new GoToCartListAdapter(mainGoToCartDto.getCartProducts(),context);
		 cartItems.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.go_to_cart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
