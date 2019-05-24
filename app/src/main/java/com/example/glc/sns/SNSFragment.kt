package com.example.glc.sns


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
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
        // Inflate the layout for this fragment
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


        //파이어 베이스 데이터 읽기
        val db= FirebaseDatabase.getInstance().getReference().child("SNS_list")
        var messageList = ArrayList<SNS_list?>()
        var import_list : SNS_list?
        var SNS_adapter :SNS_adapter
        val mlistListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                    messageList.clear()
                for(datasnapshot : DataSnapshot in p0.children){
                     import_list =datasnapshot.getValue(SNS_list::class.java)

                    messageList.add(import_list)

                }
                /* 받아온 ArrayList sort 로 리스트를 역순으로 출력 */
                messageList.reverse()


                
                sns_recycle.adapter =SNS_adapter(messageList)
                sns_recycle.layoutManager = LinearLayoutManager(context)

            }
        }
        //recycler view에 어엡터와 layoutmanager 적용
      //  sns_recycle.adapter = SNS_adapter(messageList)


        db.addValueEventListener(mlistListener)

        swipe_sns.setOnRefreshListener {
            onRefresh()
            swipe_sns.isRefreshing = false
        }

    }

    /*Recylcer view 위로 swipe시 refresh */
    override fun onRefresh() {
        //새로고침


    }
    /*Recylcer view 위로 swipe시 refresh */


    companion object {
        fun newInstance(): SNSFragment{
            val snsFragment = SNSFragment()
            return snsFragment
        }
    }

}
