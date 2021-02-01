package com.lotpick.lotpick.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lotpick.lotpick.Models.Modelclass
import com.lotpick.lotpick.ViewModels.FirebaseRepository.OnFirestoreTaskComplete

class ListViewModel : ViewModel(), OnFirestoreTaskComplete {
    private val placeListModelData = MutableLiveData<List<Modelclass>>()
    val placesListModelData: LiveData<List<Modelclass>>
        get() = placeListModelData

    override fun placesListDataAdded(placeListModelsList: List<Modelclass>) {
        placeListModelData.value = placeListModelsList
    }

    override fun onError(e: Exception) {}

    init {
        val firebaseRepository = FirebaseRepository(this)
        firebaseRepository.getPlaceData()
    }
}