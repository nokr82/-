package com.devstories.anipointcompany.android.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.aninuriandroid.adapter.VisitListAdapter
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class User_visit_List_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    var option_amount = arrayOf("5개씩 보기","10개씩 보기")

    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()
    lateinit var visitAdapter: VisitListAdapter

    lateinit var amountSP: Spinner
    lateinit var dateTV: TextView
    lateinit var itemdateLL: LinearLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
            return inflater.inflate(R.layout.fra_user_visit_analysis,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amountSP = view.findViewById(R.id.amountSP)
        dateTV = view.findViewById(R.id.dateTV)
        itemdateLL = view.findViewById(R.id.itemdateLL)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_amount)
        amountSP.adapter = adapter

        //날짜구하기
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val date = Date()
        val currentDate = formatter.format(date)


        dateTV.text = currentDate+"~"+currentDate

        loadData(1)


    }

    //방문이력 뽑기
    fun loadData(company_id: Int) {
        val params = RequestParams()
        params.put("company_id",company_id)



        MemberAction.visit_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")


                    if ("ok" == result) {

                        val visit_history = response.getString("visit_history")
                        val visit_re = response.getString("visit_re")

                        val userView = View.inflate(myContext, R.layout.item_visit, null)
                        var dateTV : TextView = userView.findViewById(R.id.dateTV)
                        var new_userTV : TextView = userView.findViewById(R.id.new_userTV)
                        var re_userTV : TextView = userView.findViewById(R.id.re_userTV)
                        var all_userTV : TextView = userView.findViewById(R.id.all_userTV)

                        all_userTV.text = visit_history
                        re_userTV.text = visit_re

                        itemdateLL.addView(userView)
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
