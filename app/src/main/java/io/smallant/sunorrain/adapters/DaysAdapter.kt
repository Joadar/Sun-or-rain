package io.smallant.sunorrain.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.smallant.sunorrain.R

/**
 * Created by jpannetier on 09/01/2018.
 */
class DaysAdapter(private val items: ArrayList<String>) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    override fun onBindViewHolder(holder: DayViewHolder?, position: Int) {
        val text = items[position]
        holder?.temperature?.text = text
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    fun add(item: String, position: Int) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun remove(item: String) {
        val position = items.indexOf(item)
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val temperature: TextView by lazy { view.findViewById<TextView>(R.id.text_temperature) }
    }
}