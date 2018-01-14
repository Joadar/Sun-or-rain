package io.smallant.sunorrain.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.smallant.sunorrain.R
import io.smallant.sunorrain.data.models.ForecastDetail
import io.smallant.sunorrain.extensions.toCeil
import io.smallant.sunorrain.extensions.toDay

/**
 * Created by jpannetier on 09/01/2018.
 */
class DaysAdapter(private var items: List<ForecastDetail>, private val context: Context?) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    override fun onBindViewHolder(holder: DayViewHolder?, position: Int) {
        val weather = items[position]
        holder?.temperature?.text = context?.resources?.getString(R.string.temperature, weather.temp?.day?.toCeil, "C")
        holder?.icon?.setImageResource(weather.icon)
        holder?.date?.text = weather.dt.toDay
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
        val icon: ImageView by lazy { view.findViewById<ImageView>(R.id.image_weather) }
        val date: TextView by lazy { view.findViewById<TextView>(R.id.text_date) }
    }
}