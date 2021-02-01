package com.lotpick.lotpick.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lotpick.lotpick.Models.Chat
import com.lotpick.lotpick.R

class MessageAdapter(private val mContext: Context, private val mChat: List<Chat>, private val imageURL: String) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    var fUser: FirebaseUser? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MSG_TYPE_RIGHT) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false)
            ViewHolder(view)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = mChat[position]
        holder.show_message.text = chat.message
        if (imageURL == "") {
            holder.pro_img.setImageResource(R.drawable.addprofile)
        } else {
            Glide.with(mContext).load(imageURL).placeholder(R.drawable.addprofile).into(holder.pro_img)
        }
        if (position == mChat.size - 1) {
            if (chat.isseen) {
                holder.txt_seen.text = "✔✔"
                holder.txt_seen.setTextColor(Color.parseColor("#0565AE"))
            } else {
                holder.txt_seen.text = "✔"
            }
        } else {
            holder.txt_seen.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var show_message: TextView = itemView.findViewById(R.id.show_message)
        var pro_img: ImageView = itemView.findViewById(R.id.profile_image)
        var txt_seen: TextView = itemView.findViewById(R.id.txt_seen)

    }

    override fun getItemViewType(position: Int): Int {
        fUser = FirebaseAuth.getInstance().currentUser
        return if (mChat[position].sender == fUser!!.uid) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    companion object {
        const val MSG_TYPE_LEFT = 0
        const val MSG_TYPE_RIGHT = 1
    }
}