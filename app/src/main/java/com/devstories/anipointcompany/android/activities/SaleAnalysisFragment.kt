package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.Actions.PointAction

import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.frag_sale_analysis.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SaleAnalysisFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>

    var option_amount = arrayOf("5개씩 보기","10개씩 보기")
    var day_type = -1 //1-오늘 2-이번주 3-이번달 4-3개월
    var page = 1    //페이지
    var limit = 5 //보여지는갯수
    var totalPage = 1 //총페이지

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.frag_sale_analysis, container, false)
    }
    ///
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_amount)
        sa_amountSP.adapter = adapter
        //스피너 선택이벤트
        sa_amountSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position==0){
                    limit = 5
                    Log.d("리미트",limit.toString())
                }else if (position==1){
                    limit = 10
                    Log.d("리미트",limit.toString())
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


        //오늘날짜구하기
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val date = Date()
        val currentDate = formatter.format(date)

        sa_nextLL.setOnClickListener {

            if (totalPage==page){
                Toast.makeText(myContext,"최대페이지입니다", Toast.LENGTH_SHORT).show()
            }else{
                page++
                Toast.makeText(myContext,page.toString()+"페이지입니다", Toast.LENGTH_SHORT).show()
                loadData(1)
            }

        }
        sa_preLL.setOnClickListener {

            if (1==page){
                Toast.makeText(myContext,"첫번쨰 페이지입니다", Toast.LENGTH_SHORT).show()
            }else{
                page--
                Toast.makeText(myContext,page.toString()+"페이지입니다", Toast.LENGTH_SHORT).show()
                loadData(1)
            }

        }

        sa_todayRL.setOnClickListener {
            setmenu()
            day_type = 1
            loadcntData()
            loadData(1)
            sa_todayTV.setTextColor(Color.parseColor("#606060"))
            sa_dateTV.text = currentDate+"~"+currentDate
        }

        sa_todayRL.callOnClick()

        sa_weekRL.setOnClickListener {
            setmenu()
            day_type = 2
            loadcntData()
            loadData(1)
            sa_weekTV.setTextColor(Color.parseColor("#606060"))
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val df = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            var startDate = df.format(calendar.getTime())
            calendar.add(Calendar.DATE, 6)
            var   endDate = df.format(calendar.getTime())
            Log.d("현재",startDate)
            Log.d("미래",endDate)
            sa_dateTV.text = startDate+" ~ "+endDate
        }
        sa_monthRL.setOnClickListener {
            setmenu()
            day_type = 3
            loadcntData()
            loadData(1)
            sa_monthTV.setTextColor(Color.parseColor("#606060"))
            val beforemonth = SimpleDateFormat("yyyy.MM.01", Locale.KOREA)
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val date = Date()
            val currentDate = beforemonth.format(date)
            val lastmonth = aftermonth.format(date).toString().substring(0,8)+endDay

            sa_dateTV.text = currentDate+" ~ "+lastmonth
        }
        sa_three_mRL.setOnClickListener {
            setmenu()
            day_type = 4
            loadcntData()
            loadData(1)
            sa_three_mTV .setTextColor(Color.parseColor("#606060"))
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val startday = cal.getActualMaximum(Calendar.MONTH)-3
            val beforemonth = SimpleDateFormat("yyyy."+startday+".dd", Locale.KOREA)
            val date = Date()
            val currentDate = beforemonth.format(date).toString()
            val lastmonth = aftermonth.format(date).toString()

            sa_dateTV.text = currentDate+" ~ "+lastmonth
        }


        //전체고객구하기
        loadcntData()
        loadData(1)


    }

    fun  setmenu(){
        sa_todayTV.setTextColor(Color.parseColor("#c5c5c5"))
        sa_monthTV.setTextColor(Color.parseColor("#c5c5c5"))
        sa_weekTV.setTextColor(Color.parseColor("#c5c5c5"))
        sa_three_mTV .setTextColor(Color.parseColor("#c5c5c5"))
    }


    //방문자수구하기
    fun loadcntData() {
        val params = RequestParams()
        params.put("company_id", PrefUtils.getStringPreference(myContext, "company_id"))
        params.put("payment_type",1)
        params.put("page",page)
        params.put("limit",limit)
        params.put("day_type",day_type)


        CompanyAction.sales_analysis(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")


                    if ("ok" == result) {
                        //오늘 가입한회원
                        val member_new_cnt:Int = response.getInt("newMemberCount")
                        val member_re_cnt = response.getInt("reMemberCount")
                        val allmember = member_new_cnt+member_re_cnt


                        sa_all_memberTV.text = allmember.toString()
                        sa_new_userTV.text = member_new_cnt.toString()
                        sa_member_re_cntTV.text  = member_re_cnt.toString()
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
        params.put("day_type",day_type)
        params.put("page",page)
        params.put("limit",limit)
        Log.d("페이지",page.toString())

        Log.d("day_type",day_type.toString())
        PointAction.user_visited(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        totalPage  = response.getInt("totalPage")

                        sa_itemdateLL.removeAllViews()

                        val points = response.getJSONArray("points")
                        Log.d("데이트",points.toString())
                        for (i in 0..points.length()-1){
                            Log.d("갯수",i.toString())
                            var json=points[i] as JSONObject
                            val date = Utils.getString(json,"date")
                            val new_member = Utils.getInt(json,"new_member")
                            val re_member = Utils.getInt(json,"re_member")
                            Log.d("데이트",re_member.toString())


                            val userView = View.inflate(myContext, R.layout.item_visit, null)
                            var dateTV : TextView = userView.findViewById(R.id.dateTV)
                            var new_userTV : TextView = userView.findViewById(R.id.new_userTV)
                            var re_userTV : TextView = userView.findViewById(R.id.re_userTV)
                            var all_userTV : TextView = userView.findViewById(R.id.all_userTV)
                            val alluser = re_member+new_member
                            Log.d("총",alluser.toString())
                            dateTV.text = date.toString()
                            all_userTV.text = alluser.toString()
                            re_userTV.text = re_member.toString()
                            new_userTV.text = new_member.toString()
                            sa_itemdateLL.addView(userView)



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


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
    ///
}
