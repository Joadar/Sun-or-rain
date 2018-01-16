package io.smallant.sunorrain.tools

import android.app.Application

class DeveloperTools(application: Application) : SORDeveloperTools(application) {
    override fun install() {
        initCrashlytics()
    }
}