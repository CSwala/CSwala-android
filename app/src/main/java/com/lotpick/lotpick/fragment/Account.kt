package com.lotpick.lotpick.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lotpick.lotpick.*
import com.lotpick.lotpick.Activity.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class Account : Fragment() {
    lateinit var username: TextView
    private var profilepic: CircleImageView? = null
    private var firebaseAuth: FirebaseAuth? = null
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val tv = view.findViewById<TextView>(R.id.logout)
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth!!.currentUser
        username = view.findViewById(R.id.profileName)
        val persInfo = view.findViewById<Button>(R.id.persInfo)
        val terms = view.findViewById<Button>(R.id.terms)
        val hostbtn = view.findViewById<Button>(R.id.hostbtn)
        val seelist = view.findViewById<Button>(R.id.seelist)
        profilepic = view.findViewById(R.id.profile_image)
        hostbtn.setOnClickListener { startActivity(Intent(activity, HostActivity::class.java)) }
        seelist.setOnClickListener { startActivity(Intent(activity, Mylist::class.java)) }
        persInfo.setOnClickListener { startActivity(Intent(activity, PersonalInfo::class.java)) }
        terms.setOnClickListener { startActivity(Intent(activity, TermsActivity::class.java)) }
        tv.setOnClickListener {
            firebaseAuth!!.signOut()
            startActivity(Intent(activity, Login::class.java))
        }
        val name = if (user != null) user.displayName else "User"
        val uid = Objects.requireNonNull(user)!!.uid
        val documentReference = db.collection("Users").document("$name $uid")
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val nAme = documentSnapshot.getString("Name")
                username.text = "Hi, $nAme"
                val tImage = documentSnapshot.getString("Profile")
                if (tImage != "") {
                    Picasso.with(context).load(tImage).into(profilepic)
                }
            } else {
                Toast.makeText(activity, "Document does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", e.toString())
        }
        return view
    }
}