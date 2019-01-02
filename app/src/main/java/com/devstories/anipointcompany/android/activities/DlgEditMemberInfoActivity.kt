package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_edit_member_info.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.devstories.anipointcompany.android.base.PrefUtils


//회원수정 다이얼로그
class DlgEditMemberInfoActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    var option_age = arrayOf("미입력","20대","30대","40대","50대","60대")


    var member_id = -1
    var gender = ""
    var company_id = -1

    var age = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_edit_member_info)

        this.context = this
        progressDialog = ProgressDialog(context)

        member_id = intent.getIntExtra("member_id", -1)
        company_id = PrefUtils.getIntPreference(context, "company_id")

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        maleLL.setOnClickListener {
            setmenu()
            maleIV.setImageResource(R.drawable.radio_on)
            gender = "M"
        }
        famaleLL.setOnClickListener {
            setmenu()
            femaleIV.setImageResource(R.drawable.radio_on)
            gender = "F"
       }
        adapter = ArrayAdapter(this, R.layout.spiner_item, option_age)
        ageSP.adapter = adapter
        //스피너 선택이벤트
        ageSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0) {
                           age = ""
                } else if (position == 1) {
                    age = "20"
                }else if (position == 2) {
                    age = "30"
                }else if (position == 3) {
                    age = "40"
                }else if (position == 4) {
                    age = "50"
                }else if (position == 5) {
                    age = "60"
                }

            }
        }


        saveLL.setOnClickListener {

           /* val birth = Utils.getString(birthET)
//            var age = Utils.getString(ageET)
            var name = Utils.getString(nameET)
            val memo = Utils.getString(memoET)
            val phone = Utils.getString(phoneET)*/

//            if(birth.length != 8) {
//                Toast.makeText(context, "생년월일은 여덟자리로 입력해주세요", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }


            editInfo()
        }

        loadData()

    }


    fun setmenu(){
        maleIV.setImageResource(R.drawable.radio_off)
        femaleIV.setImageResource(R.drawable.radio_off)
    }

    fun loadData(){

        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.my_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        val member = response.getJSONObject("member")
                        val gender = Utils.getString(member, "gender")
                        Log.d("성별",gender)
                        if (gender.equals("M")){
                            setmenu()
                            maleIV.setImageResource(R.drawable.radio_on)
                        }else if (gender.equals("F")){
                            setmenu()
                            femaleIV.setImageResource(R.drawable.radio_on)
                        }
                       var phone= Utils.getString(member, "phone")
                        var birth=     Utils.getString(member, "birth")
                        var name= Utils.getString(member, "name")
                        var memo= Utils.getString(member, "memo")
                        var age= Utils.getString(member, "age")

                        if (phone ==""){
                            phone="─"
                        }
                        if (birth ==""){
                            birth="─"
                        }
                        if (name ==""){
                            name="─"
                        }

                        if (age ==""){
                          ageSP.setSelection(0)
                        }else if (age=="20"){
                            ageSP.setSelection(1)
                        }else if (age=="30"){
                            ageSP.setSelection(2)
                        }else if (age=="40"){
                            ageSP.setSelection(3)
                        }else if (age=="50"){
                            ageSP.setSelection(4)
                        }else if (age=="60"){
                            ageSP.setSelection(5)
                        }

                        phoneET.setText(phone)
                        nameET.setText(name)
//                        ageET.setText(Utils.getString(member, "age"))
                        birthET.setText(birth)
                        memoET.setText(memo)

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

                // System.out.println(responseString);

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

    fun editInfo(){

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("birth", Utils.getString(birthET))
        params.put("age",age)
        params.put("gender",gender)
        params.put("name", Utils.getString(nameET))
        params.put("memo", Utils.getString(memoET))
        params.put("phone", Utils.getString(phoneET))

        MemberAction.edit_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        val resultIntent = Intent()
                        resultIntent.putExtra("member_id",member_id)
                        setResult(RESULT_OK, resultIntent)
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

                // System.out.println(responseString);

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

    override fun onDestroy() {
        super.onDestroy()

        progressDialog = null

    }


}
