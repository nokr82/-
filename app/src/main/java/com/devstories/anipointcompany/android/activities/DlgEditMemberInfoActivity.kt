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
import com.devstories.anipointcompany.android.base.PrefUtils


//회원수정 다이얼로그
class DlgEditMemberInfoActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var member_id = -1
    var gender = ""
    var company_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_edit_member_info)

        this.context = this
        progressDialog = ProgressDialog(context)

        member_id = intent.getIntExtra("member_id", -1)
        company_id = PrefUtils.getIntPreference(context, "company_id")


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



        saveLL.setOnClickListener {

            val birth = Utils.getString(birthET)
            val age = Utils.getString(ageET)
            val name = Utils.getString(nameET)
            val memo = Utils.getString(memoET)
            val phone = Utils.getString(phoneET)


            println("birth.length : " + birth.length)

            if(phone == "") {
                Toast.makeText(context, "핸드폰 번호를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(birth == "") {
                Toast.makeText(context, "생년월일을 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

//            if(birth.length != 8) {
//                Toast.makeText(context, "생년월일은 여덟자리로 입력해주세요", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }

            if(age == "") {
                Toast.makeText(context, "나이를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(name == "") {
                Toast.makeText(context, "이름을 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

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

                        phoneET.setText(Utils.getString(member, "phone"))
                        nameET.setText(Utils.getString(member, "name"))
                        ageET.setText(Utils.getString(member, "age"))
                        birthET.setText(Utils.getString(member, "birth"))
                        memoET.setText(Utils.getString(member, "memo"))

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
        params.put("age", Utils.getString(ageET))
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
