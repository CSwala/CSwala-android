package com.lotpick.lotpick.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.lotpick.lotpick.Adapters.UserAdapter
import com.lotpick.lotpick.Models.User
import com.lotpick.lotpick.R
import java.util.*

class Inbox : Fragment() {
    private var userAdapter: UserAdapter? = null


    private var mUser2: MutableList<User?>? = null
    var reference: DatabaseReference? = null
    var fUser: FirebaseUser? = null
    lateinit var search_txt: EditText
    lateinit var reView: RecyclerView
    lateinit var mUser: MutableList<User?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)
        search_txt = view.findViewById(R.id.search_users)
        search_txt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                searchUser(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        fUser = FirebaseAuth.getInstance().currentUser
       reView= view.findViewById(R.id.Rview)
        reView.setHasFixedSize(true)
        reView.setLayoutManager(LinearLayoutManager(context))
          mUser= ArrayList()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!
                    assert(firebaseUser != null)
                    try {
                        if (user.id != firebaseUser!!.uid) {
                            mUser.add(user)
                        }
                    } catch (e: Exception) {
                        Log.d("CHATLIST", "onDataChange: $e")
                    }
                }
                try {
                    userAdapter = UserAdapter(context!!, mUser, false)
                    reView.setAdapter(userAdapter)
                } catch (ignored: Exception) {
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return view
    }

    private fun searchUser(toString: String) {
        mUser2 = ArrayList()
        fUser = FirebaseAuth.getInstance().currentUser
        val query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(toString).endAt(toString + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userAdapter = if (search_txt!!.text.toString() != "") {
                    (mUser2 as ArrayList<User?>).clear()
                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)!!
                        if (user.id != fUser!!.uid) {
                            (mUser2 as ArrayList<User?>).add(user)
                        }
                    }
                    UserAdapter(context!!, mUser2 as ArrayList<User?>, false)
                } else {
                    UserAdapter(context!!, mUser, false)
                }
                reView!!.adapter = userAdapter
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
        super.onPause()
        Status("offline")
    }
}