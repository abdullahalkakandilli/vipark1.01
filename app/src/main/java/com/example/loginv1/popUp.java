package com.example.loginv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class popUp extends AppCompatActivity {
    WheelPicker wheelPicker;
    WheelPicker wheelPicker2;
    WheelPicker wheelPicker3;
    WheelPicker wheelPicker4;
    String tarih;
    String hour;
    //String minute;
    String date;
    String hour_of_selected_day;
    String minute_of_selected_day;
    String date_of_the_day;
    String current_hour_of_the_day;
    String parkName;
    String total_saat;

    TextView park_name_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        Intent intent  = getIntent();
        parkName = intent.getStringExtra("park_name");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;


        getWindow().setLayout((int)(width*.9), (int) (height*.3));


        WindowManager.LayoutParams params = getWindow().getAttributes();
        //params.gravity = Gravity.BOTTOM;
        params.x = 0;
        params.y = 345;


        getWindow().setAttributes(params);







        wheelPicker = findViewById(R.id.wheel_picker);
        wheelPicker2 = findViewById(R.id.wheel_picker2);
        wheelPicker3 = findViewById(R.id.wheel_picker3);
        wheelPicker4 = findViewById(R.id.wheel_picker4);

        park_name_text = (TextView) findViewById(R.id.park_name_text);

        park_name_text.setText(parkName);


        final Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        //Calendar calendar3 = Calendar.getInstance();

        Date today = calendar.getTime();
        Date today2 = calendar2.getTime();
        //Date today3 = calendar3.getTime();

        final List<String> wheel_dates = new ArrayList<>();
        List<String> wheel_hours = new ArrayList<>();
        List<String> wheel_minutes = new ArrayList<>();
        List<String> kac_saat_galacan = new ArrayList<>();


        for(int i = 1; i<49; i++){
            kac_saat_galacan.add(String.valueOf(i));
        }
        wheelPicker4.setData(kac_saat_galacan);


        int ix = 0;
        int iy = 0;

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        tarih = dateFormat.format(today);

        DateFormat dateFormat1 = new SimpleDateFormat("HH");
        hour = dateFormat1.format(today2);

        //DateFormat dateFormat2 = new SimpleDateFormat("mm");
        //minute = dateFormat2.format(today3);*/

        while(ix<30){
            ix++;
            wheel_dates.add(tarih);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            today=calendar.getTime();
            tarih = dateFormat.format(today);
        }
        while(iy<24){
            iy++;
            wheel_hours.add(hour);
            calendar2.add(Calendar.MINUTE, 60);
            today2 = calendar2.getTime();
            hour = dateFormat1.format(today2);
        }
        wheel_minutes.add("00");
        wheel_minutes.add("15");
        wheel_minutes.add("30");
        wheel_minutes.add("45");


        wheelPicker3.setData(wheel_minutes);

        wheelPicker3.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                minute_of_selected_day = data.toString();
            }
        });
        wheelPicker.setData(wheel_dates);
        wheelPicker2.setData(wheel_hours);

        wheelPicker.setSelectedItemPosition(1);
        wheelPicker2.setSelectedItemPosition(1);
        wheelPicker3.setSelectedItemPosition(1);
        wheelPicker4.setSelectedItemPosition(1);

        wheelPicker4.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                total_saat = data.toString();
                Intent intent = new Intent(popUp.this,PayActivity.class);
                intent.putExtra("total_saat",total_saat);
            }
        });


        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                date = data.toString();
                if(date.equals((date_of_the_day))){
                    wheelPicker2.setSelectedItemPosition(1);
                }else{

                }

            }
        });
        wheelPicker2.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                Calendar calendar3 = Calendar.getInstance();
                Date current_date = calendar3.getTime();
                DateFormat dateFormat3 = new SimpleDateFormat("dd MMMM yyyy");
                DateFormat dateFormat4 = new SimpleDateFormat("hh");
                date_of_the_day = dateFormat3.format(current_date);
                current_hour_of_the_day = dateFormat4.format(current_date);

                hour_of_selected_day = data.toString();
                if(date.equals(date_of_the_day)){
                    if(Integer.parseInt(hour_of_selected_day) < Integer.parseInt(current_hour_of_the_day)){
                        wheelPicker2.setSelectedItemPosition(1);
                    }else{

                    }

                }else{

                }

            }
        });






    }
    public void check_rezervation(View view){

        Intent intent = new Intent(popUp.this,PayActivity.class);
        intent.putExtra("park_name2", parkName);
        intent.putExtra("what_is_minute", minute_of_selected_day);//seçtiği saat
        intent.putExtra("what_is_hour", hour_of_selected_day);//saat
        intent.putExtra("what_is_date", date);//tarih
        intent.putExtra("how_many_hour", total_saat);//kaç saat olacağının bilgisi

        startActivity(intent);

    }
}
