package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.devstories.anipointcompany.android.Actions.PointAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class User_visit_Select2_Fragment : Fragment() {

    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null

    lateinit var ageBarChart:BarChart
    lateinit var todayRL: RelativeLayout
    lateinit var weekRL: RelativeLayout
    lateinit var monthRL: RelativeLayout
    lateinit var three_mRL: RelativeLayout

    lateinit var all_memberTV: TextView
    lateinit var todayTV: TextView
    lateinit var weekTV: TextView
    lateinit var monthTV: TextView
    lateinit var three_mTV: TextView
    lateinit var dateTV: TextView
    lateinit var tenTV: TextView
    lateinit var twoTV: TextView
    lateinit var threeTV: TextView
    lateinit var fourTV: TextView
    lateinit var fiveTV: TextView
    lateinit var sixTV: TextView
    lateinit var monTV: TextView


    var day_type = 1 //1-오늘 2-이번주 3-이번달 4-3개월
    var company_id = -1
    var totalMemberCnt = 0
    //오늘날짜구하기
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
    val date = Date()
    val currentDate = formatter.format(date)

    var type = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fra_user_visit_select2, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        all_memberTV = view.findViewById(R.id.all_memberTV)
        ageBarChart = view.findViewById(R.id.ageBarChart)
        todayTV = view.findViewById(R.id.todayTV)
        monthTV = view.findViewById(R.id.monthTV)
        weekTV = view.findViewById(R.id.weekTV)
        three_mTV = view.findViewById(R.id.three_mTV)
        todayRL = view.findViewById(R.id.todayRL)
        weekRL = view.findViewById(R.id.weekRL)
        monthRL = view.findViewById(R.id.monthRL)
        three_mRL = view.findViewById(R.id.three_mRL)
        tenTV= view.findViewById(R.id.tenTV)
        twoTV= view.findViewById(R.id.twoTV)
        threeTV= view.findViewById(R.id.threeTV)
        fourTV= view.findViewById(R.id.fourTV)
        fiveTV= view.findViewById(R.id.fiveTV)
        sixTV= view.findViewById(R.id.sixTV)
        dateTV = view.findViewById(R.id.dateTV)
        monTV = view.findViewById(R.id.monTV)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadData()
        graph()
        click()
        todayRL.callOnClick()
    }

    fun graph(){
        company_id = PrefUtils.getIntPreference(context, "company_id")



        ageBarChart.setDrawBarShadow(false)
        ageBarChart.setDrawValueAboveBar(true)
        ageBarChart.description.isEnabled = false
        ageBarChart.legend.isEnabled = false
        ageBarChart.setMaxVisibleValueCount(100)
        ageBarChart.setPinchZoom(false)
        ageBarChart.setDrawGridBackground(false)
        ageBarChart.setScaleEnabled(false)
        ageBarChart.setTouchEnabled(false)
        ageBarChart.getAxisLeft().setAxisMinimum(0f)
        ageBarChart.getAxisRight().setEnabled(false)
        ageBarChart.getAxisLeft().setValueFormatter(IAxisValueFormatter { value, axis ->
            //                return Double.parseDouble(String.format("%.2f", value)) + "h";
            var val_ = (value / totalMemberCnt) * 100f
            String.format("%.1f", val_)+ "%"
        })




    }

    fun click(){
        todayRL.setOnClickListener {
            setmenu()
            day_type = 1
            todayTV.setTextColor(Color.parseColor("#606060"))
            dateTV.text = currentDate + "~" + currentDate
            loadData()
        }
        todayRL.callOnClick()
        weekRL.setOnClickListener {
            setmenu()
            day_type = 2
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
            loadData()
        }
        monthRL.setOnClickListener {
            setmenu()
            day_type = 3
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
            loadData()
        }
        three_mRL.setOnClickListener {
            setmenu()
            day_type = 4
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
            loadData()
        }
    }

    fun setmenu() {
        todayTV.setTextColor(Color.parseColor("#c5c5c5"))
        monthTV.setTextColor(Color.parseColor("#c5c5c5"))
        weekTV.setTextColor(Color.parseColor("#c5c5c5"))
        three_mTV.setTextColor(Color.parseColor("#c5c5c5"))
    }
    //나이별데이터
    fun loadData() {

        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("day_type", day_type)
        params.put("search_type", 1)

        PointAction.user_detail(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val age = response.getJSONArray("age")
                        val ages = arrayOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일","일요일")

                        var xAxis = ageBarChart.getXAxis()
                        xAxis.setTextColor(Color.parseColor("#a0a0a0"))
                        xAxis.setDrawLabels(true)
                        xAxis.setDrawGridLines(false)
                        xAxis.setGranularity(1f) // minimum axis-step (interval) is 1
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                        xAxis.setAvoidFirstLastClipping(true)
                        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
                            // System.out.println("va 0 : " + value);
                            if (value < 0) {
                                return@IAxisValueFormatter ""
                            }
                            if (ages.size > value) {
                                ages[value.toInt()]
                            } else {
                                ""
                            }
                        }


                        var ageData: MutableList<BarEntry> = ArrayList()

                        for (i in 0 until age.length()) {
                            var data = age[i] as JSONObject
                            var data1 = age[0] as JSONObject
                            var data2 = age[1] as JSONObject
                            var data3 = age[2] as JSONObject
                            var data4 = age[3] as JSONObject
                            var data5 = age[4] as JSONObject
                            var data6 = age[5] as JSONObject
                            var data7 = age[6] as JSONObject
                            var data8 = age[7] as JSONObject

                            monTV.text = Utils.getInt(data1, "count").toString()
                            tenTV.text = Utils.getInt(data2, "count").toString()
                            twoTV.text= Utils.getInt(data3, "count").toString()
                            threeTV.text= Utils.getInt(data4, "count").toString()
                            fourTV.text= Utils.getInt(data5, "count").toString()
                            fiveTV.text= Utils.getInt(data6, "count").toString()
                            sixTV.text= Utils.getInt(data7, "count").toString()
                            totalMemberCnt = Utils.getInt(data8, "count")

                                    ageData.add(BarEntry(i.toFloat(), Utils.getInt(data, "count").toFloat()))
                        }
                        if (totalMemberCnt==0){
                            totalMemberCnt =1

                            all_memberTV.text = "0"
                        }else{
                            all_memberTV.text = totalMemberCnt.toString()
                        }
                        ageData.removeAt(7)




                        var barDataSet = BarDataSet(ageData, "12")
                        barDataSet.setColors(*intArrayOf(Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8")))
                        barDataSet.setDrawValues(false)

                        var barData = BarData(barDataSet)
                        barData.setBarWidth(0.1f)


                        ageBarChart.setData(barData)
                        ageBarChart.invalidate() // refresh


                        val entries = ArrayList<PieEntry>()



                        val dataSet: PieDataSet = PieDataSet(entries, "성 비율");
                        dataSet.setDrawIcons(false);

                        dataSet.sliceSpace = 3f
                        dataSet.iconsOffset = MPPointF(0f, 40f)
                        dataSet.selectionShift = 5f

                        val colors = ArrayList<Int>()
                        colors.add(Color.parseColor("#6799FF"))
                        colors.add(Color.parseColor("#FF00DD"))
                        dataSet.setColors(colors);

                        val data = PieData(dataSet)
                        data.setValueTextSize(11f);
                        data.setValueTextColor(Color.WHITE);

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
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

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
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


