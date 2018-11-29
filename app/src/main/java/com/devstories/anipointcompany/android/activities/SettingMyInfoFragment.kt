package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class SettingMyInfoFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var compNameET: EditText
    lateinit var phoneNum1ET: EditText
    lateinit var phoneNum2ET: EditText
    lateinit var phoneNum3ET: EditText
    lateinit var termET: EditText
    lateinit var compIdET: EditText
    lateinit var addImage1RL: RelativeLayout
    lateinit var addImage2RL: RelativeLayout
    lateinit var addImage3RL: RelativeLayout
    lateinit var tempPasswordET: EditText
    lateinit var newPasswordET: EditText
    lateinit var newPassCheckET: EditText
    lateinit var checkTV: TextView
    lateinit var infocheckTV: TextView
    lateinit var imgcheckTV: TextView




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.fragment_setting_my_info, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compNameET = view.findViewById(R.id.compNameET)
        phoneNum1ET = view.findViewById(R.id.phoneNum1ET)
        phoneNum2ET = view.findViewById(R.id.phoneNum2ET)
        phoneNum3ET = view.findViewById(R.id.phoneNum3ET)
        compIdET = view.findViewById(R.id.compIdET)
        addImage1RL = view.findViewById(R.id.addImage1RL)
        addImage2RL = view.findViewById(R.id.addImage2RL)
        termET = view.findViewById(R.id.termET)
        addImage1RL = view.findViewById(R.id.addImage1RL)
        addImage2RL = view.findViewById(R.id.addImage2RL)
        addImage3RL = view.findViewById(R.id.addImage3RL)
        tempPasswordET = view.findViewById(R.id.tempPasswordET)
        newPasswordET = view.findViewById(R.id.newPasswordET)
        newPassCheckET = view.findViewById(R.id.newPassCheckET)
        checkTV = view.findViewById(R.id.checkTV)
        infocheckTV= view.findViewById(R.id.infocheckTV)
        imgcheckTV= view.findViewById(R.id.imgcheckTV)



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_info(1)
    }

    //사업체 정보뽑기
    fun company_info(company_id: Int) {
        val params = RequestParams()
        params.put("company_id",company_id)


        CompanyAction.company_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        val company = response.getJSONObject("company")
                        val company_name = Utils.getString(company,"company_name")
                        val phone1 = Utils.getString(company,"phone1")
                        val phone2 = Utils.getString(company,"phone2")
                        val phone3 = Utils.getString(company,"phone3")


                        compNameET.setText(company_name)
                        phoneNum1ET.setText(phone1)
                        phoneNum2ET.setText(phone2)
                        phoneNum3ET.setText(phone3)



                        val points = response.getJSONArray("categories")
                        Log.d("데이트",points.toString())
                        for (i in 0..points.length()-1){

                        }

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


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }



}
