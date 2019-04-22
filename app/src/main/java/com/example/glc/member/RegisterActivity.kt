package com.example.glc.member

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.glc.R
import kotlinx.android.synthetic.main.activity_register.*

/*
    회원가입 액티비티
    - 정보를 입력한후 Register.kt 코틀린 파일을 이용해 db에 정보를 전달할수있다.
 */

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // EditText 에서 문자 가져오기
        val regi_id = regi_id.toString()
        val regi_pwd = regi_pwd.toString()
        val regi_name = regi_name.toString()
        val regi_age = regi_age.toString()


        /* 회원가입 버튼
            1. 아이디와 비밀번호 등을 db에 등록한다.
            2. 액티비티가 종료된다 , 로그인 액티비티로 이동됨
         */
        regi_btn.setOnClickListener {
            //Register(regi_id , regi_pwd , regi_name , regi_age) 회원가입 정보를 db에 보내는 함수 (미구현)
            finish()
        }





    }
}
