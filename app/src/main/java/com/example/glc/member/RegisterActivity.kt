package com.example.glc.member

import android.app.ProgressDialog
import android.content.Context
import android.os.Build.VERSION_CODES.P
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.example.glc.R
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import java.lang.Exception
import java.lang.Thread.sleep

import java.sql.Types.NULL

/*
    회원가입 액티비티
    - 정보를 입력한후 Register.kt 코틀린 파일을 이용해 db에 정보를 전달할수있다.
 */

class RegisterActivity : AppCompatActivity() {

    lateinit var  auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        //파이어 베이스 에서 인스턴스 가져오기
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()


        //회원가입 progressdialog
        val PD_register = ProgressDialog(this)
            PD_register.setMessage("회원가입 중입니다.")

        // 회원가입 버튼
        // 1. 아이디와 비밀번호 등을 db에 등록한다.
        // 2. 액티비티가 종료된다 , 로그인 액티비티로 이동됨


        regi_btn.setOnClickListener {

            //회원가입 프로그래스다이얼로그
            PD_register.show()

         // EditText 에서 문자 가져오기
            val email = regi_id.text.toString()
            val pwd = regi_pwd.text.toString()


           auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(this){
                if(it.isSuccessful){

                    Toast.makeText(this,"회원가입 성공",Toast.LENGTH_LONG).show()
                    PD_register.dismiss()
                    finish()
                }else{
                    Toast.makeText(this,"회원가입 실패",Toast.LENGTH_LONG).show()
                    }
            }
        }





    }
}





