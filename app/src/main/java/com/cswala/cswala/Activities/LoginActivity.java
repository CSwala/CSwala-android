package com.cswala.cswala.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cswala.cswala.MainActivity;
import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;
import com.cswala.cswala.utils.NetworkConnection;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBarLayout;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "Login";
    private NetworkConnection networkConnection;
    private String github_email;
//    private AuthCredential git_accessToken = null;


    @Override
    public void onBackPressed() {
        ExitDialog();
    }

    public void ExitDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Button google = findViewById(R.id.google);
        final Button github = findViewById(R.id.github);
        final Button email = findViewById(R.id.email);
        firebaseAuth = FirebaseAuth.getInstance();

        progressBarLayout = findViewById(R.id.ProgressBar);
        progressBarLayout.setVisibility(View.INVISIBLE);

        View parentLayout = findViewById(android.R.id.content);
        networkConnection = new NetworkConnection(parentLayout);

        // Buttons Fade-in Animation's Method
        timer();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        updateUI(user);


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.blink_anim);
                google.startAnimation(animation);
                if (networkConnection.isConnected(LoginActivity.this)) {
                    progressBarLayout.setVisibility(View.VISIBLE);
                    signIn();
                } else {
                    networkConnection.ShowNoConnection();
                }

            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.blink_anim);
                github.startAnimation(animation);
                if (networkConnection.isConnected(LoginActivity.this)) {
                    gitLoginDialog();
                } else {
                    networkConnection.ShowNoConnection();
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.blink_anim);
                email.startAnimation(animation);
                IntentHelper intentHelper = new IntentHelper(LoginActivity.this);
                intentHelper.GoToLoginWithEmail();
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
                Toast.makeText(this, "Please Login ", Toast.LENGTH_SHORT);
            } else {
                IntentHelper intentHelper = new IntentHelper(LoginActivity.this);
                intentHelper.GoToHome();
            }
    }


    private void validate(String userEmail, String userPassword) {

        progressBarLayout.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBarLayout.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    IntentHelper intentHelper = new IntentHelper(LoginActivity.this);
                    intentHelper.GoToHome();
                    Toast.makeText(LoginActivity.this, "Welcome back", Toast.LENGTH_SHORT).show();

                } else {
                    progressBarLayout.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void signIn() {
        mGoogleSignInClient.signOut().addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!networkConnection.isConnected(LoginActivity.this)) {
            networkConnection.ShowNoConnection();
            return;
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                progressBarLayout.setVisibility(View.INVISIBLE);
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            progressBarLayout.setVisibility(View.INVISIBLE);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            progressBarLayout.setVisibility(View.INVISIBLE);
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void gitLoginDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_githublogin,null);

        final EditText gitEmail = (EditText)mView.findViewById(R.id.email_git);
        Button loginBtn_git = (Button)mView.findViewById(R.id.login_git);
        Button cancelBtn_git = (Button)mView.findViewById(R.id.cancel_git);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        cancelBtn_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        loginBtn_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                github_email = gitEmail.getText().toString();
                progressBarLayout.setVisibility(View.VISIBLE);
                githubSignIn(github_email);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void githubSignIn(String email_github) {
        Log.i(TAG, "Inside githubSignIn");
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com", firebaseAuth);

        provider.addCustomParameter("login", email_github); /* if the user is not already signed-in on github through his/her device,
                                                                  the email id entered in the dialog box will be sent along with
                                                                  the auth request. */
        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };

        Log.i(TAG, "Before setting scopes github");

        provider.setScopes(scopes);

        Log.i(TAG, "After setting scopes github");

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();

        Log.i(TAG, "After getting pendingResult github");

        if (pendingResultTask != null) {
            Log.i(TAG, "Inside if block of pendingResult github");
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    Log.i(TAG, "Signing in github");
                                    Toast.makeText(getApplicationContext(), "Signing in...", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = authResult.getUser();
                                    updateUI(user);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    progressBarLayout.setVisibility(View.GONE);
                                    Log.i(TAG, "failed signing in github 1");
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
        } else {
//            if (git_accessToken == null) {
            Log.i(TAG, "Inside else block of pendingResult github");
            firebaseAuth
                    .startActivityForSignInWithProvider(/* activity= */ LoginActivity.this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    Log.i(TAG, "Signed in using github" + authResult.getUser().getDisplayName());
                                    Toast.makeText(getApplicationContext(), "Signed in successfully", Toast.LENGTH_SHORT).show();
//                                        git_accessToken = authResult.getCredential();
                                    FirebaseUser user = authResult.getUser();
                                    updateUI(user);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    progressBarLayout.setVisibility(View.GONE);
                                    Log.i(TAG, "Failed to sign in using github: " + e.getMessage());
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
//            }
        }
    }


    // Timer for Fade-in Animation of Buttons ( T1 - Google Button; T2 - GitHub Button )
    private void timer() {
        Timer t1, t2, t3;
        t1 = new Timer();
        t2 = new Timer();
        t3 = new Timer();
        final Button google = findViewById(R.id.google);
        final Button github = findViewById(R.id.github);
        final Button email = findViewById(R.id.email);

        google.animate().alpha(0f).setDuration(1);
        github.animate().alpha(0f).setDuration(1);
        email.animate().alpha(0f).setDuration(1);

        t1.schedule(new TimerTask() {
            @Override
            public void run() {
                google.animate().alpha(1f).setDuration(500);
            }
        }, 500);

        t2.schedule(new TimerTask() {
            @Override
            public void run() {
                github.animate().alpha(1f).setDuration(500);

            }
        }, 1000);

        t3.schedule(new TimerTask() {
            @Override
            public void run() {
                email.animate().alpha(1f).setDuration(500);

            }
        }, 1500);
    }

}

