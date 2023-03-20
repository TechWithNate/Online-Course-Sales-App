package com.tech.coursesales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccount extends AppCompatActivity {


    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mCPassword;
    private MaterialButton createBtn;
    private ProgressDialog progressDialog;
    private TextView login;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_accoutn);
        initViews();


        createBtn.setOnClickListener(v -> checkFields());
        login.setOnClickListener(v -> {
            startActivity(new Intent(CreateAccount.this, Login.class));
            finish();
        });

    }

    private void initViews(){
        mFirstName = findViewById(R.id.firstname);
        mLastName = findViewById(R.id.lastname);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mCPassword = findViewById(R.id.cPassword);
        createBtn = findViewById(R.id.create_account);
        login = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
    }

    private void checkFields() {

        if (mFirstName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
            mFirstName.requestFocus();
        } else if (mLastName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show();
            mLastName.requestFocus();
        } else if (mEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            mEmail.requestFocus();
        } else if (mPassword.getText().toString().length() < 6) {
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
        } else if (mPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
        } else if (!mCPassword.getText().toString().equals(mPassword.getText().toString())) {
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
            mCPassword.setText(null);
            mPassword.setText(null);
            mPassword.requestFocus();
        }else {
            createAccount(mEmail.getText().toString(), mPassword.getText().toString());
        }

    }


    private void createAccount(String email, String password){
        progressDialog.setMessage("Creating Account");
        progressDialog.setTitle("Create Account");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                Toast.makeText(CreateAccount.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateAccount.this, Login.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CreateAccount.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (null != user) {
            startActivity(new Intent(CreateAccount.this, MainActivity.class));
            finish();
        }
    }

}