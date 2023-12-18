package com.openclassrooms.realestatemanager.utils

public interface NetworkStateListener {
    fun onNetworkAvailable()
    fun onNetworkUnavailable()
}
