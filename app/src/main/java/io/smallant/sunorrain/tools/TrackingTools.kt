package io.smallant.sunorrain.tools

import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker


class TrackingTools(private val tracker: Tracker) {

    fun hitPage(screenName: String) {
        tracker.setScreenName(screenName)
        tracker.send(HitBuilders
                .ScreenViewBuilder()
                .build())
        tracker.setScreenName(null)
    }

    fun hitAction(category: String, action: String, label: String = "") {
        tracker.send(HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build())
    }
}