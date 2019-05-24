package com.example.glc.subway

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "realtimeStationArrival", strict = false)
class Subway {
    @get:ElementList(inline = true , required = false)
    @set:ElementList(inline = true , required = false)
    internal var Subway_info_list: List<Subway_detailinfo>? = null
}

    @Root(name="row" , strict = false)
    internal  class Subway_detailinfo{

     @set:Element(name="rowNum")
     @get:Element(name="rowNum")
     var  rowNum : String =""  //번호

        @set:Element(name="selectedCount")
        @get:Element(name="selectedCount")
        var  selectedCount : String =""  //번호

        @set:Element(name="totalCount")
        @get:Element(name="totalCount")
        var  totalCount : String =""  //번호

        @set:Element(name="subwayId")
        @get:Element(name="subwayId")
        var  subwayId : String =""  //번호

        @set:Element(name="updnLine")
        @get:Element(name="updnLine")
        var  updnLine : String =""  //번호

        @set:Element(name="trainLineNm")
        @get:Element(name="trainLineNm")
        var  trainLineNm: String =" "//~~행 ~방면


        @set:Element(name="subwayHeading")
        @get:Element(name="subwayHeading")
        var  subwayHeading : String =""  //번호

        @set:Element(name="statnFid")
        @get:Element(name="statnFid")
        var  statnFid : String =""  //번호

       @set:Element(name="statnId")
       @get:Element(name="statnId")
       var  statnId : String =""  //번호

        @set:Element(name="statnTid")
        @get:Element(name="statnTid")
        var  statnTid : String =""  //번호
        @set:Element(name="statnNm")
        @get:Element(name="statnNm")
        var  statnNm : String =""  //번호

        @set:Element(name="ordkey")
        @get:Element(name="ordkey")
        var  ordkey : String =""  //번호

        @set:Element(name="subwayList")
        @get:Element(name="subwayList")
        var  subwayList : String =""  //번호

        @set:Element(name="statnList" )
        @get:Element(name="statnList" )
        var  statnList : String =""  //번호

        @set:Element(name="barvlDt")
        @get:Element(name="barvlDt")
        var  barvlDt : String =""  //번호

        @set:Element(name="btrainNo")
        @get:Element(name="btrainNo")
        var  btrainNo : String =""  //번호

        @set:Element(name="bstatnId")
        @get:Element(name="bstatnId")
        var  bstatnId : String =""  //번호

        @set:Element(name="btrainSttus", required = false)
        @get:Element(name="btrainSttus", required = false)
        var  btrainSttus : String =""  //번호

        @set:Element(name="bstatnNm")
        @get:Element(name="bstatnNm")
        var  bstatnNm : String =""  //번호

        @set:Element(name="recptnDt")
        @get:Element(name="recptnDt")
        var  recptnDt : String =""  //번호

        @set:Element(name="arvlMsg2")
        @get:Element(name="arvlMsg2")
        var  arvlMsg2 : String =""// 현재 위치

        @set:Element(name="arvlMsg3")
        @get:Element(name="arvlMsg3")
        var  arvlMsg3 : String =""// 현재 위치

        @set:Element(name="arvlCd")
        @get:Element(name="arvlCd")
        var  arvlCd : String =""// 현재 위치


        constructor()
        constructor(
            rowNum: String,
            selectedCount: String,
            totalCount: String,
            subwayId: String,
            updnLine: String,
            trainLineNm: String,
            subwayHeading: String,
            statnFid: String,
            statnId: String,
            statnTid: String,
            statnNm: String,
            ordkey: String,
            subwayList: String,
            statnList: String,
            barvlDt: String,
            btrainNo: String,
            bstatnId: String,
            btrainSttus: String,
            bstatnNm: String,
            recptnDt: String,
            arvlMsg2: String,
            arvlMsg3: String,
            arvlCd: String
        ) {
            this.rowNum = rowNum
            this.selectedCount = selectedCount
            this.totalCount = totalCount
            this.subwayId = subwayId
            this.updnLine = updnLine
            this.trainLineNm = trainLineNm
            this.subwayHeading = subwayHeading
            this.statnFid = statnFid
            this.statnId = statnId
            this.statnTid = statnTid
            this.statnNm = statnNm
            this.ordkey = ordkey
            this.subwayList = subwayList
            this.statnList = statnList
            this.barvlDt = barvlDt
            this.btrainNo = btrainNo
            this.bstatnId = bstatnId
            this.btrainSttus = btrainSttus
            this.bstatnNm = bstatnNm
            this.recptnDt = recptnDt
            this.arvlMsg2 = arvlMsg2
            this.arvlMsg3 = arvlMsg3
            this.arvlCd = arvlCd
        }

        constructor(
            rowNum: String,
            selectedCount: String,
            totalCount: String,
            subwayId: String,
            updnLine: String,
            trainLineNm: String,
            subwayHeading: String,
            statnFid: String,
            statnId: String,
            statnTid: String,
            statnNm: String,
            ordkey: String,
            subwayList: String,
            statnList: String,
            barvlDt: String,
            btrainNo: String,
            bstatnId: String,
            bstatnNm: String,
            recptnDt: String,
            arvlMsg2: String,
            arvlMsg3: String,
            arvlCd: String
        ) {
            this.rowNum = rowNum
            this.selectedCount = selectedCount
            this.totalCount = totalCount
            this.subwayId = subwayId
            this.updnLine = updnLine
            this.trainLineNm = trainLineNm
            this.subwayHeading = subwayHeading
            this.statnFid = statnFid
            this.statnId = statnId
            this.statnTid = statnTid
            this.statnNm = statnNm
            this.ordkey = ordkey
            this.subwayList = subwayList
            this.statnList = statnList
            this.barvlDt = barvlDt
            this.btrainNo = btrainNo
            this.bstatnId = bstatnId
            this.bstatnNm = bstatnNm
            this.recptnDt = recptnDt
            this.arvlMsg2 = arvlMsg2
            this.arvlMsg3 = arvlMsg3
            this.arvlCd = arvlCd
        }


    }


