package com.devstories.anipointcompany.android.activities

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.*
import android.widget.*
import com.devstories.anipointcompany.android.Actions.MemberAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.R.id.*
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_userlist.*
import me.grantland.widget.AutofitTextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import android.view.ViewTreeObserver.OnScrollChangedListener



//고객목록메인
class User_List_Fragment : Fragment()  {


    lateinit var myContext: Context

    private var progressDialog: ProgressDialog? = null

    lateinit var userLL: LinearLayout
    lateinit var userList_new_userLL: LinearLayout
    lateinit var userList_most_freq_userLL: LinearLayout
    lateinit var userList_birth_userLL: LinearLayout
    //lateinit var joinLL : LinearLayout
    lateinit var btn_search: LinearLayout
    lateinit var entire_viewTV: TextView
    lateinit var accumulateLL: LinearLayout
    lateinit var useLL: LinearLayout
    lateinit var scrollLL: LinearLayout
    lateinit var hoSV: HorizontalScrollView


    var isBirthTab = false

    var adapterData: ArrayList<JSONObject> = ArrayList<JSONObject>()
    var type = 1
    var company_id = -1

  /*  var page = 1
    var totalpage = 0
    private val visibleThreshold = 10
    private var userScrolled = false
    private var lastItemVisibleFlag = false
    private var lastcount = 0
    private var totalItemCountScroll = 0*/

    var EDIT_MEMBER_INFO = 101
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)


        return inflater.inflate(R.layout.fra_userlist, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollLL = view.findViewById(R.id.scrollLL)
        userLL = view.findViewById(R.id.userLL)
        hoSV = view.findViewById(R.id.hoSV)
        userList_new_userLL = view.findViewById(R.id.userList_new_userLL)
        userList_most_freq_userLL = view.findViewById(R.id.userList_most_freq_userLL)
        userList_birth_userLL = view.findViewById(R.id.userList_birth_userLL)
//        joinLL = view.findViewById(R.id.joinLL)
        accumulateLL = view.findViewById(R.id.accumulateLL)
        useLL = view.findViewById(R.id.useLL)
        btn_search = view.findViewById(R.id.btn_search)
        entire_viewTV = view.findViewById(R.id.entire_viewTV)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(myContext, "company_id")

        mainData(1)

//        hoSV.setOnScrollListener(myContext)





        Utils.getViewHeight(scrollLL,object : Utils.OnHeightSetListener {
                    override fun sized(width: Int, height: Int) {
                        val lps = scrollLL.getLayoutParams()
                        lps.height = height
                        lps.width = width
                        scrollLL.setLayoutParams(lps)
                    }
                }
        )



        useLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("step", 4)
            startActivity(intent)
        }
        accumulateLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            startActivity(intent)
        }

        entire_viewTV.setOnClickListener {
            setLeftMenu()
            entire_viewTV.setTextColor(Color.parseColor("#ffffff"))
            type = 1
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
            type = 2
            mainData(2)
            isBirthTab = true
        }

        btn_search.setOnClickListener {
            var key = keywordET.text.toString()
            if (key.isEmpty()) {
                Utils.alert(context, "검색할 키워드를 입력하세요")
                return@setOnClickListener
            }
            if (key.equals("남자")||key.equals("남")||key.equals("남성")){
                key = "M"
            }else if (key.equals("여자")||key.equals("여")||key.equals("여성")){
                key = "F"
            }
            Utils.hideKeyboard(myContext)
            keywordET.setText("")
            keyWordData(key)

        }

        userList_new_userLL.setOnClickListener {
            setLeftMenu()
            new_userTV.setTextColor(Color.parseColor("#ffffff"))
            type = 3
            mainData(3)
        }

        userList_most_freq_userLL.setOnClickListener {
            setLeftMenu()
            most_freqTV.setTextColor(Color.parseColor("#ffffff"))
            type = 4
            mainData(4)
        }

    }

