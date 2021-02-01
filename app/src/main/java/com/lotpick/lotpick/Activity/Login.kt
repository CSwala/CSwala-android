package com.lotpick.lotpick.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.internal.SignInButtonImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.lotpick.lotpick.Activity.Login
import com.lotpick.lotpick.Activity.Register
import com.lotpick.lotpick.R

class Login : AppCompatActivity() {
    lateinit var Email: EditText
    lateinit var Password: EditText
    lateinit var login: Button
    private var firebaseAuth: FirebaseAuth? = null
    private var progressDialog: ProgressDialog? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Email = findViewById(R.id.etName)
        Password = findViewById(R.id.etPassword)
        login = findViewById(R.id.btnLogin)
        val userRegistration = findViewById<TextView>(R.id.tvRegister)
        val googlebutton = findViewById<SignInButtonImpl>(R.id.googlebutton)
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val user = firebaseAuth!!.currentUser
        updateUI(user)
        googlebutton.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this@Login, R.anim.blink_anim)
            googlebutton.startAnimation(animation)
            signIn()
        }
        login.setOnClickListener(View.OnClickListener {
            if (check()) {
                validate(Email.getText().toString(), Password.getText().toString())
            }
            val animation = AnimationUtils.loadAnimation(this@Login, R.anim.blink_anim)
            login.startAnimation(animation)
        })
        userRegistration.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
            val animation = AnimationUtils.loadAnimation(this@Login, R.anim.blink_anim)
            userRegistration.startAnimation(animation)
        }
    }

    private fun check(): Boolean {
        var result = false
        val password = Password!!.text.toString()
        val email = Email!!.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email!!.requestFocus()
            Email!!.isSelected = true
            Email!!.error = "Please enter a valid email address "
        } else if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
        } else {
            result = true
        }
        return result
    }

    private fun updateUI(user: FirebaseUser?) {
        run {
            if (user == null) {
                Toast.makeText(this, "Please Login ", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this@Login, MainActivity::class.java))
            }
        }
    }

    private fun validate(userEmail: String, userPassword: String) {
        progressDialog!!.setMessage("Hey! By using this app you can find Great workspaces.")
        progressDialog!!.show()
        firebaseAuth!!.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressDialog!!.dismiss()
                startActivity(Intent(this@Login, MainActivity::class.java))
                Toast.makeText(this@Login, "Welcome back", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Login, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct!!.id)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = firebaseAuth!!.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this@Login, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // ...
                }
    }

    companion object {
        private const val RC_SIGN_IN = 1
        private const val TAG = "Login"
    }
}