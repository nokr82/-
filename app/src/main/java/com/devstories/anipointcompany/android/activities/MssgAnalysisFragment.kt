package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.devstories.aninuriandroid.adapter.AnalysisAutoMessageAdapter
import com.devstories.anipointcompany.android.Actions.CouponAction

import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// 메세지 통계
class MssgAnalysisFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var company_id = -1

    lateinit var autoCouponLL:LinearLayout
    lateinit var messageLL:LinearLayout

    lateinit var autoTV:TextView
    lateinit var autoCntTV:TextView
    lateinit var msgTV:TextView
    lateinit var msgCntTV:TextView

    lateinit var searchTypeAllTV:TextView
    lateinit var searchType7DaysTV:TextView
    lateinit var searchType30DaysTV:TextView
    lateinit var searchType3MonthTV:TextView

    lateinit var searchDateTV:TextView

    lateinit var autoV:View
    lateinit var msgV:View

    lateinit var listLV:ListView

    var search_type = 1
    var date_type = 1
    var page = 1
    var totalPage = 1

    lateinit var autoAdapter:AnalysisAutoMessageAdapter
    lateinit var adapter:AnalysisAutoMessageAdapter
    var adatperData:ArrayList<JSONObject> = ArrayList<JSONObject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fra_mssg_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autoCouponLL = view.findViewById(R.id.autoCouponLL)
        messageLL = view.findViewById(R.id.messageLL)

        autoTV = view.findViewById(R.id.autoTV)
        autoCntTV = view.findViewById(R.id.autoCntTV)
        msgTV = view.findViewById(R.id.msgTV)
        msgCntTV = view.findViewById(R.id.msgCntTV)

        searchTypeAllTV = view.findViewById(R.id.searchTypeAllTV)
        searchType7DaysTV = view.findViewById(R.id.searchType7DaysTV)
        searchType30DaysTV = view.findViewById(R.id.searchType30DaysTV)
        searchType3MonthTV = view.findViewById(R.id.searchType3MonthTV)

        searchDateTV = view.findViewById(R.id.searchDateTV)

        autoV = view.findViewById(R.id.autoV)
        msgV = view.findViewById(R.id.msgV)

        listLV = view.findViewById(R.id.listLV)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(myContext, "company_id")

        autoAdapter = AnalysisAutoMessageAdapter(myContext, R.layout.item_auto_event, adatperData, 1)
        adapter = AnalysisAutoMessageAdapter(myContext, R.layout.item_auto_event, adatperData, 2)
        listLV.adapter = autoAdapter

        searchTypeAllTV.setOnClickListener {
            date_type = 1
            setTopTabMenuView()

            loadData()
        }

        searchType7DaysTV.setOnClickListener {
            date_type = 2
            setTopTabMenuView()

            loadData()
        }

        searchType30DaysTV.setOnClickListener {
            date_type = 3
            setTopTabMenuView()

            loadData()
        }

        searchType3MonthTV.setOnClickListener {
            date_type = 4
            setTopTabMenuView()

            loadData()
        }

        autoCouponLL.setOnClickListener {
            // 자동 이벤트
            search_type = 1
            setTabMenuView()

            loadData()
        }

        messageLL.setOnClickListener {
            // 맞춤 메세지
            search_type = 2
            setTabMenuView()

            loadData()
        }

        loadData()

    }

    fun loadData() {

        val params = RequestParams()
        params.put("search_type", search_type)
        params.put("date_type", date_type)
        params.put("company_id", company_id)

        CouponAction.message_analysis(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        page = Utils.getInt(response, "page")
                        totalPage = Utils.getInt(response, "totalPage")

                        if(page == 1) {
                            adatperData.clear();
                        }

                        val autoCouponCnt = Utils.getInt(response, "autoCouponCnt")
                        val messageCnt = Utils.getInt(response, "messageCnt")
                        val searchDate = Utils.getString(response, "searchDate")

                        autoCntTV.text = "(" + autoCouponCnt + ")"
                        msgCntTV.text = "(" + messageCnt + ")"

                        searchDateTV.text = searchDate

                        val list:JSONArray = response.getJSONArray("list")

                        for(i in 0 until list.length()) {
                            adatperData.add(list[i] as JSONObject)
                        }

                        if(search_type == 1) {
                            listLV.adapter = autoAdapter
                            autoAdapter.notifyDataSetChanged();
                        } else {
                            listLV.adapter = adapter
                            adapter.notifyDataSetChanged();
                        }

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

    fun setTopTabMenuView(){

        searchTypeAllTV.setTextColor(Color.parseColor("#666666"))
        searchTypeAllTV.setBackgroundColor(Color.parseColor("#00000000"))
        searchType7DaysTV.setTextColor(Color.parseColor("#666666"))
        searchType7DaysTV.setBackgroundColor(Color.parseColor("#00000000"))
        searchType30DaysTV.setTextColor(Color.parseColor("#666666"))
        searchType30DaysTV.setBackgroundColor(Color.parseColor("#00000000"))
        searchType3MonthTV.setTextColor(Color.parseColor("#666666"))
        searchType3MonthTV.setBackgroundColor(Color.parseColor("#00000000"))

        if(date_type == 1) {
            searchTypeAllTV.setTextColor(Color.parseColor("#ffffff"))
            searchTypeAllTV.setBackgroundColor(Color.parseColor("#0068df"))
        } else if(date_type == 2) {
            searchType7DaysTV.setTextColor(Color.parseColor("#ffffff"))
            searchType7DaysTV.setBackgroundColor(Color.parseColor("#0068df"))
        } else if(date_type == 3) {
            searchType30DaysTV.setTextColor(Color.parseColor("#ffffff"))
            searchType30DaysTV.setBackgroundColor(Color.parseColor("#0068df"))
        } else {
            searchType3MonthTV.setTextColor(Color.parseColor("#ffffff"))
            searchType3MonthTV.setBackgroundColor(Color.parseColor("#0068df"))
        }

    }

    fun setTabMenuView(){
        autoTV.setTextColor(Color.parseColor("#c5c5c5"))
        msgTV.setTextColor(Color.parseColor("#c5c5c5"))

        autoCntTV.setTextColor(Color.parseColor("#e7e7e7"))
        msgCntTV.setTextColor(Color.parseColor("#e7e7e7"))

        autoV.visibility = View.INVISIBLE
        msgV.visibility = View.INVISIBLE

        if(search_type == 1) {
            autoTV.setTextColor(Color.parseColor("#606060"))
            autoCntTV.setTextColor(Color.parseColor("#1c83d0"))
            autoV.visibility = View.VISIBLE
        } else {
            msgTV.setTextColor(Color.parseColor("#606060"))
            msgCntTV.setTextColor(Color.parseColor("#1c83d0"))
            msgV.visibility = View.VISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
