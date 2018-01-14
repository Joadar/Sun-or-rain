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
import io.smallant.sunorrain.extensions.replaceFragmentSafely
import io.smallant.sunorrain.extensions.toast
import io.smallant.sunorrain.helpers.AppUtils
import io.smallant.sunorrain.ui.base.BaseActivity

/**
 * Created by jpannetier on 26/04/2017.
 */
class AboutActivity : BaseActivity() {

    companion object {
        fun create(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    override var layoutId = R.layout.activity_about

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        replaceFragmentSafely(fragment = AboutActivity.AboutFragment(), containerViewId = R.id.about_container, tag = "about_container")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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
                true
            }

            val contact = findPreference(getString(R.string.key_about_contact))
            contact.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val mIntent = Intent(Intent.ACTION_SENDTO)
                mIntent.data = Uri.parse("mailto:")
                mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context?.getString(R.string.contact_default_email)))
                mIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_email_title))
                startActivity(Intent.createChooser(mIntent, getString(R.string.send_email_with)))
                true
            }

            val version = findPreference(getString(R.string.key_preferences_version))
            version.title = getString(R.string.title_preferences_version, BuildConfig.VERSION_NAME)
            var nbClick = 0
            version.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                if (nbClick == 5) {
                    context?.toast(getString(R.string.no_easter_egg))
                }
                nbClick++
                true
            }
        }
    }
}