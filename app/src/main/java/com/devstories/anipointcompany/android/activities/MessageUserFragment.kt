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
import com.devstories.anipointcompany.android.Actions.CouponAction
import kotlinx.android.synthetic.main.fra_message_wirte_step1.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream


/*     visitdaySP.adapter = adapter
       member_filter()
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
       }*/




//메세지관리(고객선택화면)

class MessageUserFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    var option_visitday = arrayOf("15일", "30일", "60일", "90일")

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



    lateinit var tenTV: TextView
    lateinit var twoTV: TextView
    lateinit var threeTV: TextView
    lateinit var fourTV: TextView
    lateinit var fiveTV: TextView
    lateinit var sixTV: TextView

    lateinit var girlTV: TextView
    lateinit var menTV: TextView
    lateinit var genderTV: TextView



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
    lateinit var limitLL: LinearLayout
    lateinit var limit_opTV: TextView
    lateinit var limit_op2TV: TextView
    lateinit var limit_opET: EditText
    lateinit var limit_op2ET: EditText

    lateinit var saveIV: ImageView


    var gender = ArrayList<String>()
    var age =  ArrayList<String>()
    var membership =  ArrayList<String>()

    var search_type = -1
    var stack_visit = -1
    var mising_day = -1
    var use_money = -1
    var left_point = -1

    var from = -1
    var to = -1
    var missing_from = -1
    var missing_to = -1
    var use_from = -1
    var use_to = -1
    var left_from = -1
    var left_to = -1



    var limit_op = ""
    var limit_op2 = ""



    var visited_date = ""
    var days7_yn = ""
    var coinResult  = ""
    var company_id = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.fra_message_wirte_step1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveIV = view.findViewById(R.id.saveIV)

        countTV = view.findViewById(R.id.countTV)
        nextTV = view.findViewById(R.id.nextTV)
        tenLL = view.findViewById(R.id.tenLL)
        twoLL = view.findViewById(R.id.twoLL)
        threeLL = view.findViewById(R.id.threeLL)
        fourLL = view.findViewById(R.id.fourLL)
        fiveLL = view.findViewById(R.id.fiveLL)
        sixLL = view.findViewById(R.id.sixLL)
        genderNLL = view.findViewById(R.id.genderNLL)
        genderMLL = view.findViewById(R.id.genderMLL)
        genderFLL = view.findViewById(R.id.genderFLL)
        tenTV = view.findViewById(R.id.tenTV)
        twoTV = view.findViewById(R.id.twoTV)
        threeTV = view.findViewById(R.id.threeTV)
        fourTV = view.findViewById(R.id.fourTV)
        fiveTV = view.findViewById(R.id.fiveTV)
        sixTV = view.findViewById(R.id.sixTV)
        genderTV = view.findViewById(R.id.genderTV)
        menTV = view.findViewById(R.id.menTV)
        girlTV = view.findViewById(R.id.girlTV)
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

        company_id = PrefUtils.getIntPreference(context, "company_id")
        search_type = 1
        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_visitday)


        setview()
        saveIV.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                saveIV.setImageResource(R.drawable.radio_on)
                from =  Utils.getInt(limit_opET)
                to = Utils.getInt(limit_op2ET)

                stack_visitTV.text = "적립횟수:"+ from.toString()+"회"+"~"+to.toString()+"회"
                stack_visit = 2

                stack_visitTV.visibility = View.VISIBLE
                member_filter()
            } else {
                stack_visit = -1
                from = -1
                to = -1
                stack_visitTV.visibility = View.GONE
                saveIV.setImageResource(R.drawable.radio_off)
            }
        }

        save2IV.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                save2IV.setImageResource(R.drawable.radio_on)
                missing_from  =  Utils.getInt(limit_op21ET)
                missing_to  = Utils.getInt(limit_op22ET)
                mising_dayTV.text = "미방문:"+ missing_from.toString()+"일"+"~"+missing_to.toString()+"일"
                mising_dayTV.visibility = View.VISIBLE
                mising_day = 3
                member_filter()
            } else {
                mising_day = -1
                missing_from  = -1
                missing_to  = -1
                mising_dayTV.visibility = View.GONE
                save2IV.setImageResource(R.drawable.radio_off)
            }
        }

        save3IV.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                save3IV.setImageResource(R.drawable.radio_on)
                use_from  =  Utils.getInt(limit_op3ET)
                use_to  = Utils.getInt(limit_op23ET)
                use_money2TV.text ="구매금액"+ use_from.toString()+"원"+"~"+use_to.toString()+"원"
                use_money2TV.visibility = View.VISIBLE
                use_money = 4
                member_filter()
            } else {
                use_money2TV.visibility = View.GONE
                use_money = -1
                use_from = -1
                use_to = -1
                save3IV.setImageResource(R.drawable.radio_off)
            }
        }
        save4IV.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                save4IV.setImageResource(R.drawable.radio_on)
                left_from  =  Utils.getInt(limit_op4ET)
                left_to  = Utils.getInt(limit_op24ET)
                left_point = 5
                left_pointTV.text = "포인트:"+left_from.toString()+"P"+"~"+left_to.toString()+"P"
                left_pointTV.visibility = View.VISIBLE
                member_filter()
            } else {
                left_point = -1
                left_pointTV.visibility = View.GONE
                left_from  = -1
                left_to  = -1
                save4IV.setImageResource(R.drawable.radio_off)
            }
        }


        acc_countRL.setOnClickListener {
            setfilter()
            setopview()
            limitLL.visibility = View.VISIBLE

            acc_countRL.setBackgroundColor(Color.parseColor("#0068df"))
            acc_countTV.setTextColor(Color.parseColor("#ffffff"))
        }
        novisitRL.setOnClickListener {
            setfilter()
            setopview()
            limit2LL.visibility = View.VISIBLE
            novisitRL.setBackgroundColor(Color.parseColor("#0068df"))
            novisitTV.setTextColor(Color.parseColor("#ffffff"))
        }
        use_moneyRL.setOnClickListener {
            setfilter()
            setopview()
            limit3LL.visibility = View.VISIBLE
            use_moneyRL.setBackgroundColor(Color.parseColor("#0068df"))
            use_moneyTV.setTextColor(Color.parseColor("#ffffff"))
        }
        pointRL.setOnClickListener {
            setfilter()
            setopview()
            limit4LL.visibility = View.VISIBLE
            pointRL.setBackgroundColor(Color.parseColor("#0068df"))
            pointTV.setTextColor(Color.parseColor("#ffffff"))
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
            member_filter ()


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
            member_filter()

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
            member_filter()

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
            member_filter()

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
            member_filter()

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
            member_filter()

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
            member_filter()

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
            member_filter()

        }


        bronzeLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                membership.add("")
                bronzeLL.setBackgroundResource(R.drawable.background_00d1ce)
                bronzeTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                membership.remove("")
                bronzeLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                bronzeTV.setTextColor(Color.parseColor("#9a9a99"))
            }
            member_filter()

        }

        silverLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                membership.add("S")
                silverLL.setBackgroundResource(R.drawable.background_00d1ce)
                silverTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                membership.remove("S")
                silverLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                silverTV.setTextColor(Color.parseColor("#9a9a99"))
            }
            member_filter()

        }

        goldLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                membership.add("G")
                goldLL.setBackgroundResource(R.drawable.background_00d1ce)
                goldTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                membership.remove("G")
                goldLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                goldTV.setTextColor(Color.parseColor("#9a9a99"))
            }
            member_filter()

        }

        vipLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                membership.add("V")
                vipLL.setBackgroundResource(R.drawable.background_00d1ce)
                vipTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                membership.remove("V")
                vipLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                vipTV.setTextColor(Color.parseColor("#9a9a99"))
            }
            member_filter()

        }

        vvipLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                membership.add("W")
                vvipLL.setBackgroundResource(R.drawable.background_00d1ce)
                vvipTV.setTextColor(Color.parseColor("#ffffff"))
            } else {
                membership.remove("W")
                vvipLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
                vvipTV.setTextColor(Color.parseColor("#9a9a99"))
            }
            member_filter()

        }




        allRL.setOnClickListener {
            setfilter()
            setopview()
            search_type = 1
            allRL.setBackgroundColor(Color.parseColor("#0068df"))
            allTV.setTextColor(Color.parseColor("#ffffff"))
            member_filter()

        }
        allRL.callOnClick()




        nextTV.setOnClickListener {
            member_filter()

            if (coinResult.equals("false")){
                Toast.makeText(myContext,"코인이 부족합니다",Toast.LENGTH_SHORT).show()
            }else {
                //브로드캐스트로 프래그먼트이동
                var intent = Intent()
                intent.putExtra("gender", gender)
                Log.d("gender", gender.toString())
                intent.putExtra("membership", membership)
                Log.d("membership", membership.toString())
                intent.putExtra("age",age)
                Log.d("age", age.toString())
                intent.putExtra("visited_date", visited_date)
                intent.putExtra("count", Utils.getString(countTV))
                intent.putExtra("from", Utils.getString(limit_opET))
                intent.putExtra("to", Utils.getString(limit_op2ET))
                intent.putExtra("missing_from", Utils.getString(limit_op21ET))
                intent.putExtra("missing_to", Utils.getString(limit_op22ET))
                intent.putExtra("use_from", Utils.getString(limit_op3ET))
                intent.putExtra("use_to", Utils.getString(limit_op23ET))
                intent.putExtra("left_from", Utils.getString(limit_op4ET))
                intent.putExtra("left_to", Utils.getString(limit_op24ET))

                intent.putExtra("stack_visit", stack_visit)
                intent.putExtra("mising_day", mising_day)
                intent.putExtra("use_money", use_money)
                intent.putExtra("left_point", left_point)

                intent.action = "STEP1_NEXT"
                myContext.sendBroadcast(intent)
            }

        }

    }


    override fun onPause() {
        super.onPause()
        search_type = -1
        stack_visit = -1
        mising_day = -1
        use_money = -1
         left_point = -1
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
    fun setview(){
        stack_visitTV.visibility = View.GONE
        mising_dayTV.visibility = View.GONE
        use_money2TV.visibility = View.GONE
        left_pointTV.visibility = View.GONE
    }

    fun setopview(){
        limit_opET.setText("")
        limit_op2ET.setText("")
        limitLL.visibility = View.GONE
        limit2LL.visibility = View.GONE
        limit3LL.visibility = View.GONE
        limit4LL.visibility = View.GONE
    }


    // 쿠폰만들기(step1) - (고객필터)
    fun member_filter() {
        val params = RequestParams()

        params.put("company_id", company_id)

        if (membership.size>0){
            for (i in 0..(membership.size -1)){
                val membershipstr = membership[i]
                //배열로 입력저장은 [] 이걸 넣어준다
                params.put("membership["+i+"]",membershipstr)
                Log.d("멤버쉽",membershipstr)
            }
        }
        if (age.size>0){
            for (i in 0..(age.size -1)){
                val agestr = age[i]
                //배열로 입력저장은 [] 이걸 넣어준다
                params.put("age["+i+"]",agestr)
                Log.d("나이",agestr)
            }
        }

        if (gender.size>0){
            for (i in 0..(gender.size -1)){
                val genderstr = gender[i]
                //배열로 입력저장은 [] 이걸 넣어준다
                params.put("gender["+i+"]",genderstr)
                Log.d("성별",genderstr)
            }
        }
        if (stack_visit==2){
            params.put("stack_visit",stack_visit)
            params.put("from",from)
            params.put("to",to)
        }
        if (mising_day==3){
            params.put("mising_day",mising_day)
            params.put("missing_from ",missing_from)
            params.put("missing_to ",missing_to)
        }
        if (use_money==4){
            params.put("use_money",use_money)
            params.put("use_from",use_from)
            params.put("use_to",use_to)
        }
        if (left_point==5){
            params.put("left_point",left_point)
            params.put("left_from",left_from)
            params.put("left_to",left_to)
        }




        CouponAction.member_filter(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    Log.d("결과",response.toString())
                    if ("ok" == result) {
                        var memberCnt = response.getString("memberCnt")
                        coinResult = response.getString("coinResult")

                        countTV.text = memberCnt


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
