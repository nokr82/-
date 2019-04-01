package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.R.id.dateTV
import com.devstories.anipointcompany.android.R.id.updateTV
import com.devstories.anipointcompany.android.activities.ReserveMemberListFragment
import com.devstories.anipointcompany.android.activities.ReserveNewMemberFragment
import com.devstories.anipointcompany.android.base.Utils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


open class MemberListAdapter (context:Context, view:Int, data: ArrayList<JSONObject>,fragment: ReserveMemberListFragment) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data: ArrayList<JSONObject> = data
    var fragment: ReserveMemberListFragment = fragment

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
        Log.d("제이슨",json.toString())
        val member = json.getJSONObject("Member")
        var member_id = Utils.getInt(member, "id")
        var phone = Utils.getString(member, "phone")
        var name = Utils.getString(member, "name")
        var customer_name = Utils.getString(member, "customer_name")
        var noshow_cnt = Utils.getInt(member, "noshow_cnt")

        Log.d("제이슨",json.toString())
        Log.d("제이슨",customer_name.toString())
        if (fragment.member_id ==member_id){
            item.radioIV.setImageResource(R.drawable.radio_on)
        }else{
            item.radioIV.setImageResource(R.drawable.radio_off)
        }
        item.m_nameTV.text = customer_name
        item.nameTV.text = name
        item.phoneTV.text = phone
        item.cntTV.text = noshow_cnt.toString()
        val point_o = json.getJSONObject("Point")
        var point = Utils.getString(point_o, "point")
        var type = Utils.getInt(point_o, "type")
        var member_coupon_id = Utils.getInt(point_o, "member_coupon_id")
        var updated = Utils.getString(point_o, "created")
        var membership_per = Utils.getInt(point_o, "membership_per")
        var cate = Utils.getInt(point_o, "cate")





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
        var radioIV = v.findViewById(R.id.radioIV) as ImageView
        var nameTV = v.findViewById(R.id.nameTV) as TextView
        var phoneTV = v.findViewById(R.id.phoneTV) as TextView
        var m_nameTV = v.findViewById(R.id.m_nameTV) as TextView
        var cntTV = v.findViewById(R.id.cntTV) as TextView
        init {

        }
    }
}
