package io.smallant.sunorrain.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.smallant.sunorrain.data.source.local.PreferencesController
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class BaseActivity: AppCompatActivity() {

    protected val preferences: PreferencesController by lazy { PreferencesController(this) }

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}