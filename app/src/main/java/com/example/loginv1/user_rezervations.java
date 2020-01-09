package com.example.loginv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class user_rezervations extends AppCompatActivity {

    String park_name;
    String random_code;
    String how_many_hour;
    String what_is_minute;
    String what_is_hour;
    String what_is_date;
    String user_phone_number;
    String price;

    TextView parkName;
    TextView rez_date;
    TextView rez_hour_and_min;
    TextView rez_price;
    TextView rd_code;
    TextView how_many;
    ImageView rez_info;
    ImageView vipark_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rezervations);

        parkName = (TextView) findViewById(R.id.park_name);
        rez_date = (TextView) findViewById(R.id.rez_date);
        rez_hour_and_min = (TextView) findViewById(R.id.rez_hour_and_min);
        rez_price = (TextView) findViewById(R.id.rez_price);
        rd_code = (TextView) findViewById(R.id.random_code);
        how_many = (TextView) findViewById(R.id.how_many);
        rez_info = (ImageView) findViewById(R.id.exit_rez_info);
        vipark_logo = (ImageView) findViewById(R.id.vipark_logo);

        Intent intent = getIntent();
        park_name = intent.getStringExtra("park_name");
        how_many_hour = intent.getStringExtra("how_many");
        what_is_minute = intent.getStringExtra("minute");
        what_is_hour = intent.getStringExtra("hour");
        what_is_date = intent.getStringExtra("date");
        random_code = intent.getStringExtra("random");
        user_phone_number = intent.getStringExtra("phone");
        price = intent.getStringExtra("price");





        parkName.setText("Park Adı: "+ park_name);
        rez_date.setText("Giriş Tarihi: " + what_is_date);
        rez_hour_and_min.setText("Giriş saati: " + what_is_hour + ":" + what_is_minute);
        rez_price.setText("Ücret: "+price);
        rd_code.setText("Rezervasyon Kodu: "+ random_code);
        how_many.setText("Rezervasyon Süresi: "+how_many_hour);




    }
    public void refresh_rez(View view){
        Intent intent = new Intent(user_rezervations.this,MapsActivity.class);
        startActivity(intent);

    }
}
