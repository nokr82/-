package com.devstories.anipointcompany.android.activities

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
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.*
import com.devstories.anipointcompany.android.Actions.PointAction
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


    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()

    lateinit var useradapter: UserListAdapter
    var data = arrayListOf<Int>()

    var year: Int = 1
    var month: Int = 1
    var day: Int = 1

    var start_date:String?=null
    var end_date :String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = ProgressDialog(myContext)
            return inflater.inflate(R.layout.fra_point_list,container,false)


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
        all_cntTV= view.findViewById(R.id.all_cntTV)
        all_stackTV= view.findViewById(R.id.all_stackTV)
        all_useTV= view.findViewById(R.id.all_useTV)
        all_couponTV= view.findViewById(R.id.all_couponTV)
        nonameTV= view.findViewById(R.id.nonameTV)
        accumulateLL = view.findViewById(R.id.accumulateLL)
        allLL = view.findViewById(R.id.allLL)
        todayLL = view.findViewById(R.id.todayLL)
        weekLL = view.findViewById(R.id.weekLL)
        monthLL = view.findViewById(R.id.monthLL)
        allTV = view.findViewById(R.id.allTV)
        todayTV = view.findViewById(R.id.todayTV)
        weekTV = view.findViewById(R.id.weekTV)
        monthTV= view.findViewById(R.id.monthTV)

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


        allLL.setOnClickListener {
            setmenu()
            allTV.setTextColor(Color.parseColor("#ffffff"))
            startdateTV.text = "총합"
            nonameTV.visibility = View.GONE
            lastdateTV.text = ""
            loadmainData(1)
        }
        todayLL.setOnClickListener {
            setmenu()
            todayTV.setTextColor(Color.parseColor("#ffffff"))
            startdateTV.text = currentDate
            nonameTV.visibility = View.VISIBLE
            lastdateTV.text=currentDate
            loadmainData(1)
        }
        weekLL.setOnClickListener {
            setmenu()
            weekTV.setTextColor(Color.parseColor("#ffffff"))
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val df = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            var startDate = df.format(calendar.getTime())
            calendar.add(Calendar.DATE, 6)
            var   endDate = df.format(calendar.getTime())
            startdateTV.text = startDate
            nonameTV.visibility = View.VISIBLE
            lastdateTV.text=endDate
            loadmainData(1)
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
            val lastmonth = aftermonth.format(date).toString().substring(0,8)+endDay

            startdateTV.text = currentDate
            nonameTV.visibility = View.VISIBLE
            lastdateTV.text=lastmonth
            loadmainData(1)
        }



        accumulateLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            startActivity(intent)
        }
        useradapter = UserListAdapter(myContext,R.layout.item_user_point_list,adapterData)
        userLV.adapter = useradapter
        loadData(1)
        userLV.setOnItemClickListener { parent, view, position, id ->
            var data = adapterData.get(position)
            Log.d("리스트선택",data.toString())
            val member = data.getJSONObject("Member")
            val member_id = Utils.getInt(member,"id")
            loaditemdata(1,member_id)

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


        loadmainData(1)

    }


    fun setmenu(){
        allTV.setTextColor(Color.parseColor("#80ffffff"))
        todayTV.setTextColor(Color.parseColor("#80ffffff"))
        weekTV.setTextColor(Color.parseColor("#80ffffff"))
        monthTV.setTextColor(Color.parseColor("#80ffffff"))
    }



    //방문이력
    fun loadData(company_id: Int) {
        val params = RequestParams()
        params.put("company_id",company_id)



        PointAction.index(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    adapterData.clear()
                    val result = response!!.getString("result")


                    if ("ok" == result) {
                        val data = response.getJSONArray("member_list")

                        for (i in 0..(data.length() - 1)) {

                            adapterData.add(data[i] as JSONObject)

                        }

                        useradapter.notifyDataSetChanged()


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



    fun datedlg(){
        DatePickerDialog(myContext, dateSetListener, year, month, day).show()
    }
    fun datedlg2(){
        DatePickerDialog(myContext, dateSetListener2, year, month, day).show()
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
        loadmainData(1)
    }
    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub

        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)

        first_dateTV.text = msg
        startdateTV.text = msg
        nonameTV.visibility = View.VISIBLE
          Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show()
        loadmainData(1)
    }

    //총포인트내역
    fun loadmainData(company_id: Int) {
        val params = RequestParams()
        start_date = Utils.getString(startdateTV)
        if (start_date.equals("총합")){
            start_date = null
        }


        params.put("company_id",company_id)
        params.put("start_date",start_date)
        System.out.print("시작"+start_date)
        params.put("end_date",end_date)
        System.out.print("끝"+end_date)




        PointAction.index(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        val data = response.getJSONObject("pointData")
                        val allCount = Utils.getInt(data,"allCount")
                        val addPointCount = Utils.getInt(data,"addPointCount")
                        val addPoint = Utils.getInt(data,"addPoint")
                        val addPointMember = Utils.getInt(data,"addPointMember")
                        val usePointCount = Utils.getInt(data,"usePointCount")
                        val usePoint = Utils.getInt(data,"usePoint")
                        val usePointMember = Utils.getInt(data,"usePointMember")
                        val allcnt =   addPointMember+usePointMember
                        val total_point_cnt = addPointCount+usePointCount


                        all_cntTV.text = allcnt.toString()+"명/"+total_point_cnt.toString()+"회"
                        all_stackTV.text = addPointMember.toString() +"명/"+addPointCount+"회/"+addPoint.toString()+"P"
                        all_useTV.text =usePointMember.toString() +"명/"+usePointCount+"회/"+usePoint.toString()+"P"


                    } else {
                    Toast.makeText(myContext,"조회실패",Toast.LENGTH_SHORT).show()
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
    fun loaditemdata(company_id: Int,member_id:Int) {
        val params = RequestParams()
        params.put("company_id",company_id)
        params.put("member_id",member_id)
        params.put("start_date",start_date)
        System.out.print("시작"+start_date)
        params.put("end_date",end_date)
        System.out.print("끝"+end_date)

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
                        var point = Utils.getString(member,"point")
                        var use_point  = Utils.getString(member,"use_point")


                        //총방문횟수
                      val visit_cnt = response.getString("point")
                        //포인트사용횟수
                        val use_point_cnt = response.getString("use_point_cnt")
                        //포인트적립횟수
                        val stack_point_cnt = response.getString("stack_point_cnt")

                        if(use_point.equals(null)){
                        use_point = "0"
                        }
                        if (point.equals(null)){
                            point = "0"
                        }
                        all_cntTV.text = visit_cnt+"회"
                        all_stackTV.text =stack_point_cnt+"회/"+point+"P"
                        all_useTV.text =use_point_cnt+"회/"+use_point+"P"


                    } else {
                        Toast.makeText(myContext,"조회실패",Toast.LENGTH_SHORT).show()
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
