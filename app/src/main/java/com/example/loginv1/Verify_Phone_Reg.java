package com.example.loginv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verify_Phone_Reg extends AppCompatActivity {
    EditText verfiy_code;
    TextView phoneNumVer;

    FirebaseAuth mAuth;
    String phoneNumber;


    PhoneAuthProvider phoneAuthProvider;

    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__phone__reg);
        verfiy_code = (EditText) findViewById(R.id.verification_code);
        mAuth = FirebaseAuth.getInstance();
        phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneNumVer = (TextView) findViewById(R.id.phoneNumVer);
        verfiy_code.requestFocus();

        phoneNumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phoneNumber);

        findViewById(R.id.verfication_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verfiy_code.getText().toString();
                if(code.isEmpty()|| code.length()<6){
                    verfiy_code.setError("Codu giriniz...");
                    verfiy_code.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumVer.setText(phoneNumber);
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);

    }
    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Verify_Phone_Reg.this,MapsActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(Verify_Phone_Reg.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify_Phone_Reg.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    public void resend_verification_code(View view){
        sendVerificationCode(phoneNumber);

    }




}
