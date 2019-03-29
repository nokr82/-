package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.aninuriandroid.adapter.ReserveListAdapter
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_reservation_manage.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class ReservationManageFragment : Fragment() {
    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    lateinit var reserveListAdapter: ReserveListAdapter
    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()



    var page = 1
    var totalpage = 0
    var company_id = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fra_reservation_manage, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        company_id = PrefUtils.getIntPreference(context,"company_id")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        reserveListAdapter = ReserveListAdapter(myContext, R.layout.item_reserve_list, adapterData,this)
        reservationLV.adapter = reserveListAdapter
        var lastitemVisibleFlag = false
        reservationLV.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                lastitemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    if (totalpage > page) {
                        page++
                        reserve_list()
                    }

                }
            }

        })

        reservTV.setOnClickListener {
            val intent = Intent(context, DlgReserveSaveActivity::class.java)
            startActivity(intent)
        }
        reserve_list()



    }

    override fun onResume() {
        super.onResume()
        reserve_list()
    }

    //예약 정보뽑기
    fun reserve_list() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("page", page)
        CompanyAction.reserve_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                Log.d("걀거",response.toString())
                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var data = response.getJSONArray("reserve")
                        totalpage = response.getInt("totalPage")
                        if (page == 1) {
                            adapterData.clear()
                            reserveListAdapter.notifyDataSetChanged()
                        }

                        for (i in 0 until data.length()) {

                            adapterData.add(data[i] as JSONObject)
                        }

                        reserveListAdapter.notifyDataSetChanged()

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




}
