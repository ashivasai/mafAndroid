package com.zambient.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zambient.constants.AppConstants;
import com.zambient.dtos.MainPromotionsListDto;
import com.zambient.dtos.PromotionList;
import com.zambient.dtos.ServiceListBeanDto;
import com.zambient.dtos.ServicesListDto;
import com.zambient.maf.ProductsListDetails;
import com.zambient.maf.PromotionsDetailsActivity;
import com.zambient.maf.R;
import com.zambient.maf.ServicesListDetails;
import com.zambient.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zambient on 2/22/2016.
 */
public class PromotionsListAdapter extends BaseAdapter {

    private ArrayList<PromotionList> list;
    private Context context;
    private static LayoutInflater inflater = null;
    public PromotionsListAdapter(ArrayList<PromotionList> list, Context context) {
        this.list =list;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (list.size() <= 0)
            return 0;

        return list.size();
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
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.doctorslistgriditem, null);
            holder.doctorimg = (ImageView)convertView.findViewById(R.id.doctorimg);
            holder.doctorname = (TextView)convertView.findViewById(R.id.doctorname);

            convertView.setTag(holder);
        }
        else{

            holder = (ViewHolder) convertView.getTag();
        }

        if(list!=null){
            if(list.get(position).getTitle()!=null){
                holder.doctorname.setText(list.get(position).getTitle());
            }
            if(list.get(position).getImage()!=null){
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
                ImageLoader imageloader = new ImageLoader(context, memoryCache);
                imageloader.displayImage(AppConstants.HTTP+list.get(position).getImage(),holder.doctorimg);
            }
        }
        holder.doctorimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsActivity(list.get(position));
            }
        });
        return convertView;
    }

    private void openDetailsActivity(PromotionList list) {

       Intent intent = new Intent(context,PromotionsDetailsActivity.class);
        intent.putExtra("selecteddata", list);
        context.startActivity(intent);
    }
}
