package com.decagonhq.clads.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decagonhq.clads.data.domain.profile.ArtisanAddress
import kotlinx.coroutines.cancel

class ArtisanLocationViewModel: ViewModel() {
    private var _artisanLocation = MutableLiveData<ArtisanAddress> ()
    val artisanLocation: LiveData<ArtisanAddress> get() = _artisanLocation

    fun setArtisanAddress(address: ArtisanAddress){
        _artisanLocation.value = address
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}