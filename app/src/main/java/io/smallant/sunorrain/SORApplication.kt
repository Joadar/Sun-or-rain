package io.smallant.sunorrain

import android.app.Application
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
    }

    override fun onCreate() {
        super.onCreate()
        initCalligraphy()

        DeveloperTools(this).install()
    }

    protected fun initCalligraphy() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build())
    }
}