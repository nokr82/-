package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.R.id.dateTV
import com.devstories.anipointcompany.android.R.id.updateTV
import com.devstories.anipointcompany.android.base.Utils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


open class UserListAdapter (context:Context, view:Int, data: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

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


        val point_o = json.getJSONObject("Point")
        var point = Utils.getString(point_o, "point")
        var type = Utils.getInt(point_o, "type")
        var member_coupon_id = Utils.getInt(point_o, "member_coupon_id")
        var updated = Utils.getString(point_o, "created")
        var membership_per = Utils.getInt(point_o, "membership_per")
        var cate = Utils.getInt(point_o, "cate")




        if (phone.length==11){
            //번호하이픈
            phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7,11)
            phone = phone.substring(0, 7) + '*' + phone.substring(8)
            phone = phone.substring(0, 6) + '*' + phone.substring(7)
            phone = phone.substring(0, 5) + '*' + phone.substring(6)
        }else{
            phone =phone
        }

        if (member_coupon_id != -1){
            if (type==1){
                if (membership_per != -1){
                    item.typeTV.text = "멤버쉽 포인트 적립"
                }else{
                    item.typeTV.text = "포인트 적립"
                }

                if (point == "-1"){
                    item.pointTV.text ="0"+"P"
                }else{
                    item.pointTV.text = Utils.comma(point)+"P"
                }
            }else{
                item.typeTV.text = "쿠폰 사용"
                val coupon = json.getJSONObject("MemberCoupon")
                var coupon_name = Utils.getString(coupon, "coupon_name")
                item.pointTV.text =coupon_name
            }
            item.nameTV.text = phone
            item.updateTV.text =updated
        }else{
            item.nameTV.text = phone
            if (point == "-1"){
                item.pointTV.text ="0"+"P"
            }else{
                item.pointTV.text = Utils.comma(point)+"P"
            }
            item.updateTV.text =updated

            if (type==1){
                if (membership_per != -1){
                    item.typeTV.text = "멤버쉽 포인트 적립"
                }else{
                    item.typeTV.text = "포인트 적립"
                }
            }else{
                item.typeTV.text = "포인트 사용"
            }

            if(cate == 3){
                item.typeTV.text = "포인트 환불"
            }else if (cate == 4){
                item.typeTV.text = "멤버쉽 결제"
                val membership = json.getJSONObject("MemberShip")
                var membership_name = Utils.getString(membership, "membership")
                if (membership_name.equals("S")){
                    item.pointTV.text ="실버 결제"
                }else if (membership_name.equals("G")){
                    item.pointTV.text ="골드 결제"
                }else if (membership_name.equals("V")){
                    item.pointTV.text ="VIP 결제"
                }else if (membership_name.equals("W")){
                    item.pointTV.text ="VVIP 결제"
                }
            }else if (cate == 5){
                item.typeTV.text = "멤버쉽 추가 적립"
            }

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
        var updateTV :TextView
        var nameTV :TextView
        var pointTV :TextView
        var typeTV :TextView
        init {
            updateTV = v.findViewById(R.id.updateTV)as TextView
            nameTV = v.findViewById(R.id.nameTV) as TextView
            pointTV = v.findViewById(R.id.pointTV) as TextView
            typeTV = v.findViewById(R.id.typeTV) as TextView

        }
    }
}
