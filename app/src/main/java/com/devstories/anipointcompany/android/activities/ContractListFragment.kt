package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_contract_list.*
import kotlinx.android.synthetic.main.fra_reservation_manage.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat

//포인트내역
class ContractListFragment : Fragment() {
    lateinit var myContext: Context


    private var progressDialog: CustomProgressDialog? = null



   var company_id = -1
    var page: Int = 1
    var totalpage: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fra_contract_list, container, false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(context, "company_id")

        contract_list()

        addcontractTV.setOnClickListener {

        }



    }






    fun contract_list() {
        val params = RequestParams()
        params.put("company_id", company_id)

        CompanyAction.contract_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    Log.d("계약",response.toString())

                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        var data = response.getJSONArray("contract")

                        for (i in 0 until data.length()) {
                            var json = data[i] as JSONObject
                            var type =json.getJSONObject("ContractType")
                            var name =Utils.getString(type,"name")
                            val userView = View.inflate(myContext, R.layout.item_contract, null)
                            var itemTV: TextView = userView.findViewById(R.id.itemTV)

                            itemTV.text = name
                            contractLL.addView(userView)
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


    override fun onPause() {
        super.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }


}
