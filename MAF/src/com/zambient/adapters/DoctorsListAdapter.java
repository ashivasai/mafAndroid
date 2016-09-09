package com.zambient.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
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
import com.zambient.maf.AppointmentActivity;
import com.zambient.maf.DoctorsListDetails;
import com.zambient.maf.R;
import com.zambient.utils.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zambient on 2/22/2016.
 */
public class DoctorsListAdapter extends BaseAdapter {

    private DoctorsListDto doctorBeanList;
    private Context context;
    private static LayoutInflater inflater = null;
    public DoctorsListAdapter(DoctorsListDto doctorBeanList, Context context) {
        this.doctorBeanList =doctorBeanList;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (doctorBeanList.androidDoctorsList.doctorsList.size() <= 0)
            return 1;

        return doctorBeanList.androidDoctorsList.doctorsList.size();
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

        if(doctorBeanList!=null){
            if(doctorBeanList.androidDoctorsList.doctorsList.get(position).name!=null){
                holder.doctorname.setText(doctorBeanList.androidDoctorsList.doctorsList.get(position).name);
            }
            if(doctorBeanList.androidDoctorsList.doctorsList.get(position).image!=null){
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
                ImageLoader imageloader = new ImageLoader(context, memoryCache);
                imageloader.displayImage(AppConstants.HTTP+doctorBeanList.androidDoctorsList.doctorsList.get(position).image,holder.doctorimg);
            }
        }
        holder.doctorimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsActivity(doctorBeanList.androidDoctorsList.doctorsList.get(position));
            }
        });
        return convertView;
    }

    private void openDetailsActivity(DoctorBeanListDto doctorBeanListDto) {

        Intent intent = new Intent(context,DoctorsListDetails.class);
        intent.putExtra("selecteddata", doctorBeanListDto);
        context.startActivity(intent);
    }
}
