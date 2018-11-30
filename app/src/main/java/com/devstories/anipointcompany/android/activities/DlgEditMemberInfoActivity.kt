package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.RootActivity
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DlgEditMemberInfoActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.context = this
        progressDialog = ProgressDialog(context)

        loginLL.setOnClickListener {
            var getName = Utils.getString(loginIdET)
            var getPW = Utils.getString(loginPassTV)


            if (getName == "" || getName == null || getName.isEmpty()) {
                Toast.makeText(context, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (getPW == "" || getPW == null || getPW.isEmpty()) {
                Toast.makeText(context, "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                loginPassTV.requestFocus()
                return@setOnClickListener
            }

            login(getName, getPW)
        }

    }

    fun login(email:String, passwd:String){

        val params = RequestParams()
        params.put("login_id", email)
        params.put("passwd", passwd)

        CompanyAction.company_login(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    println("LoginActivity result ::: $result")

                    if ("ok" == result) {
                        val company = response.getJSONObject("company")
                        println(company)
                        //val images = response.getJSONArray("images")//[]

                        val company_id = Utils.getInt(company, "id")

                        PrefUtils.setPreference(context, "company_id", company_id)
                        PrefUtils.setPreference(context, "login_id", Utils.getString(company, "login_id"))
                        PrefUtils.setPreference(context, "passwd", Utils.getString(company, "passwd"))
                        PrefUtils.setPreference(context, "company_name", Utils.getString(company, "company_name"))
                        /*PrefUtils.setPreference(context, "phone1", Utils.getString(company, "phone1"))
                        PrefUtils.setPreference(context, "phone2", Utils.getInt(company, "phone2"))
                        PrefUtils.setPreference(context, "phone3", Utils.getInt(company, "phone3"))
                        PrefUtils.setPreference(context, "s_contract_term", Utils.getInt(company, "s_contract_term"))
                        PrefUtils.setPreference(context, "e_contract_term", Utils.getInt(company, "e_contract_term"))
                        PrefUtils.setPreference(context, "created", Utils.getInt(company, "created"))
                        PrefUtils.setPreference(context, "updated", Utils.getInt(company, "updated"))
                        PrefUtils.setPreference(context, "basic_per", Utils.getInt(company, "basic_per"))
                        PrefUtils.setPreference(context, "option_per", Utils.getInt(company, "option_per"))
                        PrefUtils.setPreference(context, "del_yn", Utils.getInt(company, "del_yn"))*/

                        PrefUtils.setPreference(context, "autoLogin", true)

                        val intent = Intent(context, UserListActivity::class.java)
                        //intent.putExtra("is_push", is_push)
                        intent.putExtra("company_id", company_id)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        Toast.makeText(context, "일치하는 회원이 존재하지 않습니다.", Toast.LENGTH_LONG).show()
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
