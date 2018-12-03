package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.Actions.MemberAction.member_join
import com.devstories.anipointcompany.android.Actions.RequestStepAction
import com.devstories.anipointcompany.android.R
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

class PointActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var type = -1
    var step = -1
    var member_id = -1
    var p_type = -1

    var stackpoint = -1


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

        if (step ==4){
            opTV.text = "사용"
            m_opTV.text = "P"
        }


        stackLL.setOnClickListener {
            val totalpoint =Integer.parseInt(moneyTV.text.toString())
            Log.d("포인트", totalpoint.toString())
            stackpoint = totalpoint*3/100
            step = 3
            changeStep()
            p_type=1
            stack_point(member_id.toString())
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
            val defaultpercent = defaultpercentTV.text.toString()
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

        managerpercentLL.setOnClickListener {

            val managerpercent = managerpercentTV.text.toString()
            val money = moneyTV.text.toString()

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

        changeStep()

    }

    fun setPoint(){

        val defaultpercent = defaultpercentTV.text.toString()
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
        params.put("company_id", 1)
        params.put("member_id", member_id)
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
                                var left_point = Utils.getString(point, "balance")

                                stack_pointTV.text = left_point
                                titleTV.text = name
                                genderTV.text = gender
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
                                var left_point = Utils.getString(point, "balance")

                                stack_pointTV.text = left_point
                                titleTV.text = name
                                genderTV.text = gender
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
        params.put("point", stackpoint)
        params.put("type", p_type)
        MemberAction.point_stack(params, object : JsonHttpResponseHandler() {

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





    fun member_join() {

        var getPhone = Utils.getString(phoneET)
        var getGender = Utils.getString(genderET)
        var getAge = Utils.getString(ageET)
        var getBirth = Utils.getString(birthET)
        var getPoint = Utils.getString(stack_pointET)
        var getCoupon = Utils.getString(couponET)
        var getMemo = Utils.getString(memoET)
        var getName = Utils.getString(nameET)

        val params = RequestParams()
        params.put("company_id", 1)
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