/*
    override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onScrollStateChanged(p0: AbsListView?, scrollState: Int) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            userScrolled = true
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
            userScrolled = false

            //화면이 바닥에 닿았을때
            if (totalpage > page) {
                page++
                Toast.makeText(myContext,page.toString()+"입니다",Toast.LENGTH_SHORT).show()
                lastcount = totalItemCountScroll

                mainData(type)
            }
        }

    }*/


    fun setLeftMenu() {
        entire_viewTV.setTextColor(Color.parseColor("#80ffffff"))
        new_userTV.setTextColor(Color.parseColor("#80ffffff"))
        most_freqTV.setTextColor(Color.parseColor("#80ffffff"))
        birthTV.setTextColor(Color.parseColor("#80ffffff"))
    }

    //고객목롭뽑기
    fun mainData(type: Int) {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("type", type)
//        params.put("page",page)
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

//                        totalpage = response.getInt("totalPage")
//                        Log.d("페이지", totalpage.toString())


                        for (i in 0..(data.length() - 1)) {

                            adapterData.add(data[i] as JSONObject)

                            var json = data[i] as JSONObject
                            val member = json.getJSONObject("Member")
                            var point_o = json.getJSONObject("Point")
                            var visitedList = json.getJSONArray("VisitedList")

                            var point = Utils.getString(point_o, "balance")
                            var member_id = Utils.getInt(member, "id")


                            val userView = View.inflate(myContext, R.layout.item_user, null)

                            var dateTV: TextView = userView.findViewById(R.id.dateTV)
                            var nameTV: TextView = userView.findViewById(R.id.nameTV)
                            var pointTV: TextView = userView.findViewById(R.id.pointTV)
                            var acc_pointTV: TextView = userView.findViewById(R.id.acc_pointTV)
                            var visitTV: TextView = userView.findViewById(R.id.visitTV)
                            var name2TV: TextView = userView.findViewById(R.id.name2TV)
                            var genderTV: TextView = userView.findViewById(R.id.genderTV)
                            var ageTV: TextView = userView.findViewById(R.id.ageTV)
                            var birthTV: TextView = userView.findViewById(R.id.birthTV)
                            var use_pointTV: TextView = userView.findViewById(R.id.use_pointTV)
                            var couponTV: TextView = userView.findViewById(R.id.couponTV)
                            var visit_recordTV: TextView = userView.findViewById(R.id.visit_recordTV)
//                            var stack_pointTV: TextView = userView.findViewById(R.id.stack_pointTV)
                            var memoTV: TextView = userView.findViewById(R.id.memoTV)
                            var phoneTV: TextView = userView.findViewById(R.id.phoneTV)
                            var modiLL: LinearLayout = userView.findViewById(R.id.modiLL)
                            var msgLL: LinearLayout = userView.findViewById(R.id.msgLL)
                            var couponLL:LinearLayout = userView.findViewById(R.id.couponLL)



                            var id = Utils.getInt(member, "id")
                            var age = Utils.getString(member, "age")
                            var name = Utils.getString(member, "name")
                            var gender = Utils.getString(member, "gender")
                            var memo = Utils.getString(member, "memo")
                            var phone = Utils.getString(member, "phone")
                            var coupon = Utils.getString(member, "coupon")
                            var stack_point = Utils.getString(member, "point")
                            var use_point = Utils.getString(member, "use_point")
                            var company_id = Utils.getString(member, "company_id")
                            var birth = Utils.getString(member, "birth")
                            var created = Utils.getString(member, "created")
                            var visit = Utils.getString(member, "visit_cnt")
                            val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)

                            couponLL.setOnClickListener {
                                var intent = Intent(context, DlgCouponListActivity::class.java)
                                intent.putExtra("phone", phone)
                                Log.d("쿠폰전화",phone)
                                 startActivity(intent)
                            }


                            if (Utils.getString(point_o, "updated")!=""){
                                val updated = SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(Utils.getString(point_o, "updated"))
                                var updated_date = sdf.format(updated)
                                dateTV.text = updated_date + " 방문"
                            }
                            var r_phone:String? = null
                            if (age.equals("")){
                                age = "─"
                                ageTV.gravity = Gravity.CENTER
                            }else{
                                age+="대"
                            }

                            if (name=="─"){
                             name2TV.gravity = Gravity.CENTER
                            }



                            if (birth.equals("")){
                                birth = "─"
                                birthTV.gravity = Gravity.CENTER
                            }
                            if (name.equals("")){
                                name = "─"
                                name2TV.gravity = Gravity.CENTER
                            }
                            if (stack_point.equals("")){
                                stack_point = "0"
                            }

                            if (use_point.equals("")){
                                use_point = "0"
                            }


                            if (phone.length==11){
                                //번호하이픈
                                r_phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7,11)
                                r_phone = r_phone.substring(0, 6) + '*' + r_phone.substring(7)
                                r_phone = r_phone.substring(0, 5) + '*' + r_phone.substring(6)
                                r_phone = r_phone.substring(0, 4) + '*' + r_phone.substring(5)
                            }else{
                                r_phone =phone
                            }



                            pointTV.text = Utils.comma(point) + "P"
                            use_pointTV.text = Utils.comma(use_point) + "P"
                            acc_pointTV.text = Utils.comma(stack_point) + "P"
