package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CouponAction

import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

//메세지관리(메시지작성화면)

class MssgAnalysisFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fra_mssg_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    // 쿠폰만들기(step1) - (고객필터)
    fun member_filter() {
        val params = RequestParams()

        params.put("company_id",1)

//        if (age.size>0){
//            for (i in 0..(age.size -1)){
//                val agestr = age[i]
//                //배열로 입력저장은 [] 이걸 넣어준다
//                params.put("age["+i+"]",agestr)
//                Log.d("나이",agestr)
//            }
//        }else{
//            Toast.makeText(myContext,"나이를 선택해주세요",Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (gender.size>0){
//            for (i in 0..(gender.size -1)){
//                val genderstr = gender[i]
//                //배열로 입력저장은 [] 이걸 넣어준다
//                params.put("gender["+i+"]",genderstr)
//                Log.d("성별",genderstr)
//            }
//        }else{
//            Toast.makeText(myContext,"성별을 선택해주세요",Toast.LENGTH_SHORT).show()
//            return
//        }
//        params.put("visited_date",visited_date)





        CouponAction.member_filter(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var memberCnt = response.getString("memberCnt")

//                        countTV.text = memberCnt
                        Toast.makeText(myContext,"고객선택완료",Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(myContext,"업데이트실패", Toast.LENGTH_SHORT).show()

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
