package io.smallant.sunorrain.data.source.local

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import io.smallant.sunorrain.R

/**
 * Created by jpannetier on 15/01/2018.
 */
class PreferencesController(val context: Context) {
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val unitMeasureKey: String = context.getString(R.string.key_unit_measure)
    private val timeFormatKey: String = context.getString(R.string.key_time_format)

    companion object {
        private val APP_FIRST_LAUNCH = "app.first_launch"
    }

    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean(APP_FIRST_LAUNCH, true)
        set(value) = sharedPreferences.edit().putBoolean(APP_FIRST_LAUNCH, value).apply()

    var unitOfMeasure: String
        get() = sharedPreferences.getString(unitMeasureKey, context.getString(R.string.unit_measure_default))
        set(value) = sharedPreferences.edit().putString(unitMeasureKey, value).apply()

    var timeFormat: String
        get() = sharedPreferences.getString(timeFormatKey, context.getString(R.string.time_format_default))
        set(value) = sharedPreferences.edit().putString(timeFormatKey, value).apply()

}