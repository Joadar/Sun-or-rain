package io.smallant.sunorrain.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.smallant.sunorrain.data.source.local.PreferencesController

/**
 * Created by jpannetier on 10/01/2018.
 */
abstract class BaseFragment: Fragment() {

    protected val preferences: PreferencesController by lazy { PreferencesController(context!!) }

    abstract var layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutId != 0) {
            return inflater.inflate(layoutId, container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}