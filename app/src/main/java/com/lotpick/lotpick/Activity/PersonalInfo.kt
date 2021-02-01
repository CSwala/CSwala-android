package com.lotpick.lotpick.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.lotpick.lotpick.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.*

class PersonalInfo : AppCompatActivity() {
    private lateinit var etname: EditText
    private lateinit var etemail: EditText
    private lateinit var etgender: EditText
    private lateinit var etphone: EditText
    private lateinit var etdob: EditText
    var profilepic: CircleImageView? = null
    private lateinit var close: ImageView
    private lateinit var save: TextView
    private lateinit var changepic: TextView
    var filepath: Uri? = null
    var storage = FirebaseStorage.getInstance()
    var storageReference = storage.reference
    private val db = FirebaseFirestore.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        etname = findViewById(R.id.NAMEE)
        close = findViewById(R.id.close)
        etemail = findViewById(R.id.EMAILE)
        etgender = findViewById(R.id.etgender)
        etphone = findViewById(R.id.etphone)
        etdob = findViewById(R.id.etdob)
        save = findViewById(R.id.save)
        profilepic = findViewById(R.id.profile_image)
        changepic = findViewById(R.id.changepic)
        save.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(this@PersonalInfo, R.anim.blink_anim)
            save.startAnimation(animation)
            updateProfile()
        })
        changepic.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(this@PersonalInfo, R.anim.text_anim)
            changepic.startAnimation(animation)
            ChooseImage()
        })
        close.setOnClickListener(View.OnClickListener {
            finish()
            val animation = AnimationUtils.loadAnimation(this@PersonalInfo, R.anim.blink_anim)
            close.startAnimation(animation)
        })
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val name = if (user != null) user.displayName else "User"
        val uid = Objects.requireNonNull(user)!!.uid
        val documentReference = db.collection("Users").document("$name $uid")
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val tName = documentSnapshot.getString("Name")
                etname.setText(tName)
                val tEmail = documentSnapshot.getString("Email")
                etemail.setText(tEmail)
                val tGender = documentSnapshot.getString("Gender")
                if (tGender != "") {
                    etgender.setText(tGender)
                }
                val tDOB = documentSnapshot.getString("Date of Birth")
                if (tDOB != "") {
                    etdob.setText(tDOB)
                }
                val tPhone = documentSnapshot.getString("Phone no ")
                if (tPhone != "") {
                    etphone.setText(tPhone)
                }
                val tImage = documentSnapshot.getString("Profile")
                if (tImage != "") {
                    Picasso.with(this@PersonalInfo).load(tImage).into(profilepic)
                }
            } else {
                Toast.makeText(this@PersonalInfo, "Document does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this@PersonalInfo, "Error", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", e.toString())
        }
    }

    private fun ChooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            filepath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
                profilepic!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateProfile() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val name = etname!!.text.toString()
        val gender = etgender!!.text.toString()
        val dob = etdob!!.text.toString()
        assert(user != null)
        val uid = user!!.uid
        val phone = etphone!!.text.toString()
        val info: MutableMap<String, Any> = HashMap()
        info["Name"] = name
        info["Gender"] = gender
        info["Date of Birth"] = dob
        info["username"] = name
        if (!Patterns.PHONE.matcher(phone).matches()) {
            etphone!!.requestFocus()
            etphone!!.isSelected = true
            etphone!!.error = "Please enter a valid Phone Number"
        } else {
            info["Phone no "] = phone
        }
        val reference = storageReference.child("Uploads/Profile Pic/$name $uid")
        if (filepath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading....")
            progressDialog.show()
            reference.putFile(filepath!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this@PersonalInfo, "Personal Info Updated!", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressDialog.setMessage("Uploaded " + progress.toInt() + " %")
            }
        }
        val urlTask = reference.downloadUrl
        while (!urlTask.isSuccessful);
        val downloadUrl = urlTask.result
        val Downurl = downloadUrl.toString()
        info["Profile"] = Downurl
        info["imageURL"] = Downurl
        info["id"] = user.uid
        firebaseDatabase.child(uid).setValue(info)
        db.collection("Users").document("$name $uid")[info] = SetOptions.merge()
    }
}