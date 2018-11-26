package com.devstories.anipointcompany.android.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.aninuriandroid.adapter.VisitListAdapter
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_user_visit_analysis.view.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class User_visit_List_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    var option_amount = arrayOf("5개씩 보기","10개씩 보기")

    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()
    lateinit var visitAdapter: VisitListAdapter

    lateinit var amountSP: Spinner
    lateinit var dateTV: TextView
    lateinit var all_memberTV: TextView
    lateinit var new_userTV: TextView
    lateinit var member_re_cntTV: TextView
    lateinit var itemdateLL: LinearLayout
    lateinit var todayRL: RelativeLayout
    lateinit var weekRL: RelativeLayout
    lateinit var monthRL: RelativeLayout
    lateinit var three_mRL: RelativeLayout
    lateinit var todayTV: TextView
    lateinit var weekTV: TextView
    lateinit var monthTV: TextView
    lateinit var three_mTV: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
            return inflater.inflate(R.layout.fra_user_visit_analysis,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amountSP = view.findViewById(R.id.amountSP)
        dateTV = view.findViewById(R.id.dateTV)
        itemdateLL = view.findViewById(R.id.itemdateLL)
        all_memberTV = view.findViewById(R.id.all_memberTV)
        member_re_cntTV = view.findViewById(R.id.member_re_cntTV)
        new_userTV = view.findViewById(R.id.new_userTV)
        todayTV = view.findViewById(R.id.todayTV)
        monthTV = view.findViewById(R.id.monthTV)
        weekTV = view.findViewById(R.id.weekTV)
        three_mTV = view.findViewById(R.id.three_mTV)
        todayRL = view.findViewById(R.id.todayRL)
        weekRL = view.findViewById(R.id.weekRL)
        monthRL = view.findViewById(R.id.monthRL)
        three_mRL = view.findViewById(R.id.three_mRL)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_amount)
        amountSP.adapter = adapter

        //오늘날짜구하기
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val date = Date()
        val currentDate = formatter.format(date)
        dateTV.text = currentDate+"~"+currentDate



        todayRL.setOnClickListener {
            setmenu()
            todayTV.setTextColor(Color.parseColor("#606060"))
        }

        todayRL.callOnClick()

        weekRL.setOnClickListener {
            setmenu()
            monthTV.setTextColor(Color.parseColor("#606060"))
        }
        monthRL.setOnClickListener {
            setmenu()
            weekTV.setTextColor(Color.parseColor("#606060"))

        }
        three_mRL.setOnClickListener {
            setmenu()
            three_mTV .setTextColor(Color.parseColor("#606060"))
        }


        loadData(1)
        //전체고객구하기
        loadcntData()



    }

    fun  setmenu(){
        todayTV.setTextColor(Color.parseColor("#c5c5c5"))
        monthTV.setTextColor(Color.parseColor("#c5c5c5"))
        weekTV.setTextColor(Color.parseColor("#c5c5c5"))
        three_mTV .setTextColor(Color.parseColor("#c5c5c5"))
    }


    //방문자수구하기
    fun loadcntData() {
        val params = RequestParams()
        params.put("company_id",1)



        MemberAction.user_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")


                    if ("ok" == result) {
                        val member_cnt = response.getString("member_cnt")
                        //오늘 가입한회원
                        val member_new_cnt = response.getString("member_new_cnt")
                        val member_re_cnt = response.getString("member_re_cnt")

                        all_memberTV.text = member_cnt
                        new_userTV.text = member_new_cnt
                        member_re_cntTV.text  = member_re_cnt
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



    //방문이력 뽑기
    fun loadData(company_id: Int) {
        val params = RequestParams()
        params.put("company_id",company_id)



        MemberAction.visit_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")


                    if ("ok" == result) {

                        val visit_history = response.getString("point")
                        val visit_re = response.getString("point")

                        val userView = View.inflate(myContext, R.layout.item_visit, null)
                        var dateTV : TextView = userView.findViewById(R.id.dateTV)
                        var new_userTV : TextView = userView.findViewById(R.id.new_userTV)
                        var re_userTV : TextView = userView.findViewById(R.id.re_userTV)
                        var all_userTV : TextView = userView.findViewById(R.id.all_userTV)

                        all_userTV.text = visit_history
                        re_userTV.text = visit_re

                        itemdateLL.addView(userView)
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


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}
