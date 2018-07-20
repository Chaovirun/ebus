package com.edu.ebus.ebus.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.ebus.ebus.R;
import com.edu.ebus.ebus.recent.RecntlyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

/**
 * Created by USER on 7/12/2018.
 */

public class VerifyActivity extends AppCompatActivity {

    private String codesent;
    private String phonedata;
    private EditText entercode;
    FirebaseAuth mAuth;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_verify_layout);

        TextView dispay_number = findViewById(R.id.txt_numer_verify);
        Button bt_sumit = findViewById(R.id.btn_Submit);
        entercode = findViewById(R.id.txt_enter_code_verify);
        TextView resentcode = findViewById(R.id.txt_redent_code);
        TextView wrongnumber = findViewById(R.id.txt_wrong_number);

        Intent intent = getIntent();
        phonedata = intent.getStringExtra("number_phone");
        codesent = intent.getStringExtra("codesent");
        String nunber_ticket = intent.getStringExtra("number_ticket");
        String set_money = intent.getStringExtra("set_money");

        dispay_number.setText("0"+phonedata);
        mAuth = FirebaseAuth.getInstance();

        Log.i ("verify","n_t"+ nunber_ticket +"   "+"s_m"+ set_money +"  "+"p_d"+phonedata);

        resentcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentverificationcode();
                Toast.makeText (getApplication (),"We've sent other code to your phone.",Toast.LENGTH_LONG).show ();
            }
        });
        wrongnumber.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplication (), SetTicketActivity.class);
                startActivity (intent);
                finish ();
            }
        });

    }

    public  void onsubmit(View view){
        Log.i("verify", "onsubmit :" + codesent);
        String code = entercode.getText().toString();
        if(code.isEmpty ()) {
            Toast.makeText (getApplication (),"Please Enter Code",Toast.LENGTH_LONG).show ();
        }
        else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential (codesent, code);
            signInWithPhoneAuthCredential (credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verify succece", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);

                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText (getApplicationContext (),"Code is wrong",Toast.LENGTH_LONG).show ();
                        }
                    }
                });


    }

    private void sentverificationcode(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+855"+phonedata,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks () {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.i("verify","verify success"+phoneAuthCredential);
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText (getApplication (),"PhoneAuthcteaFaild",Toast.LENGTH_LONG).show ();
            Log.i("verify","verify fail"+e);
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent (s, forceResendingToken);
            codesent = s;
            Log.i("verify","code sent "+s+"     "+"verify Oncodesent"+forceResendingToken);
        }
    };

}
