package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.Actions.RequestStepAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.adapter.CouponListAdapter
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

    var request_step_id = -1
    var company_id = -1
    var type = -1
    var step = -1
    var member_id = -1
    var p_type = -1
    var price = 0
    var phone = ""
    var payment_type = -1
    var category_id = -1
    var per_type = -1
    var new_gender = ""
    var per = ""
    var member_coupon_id = -1
    var use_point = -1
    var couponData: ArrayList<JSONObject> = ArrayList<JSONObject>()
    lateinit var couponListAdapter: CouponListAdapter
    var new_member_yn = ""
    var stackpoint = -1
    lateinit var adapter: ArrayAdapter<String>
    var option_cate = ArrayList<String>()

    var EDIT_POINT = 101
    var option_age = arrayOf("미입력","10대","20대","30대","40대","50대","60대")

    var age = ""

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


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        dropIV.rotation = 90f

        adapter = ArrayAdapter(this, R.layout.spiner_item_join, option_age)
        ageSP.adapter = adapter

        ageSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0) {
                    age = ""
                } else if (position == 1) {
                    age = "10"
                }else if (position == 2) {
                    age = "20"
                }else if (position == 3) {
                    age = "30"
                }else if (position == 4) {
                    age = "40"
                }else if (position == 5) {
                    age = "50"
                }else if (position == 6) {
                    age = "60"
                }

            }
        }

        intent = getIntent()
        type = intent.getIntExtra("type", -1)
        step = intent.getIntExtra("step", 1)
        company_id = PrefUtils.getIntPreference(context, "company_id")

        if(step == 1) {
            per_type = 1
            stackLL.setBackgroundColor(Color.parseColor("#906e8a32"))
        } else {
            per_type = -1
            stackLL.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }


        couponListAdapter = CouponListAdapter(context, R.layout.item_member_coupon, couponData)
        couponLV.adapter = couponListAdapter

        setmenu()
        setmenu4()
        if (step == 4) {
            opTV.text = "결제"
            m_opTV.text = "￦"
        }
        company_info()
        //계산기
        cal()

        //결제내용스피너
        cate_SP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0) {
                    category_id = 1
                    Log.d("포지션", category_id.toString())
                } else if (position == 1) {
                    category_id = 2
                } else if (position == 2) {
                    category_id = 3
                } else if (position == 3) {
                    category_id = 4
                } else if (position == 4) {
                    category_id = 5
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        op_accLL.setOnClickListener {
            per_type = 3
            val intent = Intent(context, DlgEditPerActivity::class.java)
            startActivityForResult(intent, EDIT_POINT)
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

//        changeStep()

        new_maleIV.setOnClickListener {
            setmenu4()
            new_maleIV.setImageResource(R.drawable.radio_on)
            new_gender = "M"
        }
        new_femaleIV.setOnClickListener {
            setmenu4()
            new_femaleIV.setImageResource(R.drawable.radio_on)
            new_gender = "F"
        }


    }

    fun setmenu4() {
        new_femaleIV.setImageResource(R.drawable.radio_off)
        new_maleIV.setImageResource(R.drawable.radio_off)
    }

    fun setmenu3() {
        stackLL.setBackgroundColor(Color.parseColor("#ffffff"))
        stack2LL.setBackgroundColor(Color.parseColor("#ffffff"))
    }

    fun setmenu() {
        maleIV.setImageResource(R.drawable.radio_off)
        femaleIV.setImageResource(R.drawable.radio_off)
    }

    fun setmenu2() {
        cardPayIV.setImageResource(R.drawable.radio_off)
        cashPayIV.setImageResource(R.drawable.radio_off)
        depositlessIV.setImageResource(R.drawable.radio_off)
    }

    //계산클릭이벤트
    fun cal() {
        moneyTV.text = "0"
        stackLL.setOnClickListener {
            setmenu3()
            stackLL.setBackgroundColor(Color.parseColor("#906e8a32"))
            //기본퍼센트
            per = stackTV.text.toString()
            per_type = 1
            setPoint()
        }

        stack2LL.setOnClickListener {
            //임의 퍼센트
            setmenu3()
            stack2LL.setBackgroundColor(Color.parseColor("#906e8a32"))
            val managerpercent = stack2TV.text.toString()
            var money = moneyTV.text.toString()

            per = stack2TV.text.toString()

            per_type = 2
            if (managerpercent == null) {
                Toast.makeText(context, "퍼센트를 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (money == null) {
                Toast.makeText(context, "가격을 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
                money = "0"
//                return@setOnClickListener
            } else {
                val percent = managerpercent.toFloat() / 100
                val floatPoint = (money.toFloat() * percent)
                val stringPoint = floatPoint.toString()
                var splitPoint = stringPoint.split(".")
                val point = splitPoint.get(0)
                pointTV.setText(point)
            }

        }

        checkLL.setOnClickListener {
            member_join()
        }
        titleTV.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
        oneLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 1
            setPoint()
        }
        twoLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 2
            setPoint()
        }
        threeLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 3
            setPoint()
        }
        fourLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 4
            setPoint()
        }
        fiveLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 5
            setPoint()
        }
        sixLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 6
            setPoint()
        }
        sevenLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 7
            setPoint()
        }
        eightLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 8
            setPoint()
        }
        nineLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 9
            setPoint()
        }
        zeroLL.setOnClickListener {
            firstDigit()
            moneyTV.text = moneyTV.text.toString() + 0
            setPoint()
        }

        delLL.setOnClickListener {

            var text = moneyTV.text.toString()
            val defaultpercent = stackTV.text.toString()

            val text_val = text.substring(0, text.length - 1)

            if (text_val.length > 0) {

                if (text_val.equals("")){
                    moneyTV.text = text_val
                }

                moneyTV.text = text.substring(0, text.length - 1)


                val money = moneyTV.text.toString()
                if (money != null && money != "") {
                    setPoint()

                }
            } else {
                moneyTV.setText("0")
            }
        }

        useLL.setOnClickListener {



            price = Utils.getInt(moneyTV)

            if(price < 1) {
                Toast.makeText(context, "가격을 입력해주세요.", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
            }
            if (member_id<1){
                Toast.makeText(context, "회원정보가 없습니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (opTV.text.equals("적립")) {

                p_type = 1
                type = 1
                 step = 3

                if (payment_type == -1) {
                    Toast.makeText(context, "결제방식을 선택해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                if (per_type == 1) {
                    val totalpoint = Integer.parseInt(moneyTV.text.toString())
                    Log.d("포인트", totalpoint.toString())
                    stackpoint = totalpoint * Integer.parseInt(stackTV.text.toString()) / 100
                    changeStep()
                    per=stackTV.text.toString()

                    stack_point(member_id.toString())
                } else if (per_type == 2) {
                    val totalpoint = Integer.parseInt(moneyTV.text.toString())
                    Log.d("포인트", totalpoint.toString())

                    per = stack2TV.text.toString()
                    stackpoint = totalpoint * Integer.parseInt(stack2TV.text.toString()) / 100
                    changeStep()

                    stack_point(member_id.toString())
                } else if (per_type == 3) {
                    stackpoint = Integer.parseInt(pointTV.text.toString())
                    Log.d("포인트", stackpoint.toString())
                    changeStep()
                    stack_point(member_id.toString())
                } else {
                    Toast.makeText(context, "적립퍼센트를 선택해주세요", Toast.LENGTH_SHORT).show()
                }
            } else if (opTV.text.equals("결제")) {
//                val totalpoint = Integer.parseInt(moneyTV.text.toString())
//                val use_point = Integer.parseInt(stack_pointTV.text.toString())
//                stackpoint = totalpoint
//
//                if (stackpoint > use_point) {
//                    Toast.makeText(context, "포인트가 부족합니다", Toast.LENGTH_SHORT).show()
//                } else {
                step = 6
                    if (payment_type == -1) {
                        Toast.makeText(context, "결제방식을 선택해주세요", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }


                    if (use_point < 1) {

                        Toast.makeText(context, "사용자가 포인트 입력 후 진행해주세요", Toast.LENGTH_LONG).show()
//                        return@setOnClickListener
                    }

                    if(per_type > 0) {

                        stackpoint = Utils.getInt(pointTV)

                        p_type = 3
                        type = 1

                    } else {
                        stackpoint = use_point

                        p_type = 2
                        type = 2

                    }

                    step = 6
                    stack_point(member_id.toString())
                    changeStep()

            }
        }



        changeStep()

    }

    fun firstDigit() {
        if (moneyTV.text.length == 1 && moneyTV.text == "0") {
            moneyTV.text = ""
        }
    }

    fun setPoint() {

        Utils.hideKeyboard(context)

        if(per_type != 1 && per_type != 2) {
            return
        }

        var defaultpercent = ""

        if(per_type == 1) {
            defaultpercent = stackTV.text.toString()
        } else if (per_type == 2) {
            defaultpercent = stack2TV.text.toString()
        }


        var money = moneyTV.text.toString()

        if (defaultpercent == null) {
            Toast.makeText(context, "퍼센트를 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (money == null) {
            Toast.makeText(context, "가격을 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
            money = "0"
//            return
        }

        if (defaultpercent==""){
            defaultpercent="1"
        }

        val percent = defaultpercent.toFloat() / 100
        val floatPoint = (money.toFloat() * percent)
        val stringPoint = floatPoint.toString()
        var splitPoint = stringPoint.split(".")
        val point = splitPoint.get(0)
        pointTV.setText(Utils.comma(point))

    }

    // 프로세스
    fun changeStep() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("member_id", member_id)
        params.put("member_coupon_id", member_coupon_id)
        params.put("step", step)

        RequestStepAction.changeStep(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var requestStep = response.getJSONObject("RequestStep")
                        var step = Utils.getInt(requestStep, "step")
                        if (step == 3) {
                            timer!!.cancel()
                        }

//                        step = Utils.getInt(requestStep, "step")

                        timerStart()

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


    // 요청 체크
    fun checkStep() {
        val params = RequestParams()
        params.put("company_id", company_id)

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


//                        step = Utils.getInt(requestStep, "step")
                        request_step_id = Utils.getInt(requestStep, "id")
                        member_id = Utils.getInt(requestStep, "member_id")
                        member_coupon_id = Utils.getInt(requestStep, "member_coupon_id")
                        val result_step = Utils.getInt(requestStep, "step")
                        new_member_yn  = Utils.getString(requestStep, "new_member_yn")

                        if (step != result_step) {

//                            if (timer != null) {
//                                timer!!.cancel()
//                            }

                            step = result_step

                            if (step == 2) {
                                // 적립 -> 회원 정보

                                opTV.text = "적립"
                                var phone = Utils.getString(member, "phone")

                                //신규 체크
                                if (member_id == 0 || new_member_yn == "Y") {
                                    joinLL.visibility = View.VISIBLE
                                    phoneET.setText(phone)
                                    message_op_LL.visibility = View.GONE
                                    checkLL.visibility = View.VISIBLE
                                } else {
                                    message_op_LL.visibility = View.VISIBLE
                                    joinLL.visibility = View.GONE
                                    checkLL.visibility = View.GONE
                                    var left_point: String? = null
                                    var point = response.getJSONObject("Point")
                                    left_point = Utils.getString(point, "balance")
                                    stack_pointTV.text = Utils.comma(left_point)
                                }

                                var gender = Utils.getString(member, "gender")
                                var age = Utils.getString(member, "age")
                                var birth = Utils.getString(member, "birth")
                                var coupon = Utils.getString(member, "coupon")
                                var memo = Utils.getString(member, "memo")
                                var name = Utils.getString(member, "name")


                                getUserCouponList(phone)


                                titleTV.text = name
                                if (gender.equals("M")) {
                                    setmenu()
                                    maleIV.setImageResource(R.drawable.radio_on)
                                    femaleIV.setImageResource(R.drawable.radio_off)
                                } else if (gender.equals("F")) {
                                    setmenu()
                                    maleIV.setImageResource(R.drawable.radio_off)
                                    femaleIV.setImageResource(R.drawable.radio_on)
                                }

                                phoneTV.text = phone
                                if (age !=""){
                                    ageTV.text = age+"대"
                                }else{
                                    ageTV.text = "미입력"
                                }
                                birthTV.text = "생년월일: "+birth
                                couponTV.text = coupon
                                memoTV.text = "메모: "+memo

                            } else if (step == 5) {
                                var phone = Utils.getString(member, "phone")
                                var gender = Utils.getString(member, "gender")
                                var age = Utils.getString(member, "age")
                                var birth = Utils.getString(member, "birth")
                                var coupon = Utils.getString(member, "coupon")
                                var memo = Utils.getString(member, "memo")
                                var name = Utils.getString(member, "name")
                                var left_point: String? = null
                                if (new_member_yn.equals("N")) {
                                    var point = response.getJSONObject("Point")
                                    left_point = Utils.getString(point, "balance")
                                    message_op_LL.visibility = View.VISIBLE
                                    joinLL.visibility = View.GONE
                                    checkLL.visibility = View.GONE
                                }

                                titleTV.text = name
                                stack_pointTV.text = Utils.comma(left_point)
                                titleTV.text = name
                                if (gender.equals("M")) {
                                    setmenu()
                                    maleIV.setImageResource(R.drawable.radio_on)
                                } else if (gender.equals("F")) {
                                    setmenu()
                                    femaleIV.setImageResource(R.drawable.radio_on)
                                }
                                phoneTV.text = phone
                                if (age !=""){
                                    ageTV.text = age+"대"
                                }else{
                                    ageTV.text = "미입력"
                                }

                                birthTV.text = "생년월일: "+birth
                                couponTV.text = coupon
                                memoTV.text = "메모: "+memo

                                getUserCouponList(phone)

                            } else if (step == 7) {

                                use_point = Utils.getInt(requestStep, "point")

                                if (use_point==-1){
                                    use_point=0
                                }


                                usePointLL.visibility = View.VISIBLE
                                usePointTV.text = Utils.comma(use_point.toString())

                                for (i in 0 until couponData.size) {
                                    val data = couponData[i]
                                    val memberCoupon = data.getJSONObject("MemberCoupon")

                                    if(Utils.getInt(memberCoupon, "id") == member_coupon_id) {
                                        data.put("check_yn", "Y")
                                    }

                                }


                                couponListAdapter.notifyDataSetChanged()

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

        })
    }

    fun timerStart() {
        val task = object : TimerTask() {
            override fun run() {
                checkHandler.sendEmptyMessage(0)
            }
        }

        timer = Timer()
        timer!!.schedule(task, 0, 2000)
    }

    //포인트적립/사용
    fun stack_point(member_id: String) {

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("company_id", company_id)
        params.put("point", stackpoint)//사용및적립포인트
        params.put("type", type)//1적립 2사용
        params.put("per", per)//적립률
        params.put("use_point", use_point)//사용 포인트
        params.put("member_coupon_id", member_coupon_id)//사용 쿠폰
        params.put("price", price)//상품가격
        params.put("payment_type", payment_type)//결제방법
        params.put("use_type", p_type)//1적립 2사용 3 적립/사용
        params.put("category_id", category_id)//카테고리 일련번호


        MemberAction.point(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    val result = response!!.getString("result")
                    Log.d("적립", response.toString())

                    if ("ok" == result) {

                        if (new_member_yn.equals("Y")){
                            member_join()
                        }


                        val intent = Intent(context, UserListActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                        if (p_type == 1) {

                            Toast.makeText(context, stackpoint.toString() + "적립됩니다", Toast.LENGTH_SHORT).show()
                        } else if (p_type == 2) {

                            Toast.makeText(context, stackpoint.toString() + "사용됩니다", Toast.LENGTH_SHORT).show()
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
        var getPhone = Utils.getString(phoneET)
        var getAge = age


        var getBirth = Utils.getString(birthET)
        var getMemo = Utils.getString(memoET)
        var getName = Utils.getString(nameET)


        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("member_id", getid)
        params.put("age", getAge)
        params.put("name", getName)
        params.put("gender", new_gender)
        params.put("memo", getMemo)
        params.put("phone", getPhone)
        if(getBirth.length != 8) {

        }else{
            var r_birth = getBirth.substring(0, 4) + "-" + getBirth.substring(4, 6) + "-" + getBirth.substring(6, 8)
            params.put("birth", r_birth)
        }

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
        params.put("company_id", company_id)


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
                        var basic_per = Utils.getString(company, "basic_per")
                        //임의적립
                        var option_per = Utils.getInt(company, "option_per")
                        val data = response.getJSONArray("categories")
                        Log.d("카테", data.toString())
                        if (data.length()==0){
                            option_cate.add("카테고리 미설정")
                        }
                        for (i in 0..data.length() - 1) {
                            val json = data[i] as JSONObject
                            val Category = json.getJSONObject("Category")
                            val name = Utils.getString(Category, "name")
                            option_cate.add(name)

                        }
                        adapter = ArrayAdapter(context, R.layout.spiner_cal_item, option_cate)
                        cate_SP.adapter = adapter

                        if (basic_per==""){
                            basic_per="1"
                        }
                        if (option_per==-1){
                            option_per=1
                        }


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

    fun getUserCouponList(phone: String) {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("phone", phone)

        MemberAction.inquiry_point(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {


                        couponData.clear()
                        couponListAdapter.notifyDataSetChanged()

                        var data = response.getJSONArray("coupons")
                        Log.d("쿠폰데이터", data.toString())
                        for (i in 0 until data.length()) {

                            couponData.add(data[i] as JSONObject)

                        }
                        couponListAdapter.notifyDataSetChanged()


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

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONArray?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

//            override fun onStart() {
//                // show dialog
//                if (progressDialog != null) {
//
//                    progressDialog!!.show()
//                }
//            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EDIT_POINT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val point = data!!.getStringExtra("point")
                    Toast.makeText(context, point, Toast.LENGTH_SHORT).show()
                    pointTV.text = Utils.comma(point)
                }
            }
        }
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

    override fun finish() {

        if(step == 1 || step == 4) {
            endStep()
        }

        super.finish()
    }

    fun endStep() {
        val params = RequestParams()
        params.put("request_step_id", request_step_id)

        RequestStepAction.endStep(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

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

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONArray?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

//            override fun onStart() {
//                // show dialog
//                if (progressDialog != null) {
//
//                    progressDialog!!.show()
//                }
//            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

}


