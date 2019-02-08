package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.Actions.CouponAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_set_coupon.*
import org.json.JSONException
import org.json.JSONObject
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.devstories.anipointcompany.android.base.PrefUtils
import java.io.Serializable


//메시지쿠폰관리 - 쿠폰작성
class SetCouponFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var expirationLL: LinearLayout
    lateinit var expirationIV: ImageView
    lateinit var coupon_exSP: Spinner
    lateinit var coupon_prdET: TextView
    lateinit var weekdayLL: LinearLayout
    lateinit var saturdayLL: LinearLayout
    lateinit var sundayLL: LinearLayout
    lateinit var weekdayIV: ImageView
    lateinit var saturdayIV: ImageView
    lateinit var sundayIV: ImageView
    lateinit var validityIV: ImageView
    lateinit var helpTV: TextView
    lateinit var coupon_prdTV: TextView
    lateinit var skipTV: TextView
    lateinit var exTV: TextView
    lateinit var countTV: TextView
    lateinit var coupon_conET: EditText

    lateinit var adapter: ArrayAdapter<String>
    var op_expiration = arrayOf("7일", "30일", "60일", "90일")

    var validity_alarm_yn = "Y"
    var week_use_yn = "N"
    var sat_use_yn = "N"
    var sun_use_yn = "N"

    //쿠폰종류 스피너
    var type = -1
    var use_day = -1
    var count = ""
    //브로드캐스트로 고객정보전달

    var company_id = -1
    var coupon_id: String? = null
    var stack_visit = -1
    var mising_day = -1
    var use_money = -1
    var left_point = -1
    var from: String? = null
    var to: String? = null
    var missing_from : String? = null
    var missing_to : String? = null
    var use_from : String? = null
    var use_to: String? = null
    var left_from: String? = null
    var left_to: String? = null
    var gender: Serializable? = null
    var age: Serializable? = null
    var member_id = -1

    internal var step1NextReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                //브로드캐스트로 프래그먼트이동리시버
                count = intent.getStringExtra("count")

            }
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_set_coupon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expirationLL = view.findViewById(R.id.expirationLL)
        countTV = view.findViewById(R.id.countTV)
        coupon_exSP = view.findViewById(R.id.coupon_exSP)
        coupon_prdET = view.findViewById(R.id.coupon_prdET)
        skipTV = view.findViewById(R.id.skipTV)
        weekdayLL = view.findViewById(R.id.weekdayLL)
        saturdayLL = view.findViewById(R.id.saturdayLL)
        sundayLL = view.findViewById(R.id.sundayLL)
        validityIV = view.findViewById(R.id.validityIV)
        helpTV = view.findViewById(R.id.helpTV)
        expirationIV = view.findViewById(R.id.expirationIV)
        weekdayIV = view.findViewById(R.id.weekdayIV)
        saturdayIV = view.findViewById(R.id.saturdayIV)
        sundayIV = view.findViewById(R.id.sundayIV)
        coupon_prdTV = view.findViewById(R.id.coupon_prdTV)
        exTV = view.findViewById(R.id.exTV)

        coupon_conET = view.findViewById(R.id.coupon_conET)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //고객선택
        var filter1 = IntentFilter("STEP1_NEXT")
        myContext.registerReceiver(step1NextReceiver, filter1)

        company_id = PrefUtils.getIntPreference(context, "company_id")
        setmenu2()
        setmenu()

