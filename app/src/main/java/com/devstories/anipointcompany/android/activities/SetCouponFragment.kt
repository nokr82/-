package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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

//메시지쿠폰관리 - 쿠폰작성
class SetCouponFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var expirationLL: LinearLayout
    lateinit var expirationIV: ImageView
    lateinit var coupon_opSP: Spinner
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

    lateinit var adapter: ArrayAdapter<String>
    var op_coupon = arrayOf("첫가입","생일","30일","60일","90일","만든쿠폰")
    var op_expiration = arrayOf("30일","60일","90일")

    var week_use_yn = "N"
    var sat_use_yn = "N"
    var sun_use_yn = "N"
    //쿠폰종류 스피너
    var type = -1
    var use_day = -1



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_set_coupon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expirationLL= view.findViewById(R.id.expirationLL)

        coupon_opSP= view.findViewById(R.id.coupon_opSP)
        coupon_exSP= view.findViewById(R.id.coupon_exSP)
        coupon_prdET= view.findViewById(R.id.coupon_prdET)

        weekdayLL= view.findViewById(R.id.weekdayLL)
        saturdayLL= view.findViewById(R.id.saturdayLL)
        sundayLL= view.findViewById(R.id.sundayLL)
        validityIV= view.findViewById(R.id.validityIV)
        helpTV= view.findViewById(R.id.helpTV)
        expirationIV= view.findViewById(R.id.expirationIV)
        weekdayIV= view.findViewById(R.id.weekdayIV)
        saturdayIV= view.findViewById(R.id.saturdayIV)
        sundayIV= view.findViewById(R.id.sundayIV)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setmenu2()
        setmenu()

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,op_coupon)
        coupon_opSP.adapter = adapter
        //스피너 선택이벤트
        coupon_opSP.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position==0){
                    type =1
                }else if (position==1){
                    type =2
                }else if (position==2){
                    type =3
                }else if (position==3){
                    type =4
                }else if (position==4){
                    type =5
                }else if (position==5){
                    type =6
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,op_expiration)
        coupon_exSP.adapter = adapter
        //스피너 선택이벤트
        coupon_exSP.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position==0){
                    use_day=7
                }else if (position==1){
                    use_day=30
                }else if (position==2){
                    use_day = 60
                }else if (position==2){
                    use_day = 90
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }




        expirationLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                expirationIV.setImageResource(R.mipmap.box_check_on)
            } else {
                expirationIV.setImageResource(R.mipmap.box_check_off)
            }
        }

        weekdayLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
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
            if(it.isSelected) {
                sun_use_yn = "Y"
                sundayIV.setImageResource(R.mipmap.box_check_on)
            } else {
                sun_use_yn = "N"
                sundayIV.setImageResource(R.mipmap.box_check_off)
            }
        }
        validityIV.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                validityIV.setImageResource(R.mipmap.switch_off)
            } else {
                validityIV.setImageResource(R.mipmap.switch_on)
            }
        }
        nextTV.setOnClickListener {
            coupon_add()
        }
    }

    //쿠폰만들기
    fun coupon_add() {
        val params = RequestParams()
        params.put("company_id",1)
        params.put("type",type)
        params.put("name",Utils.getString(coupon_prdET))
        params.put("week_use_yn",week_use_yn)
        params.put("sat_use_yn",sat_use_yn)
        params.put("sun_use_yn",sun_use_yn)
        params.put("use_day",use_day)
        if (week_use_yn.equals("N")||week_use_yn.equals("N")||week_use_yn.equals("N")){
            Toast.makeText(myContext,"사용가능요일을 선택해주세요",Toast.LENGTH_SHORT).show()
            return
        }
        if (coupon_prdET.equals("")){
            Toast.makeText(myContext,"쿠폰이름을 입력해주세요",Toast.LENGTH_SHORT).show()
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
                        var intent = Intent()
                        intent.action = "STEP2_NEXT"
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




    fun setmenu(){
    expirationIV.setImageResource(R.mipmap.box_check_off)
    }

    fun setmenu2(){
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
