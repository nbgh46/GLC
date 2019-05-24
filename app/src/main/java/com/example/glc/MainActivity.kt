package com.example.glc

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.glc.sns.SNS_write
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

/*
    로그인 이후 넘어오는 액티비티
    하단탭에 SNS , 지하철 ,학식 ,편의시설 탭 생성
    기본적으로 SNS 이 나오게 하고 탭을 누를시 이동됨
 */
class MainActivity : AppCompatActivity() {

    //로그에 TAG 로 사용할 문자열
    val TAG = "MainActiviry"

    //파이어베이스의 test 키를 가진 데이터의 참조 객체를 가져온다.
    val ref = FirebaseDatabase.getInstance().getReference("test")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*=================================파이어베이스 연동==============================================*/
        FirebaseApp.initializeApp(this)
        //사용자 정보 액세스
            FirebaseAuth.getInstance().currentUser?.let{
            val  user_email = it.email
            Toast.makeText(this,"${user_email}님 환영합니다.",Toast.LENGTH_LONG).show()
        }
        /*=================================파이어베이스 끝==============================================*/

        /*=================================뷰페이저 프레그먼트 구현==============================================*/

        //탭 레이아웃을 viewpager 와 연동 터치 스와이프시 탭 이동이 됨

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("SNS"))
        tabLayout.addTab(tabLayout.newTab().setText("지하철").setIcon(R.drawable.baseline_subway_24))
        tabLayout.addTab(tabLayout.newTab().setText("학식"))
        tabLayout.addTab(tabLayout.newTab().setText("공지사항"))
        tabLayout.addTab(tabLayout.newTab().setText("날씨"))
        viewPager.adapter = MainPageAdapter(supportFragmentManager)

        //Viewpager의 어탭터를 mainPageAdapter로 설정정
        /*================================뷰페이저 프레그먼트 구현 끝==============================================*/




    }



    /* 뒤로가기 버튼 누를시 앱 종료 하지 다이얼로그 출력 후 종료하거나 취소 */
    override fun onBackPressed(){
       // super.onBackPressed()
        AlertDialog.Builder(this).setTitle("정말로 종료하시겠습니까?").setCancelable(true)
            .setPositiveButton("종료") { dialog: DialogInterface?, which: Int ->
    finishAffinity()
    }.setNegativeButton("취소"){ dialog: DialogInterface?, which: Int ->}.show()
    }

/* 뒤로가기 버튼 누를시 앱 종료 하지 다이얼로그 출력 후 종료하거나 취소 */

}

