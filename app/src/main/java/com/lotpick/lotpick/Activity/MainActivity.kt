package com.lotpick.lotpick.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lotpick.lotpick.BuildConfig
import com.lotpick.lotpick.R
import com.lotpick.lotpick.fragment.Account
import com.lotpick.lotpick.fragment.Explore
import com.lotpick.lotpick.fragment.Inbox
import com.lotpick.lotpick.fragment.Saved
import java.util.*

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var selectedfragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            showStartDialog()
        }
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val bottomNavigationView: BottomNavigationView? = findViewById(R.id.bottom_navigation)
        bottomNavigationView?.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                Explore()).commit()
    }

    override fun onBackPressed() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Exit?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes") { _, _ -> finishAffinity() }.show()
        }
    }

    private fun showStartDialog() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val name = if (user != null) user.displayName else "User"
        val email = if (user != null) user.email else "user@gmail.com"
        if (BuildConfig.DEBUG && user == null) {
            error("Assertion failed")
        }
        val uid = user!!.uid
        val uri = user.photoUrl
        val link = uri?.toString() ?: ""
        val info: MutableMap<String, Any> = HashMap()
        info["Name"] = name ?: "User"
        info["Email"] = email ?: "user@email.com"
        info["Uid"] = uid
        info["Profile"] = link
        info["Gender"] = ""
        info["Date of Birth"] = ""
        info["Phone no "] = ""
        info["id"] = uid
        info["username"] = name ?: "User"
        info["imageURL"] = link
        info["status"] = "offline"
        val userid = user.uid
        val ref: DatabaseReference
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid)
        ref.setValue(info)
        db.collection("Users").document("$name $uid")[info] = SetOptions.merge()
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.account -> {
                @SuppressLint("CommitPrefEdits") val editor = getSharedPreferences("PREPS", MODE_PRIVATE).edit()
                editor.putString("Userid", Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)!!.uid)
                editor.apply()
                selectedfragment = Account()
            }
            R.id.saved -> selectedfragment = Saved()
            R.id.inbox -> selectedfragment = Inbox()
            R.id.explore -> selectedfragment = Explore()
        }
        if (selectedfragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    selectedfragment!!).commit()
        }
        true
    }
}