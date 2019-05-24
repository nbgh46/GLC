package com.example.glc.sns

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.glc.R
import kotlinx.android.synthetic.main.sns_comment_item_list.view.*

class SNS_Comment_Adapter ( ListComment : ArrayList<SNS_Comment?> ) : BaseAdapter(){
    var this_list_Comment = ListComment




    override fun getItem(position: Int): SNS_Comment? {
            return this_list_Comment.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val mInflater : LayoutInflater = LayoutInflater.from(parent?.context)
        val view = convertView ?: mInflater.inflate(R.layout.sns_comment_item_list,parent,false)

/*
        view.comment_id.text = this_list_Comment.get(position)?.comment_id
        view.comment_txt.text = this_list_Comment.get(position)?.comment_text
*/
        view.comment_id.text = getItem(position)?.comment_id
        view.findViewById<TextView>(R.id.comment_txt).text = getItem(position)?.comment_text

        return view


    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return this_list_Comment.size
        }
}