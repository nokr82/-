package com.devstories.anipointcompany.android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList

open class CouponListAdapter (context: Context, view:Int, data: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data)  {
    private lateinit var item : ViewHolder
    var view : Int = view
    var data: ArrayList<JSONObject> = data

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

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

        val couponOJ = data.get(position)

        val memberCoupon = couponOJ.getJSONObject("MemberCoupon")
        val coupon = couponOJ.getJSONObject("Coupon")

        val used = Utils.getString(memberCoupon, "use_yn")
        val del = Utils.getString(memberCoupon, "del_yn")

        if (used == "N" && del == "N") {

            val coupon_name = Utils.getString(coupon, "name")
            val coupon_type = Utils.getInt(coupon, "type")
            val coupon_s_valid = SimpleDateFormat("yyyy-MM-dd")
                    .parse(Utils.getString(memberCoupon, "s_use_date"))
            val coupon_e_valid = SimpleDateFormat("yyyy-MM-dd")
                    .parse(Utils.getString(memberCoupon, "e_use_date"))
            val coupon_message = Utils.getString(coupon, "message")

            var backgroundImg = R.mipmap.coupon

            when(coupon_type) {
                1 -> {
                    backgroundImg = R.mipmap.coupon_first
                }
                2 -> {
                    backgroundImg = R.mipmap.coupon_second
                }
                3 -> {
                    backgroundImg = R.mipmap.coupon_third
                }
                4 -> {
                    backgroundImg = R.mipmap.coupon_fourth
                }
                5 -> {
                    backgroundImg = R.mipmap.coupon_first
                }
                6 -> {
                    backgroundImg = R.mipmap.coupon_second
                }
            }

            item.couponBackLL.setBackgroundResource(backgroundImg)
            item.item_couponNameTV.text = coupon_name
            item.item_messageTV.text = coupon_message
            item.item_s_validityTV.text = coupon_s_valid.toString()
            item.item_e_validityTV.text = coupon_e_valid.toString()

            /*val coupon_week = Utils.getString(coupon, "week_use_yn")
            val coupon_sat = Utils.getString(coupon, "sat_use_yn")
            val coupon_sun = Utils.getString(coupon, "sun_use_yn")*/

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

    class ViewHolder(v : View) {
        var couponBackLL :LinearLayout
        var item_couponNameTV :TextView
        var item_s_validityTV :TextView
        var item_e_validityTV :TextView
        var item_messageTV :TextView


        init {
            couponBackLL = v.findViewById(R.id.couponBackLL)
            item_couponNameTV = v.findViewById(R.id.item_couponNameTV)
            item_s_validityTV = v.findViewById(R.id.item_s_validityTV)
            item_e_validityTV = v.findViewById(R.id.item_e_validityTV)
            item_messageTV = v.findViewById(R.id.item_messageTV)
        }
    }
}