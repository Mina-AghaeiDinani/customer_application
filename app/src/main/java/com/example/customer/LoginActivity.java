package com.example.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mauth;
    private FirebaseFirestore mFirestore;
    private EditText password, email;
    private int counter = 5;
    private DatabaseReference databaseCustomer;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //If forgot password
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
                finish();
            }
        });

        password = findViewById(R.id.edtPassword);
        email = findViewById(R.id.edtEmail);
        //for Authentication
        mauth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //If user Already Login
        FirebaseUser user = mauth.getCurrentUser();
        if (user != null) {
//Check if User is a customer
            currentUserId = mauth.getCurrentUser().getUid();
            databaseCustomer = FirebaseDatabase.getInstance().getReference("CustomersProfile");
            databaseCustomer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        if (keyNode.getKey().equals(currentUserId)) {
                            //get token of each device
                            String token_id = FirebaseInstanceId.getInstance().getToken();
                            FirebaseDatabase.getInstance().getReference("UsersToken")
                                    .child(currentUserId).setValue(token_id);
                            Toast.makeText(LoginActivity.this, "Login was Successful ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, Home.class));
                            finish();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
           // Toast.makeText(LoginActivity.this, "This user is not a customer...", Toast.LENGTH_SHORT).show();
            //................

        }


        //Click on Not yet a rider?
        TextView txtNewCustomer = findViewById(R.id.txtNewCustomer);
        txtNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ApplyHelpActivity.class);
                startActivity(intent);
            }
        });

        //Click on Continue
        final Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(email.getText().toString().trim(), password.getText().toString().trim())) {
                    final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "please wait...", "processing", true);
                    mauth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        //Check if User is a customer
                                        currentUserId = mauth.getCurrentUser().getUid();
                                        databaseCustomer = FirebaseDatabase.getInstance().getReference("CustomersProfile");
                                        databaseCustomer.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                                    if (keyNode.getKey().equals(currentUserId)) {
                                                        //get token of each device
                                                        String token_id = FirebaseInstanceId.getInstance().getToken();
                                                        FirebaseDatabase.getInstance().getReference("UsersToken")
                                                                .child(currentUserId).setValue(token_id);
                                                        Toast.makeText(LoginActivity.this, "Login was Successful ", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(LoginActivity.this, Home.class));
                                                        finish();
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                        Toast.makeText(LoginActivity.this, "This user is not a customer...", Toast.LENGTH_SHORT).show();
                                        //................
                                    } else {
                                        Log.e("Error", task.getException().toString());
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        counter--;
                                        if (counter == 0) {
                                            Toast.makeText(LoginActivity.this, "No of attempts remaining...", Toast.LENGTH_SHORT).show();

                                            btnContinue.setEnabled(false);
                                        }
                                    }
                                }

                            });
                }
            }
        });


    }

    private boolean validate(String Email, String Password) {
        if ((Email.isEmpty())) {
            Toast.makeText(LoginActivity.this, "Enter your Email...", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return false;
        } else if ((Password.isEmpty())) {
            Toast.makeText(LoginActivity.this, "Enter your password...", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void checkEmailVerification() {

        FirebaseUser firebaseUser = mauth.getInstance().getCurrentUser();
        //If user did verification this flag is true
        Boolean emailFlag = firebaseUser.isEmailVerified();
        if (emailFlag) {
            Toast.makeText(LoginActivity.this, "Login was Successful ", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(LoginActivity.this, Home.class));

        } else {
            Toast.makeText(LoginActivity.this, "Verify your email", Toast.LENGTH_SHORT).show();
            mauth.signOut();
        }
    }
}
