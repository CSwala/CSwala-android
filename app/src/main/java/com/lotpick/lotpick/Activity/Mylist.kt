package com.lotpick.lotpick.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lotpick.lotpick.R
import java.util.*

class Mylist : AppCompatActivity() {
    private lateinit var listTitle: TextView
    private lateinit var close: ImageView
    private lateinit var listPrice: TextView
    private lateinit var listLocation: TextView
    private lateinit var listRating: TextView
    private lateinit var listdescription: TextView
    private lateinit var listamenities: TextView
    private lateinit var listImage: ImageView
    lateinit var editlist: Button
    private val db = FirebaseFirestore.getInstance()
    var firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser
    var name = if (user != null) user.displayName else "User"
    var uid = Objects.requireNonNull(user)!!.uid
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mylist)
        listTitle = findViewById(R.id.Title_text)
        listPrice = findViewById(R.id.price_text)
        listRating = findViewById(R.id.Rating_text)
        listLocation = findViewById(R.id.location_text)
        listImage = findViewById(R.id.place_image)
        listdescription = findViewById(R.id.desc_text)
        editlist = findViewById(R.id.editlistbtn)
        listamenities = findViewById(R.id.amenities)
        close = findViewById(R.id.close)
        val documentReference = db.collection("Users")
                .document("$name $uid")
        editlist.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@Mylist, HostActivity::class.java)
            startActivity(intent)
        })
        close.setOnClickListener(View.OnClickListener {
            finish()
            val animation = AnimationUtils.loadAnimation(this@Mylist, R.anim.blink_anim)
            close.startAnimation(animation)
        })
        listTitle.text = "title"
        listPrice.text = "Rs. " + "00000"
        listRating.text = "Not enough Ratings"
        listLocation.text = "location"
        listdescription.text = "Description"
        listamenities.text = """

    amenities
    """.trimIndent()
        Glide.with(this@Mylist).load(R.drawable.addprofile).centerCrop().placeholder(R.drawable.white_background).into(listImage)
    }
}