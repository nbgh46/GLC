package com.example.glc.subway


import android.graphics.ColorSpace.match
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.glc.Cafeteria.CateferiaFragment

import com.example.glc.R
import kotlinx.android.synthetic.main.fragment_subway_fragement.*
import kotlinx.android.synthetic.main.fragment_subway_fragement.view.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.lang.Exception
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SubwayFragement : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subway_fragement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadSubway()
        subway_btn.setOnClickListener {  //새로고침 버튼
            LoadSubway()
        }



    }

    companion object {
        fun newInstance(): SubwayFragement {
            val bundle = Bundle()

            val subwayFragement = SubwayFragement()
            return subwayFragement
        }
    }

    interface SubwayService{
        @GET("가천대")
        fun getGachon():retrofit2.Call<Subway>

    }

    fun LoadSubway() {
        val Subway_url =
            "http://swopenapi.seoul.go.kr/api/subway/5a794d4a7a6e62673731706b4e6f7a/xml/realtimeStationArrival/0/5/"


        try {

            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES) // write timeout
                .readTimeout(10, TimeUnit.MINUTES) // read timeout
                .build()

            val Subway_api = Retrofit.Builder()
                .baseUrl(Subway_url)
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

            val SubwayServie = Subway_api.create<SubwayService>(SubwayService::class.java)
            val SubwayCall = SubwayServie.getGachon()


            SubwayCall.enqueue(object : Callback<Subway> {
                override fun onResponse(call: Call<Subway>, response: Response<Subway>) {
                   // Toast.makeText(context, "연결성공", Toast.LENGTH_SHORT).show()
                    //val subway = response.body()
                    //setSubwayinfo(subway)

                    if (response.isSuccessful) {
                        val subway = response.body()
                        if (subway?.Subway_info_list!!.isEmpty()){

                        }else{
                            setSubwayinfo(subway)
                        }


                    }

                }

                override fun onFailure(call: Call<Subway>, t: Throwable) {
                  //  Toast.makeText(context, "연결실패", Toast.LENGTH_SHORT).show()
                    Log.d("Subway_eroor", t.message)
                }
            })


        }catch( e : Exception){
            e.printStackTrace()
        }
    }

    /*
            파싱한 정보를 ui에 매칭시키는 메서드
     */
    fun  setSubwayinfo(subway : Subway?){

        //왕십리행 복정 방면 매칭
            //첫번째 열차
            fun match1() {
                up_line_arvl_1_msg_heading.text = subway?.Subway_info_list?.get(0)?.trainLineNm
                up_line_arvl_1_msg.text = subway?.Subway_info_list?.get(0)?.arvlMsg2
            }

            //두번쨰 열차
            fun match2() {
            up_line_arvl_2_msg_heading.text = subway?.Subway_info_list?.get(1)?.trainLineNm
            up_line_arvl_2_msg.text = subway?.Subway_info_list?.get(1)?.arvlMsg2
        }

        //수원 ,죽전 태평 방면 매칭

            //첫번쨰 열차
            fun match3() {
                down_line_arvl_1_msg_heading.text = subway?.Subway_info_list?.get(2)?.trainLineNm
                down_line_arvl_1_msg.text = subway?.Subway_info_list?.get(2)?.arvlMsg2
            }
            //두번쨰 열차
            fun match4()  {
                    down_line_arvl_2_msg_heading.text = subway?.Subway_info_list?.get(3)?.trainLineNm
                    down_line_arvl_2_msg.text = subway?.Subway_info_list?.get(3)?.arvlMsg2
                }
        when (subway?.Subway_info_list?.size){
            0 -> {}
            1 -> {match1()}
            2 -> {match1();match2()}
            3 -> {match1();match2();match3()}
            4 -> {match1();match2();match3();match4()}
            5 ->  {match1();match2();match3();match4()}
            6 ->  {match1();match2();match3();match4()}
        }
    }


}
