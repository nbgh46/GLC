package com.example.glc.sns


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.glc.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.fragment_sns.*
import kotlinx.android.synthetic.main.fragment_sns.view.*
import java.sql.Time
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SNSFragment : Fragment() , SwipeRefreshLayout.OnRefreshListener {


    //프레그 먼트에 표시될 뷰를 생성하여 반환하는 메서드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃 인플레이트
        return inflater.inflate(R.layout.fragment_sns, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*================================FAB 선택시 WRITE액티비티로 전환==============================================*/
        floatingActionButton.setOnClickListener {
            val intent_to_write = Intent(context,SNS_write::class.java)
            intent_to_write.putExtra("type","0")
            startActivity(intent_to_write)
    }
        /*================================FAB 선택시 WRITE액티비티로 전환 끝==============================================*/


        /*====================파이어 베이스 데이터 읽기 ====================================================*/

        //파이어 베이스 db 인스턴스 가져오기
        val db= FirebaseDatabase.getInstance().getReference().child("SNS_list")

        //db를 담을 arraylist 선언
        var messageList = ArrayList<SNS_list?>()

        //messageList 에 추가할 클래스 객체
        var import_list : SNS_list?

        //recycler view 초기회
        val sns_recylce = view.findViewById<RecyclerView>(R.id.sns_recycle)


        //데이터 베이스 처리 리스너 정의
        val mlistListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            //데이터 읽기가 성공했을경우
            override fun onDataChange(p0: DataSnapshot) {

                //messageList 를 먼저 clear 데이터가 추가되거나 바뀌어서 다시 로드할 경우 중복 추가 될수가 있기때문이다.
                    messageList.clear()

                //messageList 에 테스트값 추가 recylcer view null 에러가 발생해서 추가했지만 처리 x
                    messageList.add(SNS_list("test","test","test","test","test"))

                /*========db에서 데이터를 가져오고 arraylist 에 저장하는 과정 ============*/
                for(datasnapshot : DataSnapshot in p0.children){
                     import_list =datasnapshot.getValue(SNS_list::class.java)

                    messageList.add(import_list)

                }

                /*========db에서 데이터를 가져오고 arraylist 에 저장하는 과정 ============*/


                /* 받아온 ArrayList sort 로 리스트를 역순으로 출력 */
                messageList.reverse()

                //리사이클러 뷰에 어댑터 적용
                sns_recylce.adapter =SNS_adapter(messageList)
                sns_recylce.layoutManager = LinearLayoutManager(context)

            }
        }
        //recycler view에 어엡터와 layoutmanager 적용

        //firebase db 이벤트 리스너 등록
        db.addValueEventListener(mlistListener)

/*====================파이어 베이스 데이터 읽기 ====================================================*/


  /*================================*Recylcer view 위로 swipe시 새로고침  ==========================*/

        swipe_sns.setOnRefreshListener {
            onRefresh()
            swipe_sns.isRefreshing = false
        }
        swipe_sns.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

    }

    override fun onRefresh() {
        //새로고침
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()

    }
    /*================================*Recylcer view 위로 swipe시 새로고침 ==========================*/


    companion object {
        fun newInstance(): SNSFragment{
            val snsFragment = SNSFragment()
            return snsFragment
        }
    }

}
