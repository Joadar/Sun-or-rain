package io.smallant.sunorrain.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

abstract class BaseLocationActivity :
        BaseActivity(),
        LocationListener {

    private val locationManager: LocationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10F
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101
    protected var currentLatitude: Double = 0.0
    protected var currentLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            manageUserLocation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putDouble("currentLatitude", currentLatitude)
        outState?.putDouble("currentLongitude", currentLongitude)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        currentLatitude = savedInstanceState?.getDouble("currentLatitude", 0.0) ?: 0.0
        currentLongitude = savedInstanceState?.getDouble("currentLongitude", 0.0) ?: 0.0
    }

    override fun onLocationChanged(location: Location) {
        currentLatitude = location.latitude
        currentLongitude = location.longitude
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissions.isEmpty())
            return

        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty()) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Denied
                        finish()
                    } else {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            requestLocation()
                        } else {
                            // Bob never checked click
                        }
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}

    private fun manageUserLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        } else {
            requestLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        /*locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                this)*/
        onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER))
    }
}