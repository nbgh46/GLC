package com.example.glc.notice


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.glc.R
import com.example.glc.subway.Subway_detailinfo
import kotlinx.android.synthetic.main.fragment_notice.*
import kotlinx.android.synthetic.main.fragment_notice.view.*
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
import retrofit2.http.Path
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

        var pagenum=0
        loadNoticeList(pagenum)

        notice_up_btn.setOnClickListener {

            pagenum++
            loadNoticeList(pagenum)

        }

        notice_back_btn.setOnClickListener {
            if(pagenum>0){
                pagenum--
                loadNoticeList(pagenum)
            }else{
                Toast.makeText(context,"현재 첫번쨰 페이지 입니다.",Toast.LENGTH_LONG).show()
            }
        }
        notice_web_back_btn.setOnClickListener {
           // notice_webview.destroy()
            notice_layout2.visibility=View.GONE
            notice_layout.visibility=View.VISIBLE
        }
    }

    interface NoticeService{
        @GET("bbs.jsp")
        fun setPage(
            @Query("pageNum")page:String,
            @Query("pageSize")pageSize:String,
            @Query("boardType_seq")boardType_seq:String)
                :Call<String>
    }

    fun loadNoticeList(pagenum : Int){

        try {


            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES) // write timeout
                .readTimeout(10, TimeUnit.MINUTES) // read timeout
                .build()

            val notice_api = Retrofit.Builder()
                .baseUrl("http://www.gachon.ac.kr/major/")
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

                val pagenum2 = pagenum.toString()
                val notice_Service = notice_api.create(NoticeService::class.java)
                val notice_call = notice_Service.setPage(pagenum2,"10","159")
                var notice_list: ArrayList<notice>? = ArrayList()
                notice_list?.clear()
            notice_call.enqueue(object : Callback<String>{

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val doc = Jsoup.parse(response.body().toString())

                    for (i in 5..14) {
                        val element: Elements = doc.select("div.boardlist table tbody tr td a").eq(i)
                        val element2: Elements = doc.select("div.boardlist table tbody tr").eq(i).select("td").eq(4)


                        val out = element.text()
                        val out2 = element.attr("href")
                        val out3 = element2.text()
                        var tempNotice =notice(out,out2,out3)
                            notice_list?.add(tempNotice)


                    }
                    val adapter=noitceListAdapter(notice_list)
                    notice_listview.adapter=adapter
                    notice_listview.setOnItemClickListener { parent, view, position, id ->
                        var curItem = adapter.getItem(position)
                        notice_webview.settings.javaScriptEnabled

                        notice_webview.loadUrl("http://www.gachon.ac.kr/major/"+curItem.url)
                        notice_layout2.visibility=View.VISIBLE
                        notice_layout.visibility=View.GONE

                    }


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
