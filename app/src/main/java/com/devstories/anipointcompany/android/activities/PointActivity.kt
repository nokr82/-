package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_point.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PointActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)

        this.context = this
        progressDialog = ProgressDialog(context)


        loaduserdata()


        titleTV.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
        oneLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 1)
        }
        twoLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 2)
        }
        threeLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 3)
        }
        fourLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 4)
        }
        fiveLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 5)
        }
        sixLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 6)
        }
        sevenLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 7)
        }
        eightLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 8)
        }
        nineLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 9)
        }
        zeroLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 0)
        }
        delLL.setOnClickListener {
            val text = moneyTV.getText().toString()
            if (text.length > 0){
                moneyTV.setText(text.substring(0, text.length - 1))
            }else{
            }
        }


        }

    //고객정보뽑기
    fun loaduserdata() {
        val params = RequestParams()
        params.put("member_id", 1)

        MemberAction.my_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    Log.d("리스폰", response.toString())
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var data = response.getJSONObject("member")
                        var phone = Utils.getString(data,"phone")
                        var gender = Utils.getString(data,"gender")
                        var age  = Utils.getString(data,"age")
                        var birth = Utils.getString(data,"birth")
                        var coupon = Utils.getString(data,"coupon")
                        var memo = Utils.getString(data,"memo")
                        var name = Utils.getString(data,"name")

                        titleTV.text = name
                        genderTV.text = gender
                        phoneTV.text = phone
                        ageTV.text = age
                        birthTV.text = birth
                        couponTV.text = coupon
                        memoTV.text = memo

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

    fun member_join(member_id:String) {
        val params = RequestParams()
        params.put("company_id",1)
        params.put("age", 1)
        params.put("point", use_point)
        params.put("name", 2)
        params.put("gender", 1)
        params.put("memo", use_point)
        params.put("phone", 2)
        params.put("birth", 2)

        MemberAction.member_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    val result = response!!.getString("result")
                    Log.d("적립", response.toString())

                    if ("ok" == result) {
                        Toast.makeText(context, "회원등록", Toast.LENGTH_SHORT).show()

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


    }


    }


