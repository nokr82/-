package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlinx.android.synthetic.main.fra_reserve_new_member.*

// 메세지 통계
class ReserveNewMemberFragment : Fragment() {
    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null

    var adapterData:ArrayList<JSONObject> = ArrayList<JSONObject>()

    var company_id = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fra_reserve_new_member, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        company_id = PrefUtils.getIntPreference(context,"company_id")
        nextTV.setOnClickListener {
            member_join()
        }


    }


    //가입
    fun member_join() {

        var name = Utils.getString(nameET)
        var phone = Utils.getString(phoneET)

        if (phone.length != 11){
            Toast.makeText(context,"핸드폰번호는 필수입렵입니다.",Toast.LENGTH_SHORT).show()
            return
        }


        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("name", name)
        params.put("phone", phone)



        MemberAction.member_join(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    val result = response!!.getString("result")


                    if ("ok" == result) {
                        var member_id = response.getInt("member_id")
                        val intent = Intent(myContext,DlgReserveSave2Activity::class.java)
                        intent.putExtra("member_id",member_id)
                        startActivity(intent)
                        activity!!.finish()

                        Toast.makeText(context, "회원등록완료", Toast.LENGTH_SHORT).show()
                    }else if ("already" == result){
                        Toast.makeText(context, "이미등록된 휴대폰번호입니다.", Toast.LENGTH_SHORT).show()
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