//                            stack_pointTV.text = "누적:" +Utils.comma(stack_point) + "P"

                            ageTV.text = age
                            nameTV.text = r_phone
                            name2TV.text = name

                            if (gender == "F") {
                                gender = "여성"
                            } else if (gender == "M") {
                                gender = "남성"
                            } else {
                                gender = "모름"
                            }

                            genderTV.text = gender
                            memoTV.text = memo
                            couponTV.text = coupon + "장"
                            birthTV.text = birth
                            visitTV.text = Utils.comma(visit) + "회"
                            phoneTV.text = phone

                            var str = ""



                            for (i in 0 until visitedList.length()) {
                                val json: JSONObject = visitedList[i] as JSONObject
                                val companySale = json.getJSONObject("CompanySale")


                                val created = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Utils.getString(companySale, "created"))
                                var created_str = SimpleDateFormat("yy-MM-dd").format(created)

                              /*  if (str.length > 0) {
                                    str += "\n"
                                }*/
                              var use_point =  Utils.getString(companySale, "use_point")
                                var point = Utils.getString(companySale, "point")
                                var coupon = Utils.getString(companySale, "coupon_id")



                                var price = Utils.getString(companySale, "price")
                          /*      if (use_point.equals("")){
                                    use_point="0"
                                }
                                if (point.equals("")){
                                    point = "0"
                                }
                                if (coupon.equals("")){
                                    coupon = "없음"
                                }*/

                                created_str = created_str.replace("-","")


//                                str = str + created_str + " / " + Utils.getString(category, "name") + " / " + Utils.comma(Utils.getString(companySale, "price"))+"원\n"+
//                                        "적립: " +Utils.comma(point) + "P/사용:" +Utils.comma(use_point)+ "P"+"/사용쿠폰:"+coupon



                                if (coupon!=""){
                                    val MemberCoupon = json.getJSONObject("MemberCoupon")
                                    var coupon_name = Utils.getString(MemberCoupon, "coupon_name")
                                    str = str+created_str+" 쿠폰 사용 "+coupon_name+"\n"
                                }


                                if (point != ""&& use_point!=""){
                                    str = str+created_str+" 사용 "+Utils.comma(use_point)+"P /"+" 적립 "+Utils.comma(point)+"P"+"("+Utils.comma(Utils.getString(companySale, "price"))+"*"+
                                            Utils.getString(companySale, "per")+"%)\n"
                                }
                                else if (use_point != ""){
                                    if (use_point != "0"){
                                        str = str+created_str+" 사용 "+Utils.comma(use_point)+"P\n"
                                    }
                                } else if (point != ""){
                                    str = str+created_str+" 적립 "+Utils.comma(point)+"P"+"("+Utils.comma(Utils.getString(companySale, "price"))+"*"+
                                    Utils.getString(companySale, "per")+"%)\n"
                                }else{

                                }
