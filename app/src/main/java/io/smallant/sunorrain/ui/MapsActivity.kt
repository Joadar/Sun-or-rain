package io.smallant.sunorrain.ui

import android.annotation.TargetApi
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.smallant.sunorrain.R
import kotlinx.android.synthetic.main.activity_maps.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMaxZoomPreference(5F)
        mMap.setMinZoomPreference(5F)
        mMap.uiSettings.setAllGesturesEnabled(false)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(49.1617557, 1.771124)
        mMap.addMarker(MarkerOptions().position(sydney).title("Magny-en-Vexin")).showInfoWindow()
        mMap.setOnMarkerClickListener {
            it.showInfoWindow()
            true
        }

        val camera = LatLng(49.1617557, -4.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(camera))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                displaySearch()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @TargetApi(21)
    private fun displaySearch() {
        val menuItem = findViewById<View>(R.id.action_search)
        menuItem?.let {
            val location = IntArray(2)
            menuItem.getLocationOnScreen(location)

            val x = location[0] + menuItem.width / 4
            val y = location[1] + menuItem.height / 4

            val startRadius = 0
            val endRadius = Math.hypot(main_layout.width.toDouble(), main_layout.height.toDouble()).toInt()

            val anim = ViewAnimationUtils.createCircularReveal(layout_search, x, y, startRadius.toFloat(), endRadius.toFloat())
            anim.duration = 500

            layout_search.visibility = View.VISIBLE
            anim.start()
        }
    }
}
