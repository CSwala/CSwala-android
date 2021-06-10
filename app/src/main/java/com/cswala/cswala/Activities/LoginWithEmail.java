package com.cswala.cswala.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.cswala.cswala.utils.IntentHelper;
import com.cswala.cswala.MainActivity;
import com.cswala.cswala.R;
import com.cswala.cswala.utils.NetworkConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginWithEmail extends AppCompatActivity implements View.OnClickListener {

    TextView forgot_password;
    EditText email_id, password;
    private Button login, register;

    private NetworkConnection networkConnection;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        forgot_password = (TextView) findViewById(R.id.forgotpassword);
        forgot_password.setOnClickListener(this);

        register =(Button) findViewById(R.id.lregister);
        register.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        email_id = (EditText) findViewById(R.id.lemail);
        password = (EditText) findViewById(R.id.lpassword);
        progressBar = (ProgressBar) findViewById(R.id.lproressBar);

        View parentLayout = findViewById(android.R.id.content);
        networkConnection = new NetworkConnection(parentLayout);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lregister:
                IntentHelper intentHelper=new IntentHelper(LoginWithEmail.this);
                intentHelper.GoToRegister();
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgotpassword:
                IntentHelper intentHelper1=new IntentHelper(LoginWithEmail.this);
                intentHelper1.GoToForgetPassword();
                break;
        }
    }

    private void userLogin() {
        String email = email_id.getText().toString().trim();
        String password_ = password.getText().toString().trim();

        if (email.isEmpty()) {
            email_id.setError("Email is required");
            email_id.requestFocus();
            return;
        }

        if (password_.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_id.setError("Please provide valid email!");
            email_id.requestFocus();
            return;
        }

        if(password_.length() < 6){
            password.setError("Min Password length should be 6 characters!");
            password.requestFocus();
            return;
        }

        if (!networkConnection.isConnected(LoginWithEmail.this)) {
            networkConnection.ShowNoConnection();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified())
                    {
                        IntentHelper intentHelper=new IntentHelper(LoginWithEmail.this);
                        intentHelper.GoToHome();
                        progressBar.setVisibility(View.GONE);
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginWithEmail.this,"Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }



                }else{
                    Toast.makeText(LoginWithEmail.this,"Failed to login! Please Check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}