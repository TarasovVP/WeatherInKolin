package com.example.weatherinkolin.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast

import java.util.ArrayList
import java.util.HashMap

import butterknife.ButterKnife
import com.example.weatherinkolin.R
import com.example.weatherinkolin.data.JsonParser
import com.example.weatherinkolin.model.Location
import com.example.weatherinkolin.network.HTTPGet
import com.neovisionaries.i18n.CountryCode
import java.lang.Exception

class StartActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var countries: List<String>
    internal lateinit var setCities: HashMap<String, Int>
    internal var idCity: Int? = null
    internal lateinit var city: String
    internal lateinit var country: String
    internal lateinit var countryName: String
    internal lateinit var cityName: String
    internal lateinit var userCity: String

    internal lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        val ok = findViewById<Button>(R.id.buttonOkStart)

        ok.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        val editText = findViewById<EditText>(R.id.editTextStart)
        city = editText.text.toString()
        if (!city.isEmpty() && city != null) {
            val task = JSONLocationTask()
            task.execute(city) //To change body of created functions use File | Settings | File Templates.
        }
    }
     inner class JSONLocationTask : AsyncTask<String, Void, Location>() {


        override fun doInBackground(vararg params: String): Location? {
            var location = Location()


            val loc = HTTPGet().getLocationData(params[0])

            try {
                if (loc == null) {
                    return null
                } else {
                    location = JsonParser.getLocation(loc)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return location

        }

        override fun onPostExecute(location: Location?) {
            super.onPostExecute(location)

            if (location == null) {
                Toast.makeText(applicationContext, R.string.err_repeat, Toast.LENGTH_LONG).show()
            } else {
                if (location.count == 0) {
                    Toast.makeText(applicationContext, R.string.err_nothing_found, Toast.LENGTH_LONG).show()
                } else {
                    setCities = HashMap()
                    setHash(setCities, location)
                    countries = ArrayList(setCities.keys)

                    val list = findViewById<ListView>(R.id.listCitiesStart)

                    list.choiceMode = AbsListView.CHOICE_MODE_SINGLE
                    adapter = ArrayAdapter(baseContext, R.layout.cities_list, countries)
                    list.adapter = adapter
                    list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                        city = location.listCities.get(0)
                        country = countries[position]
                        idCity = setCities[country]

                        val intent = Intent(baseContext, ShowWeatherActivity::class.java)
                        val extras = Bundle()
                        userCity = "$city, $country"
                        extras.putInt("id", idCity!!)
                        extras.putString("userCity", userCity)
                        intent.putExtras(extras)
                        startActivity(intent)
                    }
                }
            }
        }


        internal fun setHash(hashList: HashMap<String, Int>, location: Location) {
            cityName = location.listCities.get(0)
            val size = location.count
            for (i in 0 until size) {
                countryName = CountryCode.getByCode(location.listCountries.get(i)).getName()
                hashList[countryName] = location.listId.get(i)
            }


        }

    }


}



