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
import com.zambient.dtos.DoctorBeanListDto;
import com.zambient.dtos.DoctorsListDto;
import com.zambient.dtos.ServiceListBeanDto;
import com.zambient.dtos.ServicesListDto;
import com.zambient.maf.DoctorsListDetails;
import com.zambient.maf.R;
import com.zambient.maf.ServicesListDetails;
import com.zambient.utils.ImageLoader;

/**
 * Created by zambient on 2/22/2016.
 */
public class ServicesListAdapter extends BaseAdapter {

    private ServicesListDto serviceBeanList;
    private Context context;
    private static LayoutInflater inflater = null;
    public ServicesListAdapter(ServicesListDto serviceBeanList, Context context) {
        this.serviceBeanList =serviceBeanList;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (serviceBeanList.androidServicesList.serviceList.size() <= 0)
            return 1;

        return serviceBeanList.androidServicesList.serviceList.size();
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

        if(serviceBeanList.androidServicesList.serviceList!=null && serviceBeanList.androidServicesList.serviceList.size()>0){
            if(serviceBeanList.androidServicesList.serviceList.get(position).title!=null){
                holder.doctorname.setText(serviceBeanList.androidServicesList.serviceList.get(position).title);
            }
            if(serviceBeanList.androidServicesList.serviceList.get(position).image!=null){
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
                ImageLoader imageloader = new ImageLoader(context, memoryCache);
                imageloader.displayImage(AppConstants.HTTP+serviceBeanList.androidServicesList.serviceList.get(position).image,holder.doctorimg);
            }
        }
        holder.doctorimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsActivity(serviceBeanList.androidServicesList.serviceList.get(position));
            }
        });
        return convertView;
    }

    private void openDetailsActivity(ServiceListBeanDto serviceBeanListDto) {

       Intent intent = new Intent(context,ServicesListDetails.class);
        intent.putExtra("selecteddata", serviceBeanListDto);
        context.startActivity(intent);
    }
}
