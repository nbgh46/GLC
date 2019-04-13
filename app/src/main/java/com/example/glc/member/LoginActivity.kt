package com.example.glc.member

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.glc.R

/*
    로그인 액티비티
    - 아이디 비밀번호를 입력해서 DB와 대조후 맞을시 SNS 액티비티로 넘어감
    - 회원가입 액티비티로 넘어갈수있다.
    - 로그인 버튼 실행시 login.kt 실행됨
 */


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
