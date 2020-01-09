package com.example.loginv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.HashMap;

public class PayActivity extends AppCompatActivity  {
    TextView price;
    TextView parkNameText;
    TextView enter_info;
    TextView leave_info;
    String parkName;
    String park_price;
    String how_many_hour;
    String what_is_minute;
    String what_is_hour;
    String what_is_date;
    String user_phone_number;
    private FirebaseAuth mAuth;



    EditText cartHolder;
    EditText cartNumber;
    EditText skt;
    EditText cvvNum;

    int total_price;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        user_phone_number = "";


        price = (TextView) findViewById(R.id.price);
        enter_info = (TextView) findViewById(R.id.enter_info);
        leave_info = (TextView) findViewById(R.id.leave_info);
        parkNameText = (TextView) findViewById(R.id.parkName);
        cartHolder = (EditText) findViewById(R.id.cartHolder);
        cartNumber = (EditText) findViewById(R.id.cartNumber);




        skt = (EditText) findViewById(R.id.skt);
        cvvNum = (EditText) findViewById(R.id.cvvNum);
        cartNumber.requestFocus();

        parkName = intent.getStringExtra("park_name2");
        how_many_hour = intent.getStringExtra("how_many_hour");
        what_is_minute = intent.getStringExtra("what_is_minute");
        what_is_hour = intent.getStringExtra("what_is_hour");
        what_is_date = intent.getStringExtra("what_is_date");
        enter_info.setText("Giriş Tarihi: " + what_is_date + " - " +"Saat: " +what_is_hour+ " - " + what_is_minute);
        leave_info.setText("Park süresi: " + how_many_hour + " saat");

        db.collection("parks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                               if(parkName.equals(document.getData().get("name").toString())){
                                   park_price = document.getData().get("price").toString();
                                   Log.w("test_price1", "success getting documents.", task.getException());
                                   total_price = (Integer.parseInt(park_price)*Integer.parseInt(how_many_hour));
                                   Log.w("test_price2", "success getting documents.", task.getException());
                                   price.setText(String.valueOf(total_price) + " TL ");
                                   Log.w("test_price3", "success getting documents.", task.getException());
                               }else{
                                   Toast.makeText(PayActivity.this,"sgsgsgsgsg",Toast.LENGTH_LONG).show();
                                   Log.w("test_price", "Error getting documents.", task.getException());
                               }
                            }

                        }else{

                        }
                    }
                });

        parkNameText.setText(parkName);


    }
    public void final_pay_button(View view){//en son ödeme işlemi burada çıkacak

        sending_rez_infos();

    }
    public void sending_rez_infos(){

        if(user_phone_number.equals("")){

            AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
            builder.setTitle("Telefon Numarası");
            builder.setMessage("Telefon numarası olmadan rezervasyon yapamazsınız. Profil sayfasına yönlendiriliyorsunuz.");
            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent = new Intent(PayActivity.this, profile_page.class);

                    startActivity(intent);

                }
            });
            builder.show();



        }else {
            user_phone_number = mAuth.getCurrentUser().getPhoneNumber();

            HashMap<String, Object> postData = new HashMap<>();
            postData.put("park_name", parkName);
            postData.put("date", what_is_date);
            postData.put("price", String.valueOf(total_price));
            postData.put("phone", user_phone_number);
            postData.put("hour", what_is_hour);
            postData.put("how_many_hour", how_many_hour);
            postData.put("minute", what_is_minute);
            postData.put("random", "VPSH01FB");


            db.collection("userRezervations").document(user_phone_number)
                    .set(postData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                            builder.setTitle("Rezervasyon");
                            builder.setMessage("Rezervasyonunuz başarıyla oluşturulmuştur.");
                            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(PayActivity.this, user_rezervations.class);
                                    intent.putExtra("park_name", parkName);
                                    intent.putExtra("date", what_is_date);
                                    intent.putExtra("price", String.valueOf(total_price));
                                    intent.putExtra("phone", user_phone_number);
                                    intent.putExtra("hour", what_is_hour);
                                    intent.putExtra("minute", what_is_minute);
                                    intent.putExtra("how_many", how_many_hour);

                                    intent.putExtra("random", "VPSH01FB");
                                    startActivity(intent);

                                }
                            });
                            builder.show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PayActivity.this, "eksik bilgi", Toast.LENGTH_LONG).show();

                }
            });
        }

    }
}
