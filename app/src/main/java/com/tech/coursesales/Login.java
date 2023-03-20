package com.tech.coursesales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private MaterialButton loginBtn;
    private TextView mSignupBtn;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        loginBtn.setOnClickListener(v ->
            checkFields());

        mSignupBtn.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, CreateAccount.class));
            finish();
        });
    }


    private void initViews(){
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);
        mSignupBtn = findViewById(R.id.signup);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void checkFields(){
        if (mEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            mEmail.requestFocus();
        } else if (mPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
        }else {
            loginUser(mEmail.getText().toString(), mPassword.getText().toString());
        }
    }

    private void loginUser(String email, String password){
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Authentication");
        progressDialog.setMessage("Authenticating User");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (null != user) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }
}