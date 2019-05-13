package com.example.weatherinkolin.model

import java.util.ArrayList

data class Location(var listCities: ArrayList<String> = ArrayList(), var listCountries: ArrayList<String> = ArrayList(), var listId: ArrayList<Int> = ArrayList(), var count: Int = Int.MIN_VALUE) {
}