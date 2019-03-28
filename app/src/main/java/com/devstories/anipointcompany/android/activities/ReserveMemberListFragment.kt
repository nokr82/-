package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import com.devstories.aninuriandroid.adapter.MemberListAdapter
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_reserve_member_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import android.R.id
import android.content.Intent
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text


// 메세지 통계
class ReserveMemberListFragment : Fragment() {
    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null
    lateinit var adapter: MemberListAdapter
    var page = 1
    var totalpage = 0
    var company_id = -1
    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()
    var member_id = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fra_reserve_member_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        company_id = PrefUtils.getIntPreference(myContext, "company_id")
        adapter = MemberListAdapter(myContext, R.layout.item_member_list, adapterData,this)
        memberLV.adapter = adapter
        mainData()

        var lastitemVisibleFlag = false        //화면에 리스트의 마지막 아이템이 보여지는지 체크
        memberLV.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                lastitemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    if (totalpage > page) {
                        page++
                        mainData()
                    }

                }
            }

        })
        keywordET.setOnEditorActionListener() { v, actionId, event ->
            mainData()
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keywordET.setText("")
                true
            } else {
                false
            }
        }

        memberLV.setOnItemClickListener { parent, view, position, id ->
            var data = adapterData.get(position)
            Log.d("리스트선택", data.toString())
            val member = data.getJSONObject("Member")
            member_id = Utils.getInt(member, "id")
            adapter.notifyDataSetChanged()
        }

        nextTV.setOnClickListener {
            if (member_id == -1){
                Toast.makeText(myContext,"회원을 선택해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(myContext,DlgReserveSave2Activity::class.java)
            intent.putExtra("member_id",member_id)
            startActivity(intent)
        }

    }


    //고객목롭뽑기
    fun mainData() {

        val keyword = Utils.getString(keywordET)

        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("type", 1)
        params.put("keyword", keyword)
        params.put("page", page)
        MemberAction.user_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        var data = response.getJSONArray("member")

                        totalpage = response.getInt("totalPage")

                        if (page == 1) {
                            adapterData.clear()
                            adapter.notifyDataSetChanged()
                        }

                        for (i in 0 until data.length()) {

                            adapterData.add(data[i] as JSONObject)
                        }

                        adapter.notifyDataSetChanged()

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
