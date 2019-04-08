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


open class ContractListAdapter (context:Context, view:Int, data: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data) {

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
        val contract = json.getJSONObject("Contract")
        var name = Utils.getString(contract, "name")
        var phone = Utils.getString(contract, "phone")
        var contract_name = Utils.getString(contract, "contract_name")
        var contract_date = Utils.getString(contract, "contract_date")

        if (phone.length==11){
            //번호하이픈
            phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7,11)

        }else{
            phone =phone
        }

        item.phoneTV.text = phone
        item.dateTV.text = contract_date
        item.nameTV.text = name
        item.contractnameTV.text = contract_name




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
        var contractnameTV = v.findViewById(R.id.contractnameTV)as TextView
        var phoneTV = v.findViewById(R.id.phoneTV)as TextView
        var dateTV = v.findViewById(R.id.dateTV)as TextView
        var nameTV = v.findViewById(R.id.nameTV)as TextView
        init {



        }
    }
}
