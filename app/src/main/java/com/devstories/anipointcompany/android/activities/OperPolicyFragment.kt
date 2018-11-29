package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.Actions.CompanyAction.company_info

import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class OperPolicyFragment : Fragment() {


    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var left_pointTV: TextView
    lateinit var min_pointTV: TextView
    lateinit var rdo1000wonIV: ImageView
    lateinit var rdo500wonIV: ImageView
    lateinit var rdo100wonIV: ImageView
    lateinit var visitcntTV: TextView
    lateinit var costTV: TextView
    lateinit var vsitIV: ImageView
    lateinit var costIV: ImageView
    lateinit var accountTV: TextView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_oper_policy, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        left_pointTV  = view.findViewById(R.id.left_pointTV)
        min_pointTV= view.findViewById(R.id.min_pointTV)
        rdo1000wonIV= view.findViewById(R.id.rdo1000wonIV)
        rdo500wonIV= view.findViewById(R.id.rdo500wonIV)
        rdo100wonIV= view.findViewById(R.id.rdo100wonIV)
        visitcntTV= view.findViewById(R.id.visitcntTV)
        costTV= view.findViewById(R.id.costTV)
        vsitIV= view.findViewById(R.id.vsitIV)
        costIV= view.findViewById(R.id.costIV)
        accountTV= view.findViewById(R.id.accountTV)



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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


                        val cate = response.getJSONArray("categories")
                        Log.d("카테",cate.toString())
                        for (i in 0..cate.length()-1){

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
