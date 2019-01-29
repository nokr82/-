package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
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

//설정 -운영정책
class OperPolicyFragment : Fragment() {


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

    var type = -1//단골기준
    var money_type = -1//단골기준
    var company_id = 1//단골기준


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

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(context, "company_id")
        company_info()

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
                            silverPayET.setText(silver_pay.toString())
                        }

                        val silver_point = Utils.getInt(company, "silver_point")

                        if (silver_point > 0) {
                            silverPointET.setText(silver_point.toString())
                        }

                        val silver_add_point = Utils.getInt(company, "silver_add_point")

                        if (silver_add_point > 0) {
                            silverAddPointET.setText(silver_add_point.toString())
                        }

                        val gold_pay = Utils.getInt(company, "gold_pay")

                        if (gold_pay > 0) {
                            goldPayET.setText(gold_pay.toString())
                        }

                        val gold_point = Utils.getInt(company, "gold_point")

                        if (gold_point > 0) {
                            goldPointET.setText(gold_point.toString())
                        }

                        val gold_add_point = Utils.getInt(company, "gold_add_point")

                        if (gold_add_point > 0) {
                            goldAddPointET.setText(gold_add_point.toString())
                        }

                        val vip_pay = Utils.getInt(company, "vip_pay")

                        if (vip_pay > 0) {
                            vipPayET.setText(vip_pay.toString())
                        }

                        val vip_point = Utils.getInt(company, "vip_point")

                        if (vip_point > 0) {
                            vipPointET.setText(vip_point.toString())
                        }

                        val vip_add_point = Utils.getInt(company, "vip_add_point")

                        if (vip_add_point > 0) {
                            vipAddPointET.setText(vip_add_point.toString())
                        }

                        val vvip_pay = Utils.getInt(company, "vvip_pay")

                        if (vvip_pay > 0) {
                            vvipPayET.setText(vvip_pay.toString())
                        }

                        val vvip_point = Utils.getInt(company, "vvip_point")

                        if (vvip_point > 0) {
                            vvipPointET.setText(vvip_point.toString())
                        }

                        val vvip_add_point = Utils.getInt(company, "vvip_add_point")

                        if (vvip_add_point > 0) {
                            vvipPointET.setText(vvip_add_point.toString())
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

        params.put("silver_pay", Utils.getString(silverPayET))
        params.put("silver_point", Utils.getString(silverPointET))
        params.put("silver_add_point", Utils.getString(silverAddPointET))
        params.put("gold_pay", Utils.getString(goldPayET))
        params.put("gold_point", Utils.getString(goldPointET))
        params.put("gold_add_point", Utils.getString(goldAddPointET))
        params.put("vip_pay", Utils.getString(vipPayET))
        params.put("vip_point", Utils.getString(vipPointET))
        params.put("vip_add_point", Utils.getString(vipAddPointET))
        params.put("vvip_pay", Utils.getString(vvipPayET))
        params.put("vvip_point", Utils.getString(vvipPointET))
        params.put("vvip_add_point", Utils.getString(vvipAddPointET))

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


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}
