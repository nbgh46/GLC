package com.example.glc.member

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.glc.R
    /*
        회원가입 액티비티
        - 정보를 입력한후 Register.kt 코틀린 파일을 이용해 db에 정보를 전달할수있다.
     */

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}
