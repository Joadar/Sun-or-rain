package io.smallant.sunorrain.tools

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.services.common.CommonUtils
import io.smallant.sunorrain.BuildConfig

abstract class SORDeveloperTools(val application: Application) {

    abstract fun install()

    protected fun initCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder().core(CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG || CommonUtils.isEmulator(application) || CommonUtils.isRooted(application))
                .build()
        ).build()

        Fabric.with(application, crashlyticsKit)
        Crashlytics.setString("git_sha", BuildConfig.GIT_SHA)
        Crashlytics.setString("build_time", BuildConfig.BUILD_TIME)
    }
}