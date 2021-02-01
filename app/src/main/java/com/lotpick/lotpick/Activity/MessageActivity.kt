package com.lotpick.lotpick.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.lotpick.lotpick.Adapters.MessageAdapter
import com.lotpick.lotpick.Models.Chat
import com.lotpick.lotpick.Models.User
import com.lotpick.lotpick.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class MessageActivity : AppCompatActivity() {
    lateinit var pro_img: CircleImageView
    lateinit var username: TextView
    lateinit var status: TextView
    var fUser: FirebaseUser? = null
    var ref: DatabaseReference? = null
    lateinit var btSend: ImageButton
    lateinit var txtSend: EditText
    var reference: DatabaseReference? = null
    var messageAdapter: MessageAdapter? = null
    lateinit var mChat: MutableList<Chat>
    lateinit var reView: RecyclerView
    lateinit var userid: String

    var seenListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this@MessageActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            reference!!.removeEventListener(seenListener!!)
        }
        reView = findViewById(R.id.chats_rv)
        status = findViewById(R.id.OnlineTv)
        reView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        reView.setLayoutManager(linearLayoutManager)
        pro_img = findViewById(R.id.profile_image)
        username = findViewById(R.id.username)
        btSend = findViewById(R.id.btn_send)
        txtSend = findViewById(R.id.text_send)
        val intent:Intent  = getIntent()
        userid = intent.getStringExtra("userid")
        fUser = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid)
        btSend.setOnClickListener(View.OnClickListener {
            val msg = txtSend.getText().toString()
            if (msg != "") {
                sendMes(fUser!!.uid, userid, msg)
            } else {
                Toast.makeText(this@MessageActivity, "You can't send blank messages", Toast.LENGTH_SHORT).show()
            }
            txtSend.setText("")
        })
        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!
                username.setText(user.username)
                status.setText(user.status)
                if (user.imageURL == "") {
                    pro_img.setImageResource(R.drawable.addprofile)
                } else {
                    Glide.with(applicationContext).load(user.imageURL).placeholder(R.drawable.addprofile)
                            .into(pro_img)
                }
                readMes(fUser!!.uid, userid, user.imageURL)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        seenMessage(userid)
    }

    private fun seenMessage(userId: String?) {
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        try {
            seenListener = reference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        var chat: Chat?
                        chat = snapshot.getValue(Chat::class.java)
                        if (chat!!.receiver == fUser!!.uid && chat.sender == userId) {
                            val hashMap = HashMap<String, Any>()
                            hashMap["isseen"] = true
                            snapshot.ref.updateChildren(hashMap)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (ignored: NullPointerException) {
        }
    }

    private fun sendMes(sender: String, receiver: String?, message: String) {
        try {
            val ref = FirebaseDatabase.getInstance().reference
            val hashM = HashMap<String, Any?>()
            hashM["sender"] = sender
            hashM["receiver"] = receiver
            hashM["message"] = message
            hashM["isseen"] = false
            ref.child("Chats").push().setValue(hashM)
            val chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(fUser!!.uid).child(userid!!)
            chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        chatRef.child("id").setValue(userid)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            val chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(userid!!)
                    .child(fUser!!.uid)
            chatRefReceiver.child("id").setValue(fUser!!.uid)
            reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser!!.uid)
            reference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (ignored: NullPointerException) {
        }
    }

    private fun readMes(myid: String, userid: String?, imageurl: String?) {
        mChat = ArrayList()
        ref = FirebaseDatabase.getInstance().getReference("Chats")
        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mChat.clear()
                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)!!
                    if (chat.receiver == myid && chat.sender == userid ||
                            chat.receiver == userid && chat.sender == myid) {
                        mChat.add(chat)
                    }
                    messageAdapter = MessageAdapter(this@MessageActivity, mChat, imageurl!!)
                    reView!!.adapter = messageAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun Status(status: String) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser!!.uid)
        val hashMap = HashMap<String, Any>()
        hashMap["status"] = status
        reference!!.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()
        Status("online")
    }

    override fun onPause() {
        if (seenListener != null && reference != null) {
            reference!!.removeEventListener(seenListener!!)
        }
        super.onPause()
        Status("offline")
    }
}