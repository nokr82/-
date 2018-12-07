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
import com.devstories.anipointcompany.android.R
import com.github.mikephil.charting.charts.BarChart
import java.text.SimpleDateFormat
import java.util.*


class User_visit_Select3_Fragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var visit_perTV: TextView
    lateinit var all_memberTV: TextView
    lateinit var new_userTV: TextView
    lateinit var member_re_cntTV: TextView
    lateinit var ageBarChart:BarChart
    lateinit var todayRL: RelativeLayout
    lateinit var weekRL: RelativeLayout
    lateinit var monthRL: RelativeLayout
    lateinit var three_mRL: RelativeLayout
    lateinit var todayTV: TextView
    lateinit var weekTV: TextView
    lateinit var monthTV: TextView
    lateinit var three_mTV: TextView
    lateinit var dateTV: TextView
    var day_type = 1 //1-오늘 2-이번주 3-이번달 4-3개월
    var company_id = -1

    //오늘날짜구하기
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
    val date = Date()
    val currentDate = formatter.format(date)

    var type = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fra_user_visit_select3, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        ageLL = view.findViewById(R.id.ageLL)
        ageBarChart = view.findViewById(R.id.ageBarChart)
        todayTV = view.findViewById(R.id.todayTV)
        monthTV = view.findViewById(R.id.monthTV)
        weekTV = view.findViewById(R.id.weekTV)
        three_mTV = view.findViewById(R.id.three_mTV)
        todayRL = view.findViewById(R.id.todayRL)
        weekRL = view.findViewById(R.id.weekRL)
        monthRL = view.findViewById(R.id.monthRL)
        three_mRL = view.findViewById(R.id.three_mRL)
        dateTV = view.findViewById(R.id.dateTV)
        visit_perTV = view.findViewById(R.id.visit_perTV)
        all_memberTV = view.findViewById(R.id.all_memberTV)
        member_re_cntTV = view.findViewById(R.id.member_re_cntTV)
        new_userTV = view.findViewById(R.id.new_userTV)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        click()
    }

    fun click(){
        todayRL.setOnClickListener {
            setmenu()
            day_type = 1
            todayTV.setTextColor(Color.parseColor("#606060"))
            dateTV.text = currentDate + "~" + currentDate
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
        }
    }

    fun setmenu() {
        todayTV.setTextColor(Color.parseColor("#c5c5c5"))
        monthTV.setTextColor(Color.parseColor("#c5c5c5"))
        weekTV.setTextColor(Color.parseColor("#c5c5c5"))
        three_mTV.setTextColor(Color.parseColor("#c5c5c5"))
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}


