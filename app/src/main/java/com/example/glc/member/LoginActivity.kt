package com.example.glc.member

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.glc.MainActivity
import com.example.glc.R
import kotlinx.android.synthetic.main.activity_login.*

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



        // 아이디 비밀번호 를 editText 에서 가져오기
            val id : String = login_id.toString()
            val pwd : String = login_pwd.toString()


        //회원가입 버튼 - RegisterActivity로 이동  , 다른 전달할 값은 없다.
        login_regibtn.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        /*   로그인 버튼
                -ID 와 PASSWORD 를 DB와 비교해서 맞으면 TRUE 틀리면 FALSE 인 과정을 수행한다.
                -TRUE 일시 MainActivity 로 id 값과 함께 intent , false 일시 경고창 출력
         */

        login_btn.setOnClickListener {
            //로그인 db 대조 과정 (login.kt 에서 정의한다.)
            if(true) {//(db랑 일치 검사하는 함수)
                val Intent = Intent(this, MainActivity::class.java)
                Intent.putExtra("id", id)
                Intent.putExtra("pwd", pwd)
                startActivity(Intent)
            }
        }






    }
}
