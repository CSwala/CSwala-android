package com.cswala.cswala.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cswala.cswala.MainActivity;
import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;
import com.cswala.cswala.utils.NetworkConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText txtemail, txtpassword;
    Button btn_register;
    ProgressBar progressBar;
    ImageView banner;
    private FirebaseAuth firebaseAuth;
    private NetworkConnection networkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        txtemail = (EditText)findViewById(R.id.txt_email);
        txtpassword = (EditText)findViewById(R.id.txt_password);
        progressBar = (ProgressBar)findViewById(R.id.proressBar);
        banner = (ImageView)findViewById(R.id.banner);
        banner.setOnClickListener(this);

        btn_register = (Button)findViewById(R.id.buttonRegister);
        btn_register.setOnClickListener(this);

        View parentLayout = findViewById(android.R.id.content);
        networkConnection = new NetworkConnection(parentLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRegister:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        final String email = txtemail.getText().toString().trim();
        String password = txtpassword.getText().toString().trim();

        if (email.isEmpty()) {
            txtemail.setError("Email is required");
            txtemail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            txtpassword.setError("Password is required");
            txtpassword.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtemail.setError("Please provide valid email!");
            txtemail.requestFocus();
            return;
        }

        if(password.length() < 6){
            txtpassword.setError("Min Password length should be 6 characters!");
            txtpassword.requestFocus();
            return;
        }

        if (!networkConnection.isConnected(Register.this)) {
            networkConnection.ShowNoConnection();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(email);
                            Toast.makeText(Register.this, "User has been Registered Successfully!", Toast.LENGTH_LONG).show();
                            txtemail.setText("");
                            txtpassword.setText("");
                            progressBar.setVisibility(View.GONE);
                            IntentHelper intentHelper=new IntentHelper(Register.this);
                            intentHelper.GoToLoginWithEmail();

                        }else{
                            Toast.makeText(Register.this, "Failed to Register! Try Again!", Toast.LENGTH_LONG).show();
                            txtemail.setText("");
                            txtpassword.setText("");
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
    }
}