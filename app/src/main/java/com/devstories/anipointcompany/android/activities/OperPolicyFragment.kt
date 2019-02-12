package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.X
import android.view.ViewGroup
import android.widget.*
import com.devstories.aninuriandroid.adapter.MembershipAdapter
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_oper_policy.*
import org.json.JSONException
import org.json.JSONObject
import android.view.MotionEvent
import android.view.View.OnTouchListener



//설정 -운영정책
class OperPolicyFragment : Fragment(), AbsListView.OnScrollListener {


    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var left_pointTV: TextView
    lateinit var min_pointTV: EditText
    lateinit var rdo1000wonIV: ImageView
    lateinit var rdo500wonIV: ImageView
    lateinit var rdo100wonIV: ImageView
    lateinit var visitcntTV: EditText
    lateinit var costTV: EditText
    lateinit var vsitIV: ImageView
    lateinit var costIV: ImageView
    lateinit var accountTV: TextView
    lateinit var visitLL: LinearLayout
    lateinit var accountLL: LinearLayout
    lateinit var saveTV: TextView
    lateinit var high_perET: EditText
    lateinit var basic_perET: EditText
    lateinit var a_visitLL: LinearLayout
    lateinit var a_costLL: LinearLayout

    lateinit var silverPayET:EditText
    lateinit var silverPointET:EditText
    lateinit var silverAddPointET:EditText
    lateinit var goldPayET:EditText
    lateinit var goldPointET:EditText
    lateinit var goldAddPointET:EditText
    lateinit var vipPayET:EditText
    lateinit var vipPointET:EditText
    lateinit var vipAddPointET:EditText
    lateinit var vvipPayET:EditText
    lateinit var vvipPointET:EditText
    lateinit var vvipAddPointET:EditText

    lateinit var memberShipTV:TextView

    lateinit var membershipLV:ListView
    lateinit var membershipAdapter:MembershipAdapter

    lateinit var customerLL:LinearLayout
    lateinit var silverLL:LinearLayout
    lateinit var goldLL:LinearLayout
    lateinit var vipLL:LinearLayout
    lateinit var vvipLL:LinearLayout

    var type = -1//단골기준
    var money_type = -1//단골기준
    var company_id = 1//단골기준

    var silver = false
    var gold = false
    var vip = false
    var vvip = false
    var membership_type = "A"
    var membershipData:ArrayList<JSONObject> = ArrayList<JSONObject>()

    var page = 1
    var totalPage = 1
    private val visibleThreshold = 10
    private var userScrolled = false
    private var lastItemVisibleFlag = false
    private var lastcount = 0
    private var totalItemCountScroll = 0

    var EDIT_MEMBER = 102

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_oper_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        left_pointTV = view.findViewById(R.id.left_pointTV)
        min_pointTV = view.findViewById(R.id.min_pointTV)
        rdo1000wonIV = view.findViewById(R.id.rdo1000wonIV)
        rdo500wonIV = view.findViewById(R.id.rdo500wonIV)
        rdo100wonIV = view.findViewById(R.id.rdo100wonIV)
        visitcntTV = view.findViewById(R.id.visitcntTV)
        costTV = view.findViewById(R.id.costTV)
        vsitIV = view.findViewById(R.id.vsitIV)
        costIV = view.findViewById(R.id.costIV)
        accountTV = view.findViewById(R.id.accountTV)
        visitLL = view.findViewById(R.id.visitLL)
        accountLL = view.findViewById(R.id.accountLL)
        saveTV = view.findViewById(R.id.saveTV)
        high_perET = view.findViewById(R.id.high_perET)
        basic_perET = view.findViewById(R.id.basic_perET)
        a_visitLL= view.findViewById(R.id.a_visitLL)
        a_costLL= view.findViewById(R.id.a_costLL)

