package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_membership.*
import org.json.JSONException
import org.json.JSONObject

class DlgMemberShipActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var membership=""

    var company_id = -1
    var member_id = -1

    var silver = false
    var gold = false
    var vip = false
    var vvip = false

    var silver_pay = 0
    var silver_point = 0
    var silver_point_card = 0
    var silver_add_point = 0

    var gold_pay = 0
    var gold_point = 0
    var gold_point_card = 0
    var gold_add_point = 0

    var vip_pay = 0
    var vip_point = 0
    var vip_point_card = 0
    var vip_add_point = 0

    var type = -1


    var vvip_pay = 0
    var vvip_point = 0
    var vvip_point_card = 0
    var vvip_add_point = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_membership)

        this.context = this
        progressDialog = ProgressDialog(context)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        company_id = PrefUtils.getIntPreference(context, "company_id")

        silver = intent.getBooleanExtra("silver", false)
        gold = intent.getBooleanExtra("gold", false)
        vip = intent.getBooleanExtra("vip", false)
        vvip = intent.getBooleanExtra("vvip", false)

        cardLL.setOnClickListener {
            setmenu2()
            type = 2
            cardIV.setImageResource(R.drawable.radio_on)

        }
        payLL.setOnClickListener {
            setmenu2()
            type = 1
            payIV.setImageResource(R.drawable.radio_on)
        }




        silverLL.setOnClickListener {
            if (type == -1){
                Toast.makeText(context,"결제방법을 선택해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setmenu()

            silverIV.setImageResource(R.drawable.radio_on)
            payTV.text = Utils.thousand(silver_pay) + "원"
            if (type ==1){
                pointTV.text = Utils.thousand(silver_point) + "원"
            }else if (type == 2){
                pointTV.text = Utils.thousand(silver_point_card) + "원"
            }
            addPointTV.text = Utils.thousand(silver_add_point) + "%"
            membership = "실버"
        }
        goldLL.setOnClickListener {
            if (type == -1){
                Toast.makeText(context,"결제방법을 선택해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setmenu()
            goldIV.setImageResource(R.drawable.radio_on)
            payTV.text = Utils.thousand(gold_pay) + "원"
            if (type ==1){
                pointTV.text = Utils.thousand(gold_point) + "원"
            }else if (type == 2){
                pointTV.text = Utils.thousand(gold_point_card) + "원"
            }

            addPointTV.text = Utils.thousand(gold_add_point) + "%"
            membership = "골드"
        }
        vipLL.setOnClickListener {
            if (type == -1){
                Toast.makeText(context,"결제방법을 선택해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setmenu()
            vipIV.setImageResource(R.drawable.radio_on)
            payTV.text = Utils.thousand(vip_pay) + "원"
            if (type ==1){
                pointTV.text = Utils.thousand(vip_point) + "원"
            }else if (type == 2){
                pointTV.text = Utils.thousand(vip_point_card) + "원"
            }
            addPointTV.text = Utils.thousand(vip_add_point) + "%"
            membership = "VIP"
        }
        vvipLL.setOnClickListener {
            if (type == -1){
                Toast.makeText(context,"결제방법을 선택해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setmenu()
            vvipIV.setImageResource(R.drawable.radio_on)
            payTV.text = Utils.thousand(vvip_pay) + "원"
            if (type ==1){
                pointTV.text = Utils.thousand(vvip_point) + "원"
            }else if (type == 2){
                pointTV.text = Utils.thousand(vvip_point_card) + "원"
            }
            addPointTV.text = Utils.thousand(vvip_add_point) + "%"
            membership = "VVIP"
        }



        searchTV.setOnClickListener {
            val phone1 = Utils.getString(phoneNum1ET)
            val phone2 = Utils.getString(phoneNum2ET)
            val phone3 = Utils.getString(phoneNum3ET)

            if (phone1.length < 1) {
                Toast.makeText(context, "핸드폰 번호를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (phone2.length < 1) {
                Toast.makeText(context, "핸드폰 번호를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (phone3.length < 1) {
                Toast.makeText(context, "핸드폰 번호를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            searchMember(phone1 + phone2 + phone3)

        }

        saveTV.setOnClickListener {

            if (member_id < 1) {
                Toast.makeText(context, "회원을 선택해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (type < 1) {
                Toast.makeText(context, "결제방식을 선택해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (payTV.text.equals("0")) {
                Toast.makeText(context, "변경하실 등급을 선택해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            editMembership()

        }

        loadData()

    }

    fun setmenu(){
        silverIV.setImageResource(R.drawable.radio_off)
        goldIV.setImageResource(R.drawable.radio_off)
        vipIV.setImageResource(R.drawable.radio_off)
        vvipIV.setImageResource(R.drawable.radio_off)
    }
    fun setmenu2(){
        cardIV.setImageResource(R.drawable.radio_off)
        payIV.setImageResource(R.drawable.radio_off)
    }

    fun loadData() {
        val params = RequestParams()
        params.put("company_id", company_id)

        CompanyAction.company_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val company = response.getJSONObject("company")

                        silver_pay = Utils.getInt(company, "silver_pay")

                        if (silver_pay < 1) {
                            silver_pay = 0
                        }

                        silver_point = Utils.getInt(company, "silver_point")

                        if (silver_point < 1) {
                            silver_point = 0
                        }
                        silver_point_card = Utils.getInt(company, "silver_point_card")

                        if (silver_point_card < 1) {
                            silver_point_card = 0
                        }

                        silver_add_point = Utils.getInt(company, "silver_add_point")

                        if (silver_add_point < 1) {
                            silver_add_point = 0
                        }

                        gold_pay = Utils.getInt(company, "gold_pay")

                        if (gold_pay < 1) {
                            gold_pay = 0
                        }

                        gold_point = Utils.getInt(company, "gold_point")

                        if (gold_point < 1) {
                            gold_point = 0
                        }
                        gold_point_card = Utils.getInt(company, "gold_point_card")

                        if (gold_point_card < 1) {
                            gold_point_card = 0
                        }

                        gold_add_point = Utils.getInt(company, "gold_add_point")

                        if (gold_add_point < 1) {
                            gold_add_point = 0
                        }

                        vip_pay = Utils.getInt(company, "vip_pay")

                        if (vip_pay < 1) {
                            vip_pay = 0
                        }

                        vip_point = Utils.getInt(company, "vip_point")

                        if (vip_point < 1) {
                            vip_point = 0
                        }
                        vip_point_card = Utils.getInt(company, "vip_point_card")

                        if (vip_point_card < 1) {
                            vip_point_card = 0
                        }

                        vip_add_point = Utils.getInt(company, "vip_add_point")

                        if (vip_add_point < 1) {
                            vip_add_point = 0
                        }

                        vvip_pay = Utils.getInt(company, "vvip_pay")

                        if (vvip_pay < 1) {
                            vvip_pay = 0
                        }

                        vvip_point = Utils.getInt(company, "vvip_point")

                        if (vvip_point < 1) {
                            vvip_point = 0
                        }

                        vvip_point_card = Utils.getInt(company, "vvip_point_card")

                        if (vvip_point_card < 1) {
                            vvip_point_card = 0
                        }

                        vvip_add_point = Utils.getInt(company, "vvip_add_point")

                        if (vvip_add_point < 1) {
                            vvip_add_point = 0
                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }


            override fun onStart() {
                // show dialog
                if (progressDialog != null) {


                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun searchMember(phone: String) {

        val params = RequestParams()
        params.put("company_id",company_id)
        params.put("phone", phone)

        MemberAction.search(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")

                    Utils.hideKeyboard(context)

                    searchLL.visibility = View.GONE

                    member_id = -1

                    if ("ok" == result) {

                        searchLL.visibility = View.VISIBLE

                        val member = response.getJSONObject("member")

                        member_id = Utils.getInt(member, "id")

                        val membership = Utils.getString(member, "membership")
                        val name = Utils.getString(member, "name")
                        val point = Utils.getInt(member, "point")
                        var use_point = Utils.getInt(member, "use_point")
                        val balance = Utils.getInt(member, "balance")
                        val phone = Utils.getString(member, "phone")

                        var membership_str = "-"

                        if (use_point==-1){
                            use_point=0
                        }


                        if ("S" == membership) {
                            setmenu()
                            silverIV.setImageResource(R.drawable.radio_on)
                            membership_str = "실버"
                        } else if ("G" == membership) {
                            setmenu()
                            goldIV.setImageResource(R.drawable.radio_on)
                            membership_str = "골드"
                        } else if ("V" == membership) {
                            setmenu()
                            vipIV.setImageResource(R.drawable.radio_on)
                            membership_str = "VIP"
                        } else if ("W" == membership) {
                            setmenu()
                            vvipIV.setImageResource(R.drawable.radio_on)
                            membership_str = "VVIP"
                        }

                        memberMembershipTV.text = membership_str
                        memberNameTV.text = name
                        memberPointTV.text = Utils.thousand(point) + "P"
                        memberUsePointTV.text = Utils.thousand(use_point) + "P"
                        memberLeftPointTV.text = Utils.thousand(balance) + "P"

                        var r_phone = ""

                        if (phone.length == 11) {
                            //번호하이픈
                            r_phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7, 11)
                            r_phone = r_phone.substring(0, 6) + '*' + r_phone.substring(7)
                            r_phone = r_phone.substring(0, 5) + '*' + r_phone.substring(6)
                            r_phone = r_phone.substring(0, 4) + '*' + r_phone.substring(5)
                        } else {
                            r_phone = phone
                        }

                        memberPhoneTV.text = r_phone

                    } else if ("empty" == result) {
                        Toast.makeText(context, "등록된 회원이 없습니다.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "오류가 발생하였습니다.", Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }


            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null) {


                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun editMembership() {

        val params = RequestParams()
        params.put("company_id",company_id)
        params.put("member_id", member_id)
        params.put("membership", membership)
        params.put("type", type)

//        params.put("membership", memberShipSP.selectedItem)

        MemberAction.edit_membership(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")

                    Utils.hideKeyboard(context)

                    searchLL.visibility = View.GONE

                    if ("ok" == result) {
                        Toast.makeText(context, "변경되었습니다.", Toast.LENGTH_LONG).show()
                        val resultIntent = Intent()
                        setResult(RESULT_OK, resultIntent)
                        Utils.hideKeyboard(context)
                        finish()
//                        Toast.makeText(context, "변경되었습니다.", Toast.LENGTH_LONG).show()
//                        finish()

                    } else {
                        Toast.makeText(context, "오류가 발생하였습니다.", Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }



            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null) {


                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog = null
    }

}
