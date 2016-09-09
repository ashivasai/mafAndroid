package com.zambient.maf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.zambient.Services.SubmitAppointment;
import com.zambient.Services.SubmitRestaurantAppointment;
import com.zambient.constants.AppConstants;
import com.zambient.dtos.AppointmentDto;
import com.zambient.dtos.DoctorsListDto;
import com.zambient.utils.ImageLoader;
import com.zambient.utils.URLConnection;
import com.zambient.utils.Utils;

public class RestaurantAppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Context context;
    private EditText date;
    private ImageView bgimage;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
   private  EditText time;
    private Spinner spinner;
    private String selectedItem;
    private TextView specilisation;
    private String specialisation;
    private String headCount;
    private DoctorsListDto doctorsListDto;
    private String bookingEmail;
    private TextView mTitleTextView;
    private ArrayList<String> expertsList=null;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_restaurantappointment);
        
        bgimage = (ImageView)findViewById(R.id.bgimage);
        spinner = (Spinner) findViewById(R.id.spinner);
        
        setSpinner(spinner);
        specilisation=(TextView) findViewById(R.id.specilisation);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(Utils.isNotNull(AppConstants.APP_COLOR))
            toolbar.setBackgroundColor(Color.parseColor(AppConstants.APP_COLOR));*/
        ImageView dateimage = (ImageView) findViewById(R.id.dateimage);
        LinearLayout book = (LinearLayout) findViewById(R.id.book);
        final EditText name =(EditText) findViewById(R.id.name);
        final EditText contactno =(EditText) findViewById(R.id.contactno);
        final EditText mailid =(EditText) findViewById(R.id.mailid);
        context=this;
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
        //final EditText description =(EditText) findViewById(R.id.description);
        
        date = (EditText) findViewById(R.id.date);
        ImageView timeimage=(ImageView)findViewById(R.id.timeimage);
        time=(EditText)findViewById(R.id.time);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        
        if (Utils.isConnectingToInternet(context)) {
        Log.d("appresponse",AppConstants.GETAPPOINTMENTDETAILS+"businessId="+pref.getString("bussinessId",null)+"&templateId="+pref.getString("templateId",null)+"&featureId=12&userId="+pref.getString("userId",null));	
        new GetAppointmentDetails().execute(AppConstants.GETAPPOINTMENTDETAILS+"businessId="+pref.getString("bussinessId",null)+"&templateId="+pref.getString("templateId",null)+"&featureId=12&userId="+pref.getString("userId",null));
        
        }else{
            Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
        }
        timeimage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });
        dateimage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate(name, contactno, mailid, date, time);
            }
        });

    }

    private void setSpinner(Spinner spinner) {
		// TODO Auto-generated method stub
    	expertsList = new ArrayList<String>();
    	expertsList.add("No. of Persons");
        for (int i = 1; i <= 20; i++) {
            expertsList.add(i+"");
        }
        ArrayAdapter dataAdapter = new ArrayAdapter(RestaurantAppointmentActivity.this, R.layout.spinner_item, expertsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(RestaurantAppointmentActivity.this);
		
	}

	private void validate(EditText name, EditText contactno, EditText mailid, EditText date, EditText time) {
        if(!(selectedItem!=null && !selectedItem.trim().matches("No. of Persons"))){
            Utils.toast("Please Select No. of Persons",context);
        }else if(!(Utils.isNotNull(name.getText().toString())&& Utils.isNotNull(contactno.getText().toString()) && Utils.isNotNull(mailid.getText().toString()) && Utils.isNotNull(date.getText().toString()) && Utils.isNotNull(time.getText().toString()))){
            Utils.toast("Please fill all the details",context);
        }else if(!(contactno.getText().toString().trim().length()!=10) && contactno.getText().toString().contains(".") && contactno.getText().toString().contains(" ") && contactno.getText().toString().contains("-") && contactno.getText().toString().contains("#") && contactno.getText().toString().contains("*")){
            Utils.toast("Please enter valid Mobile No.",context);
        }else if(!mailid.getText().toString().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            Utils.toast("Please enter valid Email ID",context);
        }else{
            JSONObject reviewparams = new JSONObject();
            JSONObject review = new JSONObject();
            try {
                review.accumulate("headCount",headCount);
                //review.accumulate("specialization",specialisation);
                review.accumulate("name",name.getText().toString());
                review.accumulate("email",mailid.getText().toString());
                review.accumulate("contactNo",contactno.getText().toString());
                review.accumulate("date",date.getText().toString());
                review.accumulate("time",time.getText().toString());
                review.accumulate("bookingEmail",bookingEmail);

                reviewparams.accumulate("bookTable", review);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("payload", reviewparams.toString());
            if (Utils.isConnectingToInternet(context)) {
            if(Utils.isNotNull(reviewparams.toString()))
                new SubmitRestaurantAppointment(RestaurantAppointmentActivity.this,context).execute(reviewparams.toString());
            }else{
                Utils.okButtonAlertDialog(AppConstants.NETWORK_MSG, context);
            }


        }
        try {
            dateValidate(date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

   private void dateValidate(String date) throws ParseException {
        Boolean result =false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(date);
        Date toaday = sdf.parse(new Date().toString());
        Log.d("strDate",strDate.getTime()+"");
        Log.d("toaday",toaday.getTime()+"");
        /*if (toaday.after(strDate) || toaday== strDate) {
            result = true;
        }
        return result;*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                DatePickerDialog _date =   new DatePickerDialog(this, pickerListener, year,month,
                        day){
                    @Override
                    public void onDateChanged(DatePicker view, int nyear, int monthOfYear, int dayOfMonth)
                    {
                        if (nyear < year)
                            view.updateDate(year, month, day);

                        if (monthOfYear < month && nyear == year)
                            view.updateDate(year, month, day);

                        if (dayOfMonth < day && nyear == year && monthOfYear == month)
                            view.updateDate(year, month, day);

                    }
                };
                return _date;
            case 2:
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hour   = hourOfDay;
            minute = minutes;
            updateTime(hour,minute);
        }
    };
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            date.setText(new StringBuilder().append(day).append("/").append(month + 1)
                    .append("/").append(year));

        }
    };

    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        time.setText(aTime);
    }

    public void submitServiceResponse(String result) {
        Utils.okButtonAlertDialog(result,context);
    }

    private class GetAppointmentDetails extends AsyncTask<String,String,AppointmentDto> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RestaurantAppointmentActivity.this);
            pDialog.setMessage("Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected AppointmentDto doInBackground(String... params) {
            String data = null;
            try {
                URLConnection http = new URLConnection();
                data = http.readUrl(params[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("homedata",data);
            InputStream stream = new ByteArrayInputStream(data.getBytes());
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(stream);
            return gson.fromJson(reader, AppointmentDto.class);
        }

        @Override
        protected void onPostExecute(AppointmentDto appointmentDto) {
            super.onPostExecute(appointmentDto);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(appointmentDto.appointmentBean.image!=null) {
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize);
                ImageLoader imageloader = new ImageLoader(context, memoryCache);
                imageloader.displayImage(AppConstants.HTTP + appointmentDto.appointmentBean.image, bgimage);
            }
            if(appointmentDto.appointmentBean.email!=null){
                bookingEmail = appointmentDto.appointmentBean.email;
            }
            if(appointmentDto.appointmentBean.title!=null){
            	mTitleTextView.setText(appointmentDto.appointmentBean.title);
            }
        }
    }

    private class GetDoctorsList extends AsyncTask<String,String,String> {
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
        protected String doInBackground(String... params) {
            String data = null;
            try {
                URLConnection http = new URLConnection();
                data = http.readUrl(params[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("listdata", data);
           return data;
        }

        @Override
        protected void onPostExecute(String  response) {
            super.onPostExecute(response);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (response != null) {
                InputStream stream = new ByteArrayInputStream(response.getBytes());
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(stream);
                doctorsListDto = gson.fromJson(reader, DoctorsListDto.class);
                if (doctorsListDto.androidDoctorsList.doctorsList.size() != 0 && doctorsListDto.androidDoctorsList.doctorsList != null) {
                    ArrayList<String> expertsList = new ArrayList<String>();
                    expertsList.add("Select Doctor");
                    for (int i = 0; i < doctorsListDto.androidDoctorsList.doctorsList.size(); i++) {
                        expertsList.add(doctorsListDto.androidDoctorsList.doctorsList.get(i).name);

                    }
                 
                   

                }

            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = parent.getItemAtPosition(position).toString();
        if(position!=0) {
            //specialisation = doctorsListDto.androidDoctorsList.doctorsList.get(position - 1).specialization;
            headCount = expertsList.get(position);
            //specilisation.setText(specialisation);
        }else{
            //specilisation.setText(" ");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

