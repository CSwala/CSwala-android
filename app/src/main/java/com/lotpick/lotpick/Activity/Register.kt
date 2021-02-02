package com.lotpick.lotpick.Activity

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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.lotpick.lotpick.Activity.Register
import com.lotpick.lotpick.R
import java.util.*

class Register() : AppCompatActivity() {
    lateinit var userPassword: EditText
    lateinit var userEmail: EditText
    private var firebaseAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_register)
        userPassword = findViewById(R.id.etPassword)
        userEmail = findViewById(R.id.etName)
        val regButton = findViewById<Button>(R.id.btnRegister)
        val userLogin = findViewById<TextView>(R.id.tvUserLogin)
        val googlebutton = findViewById<SignInButtonImpl>(R.id.googlebutton)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val user = firebaseAuth!!.currentUser
        updateUI(user)
        googlebutton.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(this@Register, R.anim.blink_anim)
            googlebutton.startAnimation(animation)
            signIn()
        })
        regButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val animation = AnimationUtils.loadAnimation(this@Register, R.anim.blink_anim)
                regButton.startAnimation(animation)
                if (validate()) {
                    val user_email = userEmail.getText().toString().trim { it <= ' ' }
                    val user_password = userPassword.getText().toString().trim { it <= ' ' }
                    firebaseAuth!!.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(
                            object : OnCompleteListener<AuthResult?> {
                                override fun onComplete(task: Task<AuthResult?>) {
                                    if (task.isSuccessful) {
                                        Objects.requireNonNull(firebaseAuth!!.currentUser)!!.sendEmailVerification().addOnCompleteListener(object : OnCompleteListener<Void?> {
                                            override fun onComplete(task: Task<Void?>) {
                                                Toast.makeText(this@Register, "Verification Link Sent!", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                        Toast.makeText(this@Register, "Registration Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@Register, MainActivity::class.java))
                                    } else {
                                        Toast.makeText(this@Register, "Already a user", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                }
            }
        })
        userLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                startActivity(Intent(this@Register, Login::class.java))
            }
        })
    }

    private fun updateUI(user: FirebaseUser?) {
        run {
            if (user == null) {
                Toast.makeText(this, "Please Register ", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this@Register, MainActivity::class.java))
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
                val account = task.getResult(ApiException::class.java)
                assert(account != null)
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
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                    override fun onComplete(task: Task<AuthResult?>) {
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = firebaseAuth!!.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            Toast.makeText(this@Register, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }

                        // ...
                    }
                })
    }

    private fun validate(): Boolean {
        var result = false
        val password = userPassword!!.text.toString()
        val email = userEmail!!.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail!!.requestFocus()
            userEmail!!.isSelected = true
            userEmail!!.error = "Please enter a valid email address "
        } else if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
        } else {
            result = true
        }
        return result
    }

    companion object {
        private val RC_SIGN_IN = 1
        private val TAG = "Login"
    }
}