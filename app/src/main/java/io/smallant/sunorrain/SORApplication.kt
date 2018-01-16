package io.smallant.sunorrain

import android.app.Application
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import io.smallant.sunorrain.data.source.WeatherRepository
import io.smallant.sunorrain.data.source.remote.RemoteDataSource
import io.smallant.sunorrain.tools.DeveloperTools
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * Created by jpannetier on 08/01/2018.
 */

class SORApplication : Application() {

    companion object {
        val repository = WeatherRepository.getInstance(RemoteDataSource.getInstance())
        private lateinit var analytics: GoogleAnalytics
        private var tracker: Tracker? = null
    }

    override fun onCreate() {
        super.onCreate()
        initCalligraphy()

        DeveloperTools(this).install()
        analytics = GoogleAnalytics.getInstance(this)
        analytics.setDryRun(BuildConfig.DEBUG)
    }

    protected fun initCalligraphy() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    @Synchronized
    fun getDefaultTracker(): Tracker {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (tracker == null) {
            tracker = analytics.newTracker(R.xml.global_tracker)
        }
        return tracker!!
    }
}