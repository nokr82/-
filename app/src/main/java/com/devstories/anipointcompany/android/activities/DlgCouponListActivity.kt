package com.devstories.anipointcompany.android.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CouponAction
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.adapter.CouponListAdapter
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_coupon.*
import kotlinx.android.synthetic.main.fra_auto_coupon_settings.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream



class DlgCouponListActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var title = ""
    var message = ""
    var company_id = -1
    var rand_code = ""
    var member_id = -1
    var phone = ""
    var coupon_id = -1
    var h:String? = null
    var couponData: ArrayList<JSONObject> = ArrayList<JSONObject>()
    lateinit var couponListAdapter: CouponListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_coupon)

        this.context = this
        progressDialog = ProgressDialog(context)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        company_id = PrefUtils.getIntPreference(context, "company_id")
        intent = getIntent()
        phone = intent.getStringExtra("phone")
        Log.d("쿠폰데이터", phone)
        couponListAdapter = CouponListAdapter(context, R.layout.item_member_coupon, couponData)
        couponLV.adapter = couponListAdapter
        getUserCouponList(phone)
        couponLV.setOnItemClickListener { parent, view, position, id ->
            var data = couponData.get(position)
            Log.d("리스트선택", data.toString())

            val coupon = data.getJSONObject("MemberCoupon")
            val coupon_o = data.getJSONObject("Coupon")
            coupon_id = Utils.getInt(coupon, "id")
            val id = Utils.getInt(coupon, "coupon_id")
            val coupon_name = Utils.getString(coupon_o, "name")


            member_id = Utils.getInt(coupon, "member_id")
            coupon_alram(id)

        }
    }

    fun coupon_alram(id:Int) {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("member_id", member_id)
        params.put("coupon_id", id)


        CouponAction.alram_coupon(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    Log.d("인증",response.toString())
                    val result = response!!.getString("result")
                    rand_code  = response.getString("rand")
                    if ("ok" == result) {

                        var mPopupDlg: DialogInterface? = null
                        val builder = AlertDialog.Builder(context)
                        val dialogView = layoutInflater.inflate(R.layout.dlg_send_payback, null)
                        val cancelTV = dialogView.findViewById<TextView>(R.id.cancelTV)
                        val msgWriteTV = dialogView.findViewById<TextView>(R.id.msgWriteTV)
                        val titleTV = dialogView.findViewById<TextView>(R.id.titleTV)
                        val contentTV = dialogView.findViewById<TextView>(R.id.contentTV)

                        titleTV.text = "쿠폰 사용"
                        contentTV.text = "인증번호를 확인해주세요.\n"+rand_code
                        msgWriteTV.text = "사용"


                        mPopupDlg = builder.setView(dialogView).show()
                        cancelTV.setOnClickListener {
                            mPopupDlg.dismiss()
                        }
                        msgWriteTV.setOnClickListener {
                            couponData()
                            mPopupDlg.dismiss()
                        }
                    }else{

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
    fun couponData() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("member_id", member_id)
        params.put("coupon_id", coupon_id)


        CouponAction.coupon_use(params, object : JsonHttpResponseHandler() {

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
                        Log.d("쿠폰아이디", member_id.toString())
                        finish()
                    }else{

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


    override fun onDestroy() {
        super.onDestroy()

        progressDialog = null

    }


}
