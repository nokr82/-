package com.devstories.anipointcompany.android.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstories.aninuriandroid.adapter.UserListAdapter
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.*
import com.devstories.anipointcompany.android.Actions.CouponAction.send_message
import com.devstories.anipointcompany.android.Actions.PointAction
import com.devstories.anipointcompany.android.base.PrefUtils
import kotlinx.android.synthetic.main.fra_point_list.view.*
import java.text.SimpleDateFormat
import java.util.*

//포인트내역
class Point_List_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    internal lateinit var view: View
    lateinit var userLV: ListView
    lateinit var first_dateLL: LinearLayout
    lateinit var last_dateLL: LinearLayout
    lateinit var first_dateTV: TextView
    lateinit var last_dateTV: TextView
    lateinit var startdateTV: TextView
    lateinit var lastdateTV: TextView
    lateinit var all_cntTV: TextView
    lateinit var all_stackTV: TextView
    lateinit var integratedTV: TextView
    lateinit var all_useTV: TextView
    lateinit var all_couponTV: TextView
    lateinit var nonameTV: TextView
    lateinit var accumulateLL: LinearLayout
    lateinit var todayLL: LinearLayout
    lateinit var allLL: LinearLayout
    lateinit var weekLL: LinearLayout
    lateinit var monthLL: LinearLayout
    lateinit var todayTV: TextView
    lateinit var allTV: TextView
    lateinit var weekTV: TextView
    lateinit var monthTV: TextView
    lateinit var useLL: LinearLayout
    lateinit var coupon_payLL: LinearLayout
    lateinit var coupon_payTV: TextView


    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()

    lateinit var useradapter: UserListAdapter
    var data = arrayListOf<Int>()
    var totalpage = 0
    var year: Int = 1
    var month: Int = 1
    var day: Int = 1
    var company_id = -1
    var page: Int = 1
    var start_date: String? = null
    var end_date: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fra_point_list, container, false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLV = view.findViewById(R.id.userLV)
        first_dateLL = view.findViewById(R.id.first_dateLL)
        last_dateLL = view.findViewById(R.id.last_dateLL)
        first_dateTV = view.findViewById(R.id.first_dateTV)
        last_dateTV = view.findViewById(R.id.last_dateTV)
        startdateTV = view.findViewById(R.id.startdateTV)
        lastdateTV = view.findViewById(R.id.lastdateTV)
        all_cntTV = view.findViewById(R.id.all_cntTV)
        all_stackTV = view.findViewById(R.id.all_stackTV)
        all_useTV = view.findViewById(R.id.all_useTV)
        all_couponTV = view.findViewById(R.id.all_couponTV)
        nonameTV = view.findViewById(R.id.nonameTV)
        accumulateLL = view.findViewById(R.id.accumulateLL)
        allLL = view.findViewById(R.id.allLL)
        todayLL = view.findViewById(R.id.todayLL)
        weekLL = view.findViewById(R.id.weekLL)
        monthLL = view.findViewById(R.id.monthLL)
        allTV = view.findViewById(R.id.allTV)
        todayTV = view.findViewById(R.id.todayTV)
        weekTV = view.findViewById(R.id.weekTV)
        monthTV = view.findViewById(R.id.monthTV)
        useLL = view.findViewById(R.id.useLL)
        integratedTV = view.findViewById(R.id.integratedTV)
        coupon_payLL = view.findViewById(R.id.coupon_payLL)
        coupon_payTV = view.findViewById(R.id.coupon_payTV)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //날짜갖고오기
        var calendar = GregorianCalendar()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)


        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val date = Date()
        val currentDate = formatter.format(date)
        company_id = PrefUtils.getIntPreference(context, "company_id")

        allLL.setOnClickListener {
            setmenu()
            allTV.setTextColor(Color.parseColor("#ffffff"))
            startdateTV.text = "총합"
            nonameTV.visibility = View.GONE
            lastdateTV.text = ""
            loadmainData(company_id)
        }
        todayLL.setOnClickListener {
            setmenu()
            todayTV.setTextColor(Color.parseColor("#ffffff"))
            startdateTV.text = currentDate
            nonameTV.visibility = View.VISIBLE
            lastdateTV.text = currentDate
            loadmainData(company_id)
        }
        weekLL.setOnClickListener {
            setmenu()
            weekTV.setTextColor(Color.parseColor("#ffffff"))
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val df = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            var startDate = df.format(calendar.getTime())
            calendar.add(Calendar.DATE, 6)
            var endDate = df.format(calendar.getTime())
            startdateTV.text = startDate
            nonameTV.visibility = View.VISIBLE
            lastdateTV.text = endDate
            loadmainData(company_id)
        }
        monthLL.setOnClickListener {
            setmenu()
            monthTV.setTextColor(Color.parseColor("#ffffff"))
            val beforemonth = SimpleDateFormat("yyyy.MM.01", Locale.KOREA)
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val date = Date()
            val currentDate = beforemonth.format(date)
            val lastmonth = aftermonth.format(date).toString().substring(0, 8) + endDay

            startdateTV.text = currentDate
            nonameTV.visibility = View.VISIBLE
            lastdateTV.text = lastmonth
            loadmainData(company_id)
        }


        useLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("step", 4)
            startActivity(intent)
        }
        accumulateLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            startActivity(intent)
        }
        useradapter = UserListAdapter(myContext, R.layout.item_user_point_list, adapterData)
        userLV.adapter = useradapter

        var lastitemVisibleFlag = false        //화면에 리스트의 마지막 아이템이 보여지는지 체크
        userLV.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                lastitemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    if (totalpage > page) {
                        page++
                        loadmainData(company_id)
                    }

                }
            }

        })

        userLV.setOnItemClickListener { parent, view, position, id ->
            var data = adapterData.get(position)
            Log.d("리스트선택", data.toString())
            val member = data.getJSONObject("Member")
            val member_id = Utils.getInt(member, "id")
            loaditemdata(company_id, member_id)
        }


        userLV.setOnItemLongClickListener { parent, view, position, id ->
            var data = adapterData.get(position)
            val member = data.getJSONObject("Member")
            var point = data.getJSONObject("Point")
            var type = Utils.getInt(point, "type")
            var cate = Utils.getInt(point, "cate")
            var point_id = Utils.getInt(point, "id")
            if (type == 1){
                var mPopupDlg: DialogInterface? = null
                val builder = AlertDialog.Builder(myContext)
                val dialogView = layoutInflater.inflate(R.layout.dlg_send_payback, null)
                val cancelTV = dialogView.findViewById<TextView>(R.id.cancelTV)
                val msgWriteTV = dialogView.findViewById<TextView>(R.id.msgWriteTV)
                mPopupDlg = builder.setView(dialogView).show()
                cancelTV.setOnClickListener {
                    mPopupDlg.dismiss()
                }
                msgWriteTV.setOnClickListener {
                    payback(point_id)
                    loadmainData(company_id)
                    mPopupDlg.dismiss()
                }

            }else if (cate==3){
                Toast.makeText(myContext,"환불된 포인트입니다.",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(myContext,"사용한포인트는 환불이 불가합니다",Toast.LENGTH_SHORT).show()
            }
            true
        }




        first_dateLL.setOnClickListener {
            datedlg()
        }
        last_dateLL.setOnClickListener {
            datedlg2()
        }
        startdateTV.text = "총합"
        nonameTV.visibility = View.GONE
        lastdateTV.text = ""


        loadmainData(company_id)

    }


    fun setmenu() {
        allTV.setTextColor(Color.parseColor("#80ffffff"))
        todayTV.setTextColor(Color.parseColor("#80ffffff"))
        weekTV.setTextColor(Color.parseColor("#80ffffff"))
        monthTV.setTextColor(Color.parseColor("#80ffffff"))
    }

    fun datedlg() {
        DatePickerDialog(myContext, dateSetListener, year, month, day).show()
    }

    fun datedlg2() {
        DatePickerDialog(myContext, dateSetListener2, year, month, day).show()
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub

        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)

        first_dateTV.text = msg
        startdateTV.text = msg
        nonameTV.visibility = View.VISIBLE
        Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show()
        loadmainData(company_id)
    }
    private val dateSetListener2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)
        val end_msg = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth)
        end_date = end_msg
        lastdateTV.text = msg
        last_dateTV.text = msg
        nonameTV.visibility = View.VISIBLE
        Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show()
        loadmainData(company_id)
    }
    override fun onPause() {
        super.onPause()
        page = 1
    }
    //환불
    fun payback(point_id: Int) {
        val params = RequestParams()
        params.put("point_id", point_id)
        PointAction.pay_back(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        Toast.makeText(myContext,"환불되었습니다.",Toast.LENGTH_SHORT).show()

                    }else if ("already"==result){
                        Toast.makeText(myContext,"이미 환불된 포인트입니다.",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(myContext, "조회실패", Toast.LENGTH_SHORT).show()
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
    //총포인트내역
    fun loadmainData(company_id: Int) {
        val params = RequestParams()
        start_date = Utils.getString(startdateTV)
        if (start_date.equals("총합")) {
            start_date = null
        }

        params.put("company_id", company_id)
        params.put("start_date", start_date)
        System.out.print("시작" + start_date)
        params.put("end_date", end_date)
        params.put("page", page)
        System.out.print("끝" + end_date)


        PointAction.index(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                        Log.d("대이터",response.toString())
                    if ("ok" == result) {
                        val data = response.getJSONObject("pointData")
                        val allCount = Utils.getInt(data, "allCount")
                        val addPointCount = Utils.getInt(data, "addPointCount")
                        val addPoint = Utils.getInt(data, "addPoint")
                        val addPointMember = Utils.getInt(data, "addPointMember")
                        val usePointCount = Utils.getInt(data, "usePointCount")
                        val usePoint = Utils.getInt(data, "usePoint")
                        val usePointMember = Utils.getInt(data, "usePointMember")
                        val useCouponMembers = Utils.getInt(data, "useCouponMembers")
                        val useCouponCount = Utils.getInt(data, "useCouponCount")
                        var useCouponPay = Utils.getInt(data, "useCouponPay")
                        var allPointMembers = Utils.getInt(data, "allPointMembers")
                        totalpage = response.getInt("totalPage")

                        var allcnt = allPointMembers
                        val total_point_cnt = addPointCount + usePointCount

                        if (allcnt == -1){
                            allcnt = 0
                        }


                        all_couponTV.text = useCouponMembers.toString()+"명/"+useCouponCount.toString()+"회"
                        all_cntTV.text = allcnt.toString() + "명"
                        all_stackTV.text = addPointMember.toString() + "명/" + addPointCount + "회/" + Utils.comma(addPoint.toString())+ "P"
                        all_useTV.text = usePointMember.toString() + "명/" + usePointCount + "회/" + Utils.comma(usePoint.toString()) + "P"
                        if (useCouponPay==-1){
                            useCouponPay = 0
                        }


                        coupon_payTV.text = Utils.comma(useCouponPay.toString())+"원"
                        val data2 = response.getJSONArray("member_list")
                        if (page==1){
                            adapterData.clear()
                        }
                        useradapter.notifyDataSetChanged()
                        Log.d("멤버리스트", data2.toString())
                        for (i in 0..(data2.length() - 1)) {

                            adapterData.add(data2[i] as JSONObject)

                        }


                    } else {
                        Toast.makeText(myContext, "조회실패", Toast.LENGTH_SHORT).show()
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

    //아이템클릭
    fun loaditemdata(company_id: Int, member_id: Int) {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("member_id", member_id)
        params.put("start_date", start_date)
        params.put("end_date", end_date)

        PointAction.user_points(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {




                        val member_o = response.getJSONObject("member")

                        val member = member_o.getJSONObject("Member")
                        var point = Utils.getString(member, "point")
                        var use_point = Utils.getString(member, "use_point")
                        var coupon_cnt = Utils.getString(member, "coupon_cnt")
                        var coupon_pay = Utils.getString(member, "coupon_pay")


                        //총방문횟수
                        val visit_cnt = response.getString("point")
                        //포인트사용횟수
                        val use_point_cnt = response.getString("use_point_cnt")
                        //포인트적립횟수
                        val stack_point_cnt = response.getString("stack_point_cnt")

                        if (use_point.equals("")) {
                            use_point = "0"
                        }
                        if (point.equals("")) {
                            point = "0"
                        }
                        if (coupon_pay.equals("")||coupon_pay=="-1") {
                            coupon_pay = "0"
                        }
                        integratedTV.text = "방문횟수"
                        all_couponTV.text = coupon_cnt+"회"
                        all_cntTV.text = visit_cnt + "회"
                        all_stackTV.text = stack_point_cnt + "회/" + Utils.comma(point) + "P"
                        all_useTV.text = use_point_cnt + "회/" + Utils.comma(use_point) + "P"
                        coupon_payTV.text =coupon_pay+"원"

                    } else {
                        Toast.makeText(myContext, "조회실패", Toast.LENGTH_SHORT).show()
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
