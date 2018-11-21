package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R


open class UserListAdapter (val context: Context, val data:ArrayList<Int>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        lateinit var item: ViewHolder

        lateinit var retView: View

        val view: View = LayoutInflater.from(context).inflate(R.layout.item_user_point_list, null)







        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return data.size
    }

    class ViewHolder(v: View) {

        var couponLL: LinearLayout


        init {
            couponLL = v.findViewById(R.id.couponLL) as LinearLayout

        }
    }

}


