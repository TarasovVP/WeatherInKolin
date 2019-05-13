package com.example.weatherinkolin.ui


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.weatherinkolin.R
import com.example.weatherinkolin.data.JsonParser
import com.example.weatherinkolin.model.Weather
import com.example.weatherinkolin.network.HTTPGet
import org.joda.time.LocalDateTime
import java.util.*


class ShowWeatherActivity : AppCompatActivity() {

    private lateinit var userCity: String
    private val date = LocalDateTime.now()

    internal lateinit var temperatureHours: ArrayList<String>
    internal lateinit var time: ArrayList<String>
    internal lateinit var weatherIcon: ArrayList<Int>

    internal lateinit var adapter: RecycleViewAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_weather_activity)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            userCity = extras.getString("userCity")
            var id: Int = extras.getInt("id")
            val task = JSONWeatherTask()
            task.execute(id)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(R.string.menu_item)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }

    inner class JSONWeatherTask : AsyncTask<Int, Void, Weather>() {

        override fun doInBackground(vararg params: Int?): Weather? {
            var weather = Weather()
            val data = HTTPGet().getWeatherData(params[0])

            try {
                if (data == null) {
                    return null
                } else {
                    weather = JsonParser.getWeather(data)
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

            return weather

        }

        override fun onPostExecute(weather: Weather?) {
            super.onPostExecute(weather)

            val cityShow = findViewById<TextView>(R.id.cityShowWeather)
            val temperature = findViewById<TextView>(R.id.temperatureShowWeather)
            val dateTime = findViewById<TextView>(R.id.dateShowWeather)
            val weatherImage = findViewById<ImageView>(R.id.weatherShowWeather)
            if (weather != null) {
                cityShow!!.text = userCity
                dateTime!!.text = date.toString("dd MMMM yyyy \n HH:mm")
                temperature!!.text = "" + Math.round(weather.listTemp.get(0)) + "°"
                weatherImage!!.setImageResource(choiseIconWeather(weather.listIcon[0]))



                temperatureHours = ArrayList()
                setList(temperatureHours, weather)

                weatherIcon = ArrayList()
                setWeatherIcon(weatherIcon, weather)

                time = ArrayList()
                setList(time, weather)


                initAdapter()

            } else {
                val intent = Intent(applicationContext, StartActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, R.string.err_repeat, Toast.LENGTH_LONG).show()

            }

        }


    }

    fun setList(list: MutableList<String>, weather: Weather) {
        if (list == temperatureHours) {
            for (i in FIRST_COLUMN..NUMBER_OF_COLUMNS) {
                list.add((Math.round(weather.listTemp.get(i))).toString() + "°")
            }
        } else if (list === time) {
            for (i in FIRST_COLUMN..NUMBER_OF_COLUMNS) {
                list.add(weather.listTime.get(i).substring(10, 16))
            }
        }
    }


    fun setWeatherIcon(list: MutableList<Int>, weather: Weather) {
        for (i in FIRST_COLUMN..NUMBER_OF_COLUMNS) {
            list.add(choiseIconWeather(weather.listIcon.get(i)))
        }
    }

    fun initAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView)
        recyclerView.layoutManager = GridLayoutManager(baseContext, NUMBER_OF_COLUMNS)
        adapter = RecycleViewAdapter(baseContext, temperatureHours, weatherIcon, time)

        recyclerView.adapter = adapter
    }

    companion object {

        private const val NUMBER_OF_COLUMNS = 5
        private const val FIRST_COLUMN = 1
    }

    fun choiseIconWeather(getIcon: String): Int {

        var resIcon = 0
        when (getIcon) {
            "01d" -> resIcon = R.drawable.clear_sky_day
            "01n" -> resIcon = R.drawable.clear_sky_night
            "02d" -> resIcon = R.drawable.few_clouds_day
            "02n" -> resIcon = R.drawable.few_clouds_night
            "03d", "03n" -> resIcon = R.drawable.scattered_clouds_night
            "04d" -> resIcon = R.drawable.broken_clouds_day
            "04n" -> resIcon = R.drawable.broken_clouds_night
            "09d" -> resIcon = R.drawable.clear_sky_day
            "09n" -> resIcon = R.drawable.shower_rain_night
            "10d" -> resIcon = R.drawable.rain_day
            "10n" -> resIcon = R.drawable.rain_night
            "11d" -> resIcon = R.drawable.thunderstorm_day
            "11n" -> resIcon = R.drawable.thunderstorm_night
            "13d" -> resIcon = R.drawable.snow_day
            "13n" -> resIcon = R.drawable.snow_night
            "50d", "50n" -> resIcon = R.drawable.mist_day
            else -> {
            }
        }
        return resIcon

    }

}