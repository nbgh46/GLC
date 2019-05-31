package com.example.glc.Weather


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        //날씨 로딩 메서드 실행
        loadWeather()




    }


    companion object {
        fun newInstance(): WeatherFragment {
            val WeatherFragment = WeatherFragment()
            return WeatherFragment
        }
    }


    /* 날씨 Retrofit 인터페이스 정의 */
    interface WeatherService{
        @GET(".")
        fun setweather(
        @Query("lat")lat:String,
        @Query("lon")lon:String,
        @Query("appid")appid:String
        ):Call<weatherRepo>
    }
    /* 날씨 Retrofit 인터페이스 정의 */


    /*===================================================================================================
        날씨 데이터를 불러오는 메서드
        -Retrofit 을 이용한 비동식 방식 통신
        -http://api.openweathermap.org/data/2.5/weather/ url에 가천대의 위도 경도 , key 쿼리를 입력
        -json 방식의 데이터를 클래스에 담는다
        -현재 날씨상태 , 현재 온도 , 오늘 최저 , 최대온도 , 습도 , 기압 , 바람 속도 정보를 가져온다.
        -pwittchen:weathericonview 라이브러리를 사용해서 날씨 icon 사용

      ===================================================================================================*/
    fun loadWeather(){

        try{

            /* retrofit 빌더 정의 , 위도 , 경도 , key 쿼리를 입력한다. */
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

            /* retrofit 빌더 정의 , 위도 , 경도 , key 쿼리를 입력한다. */
            WeatherCall.enqueue(object : Callback<weatherRepo>{

                override fun onResponse(call: Call<weatherRepo>, response: Response<weatherRepo>) {

                    /*===============================================================================
                        -비동기 방식 통신으로 json 데이터 를 가져옴
                        -받아온 json 데이터를 클래스에 파싱
                        -받아온 데이터를 ui에 매칭
                        -icon 라이브러리를 사용해서 해당 날씨 상태에 맞게 icon 나오게 함
                     ================================================================================*/
                    val currentWeather = response.body()?.weatherList     // 받아온 json 객체 생성
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


    /*==================== 현재 날씨 상태에 맞게 icon 을 불러오는 메서드 ===================================*/
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
/*==================== 현재 날씨 상태에 맞게 icon 을 불러오는 메서드 ===================================*/
}
