package io.smallant.sunorrain.ui

import android.animation.Animator
import android.annotation.TargetApi
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.smallant.sunorrain.R
import io.smallant.sunorrain.extensions.*
import io.smallant.sunorrain.helpers.CircularRevealCompat
import io.smallant.sunorrain.helpers.SimpleAnimatorListener
import io.smallant.sunorrain.ui.base.BaseActivity
import io.smallant.sunorrain.ui.nextDays.NextDaysFragment
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), OnMapReadyCallback {

    private val menuItem by lazy { findViewById<View>(R.id.action_search) }

    private lateinit var mMap: GoogleMap

    private var searchVisible: Boolean = false
    private var isSearching: Boolean = false

    private var screenHeight: Int = 0

    private var y = 0F
    private var dy = 0F
    private var differenceY = 0F
    private var initialY = 0F
    private var initialYSaved = false
    private var isRecyclerScrolling = false
    private var canMoveNextDays = false

    private var isSearchOpening = false

    override val layoutId: Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        image_close.setOnClickListener {
            if (!isSearchOpening) {
                hideSearch()
                hideKeyboard()
            }
        }

        screenHeight = windowManager.getWindowHeight()


        replaceFragmentSafely(fragment = NextDaysFragment.create("Paris"), containerViewId = R.id.layout_next_days, allowStateLoss = true, tag = "main_container")
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                y = event.y
                dy = y - layout_next_days.y
                isRecyclerScrolling = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isRecyclerScrolling) {
                    differenceY = event.y - dy
                    if (screenHeight - differenceY < (layout_next_days.height + 100)) {
                        if (!initialYSaved) {
                            initialYSaved = true
                            initialY = differenceY
                        }
                        layout_next_days.y = differenceY
                    }
                    if (differenceY > initialY) {
                        differenceY = initialY
                        layout_next_days.y = initialY
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean("searchVisible", searchVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        searchVisible = savedInstanceState?.getBoolean("searchVisible") == true
        if (searchVisible) {
            layout_search.visible()
        }
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
                if (!isSearchOpening) {
                    displaySearch()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @TargetApi(21)
    private fun displaySearch() {
        button_search.setOnClickListener {
            onSearchWeatherClick()
        }

        button_current_location.setOnClickListener {
            onSearchWeatherClick()
        }

        menuItem?.let {
            CircularRevealCompat.circularReveal(layout_search, menuItem, main_layout, object : SimpleAnimatorListener() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    isSearchOpening = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    searchVisible = true
                    isSearchOpening = false
                }
            })

        }
    }

    private fun onSearchWeatherClick() {
        button_search.invisible()
        progress.visible()
        isSearching = true
        button_current_location.isEnabled = !isSearching
        hideKeyboard()
    }

    override fun onBackPressed() {
        if (searchVisible)
            hideSearch()
        else
            super.onBackPressed()
    }

    @TargetApi(21)
    private fun hideSearch() {
        menuItem?.let {
            CircularRevealCompat.circularHide(layout_search, menuItem, main_layout, object : SimpleAnimatorListener() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    isSearchOpening = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    searchVisible = false
                    isSearchOpening = false
                }
            })
        }
    }
}