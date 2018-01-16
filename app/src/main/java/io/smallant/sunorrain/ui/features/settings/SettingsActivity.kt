package io.smallant.sunorrain.ui.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import io.smallant.sunorrain.R
import io.smallant.sunorrain.SORApplication
import io.smallant.sunorrain.extensions.replaceFragmentSafely
import io.smallant.sunorrain.tools.TrackingTools
import io.smallant.sunorrain.ui.base.BaseActivity

/**
 * Created by jpannetier on 15/01/2018.
 */
class SettingsActivity : BaseActivity() {
    private val settingsFragment: SettingsFragment by lazy { SettingsFragment() }
    private val tracking: TrackingTools by lazy { TrackingTools((application as SORApplication).getDefaultTracker()) }

    private lateinit var oldUnit: String

    companion object {
        fun create(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }

    override var layoutId = R.layout.activity_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
        tracking.hitPage("settings")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragmentSafely(fragment = settingsFragment, containerViewId = R.id.settings_container, tag = "settings_container")
        oldUnit = preferences.unitOfMeasure
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val intent = Intent()
                if (oldUnit != preferences.unitOfMeasure) {
                    setResult(RESULT_OK, intent)
                } else {
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