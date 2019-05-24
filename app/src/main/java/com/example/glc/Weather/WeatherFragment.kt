package com.example.glc.Weather


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.glc.R
import kotlinx.android.synthetic.main.fragment_weather.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit





// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.glc.R.layout.fragment_weather, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWeather()




    }


    companion object {
        fun newInstance(): WeatherFragment {
            val WeatherFragment = WeatherFragment()
            return WeatherFragment
        }
    }

    interface WeatherService{
        @GET(".")
        fun setweather(
        @Query("lat")lat:String,
        @Query("lon")lon:String,
        @Query("appid")appid:String
        ):Call<weatherRepo>
    }

    fun loadWeather(){

        try{
            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES) // write timeout
                .readTimeout(10, TimeUnit.MINUTES) // read timeout
                .build()

            val Weatherapi = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/weather/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val WeatherService = Weatherapi.create(WeatherService::class.java)
            val WeatherCall = WeatherService.setweather("37.450800","127.128814","a882ca5cc46bf7c6888a7fb89d16f02e")

            WeatherCall.enqueue(object : Callback<weatherRepo>{

                override fun onResponse(call: Call<weatherRepo>, response: Response<weatherRepo>) {
                    val currentWeather = response.body()?.weatherList     // 받아온 json 객체 생성
                  //  Toast.makeText(context,"날씨 연결 성공",Toast.LENGTH_LONG).show()
                    weather_icon.setIconColor(Color.BLACK)
                    weather_icon.setIconSize(100)
                    setWeatherIcon(response.body()?.weatherList?.get(0)!!.icon)
                    weather_degree.text = (((response.body()?.main?.temp)!!.toFloat().toInt()) - 273 ).toString()+ "ºc"//온도 매핑
                    weather_locatrion.text = response.body()?.name
                    weather_main.text = currentWeather?.get(0)?.main
                    weather_degreeMinMax.text = (response.body()?.main?.temp_min!!.toFloat().toInt()-273).toString()+ "ºc" + "/" + (response.body()?.main?.temp_max!!.toFloat().toInt()-273).toString()+ "ºc"

                    //밑에꺼 매핑
                    weather_pressuret.text = response.body()?.main?.pressure
                    weather_windy_tv.text = response.body()?.wind?.speed
                    weather_humid.text= response.body()?.main?.humidity
                    weather_pressure_icon.setIconSize(30)
                    weather_pressure_icon.setIconColor(Color.BLACK)
                    weather_pressure_icon.setIconResource(getString(R.string.wi_earthquake))

                    weather_humidIcon.setIconSize(30)
                    weather_humidIcon.setIconColor(Color.BLACK)
                    weather_humidIcon.setIconResource(getString(R.string.wi_humidity))

                    weather_windy_icon.setIconSize(30)
                    weather_windy_icon.setIconColor(Color.BLACK)
                    weather_windy_icon.setIconResource(getString(R.string.wi_windy))


                }

                override fun onFailure(call: Call<weatherRepo>, t: Throwable) {
                //    Toast.makeText(context,"날씨 연결 실패",Toast.LENGTH_LONG).show()
                }

            })


        }catch (e : Exception){e.printStackTrace()}


    }

    fun setWeatherIcon(value :String){
        when(value){
            "01d" -> {weather_icon.setIconResource(getString(R.string.wi_day_sunny))}
            "02d" -> {weather_icon.setIconResource(getString(R.string.wi_day_cloudy))}
            "03d" -> {weather_icon.setIconResource(getString(R.string.wi_day_cloudy_gusts))}
            "04d" -> {weather_icon.setIconResource(getString(R.string.wi_day_cloudy_high))}

            "09d" -> {weather_icon.setIconResource(getString(R.string.wi_day_rain))}
            "10d" -> {weather_icon.setIconResource(getString(R.string.wi_day_rain_mix))}
            "11d" -> {weather_icon.setIconResource(getString(R.string.wi_day_rain_wind))}
            "13d" -> {weather_icon.setIconResource(getString(R.string.wi_day_snow))}
            "50d" -> {weather_icon.setIconResource(getString(R.string.wi_dust))}
            "01n" -> {weather_icon.setIconResource(getString(R.string.wi_night_clear))}
            "02n" -> {weather_icon.setIconResource(getString(R.string.wi_night_cloudy))}
            "03n" -> {weather_icon.setIconResource(getString(R.string.wi_night_alt_cloudy_gusts))}
            "04n" -> {weather_icon.setIconResource(getString(R.string.wi_night_cloudy))}

            "09n" -> {weather_icon.setIconResource(getString(R.string.wi_night_rain))}
            "10n" -> {weather_icon.setIconResource(getString(R.string.wi_night_cloudy_gusts))}
            "11n" -> {weather_icon.setIconResource(getString(R.string.wi_night_rain))}
            "13n" -> {weather_icon.setIconResource(getString(R.string.wi_night_snow))}
            "50n" -> {weather_icon.setIconResource(getString(R.string.wi_dust))}





        }
    }

}
