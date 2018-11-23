package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.R.id.*
import com.devstories.anipointcompany.android.base.Utils
import org.json.JSONObject
import java.util.ArrayList


open class VisitListAdapter (context:Context, view:Int, data: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data: ArrayList<JSONObject> = data


    override fun getView(position: Int, convertView: View?, parent : ViewGroup?): View {

        lateinit var retView: View

        if (convertView == null) {
            retView = View.inflate(context, view, null)
            item = ViewHolder(retView)
            retView.tag = item
        } else {
            retView = convertView
            item = convertView.tag as ViewHolder
            if (item == null) {
                retView = View.inflate(context, view, null)
                item = ViewHolder(retView)
                retView.tag = item
            }
        }

        var json = data.get(position)
        val visit_count = json.getJSONObject("visit_count")

        item.new_userTV.text = visit_count.toString()
        item.re_userTV.text = visit_count.toString()
        item.all_userTV.text = visit_count.toString()


        return retView
    }

    override fun getItem(position: Int): JSONObject {
        return data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.count()
    }
    class ViewHolder(v: View) {
        var dateTV :TextView
        var new_userTV :TextView
        var re_userTV :TextView
        var all_userTV :TextView


        init {
            dateTV = v.findViewById(R.id.dateTV)as TextView
            new_userTV = v.findViewById(R.id.new_userTV) as TextView
            re_userTV = v.findViewById(R.id.re_userTV)as TextView
            all_userTV = v.findViewById(R.id.all_userTV) as TextView

        }
    }
}
