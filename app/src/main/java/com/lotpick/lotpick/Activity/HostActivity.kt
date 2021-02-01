package com.lotpick.lotpick.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.lotpick.lotpick.R
import java.io.IOException
import java.util.*

class HostActivity : AppCompatActivity() {
    lateinit var addphotos: TextView
    lateinit var done: TextView
    var filepath: Uri? = null
    var location: ImageView? = null
    lateinit var close: ImageView
    var ettitle: EditText? = null
    var ettell: EditText? = null
    var etAdress: EditText? = null
    var etlistpart: EditText? = null
    var etprice: EditText? = null
    var etShared: EditText? = null
    var etopenoffice: EditText? = null
    var etseating: EditText? = null
    var etmeetroom: EditText? = null
    var etameneties: EditText? = null
    var cbwifi: CheckBox? = null
    var cbpantry: CheckBox? = null
    var cbvending: CheckBox? = null
    var cbprinter: CheckBox? = null
    var storage = FirebaseStorage.getInstance()
    var storageReference = storage.reference
    private val db = FirebaseFirestore.getInstance()
    var firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser
    var name = if (user != null) user.displayName else "User"
    val uid = Objects.requireNonNull(user)!!.uid
    var reference = storageReference.child("Uploads/Hosts/$name $uid")
    var documentReference = db.collection("Hosts").document("$name $uid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        addphotos = findViewById(R.id.Addphotos)
        location = findViewById(R.id.location_pics)
        close = findViewById(R.id.close)
        done = findViewById(R.id.done)
        cbwifi = findViewById(R.id.cbwifi)
        cbpantry = findViewById(R.id.cbpantry)
        cbprinter = findViewById(R.id.cbprinter)
        cbvending = findViewById(R.id.cbvending)
        etprice = findViewById(R.id.etprice)
        ettitle = findViewById(R.id.etTitle)
        etAdress = findViewById(R.id.etAdress)
        etlistpart = findViewById(R.id.etlistpart)
        etameneties = findViewById(R.id.etameneties)
        etmeetroom = findViewById(R.id.etmeetroom)
        etseating = findViewById(R.id.etseating)
        etShared = findViewById(R.id.etShared)
        etopenoffice = findViewById(R.id.etopenoffice)
        ettell = findViewById(R.id.ettell)
        done.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(this@HostActivity, R.anim.blink_anim)
            done.startAnimation(animation)
            updateProfile()
        })
        close.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(this@HostActivity, R.anim.blink_anim)
            close.startAnimation(animation)
            finish()
        })
        addphotos.setOnClickListener(View.OnClickListener { ChooseImage() })
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
                location!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateProfile() {
        var cwifi = "NO"
        var cpantry = "NO"
        var cvending = "NO"
        var cprinter = "NO"
        val Title = ettitle!!.text.toString()
        val adress = etAdress!!.text.toString()
        val listpart = etlistpart!!.text.toString()
        val amenities = etameneties!!.text.toString()
        val meetrooom = etmeetroom!!.text.toString()
        val seating = etseating!!.text.toString()
        val price = etprice!!.text.toString()
        val shared = etShared!!.text.toString()
        val openoffice = etopenoffice!!.text.toString()
        val tell = ettell!!.text.toString()
        if (cbwifi!!.isChecked) {
            cwifi = "YES"
        }
        if (cbpantry!!.isChecked) {
            cpantry = "YES"
        }
        if (cbprinter!!.isChecked) {
            cprinter = "YES"
        }
        if (cbvending!!.isChecked) {
            cvending = "YES"
        }
        val info: MutableMap<String, Any?> = HashMap()
        info["Title"] = Title
        info["Location"] = adress
        info["Description"] = tell
        info["Price"] = price
        info["Listing_part"] = listpart
        info["smthgelse"] = amenities
        info["No_of_MeetingRooms"] = meetrooom
        info["No_of_Seats"] = seating
        info["Shared_Office"] = shared
        info["openoffice"] = openoffice
        info["wifi"] = cwifi
        info["pantry"] = cpantry
        info["vendingmachine"] = cvending
        info["printer"] = cprinter
        if (filepath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading....")
            progressDialog.show()
            reference.putFile(filepath!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this@HostActivity, "Listing Added", Toast.LENGTH_SHORT).show()
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
        info["Image"] = Downurl
        info["Uid"] = uid
        info["UserName"] = name
        documentReference[info] = SetOptions.merge()
    }
}