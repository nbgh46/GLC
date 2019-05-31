package com.example.glc.sns

import android.app.ProgressDialog
import android.content.Intent
import android.drm.DrmStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.webkit.PermissionRequest
import android.widget.Toast
import com.example.glc.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sns_write.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat

class SNS_write : AppCompatActivity() {

    /* =================변수 선언 ======================*/

    //사진 uri
    var uri: Uri = Uri.EMPTY
    //업로드할 사진 파일 이름 초기화 (중복되지않는수 사용)
    var filename = (System.currentTimeMillis()).toString() + ".png"
    //파이어 베이스 저장소 참조 초기화
    var storage = FirebaseStorage.getInstance()
    var storageref = storage.getReferenceFromUrl("gs://glc-gachon.appspot.com/").child("images/" + filename)
    var uploadimagebool : Boolean = false
    var user_email :String? =""

    /* =================변수 선언 ======================*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sns_write)
        //데이터 베이스 연동 작업
        FirebaseApp.initializeApp(this)
        val PD_imageupload = ProgressDialog(this)
        PD_imageupload.setMessage("이미지를 업로드 중입니다.")

        val database = FirebaseDatabase.getInstance().getReference("SNS_list").push()

        val auth = FirebaseAuth.getInstance().currentUser?.let {
            user_email = it.email
        }
        //========================== 사진 버튼 ==============================================
        write_inputImagebtn.setOnClickListener {
            /*
              사진 버튼 구현
              -갤러리에서 사진 선택후 가져온다
              -가져온 사진파일은 enter 버튼 시 파이어베이스 storage 전송
              -사진 url 은 sns 데이터 베이스에 저장한다.
           */
            uploadimagebool = false
            val intent_image = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent_image, 1)
        }
        //============================사진 버튼 끝 ============================================


        /*=========================수정한 인텐트 가져오기*=========================================   */


        /* type =1 upload()    */
        if(intent.getStringExtra("type") == "1"){
            write_inputText.setText(intent.getStringExtra("sns_update_text"))
        }
        /*==================================== 글 입력 버튼 =========================================*/
        /*
            글내용 , 아이디 , 작성시간 , 사진파일 , 사용자 식별 id 를 db에 업로드
         */
        write_enterBtn.setOnClickListener {
            /* 입력버튼
                -입력한 image 텍스트 를 데이터 베이스에 저장해서 파이어베이스 db에 업로드
                - id ,시간 , 글번호 , image , imageurl 등이 업로드된다.
             */


/* ===========================글입력        ==========================================*/

            //EditText 에 글이 있는지 확인
            if (write_inputText.text.toString() != "") {
                //post 객체 생성 및 데이터 입력
                val post = SNS_list()


            /*============= 글수정 ( intent type ==1 일경우 ) =============================*/
                if(intent.getStringExtra("type") == "1"){

                    //post 객체에 데이터 입력
                    post.time = intent.getStringExtra("sns_update_time")
                    post.writer_id = user_email
                    post.context_id = intent.getStringExtra("sns_update_context_id")
                    post.text = write_inputText.text.toString()

                    /* ====  사진 파일을 변경할 경우 ==== */

                    if(uploadimagebool == true){
                        post.url = filename
                        PD_imageupload.show()
                        storageref.putFile(uri).addOnProgressListener {
                        /*사진 업로드 성공시 DB에 데이터 보냄 */
                        }.addOnSuccessListener {
                            PD_imageupload.dismiss()
                            val database = FirebaseDatabase.getInstance().getReference("SNS_list")
                            val outputmap = hashMapOf<String ,Any>()
                            outputmap.set(post.context_id,post)
                            database.updateChildren(outputmap)
                            Toast.makeText(this, "글 수정이 완료되었습니다.", Toast.LENGTH_LONG).show()
                            finish()

                            /*사진 업로드 성공시 DB에 데이터 보냄 */
                        }
                    }
                    /* ====  사진 파일을 변경할 경우 ==== */

                    /* ====  사진 파일을 변경 하지 않을 경우 ==== */
                    else{
                        val database = FirebaseDatabase.getInstance().getReference("SNS_list")
                        post.url = intent.getStringExtra("sns_update_url")
                        val outputmap = hashMapOf<String ,Any>()
                        outputmap.set(post.context_id,post)
                        database.updateChildren(outputmap)
                        Toast.makeText(this, "글 수정이 완료되었습니다.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    /* ====  사진 파일을 변경할 경우 ==== */

            }


        /*============= 글수정 ( intent type ==1 일경우 ) =============================*/



                /*================= 글쓰기 (수정 x) ========================*/
                else {
                    //업로드한 이미지파일 이름 생성
                    val daytime = SimpleDateFormat("yyyy-MM-dd hh:mm")
                    val time = daytime.format(System.currentTimeMillis())
                    //Post 객체에 데이터 입력
                    post.time = time
                    post.writer_id = user_email
                    post.context_id = database.key.toString()
                    post.text = write_inputText.text.toString()
                    post.url = filename
                    //이미지 업로딩
                    PD_imageupload.show()

                    storageref.putFile(uri).addOnProgressListener {
                    }.addOnSuccessListener {
                        PD_imageupload.dismiss()
                        database.setValue(post)
                        finish()
                    }
                }
            /*================= 글쓰기 (수정 x) ========================*/
            }


            else
            {
                Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }


        /* ===========================       글입력       ==========================================*/
    }



    /*====================================== 갤러리 에서 사진 가져오기 ====================================*/
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 1) {
                try {

                    uri = data!!.data //data에서 절대경로를 가져옴
                    var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    write_image!!.setImageBitmap(bitmap)
                    uploadimagebool=true

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    /*====================================== 갤러리 에서 사진 가져오기 ====================================*/
    }
