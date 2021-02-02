package com.lotpick.lotpick.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lotpick.lotpick.Activity.MessageActivity
import com.lotpick.lotpick.Models.Chat
import com.lotpick.lotpick.Models.User
import com.lotpick.lotpick.R

class UserAdapter(private val mContext: Context, private val mUser: MutableList<User?>, private val isChat: Boolean) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    var theLastMessage: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val user = mUser[position]
            holder.username.text = user?.username
            if (user?.imageURL == "") {
                holder.pro_img.setImageResource(R.drawable.addprofile)
            } else {
                Glide.with(mContext).load(user?.imageURL).into(holder.pro_img)
            }
            if (isChat) {
                user?.id?.let { lastMessage(it, holder.last_mess) }
            } else {
                holder.last_mess.visibility = View.GONE
            }
            if (isChat) {
                if (user?.status != null && user.status == "online") {
                    holder.img_on.visibility = View.VISIBLE
                    holder.img_off.visibility = View.GONE
                } else {
                    holder.img_on.visibility = View.GONE
                    holder.img_off.visibility = View.VISIBLE
                }
            } else {
                holder.img_on.visibility = View.GONE
                holder.img_off.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(mContext, MessageActivity::class.java)
                intent.putExtra("userid", user?.id)
                mContext.startActivity(intent)
            }
        } catch (ignored: NullPointerException) {
        }
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.username)
        var pro_img: ImageView
        val img_on: ImageView
        val img_off: ImageView
        val last_mess: TextView

        init {
            pro_img = itemView.findViewById(R.id.profile_image)
            img_on = itemView.findViewById(R.id.img_on)
            img_off = itemView.findViewById(R.id.img_off)
            last_mess = itemView.findViewById(R.id.last_msg)
        }
    }

    private fun lastMessage(userid: String, last_msg: TextView) {
        theLastMessage = "default"
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if (firebaseUser != null && chat != null) {
                        if (chat.receiver == firebaseUser.uid && chat.sender == userid ||
                                chat.receiver == userid && chat.sender == firebaseUser.uid) {
                            theLastMessage = chat.message
                        }
                    }
                }
                if ("default" == theLastMessage) {
                    last_msg.text = "No Message"
                } else {
                    last_msg.text = theLastMessage
                }
                theLastMessage = "default"
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}