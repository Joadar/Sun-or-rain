package io.smallant.sunorrain.ui.widgets

import android.content.Context
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceViewHolder
import android.util.AttributeSet
import android.widget.TextView


class MultilinesPreference : Preference {

    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int) : super(ctx, attrs, defStyle) {}

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {}

    constructor(ctx: Context) : super(ctx) {}

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder?.let {
            val textView = it.findViewById(android.R.id.title) as TextView
            textView.setSingleLine(false)
        }
    }
}