package io.smallant.sunorrain.ui.features.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import io.smallant.sunorrain.BuildConfig
import io.smallant.sunorrain.R
import io.smallant.sunorrain.SORApplication
import io.smallant.sunorrain.extensions.replaceFragmentSafely
import io.smallant.sunorrain.extensions.toast
import io.smallant.sunorrain.helpers.AppUtils
import io.smallant.sunorrain.tools.TrackingTools
import io.smallant.sunorrain.ui.base.BaseActivity

/**
 * Created by jpannetier on 26/04/2017.
 */
class AboutActivity : BaseActivity() {
    private val tracking: TrackingTools by lazy { TrackingTools((application as SORApplication).getDefaultTracker()) }

    companion object {
        fun create(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    override var layoutId = R.layout.activity_about

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
        tracking.hitPage("about")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        replaceFragmentSafely(fragment = AboutActivity.AboutFragment(), containerViewId = R.id.about_container, tag = "about_container")
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.anim.slide_down)
    }

    class AboutFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.about)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val rate = findPreference(getString(R.string.key_preferences_rate))
            rate.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                AppUtils.launchStoreIntent(context, getString(R.string.package_name))
                (activity as AboutActivity).tracking.hitAction("about", "click", "store")
                true
            }

            val contact = findPreference(getString(R.string.key_about_contact))
            contact.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val mIntent = Intent(Intent.ACTION_SENDTO)
                mIntent.data = Uri.parse("mailto:")
                mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context?.getString(R.string.contact_default_email)))
                mIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_email_title))
                startActivity(Intent.createChooser(mIntent, getString(R.string.send_email_with)))
                (activity as AboutActivity).tracking.hitAction("about", "click", "contact")
                true
            }

            val version = findPreference(getString(R.string.key_preferences_version))
            version.title = getString(R.string.title_preferences_version, BuildConfig.VERSION_NAME)
            var nbClick = 0
            version.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                if (nbClick == 5) {
                    context?.toast(getString(R.string.no_easter_egg))
                    (activity as AboutActivity).tracking.hitAction("about", "click", "easter-egg")
                }
                nbClick++
                true
            }
        }
    }
}