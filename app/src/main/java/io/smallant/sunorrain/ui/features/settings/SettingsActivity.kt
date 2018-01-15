package io.smallant.sunorrain.ui.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import io.smallant.sunorrain.R
import io.smallant.sunorrain.extensions.replaceFragmentSafely
import io.smallant.sunorrain.ui.base.BaseActivity

/**
 * Created by jpannetier on 15/01/2018.
 */
class SettingsActivity : BaseActivity(){
    private val settingsFragment: SettingsFragment by lazy { SettingsFragment() }

    companion object {
        fun create(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }

    override var layoutId = R.layout.activity_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragmentSafely(fragment = settingsFragment, containerViewId = R.id.settings_container, tag = "settings_container")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
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