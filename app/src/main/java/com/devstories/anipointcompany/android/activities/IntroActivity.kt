package com.devstories.anipointcompany.android.activities

import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.RootActivity
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.log

//인트로
class IntroActivity : RootActivity() {

    protected var _splashTime = 2000 // time to display the splash screen in ms
    private val _active = true
    private var splashThread: Thread? = null

    private var progressDialog: ProgressDialog? = null

    private var context: Context? = null

    private var is_push:Boolean = false

    val SHOW_DLG = 301

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        this.context = this
        progressDialog = ProgressDialog(context)

        // clear all notification
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancelAll()

        val buldle = intent.extras
        if (buldle != null) {
            try {
                is_push = buldle.getBoolean("FROM_PUSH")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        splashThread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    while (waited < _splashTime && _active) {
                        Thread.sleep(100)
                        waited += 100
                    }
                } catch (e: InterruptedException) {

                } finally {
                    stopIntro()
                }
            }
        }
        (splashThread as Thread).start()

    }

    private fun stopIntro() {

        val autoLogin = PrefUtils.getBooleanPreference(context, "autoLogin")
        //val autoLogin = false

        if (!autoLogin) {
            PrefUtils.clear(context)
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        } else {
            handler.sendEmptyMessage(0)
        }

    }

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            //versionInfo();
            //PrefUtils.setPreference(context, "company_id", 1)
            login()
        }
    }

    private fun login() {

        val params = RequestParams()

        params.put("login_id", PrefUtils.getStringPreference(context,"login_id"))
        params.put("passwd", PrefUtils.getStringPreference(context,"passwd"))
        //val member_type = PrefUtils.getStringPreference(context,"member_type")

        CompanyAction.company_login(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    print(response)

                    if ("ok" == result) {

                        val company = response.getJSONObject("company")
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

                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

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
                //Utils.alert(context, "조회중 장애가 발생하였습니다.")
                Utils.alert(context, "조회중 장애가 발생했습니다.")
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

                // print(errorResponse)

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // print(errorResponse)

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



}
