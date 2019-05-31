package com.example.glc.sns



//type 1 sns feed item 클래스
class SNS_list{

    var context_id =""   //글의 id
    var writer_id :String? = ""  //이메일
    var time :String? = ""   //현재시간
    var text = ""  //글내용
    var url = ""  //글 이미지 url 값값


    constructor(context_id: String, writer_id: String?, time: String, text: String, url: String ) {
        this.context_id = context_id
        this.writer_id = writer_id
        this.time = time
        this.text = text
        this.url = url
    }

    constructor()

    constructor(SNS_list :SNS_list){
        this.context_id = SNS_list.context_id
        this.writer_id = SNS_list.writer_id
        this.time = SNS_list.time
        this.text = SNS_list.text
        this.url = SNS_list.url
    }
}
//type2 댓글 클래스
class SNS_Comment{


    var comment_id = "" //댓글작성자의 아이디
    var comment_text = "" //댓글 내용

    constructor(comment_id: String, comment_text: String) {

        this.comment_id = comment_id
        this.comment_text = comment_text

    }
    constructor()
}
