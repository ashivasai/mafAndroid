package com.zambient.maf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zambient.Services.SubmitReview;
import com.zambient.adapters.ReviewsListAdapter;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.ReviewsListDto;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.URLConnection;
import com.zambient.utils.Utils;

public class ReviewsActivity extends AppCompatActivity {
    private Context context;
    private ListView reviewslistview;
    private ImageView bgimage;
    private LinearLayout book;
    private String reviewId;
    private String ratingno;
    private TextView mTitleTextView;
    private TextView noreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_reviews);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(Utils.isNotNull(AppConstants.APP_COLOR))
            toolbar.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));*/

        context =this;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        mTitleTextView = (TextView) findViewById(R.id.title_text);
        
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
        bgimage = (ImageView)findViewById(R.id.bgimage);
        book=(LinearLayout)findViewById(R.id.book);
        noreview = (TextView)findViewById(R.id.noreview);
        getDettails();
        reviewslistview = (ListView)findViewById(R.id.reviewslistview);
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewId != null)
                    showWriteReviewpopup(v);
            }
        });
    }

    private void getDettails() {
        if (Utils.isConnectingToInternet(context)) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        new GetReviewsList().execute(AppConstants.GETREVIEWS+"businessId="+pref.getString("bussinessId",null)+"&templateId="+pref.getString("templateId",null)+"&featureId=2&userId="+pref.getString("userId",null));
        }else{
            Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
        }
    }

    private void showWriteReviewpopup(View v){
        final PopupWindow pwindo;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.write_review,(ViewGroup) v.findViewById(R.id.reviewlayout));
        pwindo = new PopupWindow(layout, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView cancel =(TextView) layout.findViewById(R.id.cancel);
        TextView submit =(TextView) layout.findViewById(R.id.submit);
        RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.ratingBar);
        final EditText title =(EditText) layout.findViewById(R.id.title);
        final EditText comment =(EditText) layout.findViewById(R.id.comment);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingno =String.valueOf(rating);
                //Utils.toast(String.valueOf(rating),context);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwindo.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(ratingno!=null && !ratingno.trim().matches("0.0"))){
                    Utils.toast("Please provide rating..",context);
                }else if(!(Utils.isNotNull(title.getText().toString()) && Utils.isNotNull(comment.getText().toString()))){
                    Utils.toast("Please fill all mandatory fields",context);
                }else{

                    JSONObject reviewparams = new JSONObject();
                    JSONObject review = new JSONObject();
                    try {
                        review.accumulate("reviewId",reviewId);
                        review.accumulate("rating",ratingno);
                        review.accumulate("comment",comment.getText().toString());
                        review.accumulate("title",title.getText().toString());

                        reviewparams.accumulate("review", review);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("payload", reviewparams.toString());
                    if (Utils.isConnectingToInternet(context)) {
                    if(Utils.isNotNull(reviewparams.toString()))
                        new SubmitReview(ReviewsActivity.this,context).execute(reviewparams.toString());
                    }else{
                        Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
                    }
                    pwindo.dismiss();
                }
            }
        });
    }

    public void submitServiceResponse(String result) {
        Utils.okButtonAlertDialog(result,context);
        getDettails();
    }

    private class GetReviewsList extends AsyncTask<String,String,ReviewsListDto> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected ReviewsListDto doInBackground(String... params) {
            String data = null;
            try {
                URLConnection http = new URLConnection();
                data = http.readUrl(params[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("listdata",data);
            InputStream stream = new ByteArrayInputStream(data.getBytes());
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(stream);
            return gson.fromJson(reader, ReviewsListDto.class);
        }

        @Override
        protected void onPostExecute(ReviewsListDto reviewsListDto) {
            super.onPostExecute(reviewsListDto);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(reviewsListDto!=null){
                if(reviewsListDto.reviewsBean.image!=null) {
                    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                    final int cacheSize = maxMemory / 8;
                    LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
                    ImageLoader imageloader = new ImageLoader(context, memoryCache);
                    imageloader.displayImage(AppConstants.HTTP + reviewsListDto.reviewsBean.image, bgimage);
                }
                if(reviewsListDto.reviewsBean.reviewId!=null) {
                    reviewId=reviewsListDto.reviewsBean.reviewId.toString().trim();
                }
                if(reviewsListDto.reviewsBean.title!=null){
                	mTitleTextView.setText(reviewsListDto.reviewsBean.title);
                }
                if(reviewsListDto.reviewsBean.reviewsList.size()>0){
                reviewslistview.setAdapter(new ReviewsListAdapter(reviewsListDto,context));
                noreview.setVisibility(View.GONE);
                }else{
                	noreview.setVisibility(View.VISIBLE);
                }

            }

        }
    }
}
