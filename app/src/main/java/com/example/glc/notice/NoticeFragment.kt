package com.example.glc.notice


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.glc.R
import kotlinx.android.synthetic.main.fragment_notice.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class NoticeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    /*========================== 공지사항 불러오기 ==========================*/

        //초기페이지 1페이지 정의
        var pagenum=0

        //페이지 불러오기
        loadNoticeList(pagenum)


        //앞으로 가기버튼 pagenum 이 1올라가고 다음페이지를 로드
        notice_up_btn.setOnClickListener {

            pagenum++
            loadNoticeList(pagenum)

        }

        //뒤로라기 버튼 pagenum이 1 작아지고 이전페이지 로드 . 현재 페이지가 1페이지 일경우 Toast메세지 출력
        notice_back_btn.setOnClickListener {
            if(pagenum>0){
                pagenum--
                loadNoticeList(pagenum)
            }else{
                Toast.makeText(context,"현재 첫번쨰 페이지 입니다.",Toast.LENGTH_LONG).show()
            }
        }

        //웹뷰에서 뒤로가기 버튼
        notice_web_back_btn.setOnClickListener {

            notice_layout2.visibility=View.GONE
            notice_layout.visibility=View.VISIBLE
        }
    }

/*========================== 공지사항 불러오기 ==========================*/



/* ================ 공지사항 Retrofit 인터페이스 정의 ==========================*/
    interface NoticeService{
        @GET("bbs.jsp")
        fun setPage(
            @Query("pageNum")page:String,
            @Query("pageSize")pageSize:String,
            @Query("boardType_seq")boardType_seq:String)
                :Call<String>
    }

/* ================ 공지사항 Retrofit 인터페이스 정의 ==========================*/



/*
    공지사항을 로딩 하는 메서드
    -Retrofit 라이브러리 이용 비동식 방식 통신
    -Jsoup 라이브러리(html 크롤링 라이브러리) 이용
    -http://www.gachon.ac.kr/major/ 에서 크롤링
    -글제목 , 작성일 , 글 링크 를 String 값으로 파싱
    -ListView 를 이용해서 UI 구성 , ITEM 클릭시 안드로이드 WebView 를 이용해서 글 링크를 통해 글로 이동
 */

    fun loadNoticeList(pagenum : Int){

        try {

            //Okhttp 라이브러리를 이용해서 client 정의
            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES) // write timeout
                .readTimeout(10, TimeUnit.MINUTES) // read timeout
                .build()

            //Retrofit 빌더 정의
            val notice_api = Retrofit.Builder()
                .baseUrl("http://www.gachon.ac.kr/major/")
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            //현재 페이지를 가져오기
                val pagenum2 = pagenum.toString()

            //현재페이지를 변수를 이용해서 파싱을할 정의 , ArrayList 정의
                val notice_Service = notice_api.create(NoticeService::class.java)
                val notice_call = notice_Service.setPage(pagenum2,"10","159")
                var notice_list: ArrayList<notice>? = ArrayList()
                notice_list?.clear()
            notice_call.enqueue(object : Callback<String>{

                /*============================================================================================================
                    비동기 방식 통신
                    -jsoup라이브러리를 이용해서 html 파싱
                    -파싱한 정보를 arraylist에 대입
                    -Arraylist 를 이용해서 listview 생성
                    -item 클릭시 웹뷰를 해당 링크로 이동
                 ==============================================================================================================*/
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //jsoup 라이브러리를 이용해서 html 파싱
                    val doc = Jsoup.parse(response.body().toString())

                   /*========================== 파싱한 html 중 원하는 정보를 찾는과정 , arraylist 에 대입 ================================================*/
                    for (i in 5..14) {
                        val element: Elements = doc.select("div.boardlist table tbody tr td a").eq(i)
                        val element2: Elements = doc.select("div.boardlist table tbody tr").eq(i).select("td").eq(4)


                        val out = element.text()
                        val out2 = element.attr("href")
                        val out3 = element2.text()
                        var tempNotice =notice(out,out2,out3)
                            notice_list?.add(tempNotice)


                    }
                    /*========================== 파싱한 html 중 원하는 정보를 찾는과정 , arraylist 에 대입 ================================================*/

                    //파싱한 데이터를 이용해서 listview 생성
                    val adapter=noitceListAdapter(notice_list)
                    notice_listview.adapter=adapter

                    /* ============================== ListVeiw 를 클릭시 웹뷰이동 과정 =================================================*/
                    notice_listview.setOnItemClickListener { parent, view, position, id ->
                        var curItem = adapter.getItem(position)
                        notice_webview.settings.javaScriptEnabled

                        notice_webview.loadUrl("http://www.gachon.ac.kr/major/"+curItem.url)
                        notice_layout2.visibility=View.VISIBLE
                        notice_layout.visibility=View.GONE

                    }

                    /* ============================== ListVeiw 를 클릭시 웹뷰이동 과정 =================================================*/


                }
                override fun onFailure(call: Call<String>, t: Throwable) {

                }

            })





        }catch (e: Exception){e.printStackTrace()}
    }







    companion object {
        fun newInstance(): NoticeFragment {
            val bundle = Bundle()

            val NoticeFragment = NoticeFragment()
            return NoticeFragment
        }
    }

}
