package com.devstories.anipointcompany.android.activities

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_userlist.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class User_List_Fragment : Fragment() {
    lateinit var myContext: Context

    private var progressDialog: ProgressDialog? = null

    lateinit var userLL: LinearLayout
    lateinit var userList_new_userLL : LinearLayout
    lateinit var userList_most_freq_userLL : LinearLayout
    lateinit var userList_birth_userLL : LinearLayout
    lateinit var joinLL : LinearLayout
    lateinit var accumulateLL : LinearLayout
    lateinit var btn_search : LinearLayout
    lateinit var entire_viewTV : TextView
    lateinit var useLL : LinearLayout
    var isBirthTab = false

    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()

    var EDIT_MEMBER_INFO = 101

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)


        return inflater.inflate(R.layout.fra_userlist,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLL = view.findViewById(R.id.userLL)
        userList_new_userLL = view.findViewById(R.id.userList_new_userLL)
        userList_most_freq_userLL = view.findViewById(R.id.userList_most_freq_userLL)
        userList_birth_userLL = view.findViewById(R.id.userList_birth_userLL)
        joinLL = view.findViewById(R.id.joinLL)
        accumulateLL = view.findViewById(R.id.accumulateLL)
        useLL = view.findViewById(R.id.useLL)
        btn_search = view.findViewById(R.id.btn_search)
        entire_viewTV = view.findViewById(R.id.entire_viewTV)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainData(1)


        useLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("step",4)
            startActivity(intent)
        }
        accumulateLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            startActivity(intent)
        }

        entire_viewTV.setOnClickListener {
            setLeftMenu()
            entire_viewTV.setTextColor(Color.parseColor("#ffffff"))
            mainData(1)
        }
        //단골
        userList_most_freq_userLL.setOnClickListener {
            setLeftMenu()
            most_freqTV.setTextColor(Color.parseColor("#ffffff"))

        }

        userList_birth_userLL.setOnClickListener {
            setLeftMenu()
            birthTV.setTextColor(Color.parseColor("#ffffff"))
            mainData(2)
            isBirthTab = true
        }

        btn_search.setOnClickListener {
            var key = keywordET.text.toString()
            if (key.isEmpty()){
                Utils.alert(context, "검색할 키워드를 입력하세요")
            }
            keywordET.setText("")
            keyWordData(key)

        }

        userList_new_userLL.setOnClickListener {
            setLeftMenu()
            new_userTV.setTextColor(Color.parseColor("#ffffff"))
            mainData(3)
        }

        userList_most_freq_userLL.setOnClickListener {
            setLeftMenu()
            most_freqTV.setTextColor(Color.parseColor("#ffffff"))
            mainData(4)
        }

    }

    fun setLeftMenu(){
        entire_viewTV.setTextColor(Color.parseColor("#80ffffff"))
        new_userTV.setTextColor(Color.parseColor("#80ffffff"))
        most_freqTV.setTextColor(Color.parseColor("#80ffffff"))
        birthTV.setTextColor(Color.parseColor("#80ffffff"))
    }

