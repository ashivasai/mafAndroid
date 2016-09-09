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
import com.zambient.dtos.ExpertsList;
import com.zambient.dtos.ProductList;
import com.zambient.maf.ProductsListDetails;
import com.zambient.maf.R;
import com.zambient.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zambient on 2/22/2016.
 */
public class ExpertsListAdapter extends BaseAdapter {

    private ArrayList<ExpertsList> list= null;
    private Context context;
    private static LayoutInflater inflater = null;

    public ExpertsListAdapter(ArrayList<ExpertsList> list, Context context) {
        this.list=list;
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
        ImageView img;
        TextView name;
        TextView designation;
        TextView qualification;
        TextView more;
        TextView description;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.expertslistgriditem, null);
            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.designation = (TextView)convertView.findViewById(R.id.designation);
            holder.qualification = (TextView)convertView.findViewById(R.id.qualification);
            holder.more = (TextView)convertView.findViewById(R.id.more);
            holder.description = (TextView)convertView.findViewById(R.id.description);

            convertView.setTag(holder);
        }
        else{

            holder = (ViewHolder) convertView.getTag();
        }

        if(list!=null){
            if(list.get(position).getExpertName()!=null){
                holder.name.setText(list.get(position).getExpertName());
            }
            if(list.get(position).getDesignation()!=null){
                holder.designation.setText(list.get(position).getDesignation());
            }
            if(list.get(position).getQualification()!=null){
                holder.qualification.setText(list.get(position).getQualification());
            }
            if(list.get(position).getDescription()!=null){
                holder.description.setText(list.get(position).getDescription());
            }


            if(list.get(position).getImage()!=null){
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
                ImageLoader imageloader = new ImageLoader(context, memoryCache);
                imageloader.displayImage(AppConstants.HTTP+list.get(position).getImage(),holder.img);
            }
        }
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.description.getVisibility()==View.VISIBLE){
                    holder.description.setVisibility(View.GONE);
                    holder.more.setText("more...");
                }else{
                    holder.description.setVisibility(View.VISIBLE);
                    holder.more.setText("less");
                }

            }
        });
        return convertView;
    }


}
