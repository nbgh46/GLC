package com.example.glc.sns

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import android.widget.Toast.makeText

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.glc.R


import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.fragment_sns.*
import kotlinx.android.synthetic.main.sns_comment_item_list.view.*
import kotlinx.android.synthetic.main.sns_feed_item_ist.view.*
import java.lang.IllegalArgumentException


/*
    SNS 리사이클러뷰의 어댑터
 */

class SNS_adapter(sns_list: ArrayList<SNS_list?>) : RecyclerView.Adapter<SNS_adapter.sns_listitemViewHolder>() {


    private var sns_list: ArrayList<SNS_list?> = sns_list


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    override fun getItemCount(): Int {

        return sns_list.size
    }

    override fun onBindViewHolder(holder: sns_listitemViewHolder, position: Int) {


        holder.bindfeed(sns_list, position)

    }

    /*
         [2] 뷰홀더(ItemViewHolder)를 생성하고 반환 (뷰홀더가 새로 만들어지는 시점에만 호출)
              -리사이클러뷰에는 리스트로 출력할 각각의 아이템 뷰객체를 모두 생성하는 것이 아니라
              뷰홀더에 뷰 객체를 담아두고 사용자가 스크롤하여 보이지 않게 되는 뷰 객체를 , 새로 보일 쪽에 재사용함
              -따라서 , onCreateViewHolder() 메서드는 뷰홀더가 새로 만들어지는 시점에만 호출되며 ,
              뷰홀더가 재사용되는  시점에는 onBindBiewHolder() 메서드를 호출하여 , 기존 뷰홀더(재사용)에 데이터만 바인딩함
       */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sns_listitemViewHolder {
        /*
            뷰타입에 따라  해당 item XML 레이아웃을 인플레이션 (ItemViewHolder 생성)
               -ViewGroup 객체의 content를 이용하여 LayoutInflater 참조
               -RectcleView에 출력할 ItemViewHoloder를 생성하여반환
         */

        val view = when (viewType) {
            0 -> LayoutInflater.from(parent.context).inflate(R.layout.sns_feed_item_ist, parent, false)
            else -> throw   IllegalArgumentException(Error("매칭되는 뷰타입이 없습니다."))
        }

        return sns_listitemViewHolder(view, parent)


    }
    /*
          뷰홀더 클래스 선언
              -리사이클러뷰에는 각각의 아이템을 위한 뷰객체를 뷰홀더에 담아 관리하며 , 필요시 재사용함
              -뷰홀더에 담아 전달된 뷰객체에 데이터를 바인딩

     */


    inner class sns_listitemViewHolder(itemview: View, parent: ViewGroup) : RecyclerView.ViewHolder(itemview) {

        val viewGroup = parent

        /* SNS 내용 데이터 바인딩 */
        fun bindfeed(sns_list: ArrayList<SNS_list?>, position: Int) {


            FirebaseStorage.getInstance().reference.child("images/")
                .child(sns_list.get(position)!!.url).downloadUrl.addOnSuccessListener {
                val url = it
                Glide.with(itemView)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(600, 600)
                    .into(itemView.sns_image)
            }.addOnFailureListener {
                Glide.with(itemView)
                    .load("https://images.mypetlife.co.kr/wp-content/uploads/2018/06/06200333/pexels-photo-1108099-1024x768.jpeg")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(600, 600)
                    .fitCenter()
                    .into(itemView.sns_image)
            }

            //시간 타임 설정

            itemView.sns_timestamp.text = sns_list.get(position)?.time
            itemView.sns_name.text = sns_list.get(position)?.writer_id?.split("@")?.get(0)
            itemView.sns_txt.text = sns_list.get(position)?.text

            //댓글 출력
            loadComment(sns_list.get(position)!!.context_id, position, viewGroup, itemView)

            //댓글 입력 버튼 (db에 댓글을 객체 저장)
            itemView.sns_comment_btn.setOnClickListener {
                var comment_post = SNS_Comment(
            FirebaseAuth.getInstance().currentUser!!.email!!,
            itemView.sns_comment_edittext.text.toString()
            )
            uploadComment(comment_post, position)
        }

        //댓글 레이아웃 visibile 처리
            itemView.sns_comment_visible_txt.setOnClickListener {
                if (itemView.Feed_Footer.visibility == View.GONE) {
                    itemView.Feed_Footer.visibility = View.VISIBLE
                } else {
                    itemView.Feed_Footer.visibility = View.GONE
                }
            }


            //sns 메뉴 ( 수정 , 삭제)
            sns_getmenu(itemView.sns_menu_btn, viewGroup.context, sns_list.get(position))


        }


    }
    /* ======================댓글 바인딩 하기기 ==================================*/

    /* ========================= 댓글 입력 함수===========================================================*/

