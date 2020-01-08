package com.example.loginv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PayActivity extends AppCompatActivity {
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
        Intent intent = getIntent();
        user_phone_number = mAuth.getCurrentUser().getPhoneNumber();

        price = (TextView) findViewById(R.id.price);
        enter_info = (TextView) findViewById(R.id.enter_info);
        leave_info = (TextView) findViewById(R.id.leave_info);
        parkNameText = (TextView) findViewById(R.id.parkName);
        cartHolder = (EditText) findViewById(R.id.cartHolder);
        cartNumber = (EditText) findViewById(R.id.cartNumber);
        skt = (EditText) findViewById(R.id.skt);
        cvvNum = (EditText) findViewById(R.id.cvvNum);

        parkName = intent.getStringExtra("park_name2");
        how_many_hour = intent.getStringExtra("how_many_hour");
        what_is_minute = intent.getStringExtra("what_is_minute");
        what_is_hour = intent.getStringExtra("what_is_hour");
        what_is_date = intent.getStringExtra("what_is_date");
        enter_info.setText("Tarih: " + what_is_date + " - " +"Saat: " +what_is_hour+ " - " + what_is_minute);
        leave_info.setText("Park süresi: " + how_many_hour);


        CollectionReference collectionReference = db.collection("parks");

        collectionReference.whereEqualTo("name",parkName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document: task.getResult()){
                                park_price = document.getData().get("price").toString();

                            }
                        }else{
                            Toast.makeText(PayActivity.this,"price verisi yok",Toast.LENGTH_SHORT).show();//park yerinin price verisi yoksa bu hata çıkıyor

                        }
                    }
                });
        total_price = Integer.parseInt(park_price)*Integer.parseInt(how_many_hour);
        price.setText(total_price);
        parkNameText.setText(parkName);

    }
    public void final_pay_button(View view){ //en son ödeme işlemi burada çıkacak


    }
}
