package io.smallant.sunorrain.ui

import android.animation.Animator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.smallant.sunorrain.R
import io.smallant.sunorrain.adapters.DaysAdapter
import io.smallant.sunorrain.helpers.CircularRevealCompat
import io.smallant.sunorrain.helpers.SimpleAnimatorListener
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.view_search.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper




class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        getWindowHeight()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        close.setOnClickListener{
            hideSearch()
            hideKeyboard()
        }

        initRecycler()

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                y = event.y
                dy = y - next_days.y
                isRecyclerScrolling = false
            }
            MotionEvent.ACTION_MOVE -> {
                if(!isRecyclerScrolling) {

                    differenceY = event.y - dy
                    if (screenHeight - differenceY < (next_days.height + 100)) {
                        if (!initialYSaved) {
                            initialYSaved = true
                            initialY = differenceY
                        }
                        next_days.y = differenceY
                    }
                    if (differenceY > initialY) {
                        differenceY = initialY
                        next_days.y = initialY
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
        button_search.setOnClickListener {
            onSearchWeatherClick()
        }

        button_current_location.setOnClickListener{
            onSearchWeatherClick()
        }

        menuItem?.let {
            CircularRevealCompat.circularReveal(layout_search, menuItem, main_layout, object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    searchVisible = true
                }
            })

        }
    }

    private fun onSearchWeatherClick(){
        button_search.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE
        isSearching = true
        button_current_location.isEnabled = !isSearching
        hideKeyboard()
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

    private fun hideKeyboard() {
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getWindowHeight() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenHeight = size.y
    }

    private fun initRecycler() {
        recycler_next_days.apply {
            setHasFixedSize(true)
            addOnItemTouchListener(object: RecyclerView.OnItemTouchListener{
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    isRecyclerScrolling = true
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                }
            })
            adapter = DaysAdapter(arrayListOf("9°C", "1°C", "16°C", "15°C", "21°C"))
        }
    }
}
