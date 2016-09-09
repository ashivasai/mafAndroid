package com.zambient.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zambient.dtos.DoctorBeanListDto;
import com.zambient.dtos.DoctorsListDto;
import com.zambient.dtos.ReviewsListDto;
import com.zambient.maf.DoctorsListDetails;
import com.zambient.maf.R;
import com.zambient.utils.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by zambient on 2/22/2016.
 */
public class ReviewsListAdapter extends BaseAdapter {

    private ReviewsListDto list;
    private Context context;
    private static LayoutInflater inflater = null;
    private int prevPos = -1;
    private HashMap<Integer, TextView> map = new HashMap<Integer, TextView>();
    public ReviewsListAdapter(ReviewsListDto doctorBeanList, Context context) {
        this.list =doctorBeanList;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (list.reviewsBean.reviewsList.size() <= 0)
            return 1;

        return list.reviewsBean.reviewsList.size();
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

        TextView title;
        TextView comment;
        TextView more;
        TextView date;
        RatingBar ratingBar;
        TextView expand;
        TextView rating;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.reviewlistitem, null);

            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.comment = (TextView)convertView.findViewById(R.id.comment);
            holder.more = (TextView)convertView.findViewById(R.id.more);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.rating=(TextView)convertView.findViewById(R.id.rating);
            holder.more=(TextView)convertView.findViewById(R.id.more);
            holder.more.setTag(position);
            holder.expand=(TextView)convertView.findViewById(R.id.morecomment);

            //holder.ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);


            convertView.setTag(holder);
        }
        else{

            holder = (ViewHolder) convertView.getTag();
        }
        if(list.reviewsBean.reviewsList!=null){
            if(list.reviewsBean.reviewsList.get(position).title!=null){
                holder.title.setText(list.reviewsBean.reviewsList.get(position).title);
            }
            if(list.reviewsBean.reviewsList.get(position).comment!=null){
                holder.comment.setText(list.reviewsBean.reviewsList.get(position).comment);
                holder.expand.setText(list.reviewsBean.reviewsList.get(position).comment);
            }
           /* if(list.reviewsBean.reviewsList.get(position).da!=null){
                holder.comment.setText(list.reviewsBean.reviewsList.get(position).comment);
            }*/
            if(list.reviewsBean.reviewsList.get(position).rating!=null){
                /*holder.ratingBar.setRating(list.reviewsBean.reviewsList.get(position).rating);
                Drawable drawable = holder.ratingBar.getProgressDrawable();
                drawable.setColorFilter(Color.parseColor("#FFBF00"), PorterDuff.Mode.SRC_ATOP);*/
                holder.rating.setText("Rating :"+list.reviewsBean.reviewsList.get(position).rating);
            }
            if(list.reviewsBean.reviewsList.get(position).datetime!=null){
                holder.date.setText("Written on "+getDate(Long.parseLong(list.reviewsBean.reviewsList.get(position).datetime),"dd-MMM-yyyy"));
            }
        }
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDropDown(v, holder);
                if(holder.comment.getVisibility()==View.VISIBLE) {
                    holder.expand.setVisibility(View.VISIBLE);
                    holder.comment.setVisibility(View.GONE);
                    holder.more.setText("Less");
                }else{
                    holder.expand.setVisibility(View.GONE);
                    holder.comment.setVisibility(View.VISIBLE);
                    holder.more.setText("More...");
                }
            }
        });

        return convertView;
    }

    private void openDetailsActivity(DoctorBeanListDto doctorBeanListDto) {

        Intent intent = new Intent(context,DoctorsListDetails.class);
        intent.putExtra("selecteddata", doctorBeanListDto);
        context.startActivity(intent);
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void showDropDown(View v, final ViewHolder viewHolder) {
        int pos = (Integer)v.getTag();
        if(prevPos != pos)
        {
            TextView expand = map.get(prevPos);

            if(expand != null && expand.getVisibility() == View.VISIBLE)
            {
                expand.setVisibility(View.GONE);
                map.remove(prevPos);
            }

            prevPos = pos;
            map.put(prevPos, viewHolder.expand);
        }
        if(viewHolder.expand.getVisibility() == View.VISIBLE){
            viewHolder.expand.setVisibility(View.GONE);
            viewHolder.comment.setVisibility(View.VISIBLE);
            //viewHolder.cup.setVisibility(View.GONE);
            //viewHolder.down.setVisibility(View.VISIBLE);
        }else
        {
            viewHolder.expand.setVisibility(View.VISIBLE);
            viewHolder.comment.setVisibility(View.GONE);
            //viewHolder.cup.setVisibility(View.VISIBLE);
            //viewHolder.down.setVisibility(View.GONE);
        }
    }
}
