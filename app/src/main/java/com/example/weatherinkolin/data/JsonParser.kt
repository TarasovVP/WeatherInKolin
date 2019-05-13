package com.example.weatherinkolin.data

import com.example.weatherinkolin.model.Location
import com.example.weatherinkolin.model.Weather
import org.json.JSONException
import org.json.JSONObject

object JsonParser {

    @Throws(JSONException::class)
    fun getWeather(data: String): Weather {
        val weather = Weather()
        val jObj = JSONObject(data)

        val jArr = jObj.getJSONArray("list")
        for (i in 0..5) {
            val mWeather = jArr.getJSONObject(i)

            val jArrWeath = mWeather.getJSONArray("weather")
            val idWeather = jArrWeath.getJSONObject(0)
            weather.listIcon.add(getString("icon", idWeather))

            val mainObj = mWeather.getJSONObject("main")
            weather.listTemp.add(getDouble("temp", mainObj))

            weather.listTime.add(getString("dt_txt", mWeather))
        }


        return weather
    }

    @Throws(Exception::class)
    fun getLocation(data: String): Location {
        val location = Location()
        val jObjList = JSONObject(data.substring(2, data.length - 1))

        location.count = jObjList.getInt("count")

        if (location.count > 0) {
            val jArrLoc = jObjList.getJSONArray("list")

            for (i in 0 until location.count) {
                val citiesList = jArrLoc.getJSONObject(i)

                location.listCities.add(getString("name", citiesList))
                val id = citiesList.getInt("id")
                location.listId.add(id)

                val country = citiesList.getJSONObject("sys")

                location.listCountries.add(getString("country", country))

            }


        }

        return location
    }


    @Throws(JSONException::class)
    private fun getString(tagName: String, jObj: JSONObject): String {
        return jObj.getString(tagName)
    }
    @Throws(JSONException::class)
    private fun getDouble(tagName: String, jObj: JSONObject): Float {
        return jObj.getDouble(tagName).toFloat()
    }

}