//                                if (coupon != "-1"){
//                                    val MemberCoupon = json.getJSONObject("MemberCoupon")
//                                    var coupon_name = Utils.getString(MemberCoupon, "coupon_name")
//                                    str = str+created_str+" 쿠폰 사용 "+coupon_name+"\n"
//                                }



                            }

                            visit_recordTV.text = str

                            msgLL.setOnClickListener {
                                var intent = Intent()
                                intent.putExtra("member_id", member_id)
                                Log.d("멤버아이디", member_id.toString())
                                intent.action = "MSG_NEXT"
                                myContext.sendBroadcast(intent)
                            }


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
    fun keyWordData(keyword: String) {



        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("keyword", "$keyword")
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

                            var json = data[i] as JSONObject
                            val member = json.getJSONObject("Member")
                            var point_o = json.getJSONObject("Point")
                            var visitedList = json.getJSONArray("VisitedList")

                            var point = Utils.getString(point_o, "balance")
                            var member_id = Utils.getInt(member, "id")


                            val userView = View.inflate(myContext, R.layout.item_user, null)


                            var dateTV: TextView = userView.findViewById(R.id.dateTV)
                            var nameTV: TextView = userView.findViewById(R.id.nameTV)
                            var pointTV: TextView = userView.findViewById(R.id.pointTV)
                            var acc_pointTV: TextView = userView.findViewById(R.id.acc_pointTV)
                            var visitTV: TextView = userView.findViewById(R.id.visitTV)
                            var name2TV: TextView = userView.findViewById(R.id.name2TV)
                            var genderTV: TextView = userView.findViewById(R.id.genderTV)
                            var ageTV: TextView = userView.findViewById(R.id.ageTV)
                            var birthTV: TextView = userView.findViewById(R.id.birthTV)
                            var use_pointTV: TextView = userView.findViewById(R.id.use_pointTV)
                            var couponTV: TextView = userView.findViewById(R.id.couponTV)
                            var visit_recordTV: TextView = userView.findViewById(R.id.visit_recordTV)
//                            var stack_pointTV: TextView = userView.findViewById(R.id.stack_pointTV)
                            var memoTV: TextView = userView.findViewById(R.id.memoTV)
                            var phoneTV: TextView = userView.findViewById(R.id.phoneTV)
                            var modiLL: LinearLayout = userView.findViewById(R.id.modiLL)
                            var msgLL: LinearLayout = userView.findViewById(R.id.msgLL)
                            var couponLL:LinearLayout = userView.findViewById(R.id.couponLL)

                            var id = Utils.getString(member, "id")
                            var age = Utils.getString(member, "age")
                            var name = Utils.getString(member, "name")
                            var gender = Utils.getString(member, "gender")
                            var memo = Utils.getString(member, "memo")
                            var phone = Utils.getString(member, "phone")
                            var coupon = Utils.getString(member, "coupon")
                            var stack_point = Utils.getString(member, "point")
                            var use_point = Utils.getString(member, "use_point")
                            var company_id = Utils.getString(member, "company_id")
                            var birth = Utils.getString(member, "birth")
                            var created = Utils.getString(member, "created")
                            var visit = Utils.getString(member, "visit_cnt")
                            val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)

                            couponLL.setOnClickListener {
                                var intent = Intent(context, DlgCouponListActivity::class.java)
                                intent.putExtra("phone", phone)
                                Log.d("쿠폰전화",phone)
                                startActivity(intent)
                            }

                            if (Utils.getString(point_o, "updated")!=""){
                                val updated = SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(Utils.getString(point_o, "updated"))
                                var updated_date = sdf.format(updated)
                                dateTV.text = updated_date + " 방문"
                            }
                            var r_phone:String? = null
                            if (age.equals("")){
                                age = "─"
                                ageTV.gravity = Gravity.CENTER
                            }else{
                                age+="대"
                            }

                            if (name=="─"){
                                name2TV.gravity = Gravity.CENTER
                            }



                            if (birth.equals("")){
                                birth = "─"
                                birthTV.gravity = Gravity.CENTER
                            }
                            if (name.equals("")){
                                name = "─"
                                name2TV.gravity = Gravity.CENTER
                            }
                            if (stack_point.equals("")){
                                stack_point = "0"
                            }

                            if (use_point.equals("")){
                                use_point = "0"
                            }


                            if (phone.length==11){
                                //번호하이픈
                                r_phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7,11)
                                r_phone = r_phone.substring(0, 6) + '*' + r_phone.substring(7)
                                r_phone = r_phone.substring(0, 5) + '*' + r_phone.substring(6)
                                r_phone = r_phone.substring(0, 4) + '*' + r_phone.substring(5)
                            }else{
                                r_phone =phone
                            }



                            pointTV.text = Utils.comma(point) + "P"
                            use_pointTV.text = Utils.comma(use_point) + "P"
                            acc_pointTV.text = Utils.comma(stack_point) + "P"