    fun loadComment(
        Commtted_object_Id: String,
        position: Int,
        viewGroup: ViewGroup,
        itemview: View
    ): ArrayList<SNS_Comment?> {
        val comment =
            FirebaseDatabase.getInstance().getReference("SNS_list").child(Commtted_object_Id).child("SNS_Comment")

        var messageList = ArrayList<SNS_Comment?>()
        val Comment_list_Listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (datasnapshot: DataSnapshot in p0.children) {
                    var import_list = datasnapshot.getValue(SNS_Comment::class.java)

                    messageList.add(import_list)
                }
                val inflater = LayoutInflater.from(viewGroup.context)
                for (i in messageList) {
                    val comment_inflate = inflater.inflate(R.layout.sns_comment_item_list, null, false)

                    comment_inflate.comment_id.text = i?.comment_id?.split("@")?.get(0)
                    comment_inflate.comment_txt.text = i?.comment_text
                    itemview.comment_header.addView(comment_inflate)
                }
                if (messageList.size != 0) {
                    itemview.sns_comment_visible_txt.text = "댓글" + messageList.size.toString() + "개 보기"
                } else {
                    itemview.sns_comment_visible_txt.text = "댓글 보기"
                }
                messageList.clear()
            }



        }
        comment.addValueEventListener(Comment_list_Listener)
        return messageList
    }

    /* ========================= 댓글 츨력 함수 끝 ===========================================================*/


    /* ========================= 댓글 입력 함수 ===========================================================*/
    fun uploadComment(sns_Comment: SNS_Comment, position: Int) {
        val database =
            FirebaseDatabase.getInstance().getReference("SNS_list").child(sns_list.get(position)!!.context_id)
                .child("SNS_Comment").push()
        database.setValue(sns_Comment)
        //Toast.makeText(this,"댓글 입력이 완료 되었습니다.",Toast.LENGTH_LONG)
    }

    /* ========================= 댓글 입력 함수 끝 ===========================================================*/


    /* ======================댓글 바인딩 하기기 끝 ==================================*/


    /*===================sns item 메뉴 함수 =================================*/
    fun sns_getmenu(view: View, context: Context, sns_list: SNS_list?) {
        /*PopupMenu객체 생성
            -생성자함수의 첫번재 파라미터 : Context
             -생성자함수의 두번째 파라미터 : Popup Menu를 붙일 anchor 뷰*/
        val popupmenu = PopupMenu(context, view)

        /*Popup Menu에 들어갈 MenuItem 추가.
            -이전 포스트의 컨텍스트 메뉴(Context menu)처럼 xml 메뉴 리소스 사용
            -첫번재 파라미터 : res폴더>>menu폴더>>mainmenu.xml파일 리소스
            -두번재 파라미터 : Menu 객체->Popup Menu 객체로 부터 Menu 객체 얻어오기 */
        val menuInflater: MenuInflater
        val db = FirebaseDatabase.getInstance().getReference("SNS_list")
        val auth = FirebaseAuth.getInstance().currentUser?.email
        popupmenu.menuInflater.inflate(R.menu.sns_menu, popupmenu.menu)
        //이벤트 처리
        //sns menu 이벤트
        view.setOnClickListener {
            /*  popupmenu 핸들링
                -Popup Menu의 MenuItem을 클릭하는 것을 감지하는 listener 객체 생성
                -import android.widget.PopupMenu.OnMenuItemClickListener 가 되어있어야 합니다.
                -OnMenuItemClickListener 클래스는 다른 패키지에도 많기 때문에 PopupMenu에 반응하는 패키지를 임포트하셔야 합니다.
                */
            popupmenu.setOnMenuItemClickListener {

                when (it.itemId) {
                    /*=======================수정메뉴 클릭시 실행 동작===============================*/
                    R.id.sns_update_menu -> {

                        var intent_update = Intent(context, SNS_write::class.java)
                        intent_update.putExtra("sns_update_context_id",sns_list?.context_id)
                        intent_update.putExtra("sns_update_writer_id",sns_list?.writer_id)
                        intent_update.putExtra("sns_update_text",sns_list?.text)
                        intent_update.putExtra("sns_update_time",sns_list?.time)
                        intent_update.putExtra("sns_update_url",sns_list?.url)
                        intent_update.putExtra("type","1")
                        ContextCompat.startActivity(context,intent_update,null)

                    }


                    /*=======================수정메뉴 클릭시 실행 동작 끝===============================*/


                    /*=======================삭제메뉴 클릭시 실행 동작===============================*/
                    R.id.sns_delete_menu -> {
                        /*========================삭제전 다이얼로그 , 본인인지 확인 ================================*/
                        val delete_dialog = AlertDialog.Builder(context)
                        delete_dialog.setTitle("정말로 삭제하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("삭제") { dialog: DialogInterface?, which: Int ->
                                if (auth == sns_list?.writer_id) {  //글의 id와 사용자 id가 같은경우

                                    /*============================= 해당 쿼리 삭제 *==============================*/
                                    db.child(sns_list!!.context_id).removeValue()
                                    /*============================= 해당 쿼리 삭제 *==============================*/

                                } else {
                                    Toast.makeText(context, "사용자가 일치하지않습니다.", Toast.LENGTH_LONG).show()
                                }
                            }.setNegativeButton("취소") { dialog: DialogInterface?, which: Int -> }.show()
                    }
                    /*=======================삭제메뉴 클릭시 실행 동작 끝===============================*/
                }

                true
            }
            popupmenu.show()

        }

    }

}
