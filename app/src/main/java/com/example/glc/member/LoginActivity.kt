package com.example.glc.member


import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.glc.MainActivity
import com.example.glc.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.sql.Types.NULL

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
        //로그인 프로그래스 다이얼로그
        val PD_login = ProgressDialog(this)
            PD_login.setMessage("로그인 중입니다.")

        //파이어 베이스 인스턴스 초기화
        FirebaseApp.initializeApp(this)
        val auth = FirebaseAuth.getInstance()


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
            // 아이디 비밀번호 를 editText 에서 가져오기
            val id : String = login_id.text.toString()
            val pwd : String = login_pwd.text.toString()

            //로그인 db 대조 과정 (login.kt 에서 정의한다.)
            if(id !="" && pwd!="") {//(db랑 일치 검사하는 함수)

                //로그인 프로그래스 바 on
                PD_login.show()
                //파이어 베이스 에서 데이터 읽기
                auth.signInWithEmailAndPassword(id,pwd).addOnCompleteListener(this){
                   if(it.isSuccessful) {
                       //db와 대조성공 아이디와 패스워드가 일치할 경우 mainactivity로 아이디와 패스워드를 넘기고 전환시킨다.
                       val intent = Intent(this, MainActivity::class.java)
                       PD_login.dismiss()
                       startActivity(intent)
                       finish()
                   }else {
                       Toast.makeText(baseContext, "로그인 실패 , 아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()

                   }
                }

            }else{
                Toast.makeText(this,"아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_LONG).show()
            }


        }






    }
}
