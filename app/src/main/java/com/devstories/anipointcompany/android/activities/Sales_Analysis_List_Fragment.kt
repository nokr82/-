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
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.base.PrefUtils
import kotlinx.android.synthetic.main.fra_sales_analysis.*
import kotlin.collections.ArrayList

//고객방문분석메인
class Sales_Analysis_List_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>

    var option_amount = ArrayList<String>()
    var option_limit = arrayOf("5개씩보기", "10개씩보기")
    var categoryIndex = ArrayList<Int>()

    lateinit var pageSP: Spinner
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
    lateinit var accumulateLL: LinearLayout
    lateinit var tagTV: TextView
    lateinit var useLL: LinearLayout

    var day_type = -1 //1-오늘 2-이번주 3-이번달 4-3개월
    var page = 1    //페이지
    var limit = 5 //보여지는갯수
    var totalPage = 1 //총페이지
    var payment_type = -1
    var category_id = 1
    var company_id = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fra_sales_analysis, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageSP = view.findViewById(R.id.pageSP)
        amountSP = view.findViewById(R.id.amountSP)
        dateTV = view.findViewById(R.id.dateTV)
        itemdateLL = view.findViewById(R.id.itemdateLL)
        accumulateLL = view.findViewById(R.id.accumulateLL)
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
        tagTV = view.findViewById(R.id.tagTV)
        useLL = view.findViewById(R.id.useLL)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(context, "company_id")

        adapter = ArrayAdapter(myContext, R.layout.spiner_item, option_limit)
        pageSP.adapter = adapter
        //스피너 선택이벤트
        pageSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0) {
                    limit = 5
                } else if (position == 1) {
                    limit = 10
                }
                loadData(company_id)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        //오늘날짜구하기
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val date = Date()
        val currentDate = formatter.format(date)

        accumulateLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        nextLL.setOnClickListener {

            if (totalPage <= page) {
                Toast.makeText(myContext, "마지막 페이지입니다", Toast.LENGTH_SHORT).show()
            } else {
                page++
                Toast.makeText(myContext, page.toString() + "페이지입니다", Toast.LENGTH_SHORT).show()
                loadData(company_id)
            }

        }


        accumulateLL.setOnClickListener {
            var intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("step", 1)
            startActivity(intent)
        }
        useLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("step", 4)
            startActivity(intent)
        }

        preLL.setOnClickListener {

            if (1 == page) {
                Toast.makeText(myContext, "첫 페이지입니다", Toast.LENGTH_SHORT).show()
            } else {
                page--
                Toast.makeText(myContext, page.toString() + "페이지입니다", Toast.LENGTH_SHORT).show()
                loadData(company_id)
            }

        }

        todayRL.setOnClickListener {
            setmenu()
            day_type = 1
            loadcntData()
            loadData(company_id)
            todayTV.setTextColor(Color.parseColor("#606060"))
            dateTV.text = currentDate + "~" + currentDate
            tagTV.text = "최근 1일간 매출내역입니다."
        }

        todayRL.callOnClick()

        weekRL.setOnClickListener {
            setmenu()
            day_type = 2
            loadcntData()
            loadData(company_id)
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
            tagTV.text = "최근 7일간 매출내역입니다."
        }
        monthRL.setOnClickListener {
            setmenu()
            day_type = 3
            loadcntData()
            loadData(company_id)
            monthTV.setTextColor(Color.parseColor("#606060"))
            val beforemonth = SimpleDateFormat("yyyy.MM.01", Locale.KOREA)
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val date = Date()
            val currentDate = beforemonth.format(date)
            val lastmonth = aftermonth.format(date).toString().substring(0, 8) + endDay
            tagTV.text = "최근 30일간 매출내역입니다."

            dateTV.text = currentDate + " ~ " + lastmonth
        }
        three_mRL.setOnClickListener {
            setmenu()
            day_type = 4
            loadcntData()
            loadData(company_id)
            three_mTV.setTextColor(Color.parseColor("#606060"))
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val startday = cal.getActualMaximum(Calendar.MONTH) - 3
            val beforemonth = SimpleDateFormat("yyyy." + startday + ".dd", Locale.KOREA)
            val date = Date()
            val currentDate = beforemonth.format(date).toString()
            val lastmonth = aftermonth.format(date).toString()
            tagTV.text = "최근 3달간 매출내역입니다."

            dateTV.text = currentDate + " ~ " + lastmonth
        }

        accumulateLL.setOnClickListener {
            var intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("step", 1)
            startActivity(intent)
        }

        //전체고객구하기
        loadcntData()

        amountSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                /*if (position==0){
                    payment_type = 1
                }else if (position==1){
                    payment_type = 2
                }else if (position==2){
                    payment_type = 3
                }*/
                payment_type = position + 1

                category_id = categoryIndex[position]
                Log.d("카테",category_id.toString())
                //println("amountSP clicked. Position is $payment_type")
                loadData(company_id)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }

    fun setmenu() {
        todayTV.setTextColor(Color.parseColor("#c5c5c5"))
        monthTV.setTextColor(Color.parseColor("#c5c5c5"))
        weekTV.setTextColor(Color.parseColor("#c5c5c5"))
        three_mTV.setTextColor(Color.parseColor("#c5c5c5"))
    }


    //사업체정보
    fun loadcntData() {
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


                        val categories = response.getJSONArray("categories")
                        option_amount.clear()
                        Log.d("데이트", categories.toString())
                        option_amount.add("전체")
                        categoryIndex.add(-1)
                        for (i in 0 until categories.length()) {
                            val json = categories[i] as JSONObject
                            val company_category = json.getJSONObject("CompanyCategory")
                            val category = json.getJSONObject("Category")
                            val name = Utils.getString(category, "name")

                            option_amount.add(name)

                            val category_id = Utils.getInt(company_category, "category_id")

                            categoryIndex.add(category_id)
                        }

                        adapter = ArrayAdapter(myContext, R.layout.spiner_item, option_amount)
                        amountSP.adapter = adapter

                        adapter.notifyDataSetChanged()

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
        params.put("company_id", company_id)
        params.put("category_id", category_id)
        Log.d("카테고리",category_id.toString())
        params.put("day_type", day_type)
        params.put("limit", limit)
        params.put("page", page)
        Log.d("페이지", page.toString())
        Log.d("day_type", day_type.toString())

        CompanyAction.sales_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        val totalData = response.getJSONObject("totalData")

                        val totalPrice = Utils.getString(totalData, "totalPrice")
                        var cashTotalPrice = Utils.getInt(totalData, "cashTotalPrice")
                        val cardTotalPrice = Utils.getString(totalData, "cardTotalPrice")
                        val bankTotalPrice = Utils.getInt(totalData, "bankTotalPrice")



                        all_memberTV.text = Utils.comma(totalPrice)
                        new_userTV.text = Utils.comma(cashTotalPrice.toString())
                        member_re_cntTV.text = Utils.comma(cardTotalPrice)
                        pointTV.text = Utils.comma(bankTotalPrice.toString())


                        val list = response.getJSONArray("list")
                        //option_amount.clear()
                        itemdateLL.removeAllViews()

                        Log.d("데이트", list.toString())
                        if (list.length() > 0) {
                            for (i in 0 until list.length()) {
                                //Log.d("갯수",i.toString())
                                var json = list[i] as JSONObject
                                val date = Utils.getString(json, "date")
                                val totalPrice = Utils.getString(json, "totalPrice")
                                var cash = Utils.getInt(json, "cash")
                                val card = Utils.getString(json, "card")
                                val point = Utils.getInt(json, "bank")



                                val salesView = View.inflate(myContext, R.layout.item_sales_analysis, null)
                                var dateTV: TextView = salesView.findViewById(R.id.dateTV)
                                var totalTV: TextView = salesView.findViewById(R.id.totalTV)
                                var cashTV: TextView = salesView.findViewById(R.id.cashTV)
                                var cardTV: TextView = salesView.findViewById(R.id.cardTV)
                                var pointTV: TextView = salesView.findViewById(R.id.pointTV)

                                dateTV.text = date.toString()
                                totalTV.text = Utils.comma(totalPrice)
                                cashTV.text = Utils.comma(cash.toString())
                                cardTV.text = Utils.comma(card)
                                pointTV.text =  Utils.comma(point.toString())
                                itemdateLL.addView(salesView)

                            }

                        }


                        totalPage  = response.getInt("totalPage")

//                        option_amount.clear()


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
