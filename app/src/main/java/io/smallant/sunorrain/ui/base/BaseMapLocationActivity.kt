package io.smallant.sunorrain.ui.base

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

abstract class BaseMapLocationActivity :
        BaseLocationActivity(),
        OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var currentLocationMarker: Marker? = null

    abstract fun initMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setMaxZoomPreference(5F)
        map.setMinZoomPreference(5F)
        map.uiSettings.setAllGesturesEnabled(false)
    }

    protected fun addMarker(lat: Double, lon: Double, cityName: String) {
        currentLocationMarker?.remove()
        currentLocationMarker = map.addMarker(MarkerOptions().position(LatLng(lat, lon)).title(cityName))
        currentLocationMarker?.showInfoWindow()
        map.setOnMarkerClickListener {
            it.showInfoWindow()
            true
        }
        val camera = LatLng(lat, lon - 4.75)
        map.moveCamera(CameraUpdateFactory.newLatLng(camera))
    }
}