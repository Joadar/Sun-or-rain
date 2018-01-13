package io.smallant.sunorrain.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.smallant.sunorrain.R
import io.smallant.sunorrain.data.models.ForecastDetail
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jpannetier on 09/01/2018.
 */
class DaysAdapter(private var items: List<ForecastDetail>) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    override fun onBindViewHolder(holder: DayViewHolder?, position: Int) {
        val weather = items[position]
        holder?.temperature?.text = "${Math.ceil(weather.temp?.day?.toDouble() ?: 0.0).toInt()}°C" // TODO : move to an Extension class
        holder?.icon?.setImageResource(weather.icon)
        holder?.date?.text = day(weather.dt)
    }

    // TODO: move to an Extension class
    private fun day(time: Long): String {
        val dayNumero = SimpleDateFormat("DD", Locale.getDefault()).format(time * 1000)
        val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(time * 1000)
        return checkDay(time, dayName, dayNumero)
    }

    private fun checkDay(time: Long, dayName: String, dayNumero: String): String {
        val epochInMillis = time * 1000
        val now = Calendar.getInstance()
        val timeToCheckToday = Calendar.getInstance()
        timeToCheckToday.timeInMillis = epochInMillis

        val timeToCheckTomorrow = Calendar.getInstance()
        timeToCheckTomorrow.add(Calendar.DAY_OF_YEAR, 1)

        return when (timeToCheckToday.get(Calendar.DAY_OF_YEAR)) {
            now.get(Calendar.DAY_OF_YEAR) -> {
                if (now.get(Calendar.HOUR_OF_DAY) > 19)
                    "Tonight"
                else
                    "Today"
            }
            timeToCheckTomorrow.get(Calendar.DAY_OF_YEAR) -> "Tomorrow"
            else -> "${dayName.capitalize()}, $dayNumero"
        }
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