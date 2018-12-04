package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.Actions.CompanyAction.company_info
import com.devstories.anipointcompany.android.Actions.CouponAction

import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream

//메세지관리(고객선택화면)

class MessageUserFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    var option_visitday = arrayOf("전체", "15일", "30일", "60일", "90일")

    lateinit var visitdaySP: Spinner

    lateinit var tenLL: LinearLayout
    lateinit var twoLL: LinearLayout
    lateinit var threeLL: LinearLayout
    lateinit var fourLL: LinearLayout
    lateinit var fiveLL: LinearLayout
    lateinit var sixLL: LinearLayout

    lateinit var genderMLL: LinearLayout
    lateinit var genderFLL: LinearLayout
    lateinit var genderNLL: LinearLayout

    lateinit var citizenLL: LinearLayout
    lateinit var workerLL: LinearLayout
    lateinit var studentLL: LinearLayout

    lateinit var tenTV: TextView
    lateinit var twoTV: TextView
    lateinit var threeTV: TextView
    lateinit var fourTV: TextView
    lateinit var fiveTV: TextView
    lateinit var sixTV: TextView

    lateinit var girlTV: TextView
    lateinit var menTV: TextView
    lateinit var genderTV: TextView

    lateinit var citizenTV: TextView
    lateinit var workerTV: TextView
    lateinit var studentTV: TextView
    lateinit var cautionTV: TextView

    lateinit var allRL: RelativeLayout
    lateinit var acc_countRL: RelativeLayout
    lateinit var use_moneyRL: RelativeLayout
    lateinit var novisitRL: RelativeLayout
    lateinit var pointRL: RelativeLayout

    lateinit var allTV: TextView
    lateinit var acc_countTV: TextView
    lateinit var use_moneyTV: TextView
    lateinit var novisitTV: TextView
    lateinit var pointTV: TextView
    lateinit var nextTV: TextView
    lateinit var countTV: TextView
    lateinit var visitLL: LinearLayout
    lateinit var limitLL: LinearLayout
    lateinit var limit_opTV: TextView
    lateinit var limit_op2TV: TextView
    lateinit var limit_opET: EditText
    lateinit var limit_op2ET: EditText


    var gender = ArrayList<String>()
    var age =  ArrayList<String>()

    var visited_date = ""
    var days7_yn = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.fra_message_wirte_step1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countTV = view.findViewById(R.id.countTV)
        nextTV = view.findViewById(R.id.nextTV)
        visitdaySP = view.findViewById(R.id.visitdaySP)
        tenLL = view.findViewById(R.id.tenLL)
        twoLL = view.findViewById(R.id.twoLL)
        threeLL = view.findViewById(R.id.threeLL)
        fourLL = view.findViewById(R.id.fourLL)
        fiveLL = view.findViewById(R.id.fiveLL)
        sixLL = view.findViewById(R.id.sixLL)
        studentLL = view.findViewById(R.id.studentLL)
        workerLL = view.findViewById(R.id.workerLL)
        genderNLL = view.findViewById(R.id.genderNLL)
        genderMLL = view.findViewById(R.id.genderMLL)
        genderFLL = view.findViewById(R.id.genderFLL)
        citizenLL = view.findViewById(R.id.citizenLL)
        tenTV = view.findViewById(R.id.tenTV)
        twoTV = view.findViewById(R.id.twoTV)
        threeTV = view.findViewById(R.id.threeTV)
        fourTV = view.findViewById(R.id.fourTV)
        fiveTV = view.findViewById(R.id.fiveTV)
        sixTV = view.findViewById(R.id.sixTV)
        studentTV = view.findViewById(R.id.studentTV)
        workerTV = view.findViewById(R.id.workerTV)
        cautionTV = view.findViewById(R.id.cautionTV)
        genderTV = view.findViewById(R.id.genderTV)
        menTV = view.findViewById(R.id.menTV)
        girlTV = view.findViewById(R.id.girlTV)
        citizenTV = view.findViewById(R.id.citizenTV)
        cautionTV = view.findViewById(R.id.cautionTV)
        allRL = view.findViewById(R.id.allRL)
        use_moneyRL =view.findViewById(R.id.use_moneyRL)
        acc_countRL = view.findViewById(R.id.acc_countRL)
        novisitRL = view.findViewById(R.id.novisitRL)
        pointRL = view.findViewById(R.id.pointRL)
        allTV = view.findViewById(R.id.allTV)
        use_moneyTV =view.findViewById(R.id.use_moneyTV)
        acc_countTV = view.findViewById(R.id.acc_countTV)
        novisitTV = view.findViewById(R.id.novisitTV)
        pointTV = view.findViewById(R.id.pointTV)
        visitLL= view.findViewById(R.id.visitLL)
        limitLL= view.findViewById(R.id.limitLL)
        limit_opTV= view.findViewById(R.id.limit_opTV)
        limit_op2TV= view.findViewById(R.id.limit_op2TV)
        //처음 횟수
        limit_opET= view.findViewById(R.id.limit_opET)
        //마지막 횟수
        limit_op2ET= view.findViewById(R.id.limit_op2ET)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_visitday)
        visitdaySP.adapter = adapter

        setmenu3()
        setfilter()
        setopview()
        //스피너 선택이벤트
        visitdaySP.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position==0){

                }else if (position==1){
                    visited_date = "15"
                }else if (position==2){
                    visited_date = "30"
                }else if (position==3){
                    visited_date = "60"
                }else if (position==4){
                    visited_date = "90"
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }



        genderNLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                genderNLL.setBackgroundResource(R.drawable.background_00d1ce)
                genderTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                genderNLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                genderTV.setTextColor(Color.parseColor("#9a9a99"))
            }

        }
        genderMLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                gender.add("M")
                genderMLL.setBackgroundResource(R.drawable.background_00d1ce)
                menTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                gender.remove("M")
                genderMLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                menTV.setTextColor(Color.parseColor("#9a9a99"))
            }

        }
        genderFLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                gender.add("F")
                genderFLL.setBackgroundResource(R.drawable.background_00d1ce)
                girlTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                gender.remove("F")
                genderFLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                girlTV.setTextColor(Color.parseColor("#9a9a99"))
            }
        }

        tenLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                age.add("10")
                tenLL.setBackgroundResource(R.drawable.background_00d1ce)
                tenTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                age.remove("10")
                tenLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                tenTV.setTextColor(Color.parseColor("#9a9a99"))
            }
        }

        twoLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                age.add("20")
                twoLL.setBackgroundResource(R.drawable.background_00d1ce)
                twoTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                age.remove("20")
                twoLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                twoTV.setTextColor(Color.parseColor("#9a9a99"))
            }
        }

        threeLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                age.add("30")
                threeLL.setBackgroundResource(R.drawable.background_00d1ce)
                threeTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                age.remove("30")
                threeLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                threeTV.setTextColor(Color.parseColor("#9a9a99"))
            }
        }

        fourLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                age.add("40")
                fourLL.setBackgroundResource(R.drawable.background_00d1ce)
                fourTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                age.remove("40")
                fourLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                fourTV.setTextColor(Color.parseColor("#9a9a99"))
            }
        }

        fiveLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                age.add("50")
                fiveLL.setBackgroundResource(R.drawable.background_00d1ce)
                fiveTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                age.remove("50")
                fiveLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                fiveTV.setTextColor(Color.parseColor("#9a9a99"))
            }
        }

        sixLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                age.add("60")
                sixLL.setBackgroundResource(R.drawable.background_00d1ce)
                sixTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                age.remove("60")
                sixLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                sixTV.setTextColor(Color.parseColor("#9a9a99"))
            }
        }

        citizenLL.setOnClickListener {
            setmenu3()
            citizenLL.setBackgroundResource(R.drawable.background_00d1ce)
            citizenTV.setTextColor(Color.parseColor("#ffffff"))
        }
        studentLL.setOnClickListener {
            setmenu3()
            studentLL.setBackgroundResource(R.drawable.background_00d1ce)
            studentTV.setTextColor(Color.parseColor("#ffffff"))
        }

        workerLL.setOnClickListener {
            setmenu3()
            workerLL.setBackgroundResource(R.drawable.background_00d1ce)
            workerTV.setTextColor(Color.parseColor("#ffffff"))

        }


        allRL.setOnClickListener {
            setfilter()
            setopview()
            allRL.setBackgroundColor(Color.parseColor("#0068df"))
            allTV.setTextColor(Color.parseColor("#ffffff"))
        }
        acc_countRL.setOnClickListener {
            setfilter()
            setopview()
            limitLL.visibility = View.VISIBLE
            limit_opTV.text = "회"
            limit_op2TV.text = "회"
            acc_countRL.setBackgroundColor(Color.parseColor("#0068df"))
            acc_countTV.setTextColor(Color.parseColor("#ffffff"))
        }
        novisitRL.setOnClickListener {
            setfilter()
            setopview()
            visitLL.visibility = View.VISIBLE
            novisitRL.setBackgroundColor(Color.parseColor("#0068df"))
            novisitTV.setTextColor(Color.parseColor("#ffffff"))
        }
        use_moneyRL.setOnClickListener {
            setfilter()
            setopview()
            limit_opTV.text = "원"
            limit_op2TV.text = "원"
            limitLL.visibility = View.VISIBLE
            use_moneyRL.setBackgroundColor(Color.parseColor("#0068df"))
            use_moneyTV.setTextColor(Color.parseColor("#ffffff"))
        }
        pointRL.setOnClickListener {
            setfilter()
            setopview()
            limit_opTV.text = "P"
            limit_op2TV.text ="P"
            limitLL.visibility = View.VISIBLE
            pointRL.setBackgroundColor(Color.parseColor("#0068df"))
            pointTV.setTextColor(Color.parseColor("#ffffff"))
        }
        nextTV.setOnClickListener {
            member_filter()


        }

    }

    fun setfilter(){
        allRL.setBackgroundResource(R.drawable.background_strock_null)
        acc_countRL.setBackgroundResource(R.drawable.background_strock_null)
        novisitRL.setBackgroundResource(R.drawable.background_strock_null)
        use_moneyRL.setBackgroundResource(R.drawable.background_strock_null)
        pointRL.setBackgroundResource(R.drawable.background_strock_null)

        allTV.setTextColor(Color.parseColor("#c5c5c5"))
        use_moneyTV.setTextColor(Color.parseColor("#c5c5c5"))
        acc_countTV.setTextColor(Color.parseColor("#c5c5c5"))
        novisitTV.setTextColor(Color.parseColor("#c5c5c5"))
        pointTV.setTextColor(Color.parseColor("#c5c5c5"))

    }
    fun setopview(){
        limit_opET.setText("")
        limit_op2ET.setText("")
        visitLL.visibility = View.GONE
        limitLL.visibility = View.GONE
    }
    fun setmenu3(){
        citizenLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        workerLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        studentLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)

        citizenTV.setTextColor(Color.parseColor("#9a9a99"))
        workerTV.setTextColor(Color.parseColor("#9a9a99"))
        studentTV.setTextColor(Color.parseColor("#9a9a99"))
        cautionTV.setTextColor(Color.parseColor("#9a9a99"))
    }

    // 쿠폰만들기(step1) - (고객필터)
    fun member_filter() {
        val params = RequestParams()

        params.put("company_id",1)

        if (age.size>0){
            for (i in 0..(age.size -1)){
                val agestr = age[i]
                //배열로 입력저장은 [] 이걸 넣어준다
                params.put("age["+i+"]",agestr)
                Log.d("나이",agestr)
            }
        }else{
            Toast.makeText(myContext,"나이를 선택해주세요",Toast.LENGTH_SHORT).show()
            return
        }
        if (gender.size>0){
            for (i in 0..(gender.size -1)){
                val genderstr = gender[i]
                //배열로 입력저장은 [] 이걸 넣어준다
                params.put("gender["+i+"]",genderstr)
                Log.d("성별",genderstr)
            }
        }else{
            Toast.makeText(myContext,"성별을 선택해주세요",Toast.LENGTH_SHORT).show()
            return
        }
        params.put("visited_date",visited_date)





        CouponAction.member_filter(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var memberCnt = response.getString("memberCnt")

                        countTV.text = memberCnt
                        Toast.makeText(myContext,"고객선택완료",Toast.LENGTH_SHORT).show()
                        //브로드캐스트로 프래그먼트이동
                        var intent = Intent()
                        intent.putExtra("gender", "F")
                        intent.putExtra("age", "20")
                        intent.action = "STEP1_NEXT"
                        myContext.sendBroadcast(intent)
                    }else{
                        Toast.makeText(myContext,"업데이트실패", Toast.LENGTH_SHORT).show()

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