        silverPayET = view.findViewById(R.id.silverPayET)
        silverPointET = view.findViewById(R.id.silverPointET)
        silverAddPointET = view.findViewById(R.id.silverAddPointET)
        goldPayET = view.findViewById(R.id.goldPayET)
        goldPointET = view.findViewById(R.id.goldPointET)
        goldAddPointET = view.findViewById(R.id.goldAddPointET)
        vipPayET = view.findViewById(R.id.vipPayET)
        vipPointET = view.findViewById(R.id.vipPointET)
        vipAddPointET = view.findViewById(R.id.vipAddPointET)
        vvipPayET = view.findViewById(R.id.vvipPayET)
        vvipPointET = view.findViewById(R.id.vvipPointET)
        vvipAddPointET = view.findViewById(R.id.vvipAddPointET)

        memberShipTV = view.findViewById(R.id.memberShipTV)
        membershipLV = view.findViewById(R.id.membershipLV)

        customerLL = view.findViewById(R.id.customerLL)
        silverLL = view.findViewById(R.id.silverLL)
        goldLL = view.findViewById(R.id.goldLL)
        vipLL = view.findViewById(R.id.vipLL)
        vvipLL = view.findViewById(R.id.vvipLL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(context, "company_id")
        company_info()

        membershipAdapter = MembershipAdapter(myContext, R.layout.item_membership, membershipData)
        membershipLV.adapter = membershipAdapter
        membershipLV.setOnScrollListener(this)

        membershipLV.setOnTouchListener(OnTouchListener { v, event ->
            membershipLV.requestDisallowInterceptTouchEvent(true)

            false
        })







        rdo1000wonIV.setOnClickListener {
            setmenu()
            money_type = 3
            rdo1000wonIV.setImageResource(R.drawable.radio_on)
        }
        rdo500wonIV.setOnClickListener {
            setmenu()
            money_type = 2
            rdo500wonIV.setImageResource(R.drawable.radio_on)
        }
        rdo100wonIV.setOnClickListener {
            setmenu()
            money_type = 1
            rdo100wonIV.setImageResource(R.drawable.radio_on)
        }


        a_costLL.setOnClickListener {
            setmenu2()
            costIV.setImageResource(R.drawable.radio_on)
            accountLL.visibility = View.VISIBLE
            type = 2
        }
        a_visitLL.setOnClickListener {
            setmenu2()
            vsitIV.setImageResource(R.drawable.radio_on)
            visitLL.visibility = View.VISIBLE
            type = 1
        }
        saveTV.setOnClickListener {
            edit_info()
        }

        memberShipTV.setOnClickListener {

            if(!silver && !gold && !vip && !vvip) {
                Toast.makeText(context, "멤버십 정보를 입력 후 이용해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var intent = Intent(context, DlgMemberShipActivity::class.java)
            intent.putExtra("silver", silver)
            intent.putExtra("gold", gold)
            intent.putExtra("vip", vip)
            intent.putExtra("vvip", vvip)
            startActivityForResult(intent,EDIT_MEMBER)
        }

        customerLL.setOnClickListener {
            page = 1
            membership_type = "C"
            membership_list()
        }

        silverLL.setOnClickListener {
            page = 1
            membership_type = "S"
            membership_list()
        }

        goldLL.setOnClickListener {
            page = 1
            membership_type = "G"
            membership_list()
        }

        vipLL.setOnClickListener {
            page = 1
            membership_type = "V"
            membership_list()
        }

        vvipLL.setOnClickListener {
            page = 1
            membership_type = "W"
            membership_list()
        }

        membership_list()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EDIT_MEMBER -> {
                if (resultCode == Activity.RESULT_OK) {
                    membership_list()
                }
            }
        }


    }

    fun setmenu() {
        rdo1000wonIV.setImageResource(R.drawable.radio_off)
        rdo500wonIV.setImageResource(R.drawable.radio_off)
        rdo100wonIV.setImageResource(R.drawable.radio_off)

    }

    fun setmenu2() {
        costIV.setImageResource(R.drawable.radio_off)
        vsitIV.setImageResource(R.drawable.radio_off)
        visitLL.visibility = View.GONE
        accountLL.visibility = View.GONE
    }

    //사업체 정보뽑기
    fun company_info() {
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
                        var coin = response.getString("coin")
                        left_pointTV.text = coin

                        val company = response.getJSONObject("company")
                        // 최소사용포인트
                        val min_use_point = Utils.getString(company, "min_use_point")
                        //최소사용포인트 단위
                        val use_point_unit = Utils.getInt(company, "use_point_unit")
                        //1  N-단골기준(X) 2  V-방문기준 3  P-금액기준
                        val frequenter_type = Utils.getString(company, "frequenter_type")
                        //단골금액/몇회
                        val frequenter_standard = Utils.getString(company, "frequenter_standard")
                        // 최소사용포인트
                        val basic_per = Utils.getString(company, "basic_per")
                        //최소사용포인트 단위
                        val option_per = Utils.getString(company, "option_per")

                        basic_perET.setText(basic_per)
                        high_perET.setText(option_per)

                        min_pointTV.setText(min_use_point)
                        if (use_point_unit == 100) {
                            rdo100wonIV.callOnClick()
                        } else if (use_point_unit == 500) {
                            rdo500wonIV.callOnClick()
                        } else if (use_point_unit == 1000) {
                            rdo1000wonIV.callOnClick()
                        }

                        if (frequenter_type.equals("N")) {

                        } else if (frequenter_type.equals("V")) {
                            a_visitLL.callOnClick()
                            visitcntTV.setText(frequenter_standard)
                        } else if (frequenter_type.equals("P")) {
                            a_costLL.callOnClick()
                            costTV.setText(frequenter_standard)
                        }

                        val cate = response.getJSONArray("categories")
                        Log.d("카테", cate.toString())
                        for (i in 0..cate.length() - 1) {

                        }







                        val silver_pay = Utils.getInt(company, "silver_pay")

                        if (silver_pay > 0) {
                            silverPayET.setText(Utils.comma(silver_pay.toString()))
                            silver = true
                        }

                        val silver_point = Utils.getInt(company, "silver_point")

                        if (silver_point > 0) {
                            silverPointET.setText(Utils.comma(silver_point.toString()))
                        }

                        val silver_add_point = Utils.getInt(company, "silver_add_point")

                        if (silver_add_point > 0) {
                            silverAddPointET.setText(silver_add_point.toString())
                        }

                        val gold_pay = Utils.getInt(company, "gold_pay")

                        if (gold_pay > 0) {
                            goldPayET.setText(Utils.comma(gold_pay.toString()))
                            gold = true
                        }

                        val gold_point = Utils.getInt(company, "gold_point")

                        if (gold_point > 0) {
                            goldPointET.setText(Utils.comma(gold_point.toString()))
                        }

                        val gold_add_point = Utils.getInt(company, "gold_add_point")

                        if (gold_add_point > 0) {
                            goldAddPointET.setText(gold_add_point.toString())
                        }

                        val vip_pay = Utils.getInt(company, "vip_pay")

                        if (vip_pay > 0) {
                            vipPayET.setText(Utils.comma(vip_pay.toString()))
                            vip = true
                        }

                        val vip_point = Utils.getInt(company, "vip_point")

                        if (vip_point > 0) {
                            vipPointET.setText(Utils.comma(vip_point.toString()))
                        }

                        val vip_add_point = Utils.getInt(company, "vip_add_point")

                        if (vip_add_point > 0) {
                            vipAddPointET.setText(vip_add_point.toString())
                        }

                        val vvip_pay = Utils.getInt(company, "vvip_pay")

                        if (vvip_pay > 0) {
                            vvipPayET.setText(Utils.comma(vvip_pay.toString()))
                            vvip = true
                        }

                        val vvip_point = Utils.getInt(company, "vvip_point")

                        if (vvip_point > 0) {
                            vvipPointET.setText(Utils.comma(vvip_point.toString()))
                        }

                        val vvip_add_point = Utils.getInt(company, "vvip_add_point")

                        if (vvip_add_point > 0) {
                            vvipAddPointET.setText(vvip_add_point.toString())
                        }

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }


            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(myContext, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    responseString: String?,
                    throwable: Throwable
            ) {
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

    //사업체 정보수정
    fun edit_info() {
        val min_use_point = Utils.getString(min_pointTV)
        var frequenter_standard: String = ""
        var use_point_unit: String = ""//사용단위
        var frequenter_type: String = ""//단골기준


        if (money_type == 1) {
            use_point_unit = "100"
        } else if (money_type == 2) {
            use_point_unit = "500"
        } else if (money_type == 3) {
            use_point_unit = "1000"
        }


        if (type == 1) {
            frequenter_type = "V"
            frequenter_standard = Utils.getString(visitcntTV)
        } else if (type == 2) {
            frequenter_type = "P"
            frequenter_standard = Utils.getString(costTV)
        } else {
            frequenter_type = "N"
        }
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("min_use_point", min_use_point)
        Log.d("최소", use_point_unit)
        params.put("frequenter_standard", frequenter_standard)
        Log.d("단골", frequenter_standard)

        params.put("frequenter_type", frequenter_type)
        params.put("use_point_unit", use_point_unit)
        params.put("basic_per", Utils.getString(basic_perET))
        params.put("option_per", Utils.getString(high_perET))

        params.put("silver_pay", Utils.getString(silverPayET).replace(",",""))
        params.put("silver_point", Utils.getString(silverPointET).replace(",",""))
        params.put("silver_add_point", Utils.getString(silverAddPointET).replace(",",""))
        params.put("gold_pay", Utils.getString(goldPayET).replace(",",""))
        params.put("gold_point", Utils.getString(goldPointET).replace(",",""))
        params.put("gold_add_point", Utils.getString(goldAddPointET).replace(",",""))
        params.put("vip_pay", Utils.getString(vipPayET).replace(",",""))
        params.put("vip_point", Utils.getString(vipPointET).replace(",",""))
        params.put("vip_add_point", Utils.getString(vipAddPointET).replace(",",""))
        params.put("vvip_pay", Utils.getString(vvipPayET).replace(",",""))
        params.put("vvip_point", Utils.getString(vvipPointET).replace(",",""))
        params.put("vvip_add_point", Utils.getString(vvipAddPointET).replace(",",""))

        CompanyAction.edit_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        Toast.makeText(myContext, "수정완료", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(myContext, "수정실패", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }


            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(myContext, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    responseString: String?,
                    throwable: Throwable
            ) {
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

    fun membership_list() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("membership_type", membership_type)
        params.put("page", page)

        CompanyAction.membership_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        totalPage = Utils.getInt(response, "totalPage")

                        if (page == 1) {
                            membershipData.clear()
                            membershipAdapter.notifyDataSetChanged()
                        }

                        val list = response.getJSONArray("list")

                        for (i in 0 until list.length()) {
                            membershipData.add(list[i] as JSONObject)
                        }

                        membershipAdapter.notifyDataSetChanged()

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }


            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(myContext, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    responseString: String?,
                    throwable: Throwable
            ) {
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

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            userScrolled = true
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
            userScrolled = false

            //화면이 바닥에 닿았을때
            if (totalPage > page) {
                page++
                lastcount = totalItemCountScroll

                membership_list()
            }
        }
    }

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

        if (userScrolled && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold && page < totalPage && totalPage > 0) {
            if (totalPage > page) {
            }
        }

        //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem)
        // + 현재 화면에 보이는 리스트 아이템의갯수(visibleItemCount)가
        // 리스트 전체의 갯수(totalOtemCount)-1 보다 크거나 같을때
        lastItemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
        totalItemCountScroll = totalItemCount
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}
