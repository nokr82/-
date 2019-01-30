package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.DateUtils
import com.devstories.anipointcompany.android.base.Utils
import org.json.JSONObject
import java.util.*


open class MembershipAdapter (context:Context, view:Int, data: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

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
        val member = json.getJSONObject("Member")
        var phone = Utils.getString(member, "phone")
        var membership = Utils.getString(member, "membership")
        var membership_dt = Utils.getString(member, "membership_dt")
        var payment = Utils.getInt(member, "payment")
        var point = Utils.getInt(member, "point")
        var use_point = Utils.getInt(member, "use_point")
        var coupon = Utils.getInt(member, "coupon")

        if (phone.length==11){
            //번호하이픈
            phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7,11)
            phone = phone.substring(0, 7) + '*' + phone.substring(8)
            phone = phone.substring(0, 6) + '*' + phone.substring(7)
            phone = phone.substring(0, 5) + '*' + phone.substring(6)
        }else{
            phone =phone
        }

        item.phoneTV.text = phone
        item.membershipDtTV.text = DateUtils.getCreatedShortDate(membership_dt)

        var membershipStr = "단골"

        if (membership == "S") {
            membershipStr = "실버"
        } else if (membership == "G") {
            membershipStr = "골드"
        } else if (membership == "V") {
            membershipStr = "VIP"
        } else if (membership == "W") {
            membershipStr = "VVIP"
        }

        item.membershipTV.text = membershipStr

        item.paymentTV.text = Utils.thousand(payment) + "원"
        item.pointTV.text = Utils.thousand(point) + "P"
        item.usePointTV.text = Utils.thousand(use_point) + "P"
        item.couponTV.text = Utils.thousand(coupon) + "장"

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

        var membershipDtTV :TextView
        var membershipTV :TextView
        var paymentTV :TextView
        var pointTV :TextView
        var usePointTV :TextView
        var balanceTV :TextView
        var couponTV :TextView
        var phoneTV :TextView

        init {
            membershipDtTV = v.findViewById(R.id.membershipDtTV)as TextView
            membershipTV = v.findViewById(R.id.membershipTV) as TextView
            paymentTV = v.findViewById(R.id.paymentTV) as TextView
            pointTV = v.findViewById(R.id.pointTV) as TextView
            usePointTV = v.findViewById(R.id.usePointTV) as TextView
            balanceTV = v.findViewById(R.id.balanceTV) as TextView
            couponTV = v.findViewById(R.id.couponTV) as TextView
            phoneTV = v.findViewById(R.id.phoneTV) as TextView
        }
    }
}
