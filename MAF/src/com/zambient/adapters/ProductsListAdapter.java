package com.zambient.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zambient.Services.AddToCart;
import com.zambient.Services.SubmitAppointment;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.DoctorBeanListDto;
import com.zambient.dtos.DoctorsListDto;
import com.zambient.dtos.MainProductsListDto;
import com.zambient.dtos.ProductList;
import com.zambient.maf.AppointmentActivity;
import com.zambient.maf.DoctorsListDetails;
import com.zambient.maf.ProductsListDetails;
import com.zambient.maf.R;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.Utils;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zambient on 2/22/2016.
 */
public class ProductsListAdapter extends BaseAdapter {

    private ArrayList<ProductList> productsList= null;
    private Context context;
    private static LayoutInflater inflater = null;

    public ProductsListAdapter(ArrayList<ProductList> productsList, Context context) {
        this.productsList=productsList;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (productsList.size() <= 0)
            return 0;

        return productsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView doctorimg;
        TextView doctorname;
        TextView pricewithmetric;
        LinearLayout addToCartLinear;
        TextView priceTV;
        TextView metricTV;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.productslistgriditem, null);
            holder.doctorimg = (ImageView)convertView.findViewById(R.id.doctorimg);
            holder.doctorname = (TextView)convertView.findViewById(R.id.doctorname);
            holder.pricewithmetric = (TextView)convertView.findViewById(R.id.pricewithmetric);
            holder.addToCartLinear = (LinearLayout)convertView.findViewById(R.id.addToCartLinear);
            holder.priceTV = (TextView)convertView.findViewById(R.id.priceTV);
            holder.metricTV=(TextView)convertView.findViewById(R.id.metricTV);
            convertView.setTag(holder);
        }
        else{

            holder = (ViewHolder) convertView.getTag();
        }

        if(productsList!=null){
            if(productsList.get(position).getTitle()!=null){
                holder.doctorname.setText(productsList.get(position).getTitle());
            }
            if(productsList.get(position).getPrice()!=null && productsList.get(position).getMetric()!=null){
                //holder.pricewithmetric.setText("Rs. "+productsList.get(position).getPrice()+" / "+productsList.get(position).getMetric());
            	holder.pricewithmetric.setText(productsList.get(position).getSpecification());
            }
            
            if(productsList.get(position).getImage1()!=null){
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
                ImageLoader imageloader = new ImageLoader(context, memoryCache);
                imageloader.displayImage(AppConstants.HTTP+productsList.get(position).getImage1(),holder.doctorimg);
            }
            if(productsList.get(position).getPrice()!=null){
            	holder.priceTV.setText("Rs. "+productsList.get(position).getPrice());
            }
            if(productsList.get(position).getMetric()!=null){
            	holder.metricTV.setText(productsList.get(position).getMetric());
            }
        }
        if(AppConstants.APP_COLOR!=null){
        	GradientDrawable drawable = (GradientDrawable) holder.addToCartLinear.getBackground();
            drawable.setColor(Color.parseColor(AppConstants.APP_COLOR));
        }
        holder.addToCartLinear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addproductToCart(position);
				
			}
		});
        holder.doctorimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsActivity(productsList.get(position));
            }
        });
        return convertView;
    }

    protected void addproductToCart(int position) {
    	SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
    	JSONObject params = new JSONObject();
        
        try {
        	params.accumulate("appUser",pref.getString("userId",null));
        	params.accumulate("businessId",pref.getString("bussinessId",null));
        	params.accumulate("productId",productsList.get(position).getProductId());
        	params.accumulate("productName",productsList.get(position).getTitle());
        	params.accumulate("quantity","1");
            
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("payload", params.toString());
        Utils.toast(params.toString(), context);
        if (Utils.isConnectingToInternet(context)) {
        if(Utils.isNotNull(params.toString()))
            new AddToCart(context).execute(params.toString());
        }else{
            Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
        }
		
	}

	private void openDetailsActivity(ProductList listItem) {

        Intent intent = new Intent(context,ProductsListDetails.class);
        intent.putExtra("selecteddata", listItem);
        context.startActivity(intent);
    }
}
