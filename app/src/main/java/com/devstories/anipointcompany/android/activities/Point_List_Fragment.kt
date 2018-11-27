package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstories.aninuriandroid.adapter.UserListAdapter
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import android.app.DatePickerDialog
import android.util.Log
import android.widget.*
import com.devstories.anipointcompany.android.Actions.MemberAction
import java.util.*


class Point_List_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    internal lateinit var view: View
    lateinit var userLV: ListView
    lateinit var first_dateLL: LinearLayout
    lateinit var last_dateLL: LinearLayout
    lateinit var first_dateTV: TextView
    lateinit var last_dateTV: TextView
    lateinit var startdateTV: TextView
    lateinit var lastdateTV: TextView
    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()

    lateinit var useradapter: UserListAdapter
    var data = arrayListOf<Int>()

    var year: Int = 1
    var month: Int = 1
    var day: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.fra_point_list, container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLV = view.findViewById(R.id.userLV)
        first_dateLL = view.findViewById(R.id.first_dateLL)
        last_dateLL = view.findViewById(R.id.last_dateLL)
        first_dateTV = view.findViewById(R.id.first_dateTV)
        last_dateTV = view.findViewById(R.id.last_dateTV)
        startdateTV = view.findViewById(R.id.startdateTV)
        lastdateTV = view.findViewById(R.id.lastdateTV)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //날짜갖고오기
        var calendar = GregorianCalendar()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        useradapter = UserListAdapter(myContext,R.layout.item_user_point_list,adapterData)
        userLV.adapter = useradapter
        loadData(1, 6)

        first_dateLL.setOnClickListener {
            datedlg()
        }
        last_dateLL.setOnClickListener {
            datedlg2()
        }


    }




    //방문이력
    fun loadData(company_id: Int, type : Int) {
        val params = RequestParams()
        params.put("company_id",company_id)
        params.put("type", type)

        MemberAction.user_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    adapterData.clear()
                    val result = response!!.getString("result")


                    if ("ok" == result) {
                        val data = response.getJSONArray("member")


                        for (i in 0..(data.length() - 1)) {

                            adapterData.add(data[i] as JSONObject)

                        }

                        useradapter.notifyDataSetChanged()


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



    fun datedlg(){
        DatePickerDialog(myContext, dateSetListener, year, month, day).show()
    }
    fun datedlg2(){
        DatePickerDialog(myContext, dateSetListener2, year, month, day).show()
    }
    private val dateSetListener2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub

        val msg = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth)
        lastdateTV.text = msg
        last_dateTV.text = msg

        Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show()
    }
    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub

        val msg = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth)

        first_dateTV.text = msg
        startdateTV.text = msg

        Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }


}
