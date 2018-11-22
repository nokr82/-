package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.adapter.UserListAdapter
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject


class Point_List_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    internal lateinit var view: View
    lateinit var userLV: ListView
    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()

    lateinit var useradapter: UserListAdapter
    var data = arrayListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = ProgressDialog(myContext)
            return inflater.inflate(R.layout.fra_point_list,container,false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLV = view.findViewById(R.id.userLV)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        useradapter = UserListAdapter(myContext,R.layout.item_user_point_list,adapterData)
        userLV.adapter = useradapter



    }

    fun loadData(id: Int) {
        val params = RequestParams()
        params.put("member_id",id)



        MemberAction.user_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")


                    if ("ok" == result) {
                        val data = response.getJSONArray("list")



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
    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }


}
