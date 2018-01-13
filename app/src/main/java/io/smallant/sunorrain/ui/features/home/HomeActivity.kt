package io.smallant.sunorrain.ui.features.home

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.smallant.sunorrain.R
import io.smallant.sunorrain.SORApplication.Companion.repository
import io.smallant.sunorrain.data.models.Weather
import io.smallant.sunorrain.extensions.*
import io.smallant.sunorrain.helpers.CircularRevealCompat
import io.smallant.sunorrain.helpers.SimpleAnimatorListener
import io.smallant.sunorrain.ui.base.BaseActivity
import io.smallant.sunorrain.ui.features.nextDays.NextDaysFragment
import kotlinx.android.synthetic.main.activity_home.*
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity :
        BaseActivity(),
        OnMapReadyCallback,
        LocationListener,
        HomeContract.View {

    /**
     * MAP
     */
    private lateinit var map: GoogleMap
    private var currentLocationMarker: Marker? = null

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

    /**
     * USER LOCATION
     */

    private val locationManager: LocationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10F
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0

    /**
     * OTHER
     */

    private val presenter: HomePresenter by lazy { HomePresenter(repository) }
    private var splashScreenDisplayed: Boolean = false

    override val layoutId: Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter.view = this

        if (savedInstanceState != null) {
            layout_splashscreen.gone()
        } else {
            //presenter.getWeather("Perpignan")
        }

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
    }

    override fun displayCurrentWeather(data: Weather) {
        addMarker(data.coord.lat, data.coord.lon, data.name)
        text_temperature.text = "${Math.ceil(data.main.temp).toInt()}°"
        text_humidity.text = "${data.main.humidity}"
        text_sunrise.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(data.sys.sunrise * 1000))
        text_sunset.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(data.sys.sunset * 1000))
        image_weather.setImageResource(data.icon)
        if (!splashScreenDisplayed) {
            layout_splashscreen.fadeOut()
            splashScreenDisplayed = true
        }
        hideSearch()
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
     * USER LOCATION
     */

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
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                this)
    }

    private fun addMarker(lat: Double, lon: Double, cityName: String) {
        currentLocationMarker?.let {
            it.remove()
        }
        currentLocationMarker = map.addMarker(MarkerOptions().position(LatLng(lat, lon)).title(cityName))
        currentLocationMarker?.showInfoWindow()
        map.setOnMarkerClickListener {
            it.showInfoWindow()
            true
        }
        val camera = LatLng(lat, lon - 4.75)
        map.moveCamera(CameraUpdateFactory.newLatLng(camera))
    }

    override fun onResume() {
        super.onResume()
        manageUserLocation()
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(this)
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
    override fun onLocationChanged(location: Location) {
        currentLatitude = location.latitude
        currentLongitude = location.longitude
        presenter.getWeather(location.latitude, location.longitude)
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
        presenter.getWeather(input_city.text.toString())
    }

    @TargetApi(21)
    private fun displaySearch() {
        button_search.setOnClickListener {
            onSearchWeatherClick()
        }

        button_current_location.setOnClickListener {
            presenter.getWeather(currentLatitude, currentLongitude)
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
                    onSearchClose()
                }
            })
        }
    }

    private fun onSearchClose() {
        searchVisible = false
        isSearchOpening = false
        button_search.visible()
        progress.invisible()
        isSearching = false
        button_current_location.isEnabled = !isSearching
    }
}
