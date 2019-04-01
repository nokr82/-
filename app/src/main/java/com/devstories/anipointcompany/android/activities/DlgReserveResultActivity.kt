package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import android.view.View
import android.widget.ArrayAdapter
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_reserve_result.*
import org.json.JSONException
import org.json.JSONObject
import android.widget.Toast
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.AdapterView
import android.widget.TextView
import com.devstories.anipointcompany.android.base.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*


//회원수정 다이얼로그
class DlgReserveResultActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: CustomProgressDialog? = null

    var member_id = -1
    var company_id = -1
    var reserve_id = -1
    var customer_id = -1
    var stack_type = -1
    var payment_type = -1
    var result_type = -1
    var reserve_time = ""
    var use_point = -1
    lateinit var ma_adapter: ArrayAdapter<String>
    var managers: ArrayList<String> = ArrayList()
    var managers_ids: ArrayList<Int> = ArrayList()
    val cal = Calendar.getInstance()
    var reserve_date = ""
    var pay = ""
    var modi_type = -1

    var price = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigations(this)
        setContentView(R.layout.dlg_reserve_result)

        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)


        reserve_id = intent.getIntExtra("reserve_id", -1)
        member_id = intent.getIntExtra("member_id", -1)

        managers.add("담당자를 선택해주세요.")
        managers_ids.add(-1)
        member_id = intent.getIntExtra("member_id", -1)

        company_id = PrefUtils.getIntPreference(context, "company_id")

        setmenu()
        setmenu2()
        setmenu3()
        ma_adapter = ArrayAdapter(this, R.layout.spiner_item, managers)


        manageSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                customer_id = managers_ids[position]
                Log.d("아뒤", customer_id.toString())

            }
        }


        dateLL.setOnClickListener {
            datedlg()
        }

        cardLL.setOnClickListener {
            setmenu()
            cardIV.setImageResource(R.drawable.radio_on)
            payment_type = 1
        }
        payLL.setOnClickListener {
            setmenu()
            payIV.setImageResource(R.drawable.radio_on)
            payment_type = 2
        }

        maleLL.setOnClickListener {
            setmenu2()
            maleIV.setImageResource(R.drawable.radio_on)
            stack_type = 1
        }

        famaleLL.setOnClickListener {
            setmenu2()
            femaleIV.setImageResource(R.drawable.radio_on)
            stack_type = 2
        }
        dateLL.setOnClickListener {
            val dialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                val msg = String.format("%d : %d ", hour, min)


                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                dateTV.text = msg
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
            dialog.show()
        }

        sugerLL.setOnClickListener {
            modi_type = 1
            val dialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                val msg = String.format("%d : %d ", hour, min)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                sugerTV.text = msg
            }, 0, 0, true)
            dialog.show()
        }


        backIV.setOnClickListener {
            finish()
        }

        delTV.setOnClickListener {
            reserve_del()
        }

        reserveLL.setOnClickListener {
            setmenu3()
            result_type = 1
            reserveIV.setImageResource(R.drawable.radio_on)
        }
        reserve_comLL.setOnClickListener {
            setmenu3()
            result_type = 2
            reserve_comIV.setImageResource(R.drawable.radio_on)
        }
        noshowLL.setOnClickListener {
            setmenu3()
            result_type = 3
            noshowIV.setImageResource(R.drawable.radio_on)
        }

        modiTV.setOnClickListener {
            reserve()
        }

        saleTV.setOnClickListener {
            if (result_type!=2){
                Toast.makeText(context,"예약완료처리를 클릭해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            reserve()
            val intent = Intent(context,CalActivity::class.java)
            //하
            intent.putExtra("reserve_type",1)
            intent.putExtra("member_id",member_id)
            intent.putExtra("pay",pay)
            intent.putExtra("price",price)
            intent.putExtra("use_point",use_point)
            intent.putExtra("per_type",stack_type)
            intent.putExtra("payment_type",payment_type)
            startActivity(intent)
        }


        reserve_info()
        company_info()


    }


    fun setmenu() {
        payIV.setImageResource(R.drawable.radio_off)
        cardIV.setImageResource(R.drawable.radio_off)
    }

    fun setmenu2() {
        maleIV.setImageResource(R.drawable.radio_off)
        femaleIV.setImageResource(R.drawable.radio_off)
    }

    fun setmenu3() {
        reserveIV.setImageResource(R.drawable.radio_off)
        reserve_comIV.setImageResource(R.drawable.radio_off)
        noshowIV.setImageResource(R.drawable.radio_off)

    }

    fun datedlg() {
        var day = Utils.todayStr()
        var days = day.split("-")
        DatePickerDialog(context, dateSetListener, days[0].toInt(), days[1].toInt(), days[2].toInt()).show()
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val msg = String.format("%d.%d.%d", year, monthOfYear, dayOfMonth)
        dateTV.text = msg
    }

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
                        var basic_per = Utils.getInt(company, "basic_per")
                        var option_per = Utils.getInt(company, "option_per")
                        if (basic_per == -1) {
                            basic_per = 0
                        }
                        if (option_per == -1) {
                            option_per = 0
                        }
                        op_perTV.text = basic_per.toString() + "%"
                        basicTV.text = option_per.toString() + "%"

                        var name = ""
                        val customers = response.getJSONArray("customer")

                        var position = 0

                        for (i in 0..customers.length() - 1) {
                            //새로운뷰를 이미지의 길이만큼생성
                            var json = customers[i] as JSONObject
                            val CompanyCustomer = json.getJSONObject("CompanyCustomer")
                            name = Utils.getString(CompanyCustomer, "name")
                            val managers_id = Utils.getInt(CompanyCustomer, "id")

                            managers.add(name)
                            managers_ids.add(managers_id)
                            if ( customer_id == managers_id){
                                position =  i+1
                            }

                        }
                        manageSP.adapter = ma_adapter
                        manageSP.setSelection(position)

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

    fun reserve_info() {
        val params = RequestParams()
        params.put("reserve_id", reserve_id)


        CompanyAction.reserve_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    Log.d("리저브", response.toString())
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        val reserve_s = response.getJSONObject("reserve")
                        val reserve = reserve_s.getJSONObject("Reserve")
                        stack_type = Utils.getInt(reserve, "stack_type")
                        reserve_time = Utils.getString(reserve, "reserve_time")
                        reserve_date = Utils.getString(reserve, "reserve_date")
                        payment_type = Utils.getInt(reserve,"payment_type")
                        customer_id = Utils.getInt(reserve,"customer_id")
                        var surgery_time = Utils.getString(reserve, "surgery_time")
                        var surgery_name = Utils.getString(reserve, "surgery_name")
                        price = Utils.getInt(reserve, "price")
                        pay = Utils.getString(reserve, "pay")
                        use_point = Utils.getInt(reserve, "use_point")
                        if (price == -1){
                            price = 0
                        }
                        if (use_point == -1){
                            use_point = 0
                        }

                        priceET.setText(price.toString())
                        r_priceET.setText(pay)
                        pointET.setText(use_point.toString())

                        result_type = Utils.getInt(reserve, "result_type")

                        if (payment_type ==1){
                            cardIV.setImageResource(R.drawable.radio_on)
                        }else if(payment_type ==2){
                            payIV.setImageResource(R.drawable.radio_on)
                        }
                        if (stack_type ==1){
                            setmenu2()
                            maleIV.setImageResource(R.drawable.radio_on)


                        }else if (stack_type ==2) {
                            setmenu2()
                            femaleIV.setImageResource(R.drawable.radio_on)
                        }

                        if (result_type ==1){
                            reserveIV.setImageResource(R.drawable.radio_on)
                        }else if(result_type ==2){
                            reserve_comIV.setImageResource(R.drawable.radio_on)
                        }else if(result_type ==3){
                            noshowIV.setImageResource(R.drawable.radio_on)
                        }



                        dateTV.text = reserve_date.replace(".", "-") + " " + reserve_time
                        sugerTV.text = surgery_time
                        titleET.setText(surgery_name)

                        var customer = reserve_s.getJSONObject("CompanyCustomer")
                        var customer_name = Utils.getString(customer, "name")


                        var member = reserve_s.getJSONObject("Member")
                        var noshow_cnt = Utils.getString(member, "noshow_cnt")
                        var phone = Utils.getString(member, "phone")

                        phoneTV.text = phone
                        noshowTV.text = noshow_cnt

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

    fun reserve_del() {
        val params = RequestParams()
        params.put("reserve_id", reserve_id)


        CompanyAction.reserve_del(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        finish()
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


    fun reserve() {
        if (Utils.getString(titleET) == "") {
            Toast.makeText(context, "시술내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (Utils.getString(priceET) == "") {
            Toast.makeText(context, "시술가격을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (Utils.getString(r_priceET) == "") {
            Toast.makeText(context, "결제예정금액을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (customer_id == -1) {
            Toast.makeText(context, "담당자를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }




        var time = reserve_time
        var times = time.split(":")

        var suger = Utils.getString(sugerTV)
        var sugers = suger.split(":")

        var hours = sugers[0].trim().toInt() + times[0].trim().toInt()

        var min = sugers[1].trim().toInt() + times[1].trim().toInt()
        var m_min = 0
        if (modi_type==1){
            if (min > 59) {
                m_min = min - 60
                min = m_min
                hours = hours + 1
            }
        }

        if (hours >24){
            Toast.makeText(context,"시술시간이 24시간을 지날수 없습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("company_id", company_id)
        params.put("customer_id", customer_id)
        params.put("id", reserve_id)
        params.put("surgery_name", Utils.getString(titleET))
        params.put("reserve_time", reserve_time)
        params.put("surgery_time", hours.toString() + " : " + min.toString())
        params.put("price", Utils.getString(priceET))
        params.put("pay", Utils.getString(r_priceET))
        params.put("use_point", Utils.getString(pointET))
        params.put("result_type", result_type)
        params.put("payment_type", payment_type)
        params.put("stack_type", stack_type)
        params.put("memo", Utils.getString(memoET))
        params.put("reserve_date", reserve_date)


        CompanyAction.reserve(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        Utils.hideKeyboard(context)
                        finish()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
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


    fun hideNavigations(context: Activity) {
        val decorView = context.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }


    override fun onDestroy() {
        super.onDestroy()

        progressDialog = null

    }


}
