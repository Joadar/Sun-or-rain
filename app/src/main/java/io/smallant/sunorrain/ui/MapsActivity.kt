package io.smallant.sunorrain.ui

import android.animation.Animator
import android.annotation.TargetApi
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.smallant.sunorrain.R
import io.smallant.sunorrain.helpers.CircularRevealCompat
import io.smallant.sunorrain.helpers.SimpleAnimatorListener
import kotlinx.android.synthetic.main.activity_maps.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val menuItem by lazy { findViewById<View>(R.id.action_search) }

    private lateinit var mMap: GoogleMap

    private var searchVisible: Boolean = false

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

        close.setOnClickListener{
            hideSearch()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean("searchVisible", searchVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        searchVisible = savedInstanceState?.getBoolean("searchVisible") == true
        if(searchVisible) {
            layout_search.visibility = View.VISIBLE
        }
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
        menuItem?.let {
            CircularRevealCompat.circularReveal(layout_search, menuItem, main_layout, object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    searchVisible = true
                }
            })

        }
    }

    override fun onBackPressed() {
        if(searchVisible)
            hideSearch()
        else
            super.onBackPressed()
    }

    @TargetApi(21)
    private fun hideSearch() {
        menuItem?.let {
            CircularRevealCompat.circularHide(layout_search, menuItem, main_layout, object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    searchVisible = false
                }
            })
        }
    }
}
