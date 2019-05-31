package com.example.glc.subway


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.glc.R
import kotlinx.android.synthetic.main.fragment_subway_fragement.*
import kotlinx.android.synthetic.main.fragment_subway_fragement.view.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import java.lang.Exception
import java.util.concurrent.TimeUnit



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

        //프래그 머트 로딩시 데이터 로딩
        LoadSubway()

        //새로고침 버튼
        subway_btn.setOnClickListener {
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

    /* Retrofit interface 정의 */
    interface SubwayService{
        @GET("가천대")
        fun getGachon():retrofit2.Call<Subway>

    }
    /* Retrofit interface 정의 */

    /*=========================================================================
        -지하철 도착정보 데이터  로딩 메서드
        -Retrofit 라이브러리 , Okhttp 라이브러리 사용 비동기 방식으로 API 데이터를
        -XML 형식의 데이터를 불러옴 Subway 클래스에 대입
     ============================================================================*/
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
                    Log.d("Subway_error", t.message)
                }
            })


        }catch( e : Exception){
            e.printStackTrace()
        }
    }
/*================================= 지하철 데이터 로딩메서드 끝 =============================================*/

/*============================================================================================================
       파싱한 정보를 ui에 매칭시키는 메서드
       - subwayload 에서 받아온 데이터를 ui에 매핑 하는 매서드
       - 급행 열차가 있을경우 받아오는 지하철 수가 5 ,6 개 이상 올라가서 튕김 (기본 4개)
       - 정확한 매커니즘을 몰라서 야매로 처리
       - 막차 시간에 지하철 객체수가 1,2개로 주는 경우가 있어서 튕김
       - 열차 수에 따라 ui에 매칭되도록 처리
============================================================================================================*/
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
/*=============================== UI 매칭 시키는 메서드 끝 =============================================*/

}
