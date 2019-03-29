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
import com.devstories.anipointcompany.android.activities.ReservationManageFragment
import com.devstories.anipointcompany.android.activities.ReserveMemberListFragment
import com.devstories.anipointcompany.android.activities.ReserveNewMemberFragment
import com.devstories.anipointcompany.android.base.Utils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


open class ReserveListAdapter (context:Context, view:Int, data: ArrayList<JSONObject>, fragment: ReservationManageFragment) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data: ArrayList<JSONObject> = data
    var fragment: ReservationManageFragment = fragment

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
        val member = json.getJSONObject("Member")
        var phone = Utils.getString(member, "phone")
        var name = Utils.getString(member, "name")
        Log.d("제이슨",json.toString())
        val customer = json.getJSONObject("CompanyCustomer")
        var customer_name = Utils.getString(customer, "name")

        val reserve = json.getJSONObject("Reserve")
        var point = Utils.getString(reserve, "point")
        var reserve_time = Utils.getString(reserve, "reserve_time")
        var result_type = Utils.getInt(reserve, "result_type")
        var surgery_time = Utils.getString(reserve, "surgery_time")
        var surgery_name = Utils.getString(reserve, "surgery_name")




        item.customerTV.text =customer_name
        item.first_timeTV.text =reserve_time
        item.last_timeTV.text =surgery_time
        item.titleTV.text =surgery_name
        item.nameTV.text =phone
        if (result_type == 1){
            item.typeTV.text = "예약"
        }else  if (result_type == 2){
            item.typeTV.text = "예약완료"
        }else  if (result_type == 3){
            item.typeTV.text = "노쇼"
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
        var typeTV = v.findViewById(R.id.typeTV) as TextView
        var first_timeTV = v.findViewById(R.id.first_timeTV) as TextView
        var nameTV = v.findViewById(R.id.nameTV) as TextView
        var last_timeTV = v.findViewById(R.id.last_timeTV) as TextView
        var customerTV = v.findViewById(R.id.customerTV) as TextView
        var titleTV = v.findViewById(R.id.titleTV) as TextView
        init {

        }
    }
}
