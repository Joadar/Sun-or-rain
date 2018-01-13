package io.smallant.sunorrain.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.smallant.sunorrain.R
import io.smallant.sunorrain.data.models.ForecastDetail

/**
 * Created by jpannetier on 09/01/2018.
 */
class DaysAdapter(private var items: List<ForecastDetail>) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    override fun onBindViewHolder(holder: DayViewHolder?, position: Int) {
        val weather = items[position]
        holder?.temperature?.text = "${Math.ceil(weather.temp?.day?.toDouble() ?: 0.0).toInt()}°C"
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    fun add(items: List<ForecastDetail>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val temperature: TextView by lazy { view.findViewById<TextView>(R.id.text_temperature) }
    }
}