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


open class AnalysisAutoMessageAdapter (context:Context, view:Int, data: ArrayList<JSONObject>, type: Int) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data: ArrayList<JSONObject> = data
    var type: Int = type

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

        val message = json.getJSONObject("Message")
        val title = Utils.getString(message, "title")
        val msg = Utils.getString(message, "message")

        if(type == 1) {

            item.item_on_offIV.visibility = View.VISIBLE

            val coupon = json.getJSONObject("Coupon")

            val coupon_name= Utils.getString(coupon, "name")
            val content= Utils.getString(coupon, "content")
            val temp_yn= Utils.getString(coupon, "temp_yn")

            if(temp_yn == "Y") {
                item.item_on_offIV.setImageResource(R.mipmap.btn_off)
            } else {
                item.item_on_offIV.setImageResource(R.mipmap.btn_on)
            }

            item.itemEventTitleTV.text = coupon_name
            item.itemEventContTV.text = content

        } else {
            item.item_on_offIV.visibility = View.GONE

            item.itemEventTitleTV.text = title
            item.itemEventContTV.text = msg
        }

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

        var item_on_offIV :ImageView
        var itemEventTitleTV :TextView
        var itemEventContTV :TextView

        init {
            item_on_offIV = v.findViewById(R.id.item_on_offIV) as ImageView
            itemEventTitleTV = v.findViewById(R.id.itemEventTitleTV) as TextView
            itemEventContTV = v.findViewById(R.id.itemEventContTV) as TextView
        }
    }
}