//        args.putString("count", count)
//        args.putInt("search_type", search_type)
//        args.putSerializable("gender", gender as ArrayList<String>?)
//        Log.d("젠더",gender.toString())
//        args.putSerializable("age", age as ArrayList<String>?)
//        Log.d("젠더",age.toString())
//        args.putString("visited_date", visited_date)
//        args.putString("from", from)
//        args.putString("to", to)


        if (getArguments() != null) {
            member_id = getArguments()!!.getInt("member_id", -1)
            Log.d("멤버디", member_id.toString())
            if (member_id != -1) {
                    count = "1"
            }else{
                sendcouponTV.visibility = View.GONE
                count = getArguments()!!.getString("count")
                stack_visit  = getArguments()!!.getInt("stack_visit")
                mising_day  = getArguments()!!.getInt("mising_day")
                use_money  = getArguments()!!.getInt("use_money")
                left_point  = getArguments()!!.getInt("left_point")
                gender = getArguments()!!.getSerializable("gender")
                age = getArguments()!!.getSerializable("age")
                from = getArguments()!!.getString("from")
                to = getArguments()!!.getString("to")
                missing_from = getArguments()!!.getString("missing_from")
                missing_to = getArguments()!!.getString("missing_to")
                use_from = getArguments()!!.getString("use_from")
                use_to = getArguments()!!.getString("use_to")
                left_from = getArguments()!!.getString("left_from")
                left_to = getArguments()!!.getString("left_to")

            }

        }



        countTV.text = "목록에서 선택한 " + count + " 명"

        adapter = ArrayAdapter(myContext, R.layout.spiner_item, op_expiration)
        coupon_exSP.adapter = adapter
        //스피너 선택이벤트
        coupon_exSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0) {
                    use_day = 7
                    exTV.setText("쿠폰 받은 날부터 7일간 유효")
                } else if (position == 1) {
                    use_day = 30
                    exTV.setText("쿠폰 받은 날부터 30일간 유효")
                } else if (position == 2) {
                    use_day = 60
                    exTV.setText("쿠폰 받은 날부터 60일간 유효")
                } else if (position == 3) {
                    use_day = 90
                    exTV.setText("쿠폰 받은 날부터 90일간 유효")
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }




        expirationLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                expirationIV.setImageResource(R.mipmap.box_check_on)
            } else {
                expirationIV.setImageResource(R.mipmap.box_check_off)
            }
        }

        weekdayLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                week_use_yn = "Y"
                weekdayIV.setImageResource(R.mipmap.box_check_on)
            } else {
                week_use_yn = "N"
                weekdayIV.setImageResource(R.mipmap.box_check_off)
            }
        }
        saturdayLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                sat_use_yn = "Y"
                saturdayIV.setImageResource(R.mipmap.box_check_on)
            } else {
                sat_use_yn = "N"
                saturdayIV.setImageResource(R.mipmap.box_check_off)
            }
        }
        sundayLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                sun_use_yn = "Y"
                sundayIV.setImageResource(R.mipmap.box_check_on)
            } else {
                sun_use_yn = "N"
                sundayIV.setImageResource(R.mipmap.box_check_off)
            }
        }
        validityIV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                validityIV.setImageResource(R.mipmap.off)
                validity_alarm_yn = "N"
            } else {
                validityIV.setImageResource(R.mipmap.on)
                validity_alarm_yn = "Y"
            }
        }
        etchange()

        skipTV.setOnClickListener {
            var intent = Intent()
            intent.action = "SKIP_NEXT"
            if (member_id != -1){
                intent.putExtra("member_id", member_id)
            }else{
                intent.putExtra("count", count)
                intent.putExtra("gender", gender)
                intent.putExtra("age", age)
                intent.putExtra("from", from)
                intent.putExtra("to", to)
                intent.putExtra("missing_from",missing_from)
                intent.putExtra("missing_to", missing_to)
                intent.putExtra("use_from",use_from)
                intent.putExtra("use_to", use_to)
                intent.putExtra("left_from", left_from)
                intent.putExtra("left_to", left_to)

                intent.putExtra("stack_visit", stack_visit)
                intent.putExtra("mising_day", mising_day)
                intent.putExtra("use_money", use_money)
                intent.putExtra("left_point", left_point)


            }
            myContext.sendBroadcast(intent)
        }

        sendcouponTV.setOnClickListener {
            coupon_add2()
        }

        nextTV.setOnClickListener {
            coupon_add()
        }
    }
    override fun onPause() {
        super.onPause()
        setmenu2()
        setmenu()
        count = "0"
        coupon_prdET.setText("")
        coupon_conET.setText("")
    }
    fun etchange() {
        //에딧텍스트 입력댈떄마다변화
        coupon_prdET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                // 입력되는 텍스트에 변화가 있을 때

            }

            override fun afterTextChanged(arg0: Editable) {
                coupon_prdTV.text = Utils.getString(coupon_prdET)
                // 입력이 끝났을 때

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // 입력하기 전에

            }

        })
    }

    //쿠폰만들기
    fun self_coupon() {
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("coupon_id", coupon_id)


        CouponAction.self_coupon(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    Log.d("결과",response.toString())
                    if ("ok" == result) {

                        Toast.makeText(myContext,"쿠폰이 정상발행되었습니다",Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(myContext, "업데이트실패", Toast.LENGTH_SHORT).show()
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

    //쿠폰만들기
    fun coupon_add2() {

        var content = Utils.getString(coupon_conET)

        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("type", 6)
        params.put("name", Utils.getString(coupon_prdET))
        params.put("week_use_yn", week_use_yn)
        params.put("sat_use_yn", sat_use_yn)
        params.put("sun_use_yn", sun_use_yn)
        params.put("content", content)
        params.put("use_day", use_day)
        params.put("validity_alarm_yn", validity_alarm_yn)
        if (week_use_yn.equals("N") && sat_use_yn.equals("N") && sun_use_yn.equals("N")) {
            Toast.makeText(myContext, "사용가능요일을 선택해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (coupon_prdET.equals("")) {
            Toast.makeText(myContext, "쿠폰이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }




        CouponAction.coupon_add(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        coupon_id = response.getString("coupon_id")
                        self_coupon()

                    } else {
                        Toast.makeText(myContext, "업데이트실패", Toast.LENGTH_SHORT).show()
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

    //쿠폰만들기
    fun coupon_add() {

        var content = Utils.getString(coupon_conET)

        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("type", 6)
        params.put("name", Utils.getString(coupon_prdET))
        params.put("week_use_yn", week_use_yn)
        params.put("sat_use_yn", sat_use_yn)
        params.put("sun_use_yn", sun_use_yn)
        params.put("content", content)
        params.put("use_day", use_day)
        params.put("validity_alarm_yn", validity_alarm_yn)
        if (week_use_yn.equals("N") && sat_use_yn.equals("N") && sun_use_yn.equals("N")) {
            Toast.makeText(myContext, "사용가능요일을 선택해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (coupon_prdET.equals("")) {
            Toast.makeText(myContext, "쿠폰이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }




        CouponAction.coupon_add(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var coupon_id = response.getString("coupon_id")

                        var intent = Intent()
                        intent.action = "STEP2_NEXT"
                        if (member_id!= -1){
                            intent.putExtra("member_id", member_id)
                            intent.putExtra("coupon_id", coupon_id)
                        }else{
                            intent.putExtra("coupon_id", coupon_id)
                       /*     intent.putExtra("count", count)
                            intent.putExtra("search_type", search_type)
                            intent.putExtra("gender", gender)
                            intent.putExtra("age", age)
                            intent.putExtra("visited_date", visited_date)
                            intent.putExtra("from", from)
                            intent.putExtra("to", to)*/
                            intent.putExtra("count", count)
                            intent.putExtra("gender", gender)
                            intent.putExtra("age", age)
                            intent.putExtra("from", from)
                            intent.putExtra("to", to)
                            intent.putExtra("missing_from",missing_from)
                            intent.putExtra("missing_to", missing_to)
                            intent.putExtra("use_from",use_from)
                            intent.putExtra("use_to", use_to)
                            intent.putExtra("left_from", left_from)
                            intent.putExtra("left_to", left_to)

                            intent.putExtra("stack_visit", stack_visit)
                            intent.putExtra("mising_day", mising_day)
                            intent.putExtra("use_money", use_money)
                            intent.putExtra("left_point", left_point)
                        }
                        myContext.sendBroadcast(intent)
                    } else {
                        Toast.makeText(myContext, "업데이트실패", Toast.LENGTH_SHORT).show()
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


    fun setmenu() {
        expirationIV.setImageResource(R.mipmap.box_check_off)
    }

    fun setmenu2() {
        weekdayIV.setImageResource(R.mipmap.box_check_off)
        saturdayIV.setImageResource(R.mipmap.box_check_off)
        sundayIV.setImageResource(R.mipmap.box_check_off)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
