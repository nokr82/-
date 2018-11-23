package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.R.id.*
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

        Log.d("데이터",data.toString())

        val member = json.getJSONObject("Member")

        val userView = View.inflate(context, R.layout.item_user, null)
        var dateTV :TextView = userView.findViewById(R.id.dateTV)
        var nameTV :TextView= userView.findViewById(R.id.nameTV)
        var pointTV :TextView= userView.findViewById(R.id.pointTV)
        var acc_pointTV :TextView= userView.findViewById(R.id.acc_pointTV)
        var visitTV :TextView= userView.findViewById(R.id.visitTV)
        var name2TV :TextView= userView.findViewById(R.id.name2TV)
        var genderTV :TextView= userView.findViewById(R.id.genderTV)
        var ageTV :TextView= userView.findViewById(R.id.ageTV)
        var birthTV :TextView= userView.findViewById(R.id.birthTV)
        var use_pointTV :TextView= userView.findViewById(R.id.use_pointTV)
        var couponTV:TextView= userView.findViewById(R.id.couponTV)
        var visit_recordTV:TextView= userView.findViewById(R.id.visit_recordTV)
        var memoTV:TextView= userView.findViewById(R.id.memoTV)
        var phoneTV:TextView= userView.findViewById(R.id.phoneTV)

        var id = Utils.getString(member, "id")
        var age =   Utils.getString(member, "age")
        var name = Utils.getString(member, "name")
        var gender =   Utils.getString(member, "gender")
        var memo = Utils.getString(member, "memo")
        var phone =   Utils.getString(member, "phone")
        var coupon = Utils.getString(member, "coupon")
        var point =   Utils.getString(member, "point")
        var use_point =   Utils.getString(member, "use_point")
        var company_id = Utils.getString(member, "company_id")
        var birth =   Utils.getString(member, "birth")
        var created = Utils.getString(member, "created")
        var updated =   Utils.getString(member, "updated")
        var visit =   Utils.getString(member, "visit")


        ageTV.text = age
        nameTV.text = name
       name2TV.text = name
        genderTV.text = gender
        memoTV.text = memo
        couponTV.text = coupon
        pointTV.text = point
        use_pointTV.text = use_point
        birthTV.text = birth
        visitTV.text = visit
        phoneTV.text = phone


        item.userLL.addView(userView)




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
        var userLL :LinearLayout


        init {
            userLL = v.findViewById(R.id.userLL)as LinearLayout

        }
    }
}