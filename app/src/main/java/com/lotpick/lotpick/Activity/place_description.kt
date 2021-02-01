package com.lotpick.lotpick.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lotpick.lotpick.R
import java.util.*

class place_description : AppCompatActivity() {
    private lateinit var listTitle: TextView
    private lateinit var close: ImageView
    private lateinit var fav: CheckBox
    private lateinit var listPrice: TextView
    private lateinit var listLocation: TextView
    private lateinit var listRating: TextView
    private lateinit var listdescription: TextView
    private lateinit var listamenities: TextView
    private lateinit var listImage: ImageView
    private lateinit var messagebtn: Button
    private var bookbtn: Button? = null
    private val db = FirebaseFirestore.getInstance()
    var firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser
    var name = if (user != null) user.displayName else "User"
    var uid = Objects.requireNonNull(user)!!.uid

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_description)
        val title = intent.getStringExtra("Title")
        val price = intent.getStringExtra("Price")
        val location = intent.getStringExtra("Location")
        val desc = intent.getStringExtra("Desc")
        val image = intent.getStringExtra("Image")
        val amenities = intent.getStringExtra("Amenities")
        val id = intent.getStringExtra("Id")
        listTitle = findViewById(R.id.Title_text)
        listPrice = findViewById(R.id.price_text)
        listRating = findViewById(R.id.Rating_text)
        listLocation = findViewById(R.id.location_text)
        listImage = findViewById(R.id.place_image)
        listdescription = findViewById(R.id.desc_text)
        messagebtn = findViewById(R.id.messagebtn)
        listamenities = findViewById(R.id.amenities)
        bookbtn = findViewById(R.id.Bookbtn)
        close = findViewById(R.id.close)
        fav = findViewById(R.id.fav)
        val UID = intent.getStringExtra("Uid")
        val documentReference = db.collection("Users")
                .document("$name $uid").collection("favourites").document(id)
        val info: MutableMap<String, Any> = HashMap()
        info["Title"] = title
        info["Price"] = price
        info["Location"] = location
        info["Description"] = desc
        info["Image"] = image
        info["Uid"] = UID
        messagebtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@place_description, MessageActivity::class.java)
            intent.putExtra("userid", UID)
            startActivity(intent)
        })
        fav.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val animation = AnimationUtils.loadAnimation(this@place_description,
                        R.anim.blink_anim)
                fav.startAnimation(animation)
                documentReference.set(info)
            } else {
                val animation = AnimationUtils.loadAnimation(this@place_description,
                        R.anim.blink_anim)
                fav.startAnimation(animation)
                documentReference.delete()
            }
        })
        val collectionReference = db.collection("Users")
                .document("$name $uid").collection("favourites")
        collectionReference.document(id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document!!.exists()) {
                    fav.setChecked(true)
                } else {
                    fav.setChecked(false)
                }
            } else {
                Log.d("PlaceDescription", "Failed with: ", task.exception)
            }
        }
        close.setOnClickListener(View.OnClickListener {
            finish()
            val animation = AnimationUtils.loadAnimation(this@place_description, R.anim.blink_anim)
            close.startAnimation(animation)
        })
        listTitle.setText(title)
        listPrice.setText("Rs. $price")
        listRating.setText("Not enough Ratings")
        listLocation.setText(location)
        listdescription.setText(desc)
        listamenities.setText("""

    $amenities
    """.trimIndent())
        Glide.with(this@place_description).load(image).centerCrop().placeholder(R.drawable.white_background).into(listImage)
    }
}