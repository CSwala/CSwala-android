package com.lotpick.lotpick.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lotpick.lotpick.Adapters.ListAdapter
import com.lotpick.lotpick.Adapters.ListAdapter.OnListItemClicked
import com.lotpick.lotpick.ViewModels.FavListViewModel
import com.lotpick.lotpick.R
import com.lotpick.lotpick.Activity.place_description

class Saved : Fragment(), OnListItemClicked {
    lateinit var listView: RecyclerView
    private var adapter: ListAdapter? = null
    private var fadeInAnim: Animation? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.Fav_Firestore_list)
        adapter = ListAdapter(this)
        listView.setLayoutManager(LinearLayoutManager(context))
        listView.setHasFixedSize(true)
        listView.setAdapter(adapter)
        fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val placeListViewModel = ViewModelProvider(requireActivity()).get(FavListViewModel::class.java)
        placeListViewModel.placesListModelData.observe(viewLifecycleOwner, { placeListModels ->
            listView!!.startAnimation(fadeInAnim)
            adapter!!.setPlaceListModels(placeListModels)
            adapter!!.notifyDataSetChanged()
        })
    }

    override fun onItemClicked(title: String?, rating: String?, price: String?, location: String?, desc: String?, image: String?, id: String?, pos: Int, Uid: String?, userName: String?, amenities: String?) {
        val intent = Intent(activity, place_description::class.java)
        intent.putExtra("Title", title)
        intent.putExtra("Rating", rating)
        intent.putExtra("Price", price)
        intent.putExtra("Location", location)
        intent.putExtra("Desc", desc)
        intent.putExtra("Image", image)
        intent.putExtra("Id", id)
        intent.putExtra("Pos", pos)
        intent.putExtra("Uid", Uid)
        intent.putExtra("username", userName)
        intent.putExtra("Amenities", amenities)
        startActivity(intent)
    }
}