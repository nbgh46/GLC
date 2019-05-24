package com.example.glc.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.glc.R
import kotlinx.android.synthetic.main.notice_list_item.view.*

class noitceListAdapter( noticeList : ArrayList<notice>?) : BaseAdapter(){
    var noticeList_adapter = noticeList

    override fun getCount(): Int {
        return noticeList_adapter!!.size
    }

    override fun getItem(position: Int): notice {
        return  noticeList_adapter!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val notice_inflater = LayoutInflater.from(parent?.context)
        val view = convertView ?: notice_inflater.inflate(R.layout.notice_list_item,parent,false)

        view.notice_tv_1.text = noticeList_adapter?.get(position)?.context
        view.notice_tv_2.text = noticeList_adapter?.get(position)?.date
        //view.notice_tv_3.text = noticeList_adapter?.get(position)?.url


        return view
    }





}