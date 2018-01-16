package io.smallant.sunorrain.ui.features.home

import android.animation.Animator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.android.gms.maps.SupportMapFragment
import io.smallant.sunorrain.R
import io.smallant.sunorrain.SORApplication.Companion.repository
import io.smallant.sunorrain.data.models.Weather
import io.smallant.sunorrain.extensions.*
import io.smallant.sunorrain.helpers.AppConstant
import io.smallant.sunorrain.helpers.CircularRevealCompat
import io.smallant.sunorrain.helpers.JsonController
import io.smallant.sunorrain.helpers.SimpleAnimatorListener
import io.smallant.sunorrain.ui.base.BaseMapLocationActivity
import io.smallant.sunorrain.ui.features.about.AboutActivity
import io.smallant.sunorrain.ui.features.nextDays.NextDaysFragment
import io.smallant.sunorrain.ui.features.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


@SuppressLint("ShowToast")
class HomeActivity :
        BaseMapLocationActivity(),
        HomeContract.View {

    /**
     * SEARCH
     */
    private var searchVisible: Boolean = false
    private var isSearchOpening = false
    private var isSearching: Boolean = false
    private var searchItem: MenuItem? = null

    /**
     * NEXT DAYS TRANSITION
     */
    private var y = 0F
    private var dy = 0F
    private var differenceY = 0F
    private var initialY = 0F
    private var initialYSaved = false
    private var layoutNextDaysHeight = 0
    private val nextDaysHeightVisible: Int by lazy { resources.getDimensionPixelSize(R.dimen.next_days_height_visible) }

    /**
     * OTHER
     */

    private val presenter: HomePresenter by lazy { HomePresenter(repository) }
    private var splashScreenDisplayed: Boolean = false
    private var toastError: Toast? = null
    private val jsonController: JsonController by lazy { JsonController(this) }
    private var currentWeather: Weather? = null

    override val layoutId: Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter.view = this

        if (savedInstanceState != null) {
            layout_splashscreen.gone()
            splashScreenDisplayed = true
        } else {
            initNextDaysFragment()
        }
        layout_opacity.alpha = 0F
        layout_next_days.post {
            layoutNextDaysHeight = layout_next_days.height
        }
        initClickListener()

        manageSearchActions()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (splashScreenDisplayed) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    y = event.y
                    dy = y - layout_next_days.y
                }
                MotionEvent.ACTION_MOVE -> {
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
        outState?.putSerializable("currentWeather", currentWeather)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        searchVisible = savedInstanceState?.getBoolean("searchVisible") == true
        if (searchVisible) {
            layout_search.visible()
        }
        currentWeather = savedInstanceState?.getSerializable("currentWeather") as Weather
        currentWeather?.let {
            displayWeatherInfos(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        searchItem = menu?.findItem(R.id.action_search)
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
            R.id.action_about -> {
                AboutActivity.create(this)
                return true
            }
            R.id.action_settings -> {
                startActivityForResult(Intent(this, SettingsActivity::class.java), AppConstant.REQUEST_CODE_SETTINGS)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQUEST_CODE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                currentWeather?.let {
                    changeTemperatureSymbol()
                    it.main.temp = if (preferences.unitOfMeasure == getString(R.string.imperial))
                        it.main.temp.convertCelciusToFahrenheit()
                    else
                        it.main.temp.convertFahrenheitToCelcius()

                    displayWeatherInfos(it)
                    val nextDaysFragment = supportFragmentManager.findFragmentById(R.id.layout_next_days)
                    if (nextDaysFragment is NextDaysFragment) {
                        nextDaysFragment.updateTemperature()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (searchVisible)
            hideSearch()
        else
            super.onBackPressed()
    }

    override fun displayCurrentWeather(data: Weather) {
        displayWeatherInfos(data)
        mapIsReady()

        if (!splashScreenDisplayed) {
            layout_splashscreen.fadeOut({ splashScreenDisplayed = true })
        }
        replaceFragmentSafely(fragment = NextDaysFragment.create(data.coord.lat, data.coord.lon), containerViewId = R.id.layout_next_days, allowStateLoss = true, tag = "main_container")
        hideSearch()
    }

    override fun mapIsReady() {
        currentWeather?.let {
            addMarker(it.coord.lat, it.coord.lon, it.name)
        }
    }

    private fun displayWeatherInfos(data: Weather) {
        currentWeather = data
        val timeZone = jsonController.getTimeZone(data.sys.country)
        text_time.text = (Calendar.getInstance().timeInMillis / 1000).getHoursMinutes(timeZone)
        text_temperature.text = getString(R.string.temperature, data.main.temp.toCeil, "")
        text_humidity.text = getString(R.string.humidity, data.main.humidity.toInt())
        text_sunrise.text = data.sys.sunrise.getHoursMinutes(timeZone)
        text_sunset.text = data.sys.sunset.getHoursMinutes(timeZone)
        image_weather.setImageResource(data.icon)
        changeTemperatureSymbol()
    }

    private fun changeTemperatureSymbol() {
        text_temperature_symbol.text =
                if (preferences.unitOfMeasure == getString(R.string.imperial))
                    getString(R.string.temperature_imperial)
                else
                    getString(R.string.temperature_metrics)
    }

    override fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initNextDaysFragment() {
        replaceFragmentSafely(fragment = NextDaysFragment.create("Miami"), containerViewId = R.id.layout_next_days, allowStateLoss = true, tag = "main_container")
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
    override fun onLocationChanged(location: Location) {
        super.onLocationChanged(location)
        presenter.getWeather(location.latitude, location.longitude, preferences.unitOfMeasure)
    }

    /**
     * SEARCH A CITY
     */

    override fun displaySearchError() {
        displayToast("Impossible to find this city, please try another one")
        resultSearching()
    }

    private fun onSearchWeatherClick() {
        if (!input_city.text.isNullOrEmpty()) {
            presenter.getWeather(input_city.text.toString(), preferences.unitOfMeasure)
            viewActionsOnSearchWeatherClicked()
        } else {
            displayToast("Enter a city before clicking on search button :)")
        }
    }

    private fun viewActionsOnSearchWeatherClicked() {
        hideKeyboard()
        isSearching = true
        button_current_location.isEnabled = !isSearching
        button_search.invisible()
        progress.visible()
    }

    private fun manageSearchActions() {
        button_search.setOnClickListener {
            onSearchWeatherClick()
        }

        button_current_location.setOnClickListener {
            presenter.getWeather(currentLatitude, currentLongitude, preferences.unitOfMeasure)
            viewActionsOnSearchWeatherClicked()
        }

        input_city.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSearchWeatherClick()
                true
            } else {
                false
            }
        }
    }

    @TargetApi(21)
    private fun displaySearch() {
        searchItem?.let {
            CircularRevealCompat.circularReveal(layout_search, findViewById<View>(R.id.action_search), main_layout, object : SimpleAnimatorListener() {
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
        searchItem?.let {
            CircularRevealCompat.circularHide(layout_search, findViewById<View>(R.id.action_search), main_layout, object : SimpleAnimatorListener() {
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
        resultSearching()
    }

    private fun resultSearching() {
        button_search.visible()
        progress.invisible()
        isSearching = false
        button_current_location.isEnabled = !isSearching
    }

    private fun displayToast(message: String) {
        toastError?.cancel()
        toastError = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toastError?.show()
    }
}