//고객목롭뽑기
    fun mainData(type : Int) {
        val params = RequestParams()
        params.put("company_id", 1)
        params.put("type", type)

        MemberAction.user_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    userLL.removeAllViews()
                    adapterData.clear()

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        var data = response.getJSONArray("member")

                        for (i in 0..(data.length() - 1)) {

                            adapterData.add(data[i] as JSONObject)

                            var json=data[i] as JSONObject
                            val member = json.getJSONObject("Member")
                            var point_o  = json.getJSONObject("Point")
                            var visitedList  = json.getJSONArray("VisitedList")

                            var point =   Utils.getString(point_o, "balance")
                            var member_id =   Utils.getInt(member, "id")


                            val userView = View.inflate(myContext, R.layout.item_user, null)


                            var dateTV : TextView = userView.findViewById(R.id.dateTV)
                            var nameTV : TextView = userView.findViewById(R.id.nameTV)
                            var pointTV : TextView = userView.findViewById(R.id.pointTV)
                            var acc_pointTV : TextView = userView.findViewById(R.id.acc_pointTV)
                            var visitTV : TextView = userView.findViewById(R.id.visitTV)
                            var name2TV : TextView = userView.findViewById(R.id.name2TV)
                            var genderTV : TextView = userView.findViewById(R.id.genderTV)
                            var ageTV : TextView = userView.findViewById(R.id.ageTV)
                            var birthTV : TextView = userView.findViewById(R.id.birthTV)
                            var use_pointTV : TextView = userView.findViewById(R.id.use_pointTV)
                            var couponTV: TextView = userView.findViewById(R.id.couponTV)
                            var visit_recordTV: TextView = userView.findViewById(R.id.visit_recordTV)
                            var stack_pointTV: TextView = userView.findViewById(R.id.stack_pointTV)
                            var memoTV: TextView = userView.findViewById(R.id.memoTV)
                            var phoneTV: TextView = userView.findViewById(R.id.phoneTV)
                            var modiLL: LinearLayout = userView.findViewById(R.id.modiLL)



                            var id = Utils.getString(member, "id")
                            var age =   Utils.getString(member, "age")
                            var name = Utils.getString(member, "name")
                            var gender =   Utils.getString(member, "gender")
                            var memo = Utils.getString(member, "memo")
                            var phone =   Utils.getString(member, "phone")
                            var coupon = Utils.getString(member, "coupon")
                            var stack_point =   Utils.getString(member, "point")
                            var use_point =   Utils.getString(member, "use_point")
                            var company_id = Utils.getString(member, "company_id")
                            var birth =   Utils.getString(member, "birth")
                            var created = Utils.getString(member, "created")
                            var visit =   Utils.getString(member, "visit_cnt")
                            val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
                            val updated = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Utils.getString(member, "updated"))
                            val updated_date = sdf.format(updated)

                            pointTV.text = point+"P"
                            use_pointTV.text = use_point+"P"
                            acc_pointTV.text = stack_point+"P"
                            stack_pointTV.text = "누적:"+stack_point+"P"
                            dateTV.text = updated_date+" 방문"
                            ageTV.text = age+"세"
                            nameTV.text = phone
                            name2TV.text = name

                            if(gender == "F") {
                                gender = "여"
                            } else if(gender == "M"){
                                gender = "남"
                            } else {
                                gender = "모름"
                            }

                            genderTV.text = gender
                            memoTV.text = memo
                            couponTV.text = coupon+"장"
                            birthTV.text = birth
                            visitTV.text = visit+"회"
                            phoneTV.text = phone

                            var str = ""

                            for(i in 0 until visitedList.length()) {
                                val json: JSONObject = visitedList[i] as JSONObject
                                val companySale = json.getJSONObject("CompanySale")
                                val category = json.getJSONObject("Category")

                                val created = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Utils.getString(companySale, "created"))
                                val created_str = SimpleDateFormat("yyyy-MM-dd").format(created)

                                if(str.length > 0) {
                                    str += "\n";
                                }

                                str = str + created_str + " / " + Utils.getString(category, "name") + " / " + Utils.comma(Utils.getString(companySale, "price"))

                            }

                            visit_recordTV.text = str


                            modiLL.setOnClickListener {
                                var intent = Intent(context, DlgEditMemberInfoActivity::class.java)
                                intent.putExtra("member_id", member_id)
                                startActivityForResult(intent, EDIT_MEMBER_INFO)
                            }


                            userLL.addView(userView)
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

    //키워드 :::: 키워드는 어쩔 수 없음 그냥 남겨두셈
    fun keyWordData(keyword:String) {
        val params = RequestParams()
        params.put("company_id", 1)
        params.put("keyword", "$keyword")

        MemberAction.user_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    userLL.removeAllViews()
                    adapterData.clear()

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        var data = response.getJSONArray("member")

                        for (i in 0..(data.length() - 1)) {
                            Log.d("갯수", i.toString())
                            adapterData.add(data[i] as JSONObject)
                            var json=data[i] as JSONObject
                            val member = json.getJSONObject("Member")
                            var point_o  = json.getJSONObject("Point")

                            var point =   Utils.getString(point_o, "balance")


                            val userView = View.inflate(myContext, R.layout.item_user, null)
                            var dateTV : TextView = userView.findViewById(R.id.dateTV)
                            var nameTV : TextView = userView.findViewById(R.id.nameTV)
                            var pointTV : TextView = userView.findViewById(R.id.pointTV)
                            var acc_pointTV : TextView = userView.findViewById(R.id.acc_pointTV)
                            var visitTV : TextView = userView.findViewById(R.id.visitTV)
                            var name2TV : TextView = userView.findViewById(R.id.name2TV)
                            var genderTV : TextView = userView.findViewById(R.id.genderTV)
                            var ageTV : TextView = userView.findViewById(R.id.ageTV)
                            var birthTV : TextView = userView.findViewById(R.id.birthTV)
                            var use_pointTV : TextView = userView.findViewById(R.id.use_pointTV)
                            var couponTV: TextView = userView.findViewById(R.id.couponTV)
                            var visit_recordTV: TextView = userView.findViewById(R.id.visit_recordTV)
                            var stack_pointTV: TextView = userView.findViewById(R.id.stack_pointTV)
                            var memoTV: TextView = userView.findViewById(R.id.memoTV)
                            var phoneTV: TextView = userView.findViewById(R.id.phoneTV)



                            var id = Utils.getString(member, "id")
                            var age =   Utils.getString(member, "age")
                            var name = Utils.getString(member, "name")
                            var gender =   Utils.getString(member, "gender")
                            var memo = Utils.getString(member, "memo")
                            var phone =   Utils.getString(member, "phone")
                            var coupon = Utils.getString(member, "coupon")
                            var stack_point =   Utils.getString(member, "point")
                            var use_point =   Utils.getString(member, "use_point")
                            var company_id = Utils.getString(member, "company_id")
                            var birth =   Utils.getString(member, "birth")
                            var created = Utils.getString(member, "created")
                            var visit =   Utils.getString(member, "visit_cnt")
                            val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
                            val updated = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Utils.getString(member, "updated"))
                            val updated_date = sdf.format(updated)

                            pointTV.text = point+"P"
                            use_pointTV.text = use_point+"P"
                            acc_pointTV.text = stack_point+"P"
                            stack_pointTV.text = "누적:"+stack_point+"P"
                            dateTV.text = updated_date+" 방문"
                            ageTV.text = age+"세"
                            nameTV.text = name
                            name2TV.text = name
                            genderTV.text = gender
                            memoTV.text = memo
                            couponTV.text = coupon+"장"
                            birthTV.text = birth
                            visitTV.text = visit+"회"
                            phoneTV.text = phone

                            userLL.addView(userView)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            EDIT_MEMBER_INFO -> {
                if (resultCode == RESULT_OK) {

                    if (data != null) {
                        var member_id = data.getIntExtra("member_id", -1)



                    }

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}
