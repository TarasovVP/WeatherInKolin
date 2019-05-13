package com.example.weatherinkolin.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import com.example.weatherinkolin.R

class RecycleViewAdapter internal constructor(
    context: Context,
    private val temperData: List<String>,
    private val weatherData: List<Int>,
    private val timeData: List<String>
) : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.weather_view, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tem = temperData[position]
        holder.temper!!.text = tem
        val tim = timeData[position]
        holder.time!!.text = tim
        val wthIc = weatherData[position]
        holder.weatherIcon!!.setImageResource(wthIc)
    }

    override fun getItemCount(): Int {
        return NUMBER

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var temper = itemView.findViewById<TextView>(R.id.tempShowWeather)
        var time = itemView.findViewById<TextView>(R.id.timeShowWeather)
        var weatherIcon = itemView.findViewById<ImageView>(R.id.weatherIconShowWeather)

    }

    companion object {

        private val NUMBER = 5
    }

}
