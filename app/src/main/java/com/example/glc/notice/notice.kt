package com.example.glc.notice

/*
    공지사항 class
    -공지사항을 파싱해서 받아올 정보를  받을 클래스
 */


class notice {

    var context :String="" //글 내용
    var url : String = ""   //클릭시 이동
    var date :String =""    //작성일자
    constructor()
    constructor(context: String, url: String, date: String) {
        this.context = context
        this.url = url
        this.date = date
    }


}