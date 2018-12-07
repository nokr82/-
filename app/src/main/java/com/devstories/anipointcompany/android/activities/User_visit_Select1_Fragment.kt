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
import android.widget.*
import com.devstories.aninuriandroid.adapter.VisitListAdapter
import com.devstories.anipointcompany.android.Actions.PointAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import com.devstories.anipointcompany.android.base.PrefUtils

//고객방문분석메인
class User_visit_Select1_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    var option_amount = arrayOf("5개씩 보기", "10개씩 보기")

    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()
    lateinit var visitAdapter: VisitListAdapter

    lateinit var amountSP: Spinner
    lateinit var dateTV: TextView
    lateinit var all_memberTV: TextView
    lateinit var new_userTV: TextView
    lateinit var member_re_cntTV: TextView
    lateinit var itemdateLL: LinearLayout
    lateinit var nextLL: LinearLayout
    lateinit var preLL: LinearLayout
    lateinit var todayRL: RelativeLayout
    lateinit var weekRL: RelativeLayout
    lateinit var monthRL: RelativeLayout
    lateinit var three_mRL: RelativeLayout
    lateinit var todayTV: TextView
    lateinit var weekTV: TextView
    lateinit var monthTV: TextView
    lateinit var three_mTV: TextView
    lateinit var va_accumulateLL: LinearLayout
    lateinit var useLL: LinearLayout
    lateinit var visit_perTV: TextView

    var day_type = 1 //1-오늘 2-이번주 3-이번달 4-3개월
    var page = 1      //페이지
    var limit = 5     //보여지는갯수
    var totalPage = 1  //총페이지
    var company_id = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fra_user_visit_select1, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        visit_perTV = view.findViewById(R.id.visit_perTV)
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
        nextLL = view.findViewById(R.id.nextLL)
        preLL = view.findViewById(R.id.preLL)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(context, "company_id")

        adapter = ArrayAdapter(myContext, R.layout.spiner_item, option_amount)
        amountSP.adapter = adapter
        //스피너 선택이벤트
        amountSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0) {
                    limit = 5
                    Log.d("리미트", limit.toString())
                    loadData()
                } else if (position == 1) {
                    limit = 10
                    Log.d("리미트", limit.toString())
                    loadData()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


        //오늘날짜구하기
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val date = Date()
        val currentDate = formatter.format(date)



        nextLL.setOnClickListener {

            if (totalPage <= page) {
                Toast.makeText(myContext, "마지막 페이지입니다", Toast.LENGTH_SHORT).show()

            } else {
                page++
                Toast.makeText(myContext, page.toString() + "페이지입니다", Toast.LENGTH_SHORT).show()
                loadData()
            }

        }

        preLL.setOnClickListener {

            if (1 == page) {
                Toast.makeText(myContext, "첫 페이지입니다", Toast.LENGTH_SHORT).show()
            } else {
                page--
                Toast.makeText(myContext, page.toString() + "페이지입니다", Toast.LENGTH_SHORT).show()
                loadData()
            }

        }

        todayRL.setOnClickListener {
            setmenu()
            day_type = 1
            loadcntData()
            loadData()
            todayTV.setTextColor(Color.parseColor("#606060"))
            dateTV.text = currentDate + "~" + currentDate
        }

        todayRL.callOnClick()

        weekRL.setOnClickListener {
            setmenu()
            day_type = 2
            loadcntData()
            loadData()
            weekTV.setTextColor(Color.parseColor("#606060"))
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val df = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            var startDate = df.format(calendar.getTime())
            calendar.add(Calendar.DATE, 6)
            var endDate = df.format(calendar.getTime())
            Log.d("현재", startDate)
            Log.d("미래", endDate)
            dateTV.text = startDate + " ~ " + endDate
        }
        monthRL.setOnClickListener {
            setmenu()
            day_type = 3
            loadcntData()
            loadData()
            monthTV.setTextColor(Color.parseColor("#606060"))
            val beforemonth = SimpleDateFormat("yyyy.MM.01", Locale.KOREA)
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val date = Date()
            val currentDate = beforemonth.format(date)
            val lastmonth = aftermonth.format(date).toString().substring(0, 8) + endDay

            dateTV.text = currentDate + " ~ " + lastmonth
        }
        three_mRL.setOnClickListener {
            setmenu()
            day_type = 4
            loadcntData()
            loadData()
            three_mTV.setTextColor(Color.parseColor("#606060"))
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val startday = cal.getActualMaximum(Calendar.MONTH) - 3
            val beforemonth = SimpleDateFormat("yyyy." + startday + ".dd", Locale.KOREA)
            val date = Date()
            val currentDate = beforemonth.format(date).toString()
            val lastmonth = aftermonth.format(date).toString()

            dateTV.text = currentDate + " ~ " + lastmonth
        }



        //전체고객구하기
        loadcntData()
        loadData()


    }

    fun setmenu() {
        todayTV.setTextColor(Color.parseColor("#c5c5c5"))
        monthTV.setTextColor(Color.parseColor("#c5c5c5"))
        weekTV.setTextColor(Color.parseColor("#c5c5c5"))
        three_mTV.setTextColor(Color.parseColor("#c5c5c5"))
    }

    //방문자수구하기
    fun loadcntData() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("day_type", day_type)


        PointAction.user_visited(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")


                    if ("ok" == result) {
                        //오늘 가입한회원
                        val member_new_cnt: Int = response.getInt("newMemberCount")
                        val member_re_cnt = response.getInt("reMemberCount")
                        val allmember = member_new_cnt + member_re_cnt
                        val re_per = member_re_cnt.toFloat() / allmember.toFloat() * 100
                        Log.d("숫자", member_re_cnt.toString())
                        Log.d("숫자", allmember.toString())
                        Log.d("숫자", re_per.toString())

                        var strNumber = String.format("%.1f", re_per);

                        visit_perTV.text = strNumber + "%"
                        all_memberTV.text = allmember.toString()
                        new_userTV.text = member_new_cnt.toString()
                        member_re_cntTV.text = member_re_cnt.toString()
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
    fun loadData() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("day_type", day_type)
        params.put("page", page)
        params.put("limit", limit)
        Log.d("페이지", page.toString())

        Log.d("day_type", day_type.toString())
        PointAction.user_visited(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                itemdateLL.removeAllViews()

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        totalPage = response.getInt("totalPage")

                        val points = response.getJSONArray("points")
                        Log.d("데이트", points.toString())
                        for (i in 0 until points.length()) {
                            Log.d("갯수", i.toString())
                            var json = points[i] as JSONObject
                            val date = Utils.getString(json, "date")
                            val new_member = Utils.getInt(json, "new_member")
                            val re_member = Utils.getInt(json, "re_member")
                            Log.d("데이트", re_member.toString())

                            val userView = View.inflate(myContext, R.layout.item_visit, null)
                            var dateTV: TextView = userView.findViewById(R.id.dateTV)
                            var new_userTV: TextView = userView.findViewById(R.id.new_userTV)
                            var re_userTV: TextView = userView.findViewById(R.id.re_userTV)
                            var all_userTV: TextView = userView.findViewById(R.id.all_userTV)
                            val alluser = re_member + new_member
                            Log.d("총", alluser.toString())
                            dateTV.text = date.toString()
                            all_userTV.text = alluser.toString()
                            re_userTV.text = re_member.toString()
                            new_userTV.text = new_member.toString()

                            itemdateLL.addView(userView)

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

}
