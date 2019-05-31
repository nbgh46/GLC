package com.example.glc

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.glc.Cafeteria.CateferiaFragment
import com.example.glc.Weather.WeatherFragment
import com.example.glc.notice.NoticeFragment
import com.example.glc.sns.SNSFragment
import com.example.glc.subway.SubwayFragement

/*
      메인 화면에 뷰페이저 어댑터
      탭 1. sns  탭 2.지하철 탭3 .학식 탭4. 공지사항 탭5.날씨
 */

class MainPageAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){

    //탭 이름 배열 선언
    val tabTitle  = arrayOf("SNS","자히철","학식","공지사항","날씨")

    //탭 숫자
    override fun getCount(): Int {
        return 5
    }

    //탭 타이틀
    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitle[position]
    }
    //viewpager 에 각 포지션에 그려야할 fragment 를 반환
    override fun getItem(p0: Int): Fragment {
        //프레그먼트에서 newInstance() 메서드를 통해서 필요한 파라미터를 전달
        when(p0){
            0-> return SNSFragment.newInstance()
            1 -> return SubwayFragement.newInstance()
            2 -> return CateferiaFragment.newInstance()
            3 -> return NoticeFragment.newInstance()
            4 -> return WeatherFragment.newInstance()
            else-> return SNSFragment.newInstance()
        }
    }
}



