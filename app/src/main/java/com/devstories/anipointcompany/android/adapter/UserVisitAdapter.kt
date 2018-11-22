package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.R.id.couponLL
import com.devstories.anipointcompany.android.base.Utils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


open class UserVisitAdapter (context:Context, view:Int, data: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data


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
        var id = Utils.getString(member, "id")
        var age =   Utils.getString(member, "age")
        var name = Utils.getString(member, "name")
        var gender =   Utils.getString(member, "gender")
        var memo = Utils.getString(member, "memo")
        var phone =   Utils.getString(member, "phone")
        var coupon = Utils.getString(member, "coupon")
        var point =   Utils.getString(member, "point")
        var use_point =   Utils.getString(member, "use_point")
        var current_company_id = Utils.getString(member, "current_company_id")
        var birth =   Utils.getString(member, "birth")
        var created = Utils.getString(member, "created")
        var updated =   Utils.getString(member, "updated")
        var visit =   Utils.getString(member, "visit")


        item.ageTV.text = age
        item.nameTV.text = name
        item.name2TV.text = name
        item.genderTV.text = gender
        item.memoTV.text = memo
        item.couponTV.text = coupon
        item.pointTV.text = point
        item.use_pointTV.text = use_point
        item.birthTV.text = birth
        item.visitTV.text = visit
        item.phoneTV.text = phone





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
        var nameTV :TextView
        var pointTV :TextView
        var acc_pointTV :TextView
        var visitTV :TextView
        var name2TV :TextView
        var genderTV :TextView
        var ageTV :TextView
        var birthTV :TextView
        var use_pointTV :TextView
        var couponTV:TextView
        var visit_recordTV:TextView
        var memoTV:TextView
        var phoneTV:TextView

        init {
            dateTV = v.findViewById(R.id.dateTV)as TextView
            nameTV = v.findViewById(R.id.nameTV) as TextView
            pointTV = v.findViewById(R.id.pointTV) as TextView
            acc_pointTV = v.findViewById(R.id.acc_pointTV) as TextView
            visitTV = v.findViewById(R.id.visitTV) as TextView
            name2TV = v.findViewById(R.id.name2TV) as TextView
            genderTV = v.findViewById(R.id.genderTV) as TextView
            ageTV = v.findViewById(R.id.ageTV) as TextView
            birthTV = v.findViewById(R.id.birthTV) as TextView
            use_pointTV = v.findViewById(R.id.use_pointTV) as TextView
            couponTV = v.findViewById(R.id.couponTV) as TextView
            phoneTV = v.findViewById(R.id.phoneTV) as TextView
            visit_recordTV = v.findViewById(R.id.visit_recordTV) as TextView
            memoTV = v.findViewById(R.id.memoTV) as TextView

        }
    }
}