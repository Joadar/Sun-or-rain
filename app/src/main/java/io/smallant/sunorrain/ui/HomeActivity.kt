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

    /**
     * MAP
     */
    private lateinit var map: GoogleMap

    /**
     * SEARCH
     */
    private var searchVisible: Boolean = false
    private var isSearchOpening = false
    private var isSearching: Boolean = false

    /**
     * NEXT DAYS TRANSITION
     */
    private var y = 0F
    private var dy = 0F
    private var differenceY = 0F
    private var initialY = 0F
    private var initialYSaved = false
    private var isRecyclerScrolling = false
    private var layoutNextDaysHeight = 0
    private var screenHeight: Int = 0
    private val nextDaysHeightVisible: Int by lazy { resources.getDimensionPixelSize(R.dimen.next_days_height_visible) }

    /**
     * SEARCH
     */
    private val menuItem by lazy { findViewById<View>(R.id.action_search) }

    override val layoutId: Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initMap()
        initClickListener()
        initNextDaysFragment()
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
                    if (y - event.y <= layoutNextDaysHeight) {
                        if (!initialYSaved) {
                            initialYSaved = true
                            initialY = differenceY
                        }
                        val alpha = 0.2F * ((initialY - layout_next_days.y) / (layoutNextDaysHeight))

                        layout_opacity.alpha = alpha
                        layout_next_days.y = differenceY
                    }

                    if (differenceY > initialY) {
                        layout_opacity.alpha = 0F
                        differenceY = initialY
                        layout_next_days.y = initialY
                    }

                    if (differenceY < initialY - layoutNextDaysHeight + nextDaysHeightVisible) {
                        layout_next_days.y = initialY - layoutNextDaysHeight.toFloat() + nextDaysHeightVisible
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

    override fun onBackPressed() {
        if (searchVisible)
            hideSearch()
        else
            super.onBackPressed()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setMaxZoomPreference(5F)
        map.setMinZoomPreference(5F)
        map.uiSettings.setAllGesturesEnabled(false)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(49.1617557, 1.771124)
        map.addMarker(MarkerOptions().position(sydney).title("Magny-en-Vexin")).showInfoWindow()
        map.setOnMarkerClickListener {
            it.showInfoWindow()
            true
        }

        val camera = LatLng(49.1617557, -4.0)
        map.moveCamera(CameraUpdateFactory.newLatLng(camera))
    }

    private fun initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initNextDaysFragment() {
        screenHeight = windowManager.getWindowHeight()

        layout_opacity.alpha = 0F
        layout_next_days.post {
            layoutNextDaysHeight = layout_next_days.height
        }

        replaceFragmentSafely(fragment = NextDaysFragment.create("Paris"), containerViewId = R.id.layout_next_days, allowStateLoss = true, tag = "main_container")
    }

    private fun initClickListener() {
        image_close.setOnClickListener {
            if (!isSearchOpening) {
                hideSearch()
                hideKeyboard()
            }
        }
    }

    /**
     * SEARCH A CITY
     */

    private fun onSearchWeatherClick() {
        button_search.invisible()
        progress.visible()
        isSearching = true
        button_current_location.isEnabled = !isSearching
        hideKeyboard()
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
