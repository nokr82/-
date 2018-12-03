package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.Actions.MemberAction.member_join
import com.devstories.anipointcompany.android.Actions.RequestStepAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.R.id.*
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.RootActivity
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_point.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class CalActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var type = -1
    var step = -1
    var member_id = -1
    var p_type = -1
    var phone = ""
    var payment_type  =-1
   var category_id = -1
    var per_type = -1


    var stackpoint = -1
    lateinit var adapter: ArrayAdapter<String>
    var option_cate = ArrayList<String>()

    internal var checkHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            checkStep()
        }
    }

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)

        this.context = this
        progressDialog = ProgressDialog(context)

        intent = getIntent()
        type = intent.getIntExtra("type", -1)
        step = intent.getIntExtra("step", 1)

        setmenu()
        if (step ==4){
            opTV.text = "사용"
            m_opTV.text = "P"
        }
        company_info()
        //계산기
        cal()


        //결제내용스피너
        cate_SP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position==0){
                    category_id =1
                    Log.d("포지션",category_id.toString())
                }else if (position==1){
                    category_id =2
                }else if (position==2){
                    category_id =3
                }else if (position==3){
                    category_id =4
                }else if (position==4){
                    category_id =5
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        op_accLL.setOnClickListener {

        }

        cardPayLL.setOnClickListener {
            setmenu2()
            cardPayIV.setImageResource(R.drawable.radio_on)
            payment_type = 1
        }
        cashPayLL.setOnClickListener {
            setmenu2()
            cashPayIV.setImageResource(R.drawable.radio_on)
            payment_type = 2
        }
        //무통장입금
        depositlessLL.setOnClickListener {
            setmenu2()
            depositlessIV.setImageResource(R.drawable.radio_on)
            payment_type = 3
        }
        changeStep()

    }




    fun setmenu(){
        maleIV.setImageResource(R.drawable.radio_off)
        femaleIV.setImageResource(R.drawable.radio_off)
    }
    fun setmenu2(){
        cardPayIV.setImageResource(R.drawable.radio_off)
        cashPayIV.setImageResource(R.drawable.radio_off)
        depositlessIV.setImageResource(R.drawable.radio_off)
    }

    //계산클릭이벤트
    fun cal(){
        stackLL.setOnClickListener {
            //기본퍼센트
            per_type = 1

        }
        stack2LL.setOnClickListener {
            //임의 퍼센트
            val managerpercent = stack2TV.text.toString()
            val money = moneyTV.text.toString()
            per_type = 2
            if (managerpercent == null) {
                Toast.makeText(context, "퍼센트를 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (money == null) {
                Toast.makeText(context, "가격을 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val percent = managerpercent.toFloat() / 100
            val floatPoint = (money.toFloat() * percent)
            val stringPoint = floatPoint.toString()
            var splitPoint =  stringPoint.split(".")
            val point = splitPoint.get(0)
            pointTV.setText(point)

        }

        checkLL.setOnClickListener {
            member_join()
        }
        titleTV.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
        oneLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 1)
            setPoint()
        }
        twoLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 2)
            setPoint()
        }
        threeLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 3)
            setPoint()
        }
        fourLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 4)
            setPoint()
        }
        fiveLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 5)
            setPoint()
        }
        sixLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 6)
            setPoint()
        }
        sevenLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 7)
            setPoint()
        }
        eightLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 8)
            setPoint()
        }
        nineLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 9)
            setPoint()
        }
        zeroLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 0)
            setPoint()
        }

        delLL.setOnClickListener {
            val text = moneyTV.getText().toString()
            val defaultpercent =stackTV.text.toString()
            if (text.length > 0) {
                moneyTV.setText(text.substring(0, text.length - 1))
                val money = moneyTV.text.toString()
                if(money != null && money != "") {
                    val percent = defaultpercent.toFloat() / 100
                    val floatPoint = (money.toFloat() * percent)
                    val stringPoint = floatPoint.toString()
                    var splitPoint = stringPoint.split(".")
                    val point = splitPoint.get(0)
                    pointTV.setText(point)
                }
            } else {
            }
        }
        useLL.setOnClickListener {
            if(opTV.text.equals("적립")){
                if (per_type==1){
                    val totalpoint =Integer.parseInt(moneyTV.text.toString())
                    Log.d("포인트", totalpoint.toString())
                    stackpoint = totalpoint*Integer.parseInt(stackTV.text.toString())/100
                    step = 3
                    changeStep()
                    p_type=1
                    stack_point(member_id.toString())
                }else if (per_type ==2){
                    val totalpoint =Integer.parseInt(moneyTV.text.toString())
                    Log.d("포인트", totalpoint.toString())
                    stackpoint = totalpoint*Integer.parseInt(stack2TV.text.toString())/100
                    step = 3
                    changeStep()
                    p_type=1
                    stack_point(member_id.toString())
                }else{
                    Toast.makeText(context,"적립퍼센트를 선택해주세요",Toast.LENGTH_SHORT).show()
                }

            }

            if (opTV.text.equals("사용")){
            val totalpoint =Integer.parseInt(moneyTV.text.toString())
            val use_point =Integer.parseInt(stack_pointTV.text.toString())
            stackpoint = totalpoint
            Log.d("사용포인트", totalpoint.toString())
            if(stackpoint>use_point){
                Toast.makeText(context,"포인트가 부족합니다",Toast.LENGTH_SHORT).show()
            }else{
                step = 6
                p_type=2
                stack_point(member_id.toString())
                changeStep()
            }
            }
        }



        changeStep()

    }

    fun setPoint(){

        val defaultpercent = stackTV.text.toString()
        val money = moneyTV.text.toString()

        if (defaultpercent == null) {
            Toast.makeText(context, "퍼센트를 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (money == null) {
            Toast.makeText(context, "가격을 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val percent = defaultpercent.toFloat() / 100
        val floatPoint = (money.toFloat() * percent)
        val stringPoint = floatPoint.toString()
        var splitPoint =  stringPoint.split(".")
        val point = splitPoint.get(0)
        pointTV.setText(point)

    }

    // 프로세스
    fun changeStep() {
        val params = RequestParams()
        params.put("member_id",member_id)
        params.put("company_id", 1)
        params.put("point", stackpoint)//사용및적립포인트
        params.put("type", p_type)//1적립 2사용
        //    params.put("use_point", p_type)//사용 포인트
        params.put("price", p_type)//상품가격
        params.put("payment_type", payment_type)//결제방법
        params.put("use_type", p_type)//1적립 2사용 3 적립/사용
        params.put("category_id",category_id)//카테고리 일련번호

        if (payment_type==-1){
            Toast.makeText(context,"결제방식을 선택해주세요",Toast.LENGTH_SHORT).show()
            return
        }


        MemberAction.point(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    val result = response!!.getString("result")
                    Log.d("적립",response.toString())

                    if ("ok" == result) {
                        var requestStep = response.getJSONObject("RequestStep")
                        var step = Utils.getInt(requestStep,"step")
                        if (step ==3){
                        timer!!.cancel()
                        }

//                        step = Utils.getInt(requestStep, "step")



                        timerStart()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }


            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONObject?
            ) {
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


    // 요청 체크
    fun checkStep() {
        val params = RequestParams()
        params.put("company_id", 1)

        RequestStepAction.checkStep(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var requestStep = response.getJSONObject("RequestStep")
                        var member = response.getJSONObject("Member")
                        var point  = response.getJSONObject("Point")

//                        step = Utils.getInt(requestStep, "step")
                        member_id = Utils.getInt(requestStep, "member_id")
                        val result_step = Utils.getInt(requestStep, "step")
                        val new_member_yn = Utils.getString(requestStep, "new_member_yn")

                        if(step != result_step) {

                            if (timer != null) {
                                timer!!.cancel()
                            }

                            step = result_step

                            if(step == 2) {
                                // 적립 -> 회원 정보

                                opTV.text = "적립"
                                //신규회원이 아닐경우
                                if(new_member_yn == "Y") {
                                    joinLL.visibility = View.VISIBLE
                                    message_op_LL.visibility = View.GONE
                                    checkLL.visibility = View.VISIBLE
                                } else {
                                    message_op_LL.visibility = View.VISIBLE
                                    joinLL.visibility = View.GONE
                                    checkLL.visibility = View.GONE
                                }

                                var phone = Utils.getString(member, "phone")
                                var gender = Utils.getString(member, "gender")
                                var age = Utils.getString(member, "age")
                                var birth = Utils.getString(member, "birth")
                                var coupon = Utils.getString(member, "coupon")
                                var memo = Utils.getString(member, "memo")
                                var name = Utils.getString(member, "name")
                                var left_point:String? = null
                                if (new_member_yn.equals("N")){
                                    left_point = Utils.getString(point, "balance")
                                }


                                stack_pointTV.text = left_point
                                titleTV.text = name
                                if (gender.equals("M")){
                                    setmenu()
                                    maleIV.setImageResource(R.drawable.radio_on)
                                }else if (gender.equals("F")){
                                    setmenu()
                                    femaleIV.setImageResource(R.drawable.radio_on)
                                }
                                phoneTV.text = phone
                                ageTV.text = age
                                birthTV.text = birth
                                couponTV.text = coupon
                                memoTV.text = memo

                            }else if(step == 5){
                                var phone = Utils.getString(member, "phone")
                                var gender = Utils.getString(member, "gender")
                                var age = Utils.getString(member, "age")
                                var birth = Utils.getString(member, "birth")
                                var coupon = Utils.getString(member, "coupon")
                                var memo = Utils.getString(member, "memo")
                                var name = Utils.getString(member, "name")
                                var left_point:String? = null
                                if (new_member_yn.equals("N")){
                                    left_point = Utils.getString(point, "balance")
                                }

                                titleTV.text = name
                                stack_pointTV.text = left_point
                                titleTV.text = name
                                if (gender.equals("M")){
                                    setmenu()
                                    maleIV.setImageResource(R.drawable.radio_on)
                                }else if (gender.equals("F")){
                                    setmenu()
                                    femaleIV.setImageResource(R.drawable.radio_on)
                                }
                                phoneTV.text = phone
                                ageTV.text = age
                                birthTV.text = birth
                                couponTV.text = coupon
                                memoTV.text = memo
                            }

                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }


            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }


            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONObject?
            ) {
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

    fun timerStart(){
        val task = object : TimerTask() {
            override fun run() {
                checkHandler.sendEmptyMessage(0)
            }
        }

        timer = Timer()
        timer!!.schedule(task, 0, 2000)
    }
    //포인트적립/사용
    fun stack_point(member_id:String) {
        val params = RequestParams()
        params.put("member_id",member_id)
        params.put("company_id", 1)
        params.put("point", stackpoint)//사용및적립포인트
        params.put("type", p_type)//1적립 2사용
        //    params.put("use_point", p_type)//사용 포인트
        params.put("price", p_type)//상품가격
        params.put("payment_type", payment_type)//결제방법
        params.put("use_type", p_type)//1적립 2사용 3 적립/사용
        params.put("category_id",category_id)//카테고리 일련번호

        if (payment_type==-1){
            Toast.makeText(context,"결제방식을 선택해주세요",Toast.LENGTH_SHORT).show()
            return
        }


        MemberAction.point(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    val result = response!!.getString("result")
                    Log.d("적립",response.toString())

                    if ("ok" == result) {
                        val intent = Intent(context, UserListActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        if (p_type==1){
                            Toast.makeText(context,stackpoint.toString()+"적립됩니다",Toast.LENGTH_SHORT).show()
                        }else if (p_type==2){
                            Toast.makeText(context,stackpoint.toString()+"사용됩니다",Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
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




    //가입
    fun member_join() {
        var getid = member_id
        var getPhone = Utils.getString(phoneTV)
        var getGender = Utils.getString(genderET)
        var getAge = Utils.getString(ageET)
        var getBirth = Utils.getString(birthET)
        var getPoint = Utils.getString(stack_pointET)
        var getCoupon = Utils.getString(couponET)
        var getMemo = Utils.getString(memoET)
        var getName = Utils.getString(nameET)

        val params = RequestParams()
        params.put("company_id", 1)
        params.put("member_id", getid)
        params.put("age", getAge)
        params.put("point", getPoint)
        params.put("name", getName)
        params.put("gender", getGender)
        params.put("memo", getMemo)
        params.put("phone", getPhone)
        params.put("birth", getBirth)

        MemberAction.member_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    val result = response!!.getString("result")


                    if ("ok" == result) {
                        Toast.makeText(context, "회원등록완료", Toast.LENGTH_SHORT).show()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
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

    //사업체 정보뽑기
    fun company_info() {
        val params = RequestParams()
        params.put("company_id",1)


        CompanyAction.company_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        val company = response.getJSONObject("company")
                        // 기본적립
                        val basic_per = Utils.getString(company,"basic_per")
                        //임의적립
                        val option_per = Utils.getInt(company,"option_per")
                        val data = response.getJSONArray("categories")
                        Log.d("카테",data.toString())
                        for (i in 0..data.length()-1){
                            val json = data[i]as JSONObject
                            val Category  = json.getJSONObject("Category")
                            val name = Utils.getString(Category,"name")
                            option_cate.add(name)

                        }
                        adapter = ArrayAdapter(context,R.layout.spiner_cal_item,option_cate)
                        cate_SP.adapter = adapter


                        stackTV.text = basic_per.toString()
                        stack2TV.text = option_per.toString()

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
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
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

        if (timer != null) {
            timer!!.cancel()
        }

    }

}