//                            stack_pointTV.text = "누적:" +Utils.comma(stack_point) + "P"

                            ageTV.text = age
                            nameTV.text = r_phone
                            name2TV.text = name

                            if (gender == "F") {
                                gender = "여성"
                            } else if (gender == "M") {
                                gender = "남성"
                            } else {
                                gender = "모름"
                            }

                            genderTV.text = gender
                            memoTV.text = memo
                            couponTV.text = coupon + "장"
                            birthTV.text = birth
                            visitTV.text = Utils.comma(visit) + "회"
                            phoneTV.text = phone

                            var str = ""



                            for (i in 0 until visitedList.length()) {
                                val json: JSONObject = visitedList[i] as JSONObject
                                val companySale = json.getJSONObject("CompanySale")


                                val created = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Utils.getString(companySale, "created"))
                                var created_str = SimpleDateFormat("yy-MM-dd").format(created)

                                /*  if (str.length > 0) {
                                      str += "\n"
                                  }*/
                                var use_point =  Utils.getString(companySale, "use_point")
                                var point = Utils.getString(companySale, "point")
                                var coupon = Utils.getString(companySale, "coupon_id")



                                var price = Utils.getString(companySale, "price")
                                /*      if (use_point.equals("")){
                                          use_point="0"
                                      }
                                      if (point.equals("")){
                                          point = "0"
                                      }
                                      if (coupon.equals("")){
                                          coupon = "없음"
                                      }*/

                                created_str = created_str.replace("-","")


//                                str = str + created_str + " / " + Utils.getString(category, "name") + " / " + Utils.comma(Utils.getString(companySale, "price"))+"원\n"+
//                                        "적립: " +Utils.comma(point) + "P/사용:" +Utils.comma(use_point)+ "P"+"/사용쿠폰:"+coupon



                                if (coupon!=""){
                                    val MemberCoupon = json.getJSONObject("MemberCoupon")
                                    var coupon_name = Utils.getString(MemberCoupon, "coupon_name")
                                    str = str+created_str+" 쿠폰 사용 "+coupon_name+"\n"
                                }


                                if (point != ""&& use_point!=""){
                                    str = str+created_str+" 사용 "+Utils.comma(use_point)+"P /"+" 적립 "+Utils.comma(point)+"P"+"("+Utils.comma(Utils.getString(companySale, "price"))+"*"+
                                            Utils.getString(companySale, "per")+"%)\n"
                                }
                                else if (use_point != ""){
                                    if (use_point != "0"){
                                        str = str+created_str+" 사용 "+Utils.comma(use_point)+"P\n"
                                    }
                                } else if (point != ""){
                                    str = str+created_str+" 적립 "+Utils.comma(point)+"P"+"("+Utils.comma(Utils.getString(companySale, "price"))+"*"+
                                            Utils.getString(companySale, "per")+"%)\n"
                                }else{

                                }
//                                if (coupon != "-1"){
//                                    val MemberCoupon = json.getJSONObject("MemberCoupon")
//                                    var coupon_name = Utils.getString(MemberCoupon, "coupon_name")
//                                    str = str+created_str+" 쿠폰 사용 "+coupon_name+"\n"
//                                }



                            }

                            visit_recordTV.text = str

                            msgLL.setOnClickListener {
                                var intent = Intent()
                                intent.putExtra("member_id", member_id)
                                Log.d("멤버아이디", member_id.toString())
                                intent.action = "MSG_NEXT"
                                myContext.sendBroadcast(intent)
                            }


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            EDIT_MEMBER_INFO -> {
                if (resultCode == RESULT_OK) {
                    mainData(1)
                    val member_id = data!!.getIntExtra("member_id", -1)
                    Log.d("받아오는 값", member_id.toString())

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

