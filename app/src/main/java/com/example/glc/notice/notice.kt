package com.example.glc.notice


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