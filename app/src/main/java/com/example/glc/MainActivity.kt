package com.example.glc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

    /*
        로그인 이후 넘어오는 액티비티
        하단탭에 SNS , 지하철 ,학식 ,편의시설 탭 생성
        기본적으로 SNS 이 나오게 하고 탭을 누를시 이동됨
     */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
