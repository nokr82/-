package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import com.devstories.aninuriandroid.adapter.ContractListAdapter
import com.devstories.aninuriandroid.adapter.UserListAdapter
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
    lateinit var contractListAdapter: ContractListAdapter

    var contract_id = -1
   var company_id = -1
    var page: Int = 1
    var totalpage: Int = 1
    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()
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

        contractListAdapter = ContractListAdapter(myContext, R.layout.item_contract_list, adapterData)
        contractLV.adapter = contractListAdapter

        var lastitemVisibleFlag = false
        contractLV.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                lastitemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    if (totalpage > page) {
                        page++
                        contract_view_list()
                    }

                }
            }

        })

        contractLV.setOnItemClickListener { parent, view, position, id ->
            var data = adapterData.get(position)
            Log.d("리스트선택", data.toString())
            val contract = data.getJSONObject("Contract")
            val cont_id = Utils.getInt(contract, "id")
            val intent  = Intent(context,ContractViewActivity::class.java)
            intent.putExtra("cont_id",cont_id)
            startActivity(intent)


        }

        btn_search.setOnClickListener {
            page =1
            contract_view_list()
        }


        addcontractTV.setOnClickListener {
            val intent = Intent(myContext,ContractWriteActivity::class.java)
            startActivity(intent)
        }

        contract_view_list()


    }






    fun contract_view_list() {

        var keyword = Utils.getString(keywordET)

        val params = RequestParams()
        params.put("searchKeyword", keyword)
        params.put("company_id", company_id)
        params.put("contract_id", contract_id)
        params.put("page", page)


        CompanyAction.contract_view_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    Log.d("계약리스트",response.toString())

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        totalpage = response!!.getInt("totalPage")
                        val contract = response!!.getJSONArray("contract")

                        keywordET.setText("")

                        if (page==1){
                            adapterData.clear()
                            contractListAdapter.notifyDataSetChanged()
                        }

                        for (i in 0 until contract.length()) {

                            Log.d("데이터",contract[i].toString())
                            adapterData.add(contract[i] as JSONObject)

                        }

                        contractListAdapter.notifyDataSetChanged()

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
                        val userView = View.inflate(myContext, R.layout.item_contract, null)
                        var itemTV: TextView = userView.findViewById(R.id.itemTV)
                        itemTV.text = "전체"
                        itemTV.setOnClickListener {
                            contract_id = -1
                            contract_view_list()
                        }
                        contractLL.addView(userView)

                        for (i in 0 until data.length()) {
                            var json = data[i] as JSONObject

                            var type =json.getJSONObject("ContractType")
                            var name =Utils.getString(type,"name")

                            val userView = View.inflate(myContext, R.layout.item_contract, null)
                            var itemTV: TextView = userView.findViewById(R.id.itemTV)


                            itemTV.text = name

                            itemTV.setOnClickListener {
                                contract_id =Utils.getInt(type,"id")
                                contract_view_list()
                            }

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


    override fun onResume() {
        super.onResume()
        contract_id = -1
        page = 1
        contract_view_list()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }


}
