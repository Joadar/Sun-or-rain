package io.smallant.sunorrain.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri

object AppUtils {

    /**
     * method to check if the given intent can be handle by system.
     *
     * @param context
     * @param intent
     * @return
     */
    fun canHandleIntent(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        return intent.resolveActivity(packageManager) != null
    }

    /**
     * Get the intent for the play store website.
     *
     * @param packageName
     * @return
     */
    fun getWebStoreIntent(packageName: String): Intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName))

    /**
     * launch the android market (app or website) to display application page from its package name.
     *
     * @param context
     * @param packageName
     */
    fun launchStoreIntent(context: Context?, packageName: String) {
        context?.let {
            if (canHandleIntent(it, getStoreIntent(packageName))) {
                it.startActivity(getStoreIntent(packageName))
            } else {
                it.startActivity(getWebStoreIntent(packageName))
            }
        }
    }

    /**
     * Get the intent for the play store app.
     *
     * @param packageName
     * @return
     */
    fun getStoreIntent(packageName: String): Intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName))
}