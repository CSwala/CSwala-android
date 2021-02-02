package com.lotpick.lotpick.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lotpick.lotpick.Adapters.ListAdapter.ListViewHolder
import com.lotpick.lotpick.Models.Modelclass
import com.lotpick.lotpick.R

class ListAdapter(private val onListItemClicked: OnListItemClicked) : RecyclerView.Adapter<ListViewHolder>() {
    private var tvTitle: String? = null
    var id: String? = null
    var Listing_part: String? = null
    var No_of_MeetingRooms: String? = null
    var No_of_Seats: String? = null
    var Shared_Office: String? = null
    var openoffice: String? = null
    var wifi: String? = null
    var pantry: String? = null
    var vendingmachine: String? = null
    var printer: String? = null
    var smthngelse: String? = null
    private var tvPrice: String? = null
    private var tvLocation: String? = null
    private var tvRating: String? = null
    private var tvImage: String? = null
    private var tvDesc: String? = null
    private var tvUid: String? = null
    private var tvUserName: String? = null
    private var placeListModels: List<Modelclass>? = null
    fun setPlaceListModels(placeListModels: List<Modelclass>?) {
        this.placeListModels = placeListModels
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val location = placeListModels!![position].location
        val rating = placeListModels!![position].rating
        val price = placeListModels!![position].price
        val imageuri = placeListModels!![position].image
        var listTitle = placeListModels!![position].title
        if (listTitle.length > 20) {
            listTitle = listTitle.substring(0, 20)
        }
        holder.listTitle.text = "$listTitle..."
        holder.listLocation.text = location
        holder.listRating.text = "0.0/5.0"
        holder.listPrice.text = "Rs.  $price"
        Glide.with(holder.itemView.context).load(imageuri).centerCrop().placeholder(R.drawable.white_background).into(holder.listImage)
    }

    override fun getItemCount(): Int {
        return if (placeListModels == null) {
            0
        } else {
            placeListModels!!.size
        }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val listTitle: TextView
        val listPrice: TextView
        val listLocation: TextView
        val listRating: TextView
        var listImage: ImageView
        override fun onClick(v: View) {
            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.blink_anim)
            itemView.startAnimation(animation)
            val pos = adapterPosition
            tvTitle = placeListModels!![pos].title
            tvLocation = placeListModels!![pos].location
            tvRating = placeListModels!![pos].rating
            tvPrice = placeListModels!![pos].price
            tvDesc = placeListModels!![pos].description
            tvImage = placeListModels!![pos].image
            tvUid = placeListModels!![pos].uid
            tvUserName = placeListModels!![pos].userName
            id = placeListModels!![pos].id
            Listing_part = placeListModels!![pos].listing_part
            No_of_MeetingRooms = placeListModels!![pos].no_of_MeetingRooms
            No_of_Seats = placeListModels!![pos].no_of_Seats
            Shared_Office = placeListModels!![pos].shared_Office
            openoffice = placeListModels!![pos].openoffice
            wifi = placeListModels!![pos].wifi
            pantry = placeListModels!![pos].pantry
            vendingmachine = placeListModels!![pos].vendingmachine
            printer = placeListModels!![pos].printer
            smthngelse = placeListModels!![pos].smthgelse
            val amenities = """
                Listing part of office: $Listing_part
                No. of Meeting rooms: $No_of_MeetingRooms
                No. of Seats: $No_of_Seats
                Shared Office : $Shared_Office
                Open Office: $openoffice
                Wi-Fi: $wifi
                Pantry: $pantry
                Vending machines: $vendingmachine
                Printers: $printer
                Other details: $smthngelse
                """.trimIndent()
            onListItemClicked.onItemClicked(tvTitle, tvRating, tvPrice, tvLocation, tvDesc, tvImage, id, pos, tvUid, tvUserName, amenities)
        }

        init {
            listTitle = itemView.findViewById(R.id.list_title)
            listPrice = itemView.findViewById(R.id.list_price)
            listRating = itemView.findViewById(R.id.list_rating)
            listLocation = itemView.findViewById(R.id.list_location)
            listImage = itemView.findViewById(R.id.list_image)
            listImage = itemView.findViewById(R.id.list_image)
            itemView.setOnClickListener(this)
        }
    }

    interface OnListItemClicked {
        fun onItemClicked(title: String?, rating: String?, price: String?, location: String?, desc: String?, image: String?, Id: String?, pos: Int, Uid: String?, userName: String?, amenities: String?)
    }
}