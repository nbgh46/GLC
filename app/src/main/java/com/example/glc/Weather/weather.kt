package com.example.glc.Weather

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

/*
    날씨 클래스
    -OpenWeatherMap API 에서 받아올 데이터를 담을 클래스 정의
    -JSON 방식 데이터를 받을수있도록 정의
    날씨 url
    현재날씨:
    http://api.openweathermap.org/data/2.5/weather?lat=37.450800&lon=127.128814&appid=a882ca5cc46bf7c6888a7fb89d16f02e
    3시간 날씨
    http://api.openweathermap.org/data/2.5/forecast?lat=37.450800&lon=127.128814&appid=a882ca5cc46bf7c6888a7fb89d16f02e

 */


class weatherRepo{
    @SerializedName("weather")
    var weatherList : ArrayList<Weather>? =ArrayList<Weather>()
    @SerializedName("main")
    val main :Main? = null
    @SerializedName("sys")
    val sys :Sys? = null
    @SerializedName("coord")
    val coord :Coord? = null
    @SerializedName("clouds")
    val clouds :Clouds? = null
    @SerializedName("wind")
    val wind :Wind? = null

    class Coord{

        @SerializedName("lon")
        val lon :String = ""
        @SerializedName("lat")
        val lat :String = ""
    }

    class Weather{

        @SerializedName("id")
        val id :String = ""
        @SerializedName("main")
        val main :String = ""
        @SerializedName("description")
        val description :String = ""
        @SerializedName("icon")
        val icon :String = ""

    }
    @SerializedName("base")
    val base :String = ""
    class Main{
        @SerializedName("temp")
        val temp :String = ""
        @SerializedName("pressure")
        val pressure :String = ""
        @SerializedName("humidity")
        val humidity :String = ""
        @SerializedName("temp_min")
        val temp_min :String = ""
        @SerializedName("temp_max")
        val temp_max :String = ""

    }
    class Clouds{
        @SerializedName("all")
        val all :String = ""
    }

    class Sys{
        @SerializedName("type")
        val type :String = ""
        @SerializedName("id")
        val id :String = ""
        @SerializedName("message")
        val message :String = ""
        @SerializedName("country")
        val country :String = ""
        @SerializedName("sunrise")
        val sunrise :String = ""
        @SerializedName("sunset")
        val sunset :String = ""

    }

    class Wind{
        @SerializedName("speed")
        val speed :String = ""
        @SerializedName("deg")
        val deg :String = ""
        @SerializedName("gust")
        val gust :String = ""
    }

    @SerializedName("id")
    val id :String = ""
    @SerializedName("name")
    val name :String = ""

    @SerializedName("dt")
    val dt :String = ""
    @SerializedName("cod")
    val cod :String = ""

}