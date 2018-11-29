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
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.R.id.*
import kotlinx.android.synthetic.main.fra_user_visit_analysis.*


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
    lateinit var accumulateLL: LinearLayout


  var day_type = -1
    var showCnt = 5;

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_amount)
        amountSP.adapter = adapter

        //오늘날짜구하기
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val date = Date()
        val currentDate = formatter.format(date)

        accumulateLL.setOnClickListener {
            val intent = Intent(myContext, PointActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btn_pre.setOnClickListener {

        }

        btn_next.setOnClickListener {

        }

        todayRL.setOnClickListener {
            setmenu()
            day_type = 1
            loadcntData()
            loadData(1)
            todayTV.setTextColor(Color.parseColor("#606060"))
            dateTV.text = currentDate+"~"+currentDate
        }

        todayRL.callOnClick()

        weekRL.setOnClickListener {
            setmenu()
            day_type = 2
            loadcntData()
            loadData(1)
            weekTV.setTextColor(Color.parseColor("#606060"))
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val df = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            var startDate = df.format(calendar.getTime())
            calendar.add(Calendar.DATE, 6)
            var   endDate = df.format(calendar.getTime())
            Log.d("현재",startDate)
            Log.d("미래",endDate)
            dateTV.text = startDate+" ~ "+endDate
        }
        monthRL.setOnClickListener {
            setmenu()
            day_type = 3
            loadcntData()
            loadData(1)
            monthTV.setTextColor(Color.parseColor("#606060"))
            val beforemonth = SimpleDateFormat("yyyy.MM.01", Locale.KOREA)
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val date = Date()
            val currentDate = beforemonth.format(date)
            val lastmonth = aftermonth.format(date).toString().substring(0,8)+endDay

            dateTV.text = currentDate+" ~ "+lastmonth
        }
        three_mRL.setOnClickListener {
            setmenu()
            day_type = 4
            loadcntData()
            loadData(1)
            three_mTV .setTextColor(Color.parseColor("#606060"))
            val aftermonth = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val cal = Calendar.getInstance()
            //그달의 마지막일 구하기
            val startday = cal.getActualMaximum(Calendar.MONTH)-3
            val beforemonth = SimpleDateFormat("yyyy."+startday+".dd", Locale.KOREA)
            val date = Date()
            val currentDate = beforemonth.format(date).toString()
            val lastmonth = aftermonth.format(date).toString()

            dateTV.text = currentDate+" ~ "+lastmonth
        }

        accumulateLL.setOnClickListener {
            var intent = Intent(myContext, PointActivity::class.java)
            intent.putExtra("step", 1)
            startActivity(intent)
        }

        //전체고객구하기
        loadcntData()
        loadData(1)


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
        params.put("day_type",day_type)


        PointAction.user_visited(params, object : JsonHttpResponseHandler() {

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


                        all_memberTV.text = allmember.toString()
                        new_userTV.text = member_new_cnt.toString()
                        member_re_cntTV.text  = member_re_cnt.toString()
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
        Log.d("day_type",day_type.toString())
        PointAction.user_visited(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        itemdateLL.removeAllViews()
                        val points = response.getJSONArray("points")
                        Log.d("데이트",points.toString())
                        for (i in 0..points.length()-1){
                            Log.d("갯수",i.toString())
                            var json=points[i] as JSONObject
                            val date = Utils.getString(json,"date")
                            val new_member = Utils.getInt(json,"new_member")
                            val re_member =Utils.getInt(json,"re_member")
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
