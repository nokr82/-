package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.devstories.aninuriandroid.adapter.ReserveListAdapter
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_reservation_manage.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ReservationManageFragment : Fragment() {
    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>

    lateinit var reserveListAdapter: ReserveListAdapter
    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()

    var years = ArrayList<String>()
    var months = arrayOf("1월", "2월", "3월", "4월","5월","6월","7월","8월","9월", "10월", "11월", "12월")
    var calendarData: ArrayList<JSONObject> = ArrayList<JSONObject>()

    var page = 1
    var totalpage = 0
    var company_id = -1

    var year = 0
    var month = 0
    var day = 0
    var reserve_date = ""

    var search_date = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fra_reservation_manage, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        company_id = PrefUtils.getIntPreference(context, "company_id")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        reserveListAdapter = ReserveListAdapter(myContext, R.layout.item_reserve_list, adapterData, this)
        reservationLV.adapter = reserveListAdapter

        reservationLV.setOnItemClickListener { parent, view, position, id ->
            var data = adapterData.get(position)
            val reserve = data.getJSONObject("Reserve")
            val reserve_id = Utils.getInt(reserve, "id")
            val result_step = Utils.getInt(reserve, "result_step")
            val member_id = Utils.getInt(reserve, "member_id")
            if (result_step ==2){
                Toast.makeText(context,"예약이 완료된 항목입니다.",Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }

            val intent = Intent(myContext, DlgReserveResultActivity::class.java)
            intent.putExtra("reserve_id", reserve_id)
            intent.putExtra("member_id", member_id)
            startActivity(intent)
        }

        var lastitemVisibleFlag = false
        reservationLV.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                lastitemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    if (totalpage > page) {
                        page++
                        reserve_list()
                    }

                }
            }

        })

        reservTV.setOnClickListener {
            val intent = Intent(context, DlgReserveSaveActivity::class.java)
            startActivity(intent)
        }

        val cal: Calendar = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH) + 1
        day = cal.get(Calendar.DAY_OF_MONTH)

        reserve_date = year.toString()+"."+month.toString()+"."+day.toString()

        for (i in (year - 20) .. year) {
            years.add(i.toString() + "년")
        }

        adapter = ArrayAdapter<String>(context, R.layout.callender_spinner_item, years)
        yearsSP.adapter = adapter
        yearsSP.setSelection(years.size - 1)
        yearsSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                var year = years[i].substring(0, (years[i].length - 1))
                calendarGV.year = year.toInt()

                calendarGV.draw()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                return
            }
        }

        adapter = ArrayAdapter<String>(context, R.layout.callender_spinner_item, months)
        monthSP.adapter = adapter
        monthSP.setSelection(month - 1)
        monthSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                var month = months[i].substring(0, (months[i].length - 1))
                calendarGV.month = month.toInt() - 1

                calendarGV.draw()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                return
            }
        }

        prevMonthLL.setOnClickListener {

            var month = monthSP.selectedItemPosition

            var position = monthSP.selectedItemPosition - 1

            if (month == 0) {
                position = months.size - 1
                month = position

                calendarGV.year = yearsSP.selectedItemPosition - 1
                yearsSP.setSelection(yearsSP.selectedItemPosition - 1)
            }

            calendarGV.month = month
            monthSP.setSelection(position)

            calendarGV.draw()

        }

        nextMonthLL.setOnClickListener {

            var position = monthSP.selectedItemPosition + 1

            if (monthSP.selectedItemPosition == (months.size - 1)) {
                position = 0

                var years_index = yearsSP.selectedItemPosition + 1

                if (years_index >= years.size) {
                    return@setOnClickListener
                }
                calendarGV.year = years_index
                yearsSP.setSelection(years_index)

            }

            calendarGV.month = position
            monthSP.setSelection(position)

            calendarGV.draw()
        }

        calendarGV.setOnDateSelectedListener { year, month, day ->
            println("year : $year, month : $month; day : $day")
            Log.d("날짜",year.toString()+"."+month.toString()+"."+day.toString())
            reserve_date = year.toString()+"."+month.toString()+"."+day.toString()
            reserve_list()
        }

        reserve_list()

        reservationDays()

    }

    override fun onResume() {
        super.onResume()
        reserve_list()
    }

    //예약 정보뽑기
    fun reserve_list() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("page", page)
        params.put("reserve_date", reserve_date)

        CompanyAction.reserve_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        var data = response.getJSONArray("reserve")
                        totalpage = response.getInt("totalPage")
                        search_date = Utils.getString(response, "search_date")
                        var seletedDate = ""
                        if (search_date != ""){
                           seletedDate = SimpleDateFormat("yyyy-MM-dd").format(SimpleDateFormat("yyyy.M.d").parse(search_date))
                        }

                        if (page == 1) {
                            adapterData.clear()
                            reserveListAdapter.notifyDataSetChanged()
                        }

                        for (i in 0 until data.length()) {
                            adapterData.add(data[i] as JSONObject)
                        }

                        calendarGV.seletedDate = seletedDate

                        calendarGV.draw()

                        reserveListAdapter.notifyDataSetChanged()

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

    fun reservationDays() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("year", year)
        params.put("month", month)

        CompanyAction.reservation_days(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        var reservation = response.getJSONArray("reservation")

                        Log.d("아니",reservation.toString())
                        for (i in 0 until reservation.length()) {
                            calendarData.add(reservation[i] as JSONObject)
                        }

                        calendarGV.objectData = calendarData
                        calendarGV.draw()

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

}
