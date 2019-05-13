package com.example.weatherinkolin.network

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HTTPGet {

    private fun httpget(data: String): String? {
        var con: HttpURLConnection? = null
        var buffer: StringBuffer? = null
        try {
            con = URL(data).openConnection() as HttpURLConnection
            con.connect()

            try {
                con.inputStream.use { `is` ->
                    buffer = StringBuffer()
                    val br = BufferedReader(InputStreamReader(`is`))
                    var line: String
                    while (true) {
                        line = br.readLine() ?: break
                        buffer!!.append(line).append("\r\n")
                    }

                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            con?.disconnect()
        }

        return if (buffer != null) buffer!!.toString() else null

    }

    fun getLocationData(location: String): String? {
        return httpget(FIND_URL + location + PARAM + KEY)
    }

    fun getWeatherData(request: Int?): String? {
        return httpget(BASE_URL + request + KEY)
    }

    companion object {
        private val KEY = "&appid=b6907d289e10d714a6e88b30761fae22"
        private val FIND_URL = "https://openweathermap.org//data/2.5/find?callback=?&q="
        private val PARAM = "&type=like&sort=population&cnt=30"
        private val BASE_URL = "http://openweathermap.org/data/2.5/forecast?q="
    }
}
