package com.example.glc.Cafeteria



import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_cateferia.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.lang.Exception




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CateferiaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.glc.R.layout.fragment_cateferia, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
            학식 메뉴 구성
            - FrameLayout 으로 구성 , 버튼 클릭시 선택layout 과 detaillayout 전환
            - 버튼 클릭시 Detail Layout 으로 이동하고 파싱할 url을 대입
            - 해당 요일 클릭시 학식 데이터 로딩
         */

        var cafeteria_type :String =""
        var cafeteria_day : Int =0

        //교육대학원 버튼
        cafeteria_edu_btn.setOnClickListener {
            cafeteria_type="etc/food.jsp?gubun=B"
            cafeterla_firstlayout.visibility=View.GONE
            cafeteria_secondlayout.visibility=View.VISIBLE
        }

        //여술대학 버튼
        cafeteria_art_btn.setOnClickListener {
            cafeteria_type="etc/food.jsp?gubun=A"
            cafeterla_firstlayout.visibility=View.GONE
            cafeteria_secondlayout.visibility=View.VISIBLE
        }

        //비전타워 버튼
        cafeteria_vision_btn.setOnClickListener {
            cafeteria_type="etc/food.jsp?gubun=C"
            cafeterla_firstlayout.visibility=View.GONE
            cafeteria_secondlayout.visibility=View.VISIBLE
        }

        //뒤로가기 버튼
        cafeteria_back_btn.setOnClickListener {
            cafeteria_secondlayout.visibility=View.GONE
            cafeterla_firstlayout.visibility=View.VISIBLE
            cafeteria_type=""
            getdata_cafeteria(cafeteria_type, cafeteria_day)
        }

            /*  월~금 버튼 */
        cafeteria_monday.setOnClickListener {
            cafeteria_day=0
            getdata_cafeteria(cafeteria_type, cafeteria_day)

        }
        cafeteria_tuesday.setOnClickListener {
            cafeteria_day=2
            getdata_cafeteria(cafeteria_type, cafeteria_day)

        }
        cafeteria_Wednesday.setOnClickListener {
            cafeteria_day=4
            getdata_cafeteria(cafeteria_type, cafeteria_day)

        }
        cafeteria_thursday.setOnClickListener {
            cafeteria_day=6
            getdata_cafeteria(cafeteria_type, cafeteria_day)

        }
        cafeteria_friday.setOnClickListener {
            cafeteria_day=8
            getdata_cafeteria(cafeteria_type, cafeteria_day)

        }

    }

    companion object {
        fun newInstance(): CateferiaFragment {
            val bundle = Bundle()
            val cateferiaFragmentFragment = CateferiaFragment()
            return cateferiaFragmentFragment
        }
    }

    fun getdata_cafeteria(cafeteria_type:String , cafeteria_day : Int){
     /*비전타워 학식정보 가져요기
       -프레임 레이아웃 처음 화면 gone , detail 레이아웃 on
       -주어진 레이아웃에 비전타워 학식 정보 parsing
      */
        try {

            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            val cafeteria_vision_retrofit = Retrofit.Builder()
                .baseUrl("http://www.gachon.ac.kr/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build()

            val url2 = "food.jsp"
            val cafeteria_Service = cafeteria_vision_retrofit.create(CafeteriaService::class.java)
            val cafeteria_call  = cafeteria_Service.Cafeteria_info(cafeteria_type)


                cafeteria_call.enqueue(object : retrofit2.Callback<String>{

                override fun onResponse(call: Call<String>, response: Response<String>) {


                    val url = response.body()
                  val doc = Jsoup.parse(response.body().toString())
                    doc.select("br").append("$$$")
                    for (i in 0..2) {
                        val element: Elements = doc.select("div.food_list table tbody tr").eq(cafeteria_day).select("td").eq(i)
                        var receive_text = element.text().replace("$$$","\n")

                        when(i){
                            0 ->   {cafeteria_text1.text = receive_text}
                            1 ->   {cafeteria_text2.text = receive_text}
                            2 ->   {cafeteria_text3.text = receive_text}

                        }
                     }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                }
            } )

        }catch (e : Exception){e.printStackTrace()}

    }

    interface CafeteriaService{
            @GET()
            fun Cafeteria_info(@Url url: String):retrofit2.Call<String>
    }

}
