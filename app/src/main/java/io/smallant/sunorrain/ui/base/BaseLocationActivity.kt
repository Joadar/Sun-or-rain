package io.smallant.sunorrain.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


abstract class BaseLocationActivity :
        BaseActivity(),
        LocationListener {

    private val locationManager: LocationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val fusedLocationClient : FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10F
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    private val PERMISSIONS_REQUEST_ACCESS_LOCATION = 101
    protected var currentLatitude: Double = 0.0
    protected var currentLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            requestLocation()
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

    @SuppressLint("MissingPermission")
    override fun onLocationChanged(location: Location) {
        currentLatitude = location.latitude
        currentLongitude = location.longitude
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissions.isEmpty())
            return

        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_LOCATION -> {
                if (grantResults.isNotEmpty()) {
                    val fineLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val coarseLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (fineLocation && coarseLocation) {
                        requestLocation()
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please grant permissions for a precise weather",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE") { permissionsRequest() }.show()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}


    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
            when {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null ->
                    onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER))
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null ->
                    onLocationChanged(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))
                else -> {
                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        if(it != null)
                            onLocationChanged(it)
                        else
                            checkGPS()
                    }

                }
            }
        } else {
            permissionsRequest()
        }
    }

    private fun permissionsRequest() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSIONS_REQUEST_ACCESS_LOCATION)
    }

    private fun checkLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsRequest()
        }
    }

    private fun checkGPS() {
        AlertDialog.Builder(this).setMessage("Your GPS seems to be disabled, do you want to enable it?")
        .setCancelable(false)
        .setPositiveButton("Yes") { p0, p1 -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        .setNegativeButton("No"){ p0, p1 -> finish()}
        .create()
        .show()
    }
}