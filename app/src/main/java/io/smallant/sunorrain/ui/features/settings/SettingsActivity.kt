package io.smallant.sunorrain.ui.features.settings

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import io.smallant.sunorrain.R
import io.smallant.sunorrain.SORApplication
import io.smallant.sunorrain.extensions.replaceFragmentSafely
import io.smallant.sunorrain.helpers.AppConstant
import io.smallant.sunorrain.tools.TrackingTools
import io.smallant.sunorrain.ui.base.BaseActivity

/**
 * Created by jpannetier on 15/01/2018.
 */
class SettingsActivity : BaseActivity() {
    private val settingsFragment: SettingsFragment by lazy { SettingsFragment() }
    private val tracking: TrackingTools by lazy { TrackingTools((application as SORApplication).getDefaultTracker()) }

    private lateinit var oldUnit: String
    private lateinit var oldTimeFormat: String

    override var layoutId = R.layout.activity_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
        tracking.hitPage("settings")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragmentSafely(fragment = settingsFragment, containerViewId = R.id.settings_container, tag = "settings_container")
        oldUnit = preferences.unitOfMeasure
        oldTimeFormat = preferences.timeFormat
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val intent = Intent()
                if (oldUnit != preferences.unitOfMeasure && oldTimeFormat != preferences.timeFormat) {
                    setResult(AppConstant.RESULT_UNIT_AND_TIME, intent)
                } else if(oldUnit != preferences.unitOfMeasure){
                    setResult(AppConstant.RESULT_UNIT_MESURE, intent)
                } else if (oldTimeFormat != preferences.timeFormat) {
                    setResult(AppConstant.RESULT_TIME_FORMAT, intent)
                }
                else {
                    setResult(RESULT_CANCELED, intent)
                }
                finish()
                overridePendingTransition(0, R.anim.slide_down)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }
    }
}