package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CouponAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Config
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.nostra13.universalimageloader.core.ImageLoader
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_auto_coupon_settings.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException

class AutoCouponSettingsFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    var company_id = -1

    var coupon_id = -1
    var coupon_type1_id = -1
    var coupon_type2_id = -1
    var coupon_type3_id = -1
    var coupon_type4_id = -1
    var coupon_type5_id = -1
    var type = 1
    var week_use_yn = "N"
    var sat_use_yn = "N"
    var sun_use_yn = "N"
    var validity_alarm_yn = "N"
    var no_visit_30 = ""
    var no_visit_60 = ""
    var no_visit_90 = ""
    var use_day = 7

    var op_expiration = arrayOf("30일", "60일", "90일")
    var bitmap: BitmapDrawable? = null
    var thumbnail: Bitmap? = null
    var contentURI: Uri? = null

    private val GALLERY = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.fra_auto_coupon_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(myContext, "company_id")

        couponDateSP.adapter = ArrayAdapter(myContext, R.layout.spiner_item, op_expiration)



        companyNameTV.setOnClickListener {
            var message = messageET.text.toString() + "{_매장이름_}"
            messageET.setText(message)
        }

        memberNameTV.setOnClickListener {

            var message = messageET.text.toString() + "{_고객이름_}"

            messageET.setText(message)

        }

        companyTelTV.setOnClickListener {

            var message = messageET.text.toString() + "{_매장번호_}"

            messageET.setText(message)

        }

        pointTV.setOnClickListener {

            var message = messageET.text.toString() + "{_포인트_}"

            messageET.setText(message)

        }




        newMemberLL.setOnClickListener {
            setMenuView()
            newMemberLL.setBackgroundColor(Color.parseColor("#eeeeee"))

            couponNameTV.text = "첫 가입 고객 환영 쿠폰"
            type = 1
            couponData(coupon_type1_id)
        }

        birthMemberLL.setOnClickListener {
            setMenuView()
            birthMemberLL.setBackgroundColor(Color.parseColor("#eeeeee"))

            couponNameTV.text = "생일 고객 축하 쿠폰"
            type = 2
            couponData(coupon_type2_id)
        }

        noVisit30LL.setOnClickListener {
            setMenuView()
            noVisit30LL.setBackgroundColor(Color.parseColor("#eeeeee"))

            couponNameTV.text = "30일 미방문 고객 쿠폰"
            type = 3
            couponData(coupon_type3_id)
        }

        noVisit60LL.setOnClickListener {
            setMenuView()
            noVisit60LL.setBackgroundColor(Color.parseColor("#eeeeee"))

            couponNameTV.text = "60일 미방문 고객 쿠폰"
            type = 4
            couponData(coupon_type4_id)
        }
        imgLL.setOnClickListener {
            choosePhotoFromGallary()
        }
        noVisit90LL.setOnClickListener {
            setMenuView()
            noVisit90LL.setBackgroundColor(Color.parseColor("#eeeeee"))

            couponNameTV.text = "90일 미방문 고객 쿠폰"
            type = 5
            couponData(coupon_type5_id)
        }
        weekDayLL.setOnClickListener {
            if (week_use_yn == "Y") {
                week_use_yn = "N"
                weekDayIV.setImageResource(R.mipmap.box_check_off)
            } else {
                week_use_yn = "Y"
                weekDayIV.setImageResource(R.mipmap.box_check_on)
            }
        }

        satDayLL.setOnClickListener {
            if (sat_use_yn == "Y") {
                sat_use_yn = "N"
                satDayIV.setImageResource(R.mipmap.box_check_off)
            } else {
                sat_use_yn = "Y"
                satDayIV.setImageResource(R.mipmap.box_check_on)
            }
        }

        sunDayLL.setOnClickListener {
            if (sun_use_yn == "Y") {
                sun_use_yn = "N"
                sunDayIV.setImageResource(R.mipmap.box_check_off)
            } else {
                sun_use_yn = "Y"
                sunDayIV.setImageResource(R.mipmap.box_check_on)
            }
        }

        validityLL.setOnClickListener {
            if (validity_alarm_yn == "Y") {
                validity_alarm_yn = "N"
                validityIV.setImageResource(R.mipmap.off)
            } else {
                validity_alarm_yn = "Y"
                validityIV.setImageResource(R.mipmap.on)
            }
        }

        newMemberIV.setOnClickListener {

            if(coupon_type1_id < 1) {
                Toast.makeText(myContext,"저장된 쿠폰에 정보가 없습니다.정보저장후 시도해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changeTempYn(coupon_type1_id)
        }

        birthMemberIV.setOnClickListener {

            if(coupon_type2_id < 1) {
                Toast.makeText(myContext,"저장된 쿠폰에 정보가 없습니다.정보저장후 시도해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changeTempYn(coupon_type2_id)
        }

        noVisit30IV.setOnClickListener {

            if(coupon_type3_id < 1) {
                Toast.makeText(myContext,"저장된 쿠폰에 정보가 없습니다.정보저장후 시도해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changeTempYn(coupon_type3_id)
        }

        noVisit60IV.setOnClickListener {

            if(coupon_type4_id < 1) {

      /*          couponNameTV.text = "60일 미방문 고객 쿠폰"
                type = 4
                couponData(coupon_type4_id)*/
                Toast.makeText(myContext,"저장된 쿠폰에 정보가 없습니다.정보저장후 시도해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                changeTempYn(coupon_type4_id)
            }

        }

        noVisit90IV.setOnClickListener {

            if(coupon_type5_id < 1) {
                Toast.makeText(myContext,"저장된 쿠폰에 정보가 없습니다.정보저장후 시도해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changeTempYn(coupon_type5_id)

        }

        saveTV.setOnClickListener {

            if (sun_use_yn == "N" && sat_use_yn == "N" && week_use_yn == "N") {
                Toast.makeText(context, "사용 가능 요일을 선택해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val position = couponDateSP.selectedItemPosition

            if (position == 0) {
                use_day = 30
            } else if (position == 1) {
                use_day = 60
            } else {
                use_day = 90
            }

            val content = Utils.getString(contentET)

            if(content == "") {
                Toast.makeText(context, "쿠폰 내용을 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            editCoupon()
        }
        delIV.setOnClickListener {
            imgIV.setImageResource(0)
            delIV.visibility = View.GONE

        }
        loadData()

    }


    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                delIV.visibility = View.VISIBLE
                contentURI = data!!.data
                Log.d("uri", contentURI.toString())
                //content://media/external/images/media/1200

                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(myContext.contentResolver, contentURI)
                    thumbnail = Utils.rotate(myContext.contentResolver, thumbnail, contentURI)
                    Log.d("thumbnail", thumbnail.toString())
                    imgIV.setImageBitmap(thumbnail)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(myContext, "바꾸기실패", Toast.LENGTH_SHORT).show()
                }

            }
        }


    }


    fun setMenuView() {
        newMemberLL.setBackgroundColor(Color.parseColor("#00000000"))
        birthMemberLL.setBackgroundColor(Color.parseColor("#00000000"))
        noVisit30LL.setBackgroundColor(Color.parseColor("#00000000"))
        noVisit60LL.setBackgroundColor(Color.parseColor("#00000000"))
        noVisit90LL.setBackgroundColor(Color.parseColor("#00000000"))
    }

    fun loadData() {
        val params = RequestParams()
        params.put("company_id", company_id)

        CouponAction.auto_coupon(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        val newMemberCouponResult = response.getString("newMemberCouponResult")
                        val birthMemberCouponResult = response.getString("birthMemberCouponResult")
                        val noVisit30Result = response.getString("noVisit30Result")
                        val noVisit60Result = response.getString("noVisit60Result")
                        val noVisit90Result = response.getString("noVisit90Result")

                        coupon_type1_id = Utils.getInt(response, "coupon_type1_id")
                        coupon_type2_id = Utils.getInt(response, "coupon_type2_id")
                        coupon_type3_id = Utils.getInt(response, "coupon_type3_id")
                        coupon_type4_id = Utils.getInt(response, "coupon_type4_id")
                        coupon_type5_id = Utils.getInt(response, "coupon_type5_id")

                        if (newMemberCouponResult == "ok") {
                            newMemberIV.setImageResource(R.mipmap.on)
                        } else {
                            newMemberIV.setImageResource(R.mipmap.off)
                        }

                        if (birthMemberCouponResult == "ok") {
                            birthMemberIV.setImageResource(R.mipmap.on)
                        } else {
                            birthMemberIV.setImageResource(R.mipmap.off)
                        }

                        if (noVisit30Result == "ok") {
                            noVisit30IV.setImageResource(R.mipmap.on)
                        } else {
                            noVisit30IV.setImageResource(R.mipmap.off)
                        }

                        if (noVisit60Result == "ok") {
                            noVisit60IV.setImageResource(R.mipmap.on)
                        } else {
                            noVisit60IV.setImageResource(R.mipmap.off)
                        }

                        if (noVisit90Result == "ok") {
                            noVisit90IV.setImageResource(R.mipmap.on)
                        } else {
                            noVisit90IV.setImageResource(R.mipmap.off)
                        }

                        if(coupon_type1_id > 0) {

                            val coupon = response.getJSONObject("newMemberCoupon")
                            type = Utils.getInt(coupon, "type")
                            coupon_id = Utils.getInt(coupon, "id")
                            use_day = Utils.getInt(coupon, "use_day")
                            week_use_yn = Utils.getString(coupon, "week_use_yn")
                            sat_use_yn = Utils.getString(coupon, "sat_use_yn")
                            sun_use_yn = Utils.getString(coupon, "sun_use_yn")
                            validity_alarm_yn = Utils.getString(coupon, "validity_alarm_yn")

                            if (use_day == 30) {
                                couponDateSP.setSelection(0)
                            } else if (use_day == 60) {
                                couponDateSP.setSelection(1)
                            } else {
                                couponDateSP.setSelection(2)
                            }

                            if (week_use_yn == "Y") {
                                weekDayIV.setImageResource(R.mipmap.box_check_on)
                            } else {
                                weekDayIV.setImageResource(R.mipmap.box_check_off)
                            }

                            if (sat_use_yn == "Y") {
                                satDayIV.setImageResource(R.mipmap.box_check_on)
                            } else {
                                satDayIV.setImageResource(R.mipmap.box_check_off)
                            }

                            if (sun_use_yn == "Y") {
                                sunDayIV.setImageResource(R.mipmap.box_check_on)
                            } else {
                                sunDayIV.setImageResource(R.mipmap.box_check_off)
                            }

                            if (validity_alarm_yn == "Y") {
                                validityIV.setImageResource(R.mipmap.on)
                            } else {
                                validityIV.setImageResource(R.mipmap.off)
                            }

                            var image_uri = Utils.getString(coupon, "image_uri")
                            if (image_uri !=""){
                                delIV.visibility = View.VISIBLE
                            }else{
                                delIV.visibility = View.GONE
                            }
                            var image = Config.url + image_uri
                            ImageLoader.getInstance().displayImage(image,imgIV, Utils.UILoptionsUserProfile)
                            contentET.setText(Utils.getString(coupon, "content"))
                            titleET.setText(Utils.getString(coupon,"msg_title"))
                            messageET.setText(Utils.getString(coupon, "msg_content"))

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

    fun couponData(id: Int) {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("coupon_id", id)

        titleET.setText("")
        messageET.setText("")


        imgIV.setImageResource(0)
        CouponAction.coupon(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    couponDateSP.setSelection(0)
                    weekDayIV.setImageResource(R.mipmap.box_check_off)
                    satDayIV.setImageResource(R.mipmap.box_check_off)
                    sunDayIV.setImageResource(R.mipmap.box_check_off)
                    validityIV.setImageResource(R.mipmap.off)

                    coupon_id = -1
                    use_day = 30
                    week_use_yn = "N"
                    sat_use_yn = "N"
                    sun_use_yn = "N"
                    validity_alarm_yn = "N"

                    contentET.setText("")

                    if ("ok" == result) {

                        val coupon = response.getJSONObject("coupon")
                        type = Utils.getInt(coupon, "type")
                        coupon_id = Utils.getInt(coupon, "id")
                        use_day = Utils.getInt(coupon, "use_day")
                        week_use_yn = Utils.getString(coupon, "week_use_yn")
                        sat_use_yn = Utils.getString(coupon, "sat_use_yn")
                        sun_use_yn = Utils.getString(coupon, "sun_use_yn")
                        validity_alarm_yn = Utils.getString(coupon, "validity_alarm_yn")

                        if(use_day == 30) {
                            couponDateSP.setSelection(0)
                        } else if (use_day == 60) {
                            couponDateSP.setSelection(1)
                        } else {
                            couponDateSP.setSelection(2)
                        }

                        if (week_use_yn == "Y") {
                            weekDayIV.setImageResource(R.mipmap.box_check_on)
                        }

                        if (sat_use_yn == "Y") {
                            satDayIV.setImageResource(R.mipmap.box_check_on)
                        }

                        if (sun_use_yn == "Y") {
                            sunDayIV.setImageResource(R.mipmap.box_check_on)
                        }

                        if (validity_alarm_yn == "Y") {
                            validityIV.setImageResource(R.mipmap.on)
                        }

                        var image_uri = Utils.getString(coupon, "image_uri")
                        if (image_uri !=""){
                            delIV.visibility = View.VISIBLE
                        }else{
                            delIV.visibility = View.GONE
                        }

                        var image = Config.url + image_uri
                        ImageLoader.getInstance().displayImage(image,imgIV, Utils.UILoptionsUserProfile)
                        messageET.setText(Utils.getString(coupon, "msg_content"))
                        titleET.setText(Utils.getString(coupon, "msg_title"))
                        contentET.setText(Utils.getString(coupon, "content"))

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

    fun editCoupon() {

        val msg_content = Utils.getString(messageET)

        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("coupon_id", coupon_id)
        params.put("msg_content", msg_content)
        if (imgIV.drawable != null) {
            bitmap = imgIV.drawable as BitmapDrawable
            params.put("upload", ByteArrayInputStream(Utils.getByteArray(bitmap!!.bitmap)))
        }else{
            params.put("image", "")
            params.put("image_uri","")

        }
        params.put("type", type)
        params.put("use_day", use_day)
        params.put("week_use_yn", week_use_yn)
        params.put("sat_use_yn", sat_use_yn)
        params.put("sun_use_yn", sun_use_yn)
        params.put("validity_alarm_yn", validity_alarm_yn)
        params.put("name", Utils.getString(couponNameTV))
        params.put("content", Utils.getString(contentET))
        params.put("msg_title", Utils.getString(titleET))

        CouponAction.edit_coupon(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        Toast.makeText(myContext, "저장되었습니다", Toast.LENGTH_LONG).show()

                        val coupon = response.getJSONObject("coupon")
                        type = Utils.getInt(coupon, "type")
                        coupon_id = Utils.getInt(coupon, "id")
                        use_day = Utils.getInt(coupon, "use_day")
                        week_use_yn = Utils.getString(coupon, "week_use_yn")
                        sat_use_yn = Utils.getString(coupon, "sat_use_yn")
                        sun_use_yn = Utils.getString(coupon, "sun_use_yn")
                        validity_alarm_yn = Utils.getString(coupon, "validity_alarm_yn")

                        if(use_day == 30) {
                            couponDateSP.setSelection(0)
                        } else if (use_day == 60) {
                            couponDateSP.setSelection(1)
                        } else {
                            couponDateSP.setSelection(2)
                        }

                        if (week_use_yn == "Y") {
                            weekDayIV.setImageResource(R.mipmap.box_check_on)
                        } else {
                            weekDayIV.setImageResource(R.mipmap.box_check_off)
                        }

                        if (sat_use_yn == "Y") {
                            satDayIV.setImageResource(R.mipmap.box_check_on)
                        } else {
                            satDayIV.setImageResource(R.mipmap.box_check_off)
                        }

                        if (sun_use_yn == "Y") {
                            sunDayIV.setImageResource(R.mipmap.box_check_on)
                        } else {
                            sunDayIV.setImageResource(R.mipmap.box_check_off)
                        }

                        if (validity_alarm_yn == "Y") {
                            validityIV.setImageResource(R.mipmap.on)
                        } else {
                            validityIV.setImageResource(R.mipmap.off)
                        }

                        if(type == 1) {
                            newMemberIV.setImageResource(R.mipmap.on)
                            coupon_type1_id = coupon_id
                        } else if (type == 2) {
                            birthMemberIV.setImageResource(R.mipmap.on)
                            coupon_type2_id = coupon_id
                        } else if (type == 3) {
                            noVisit30IV.setImageResource(R.mipmap.on)
                            coupon_type3_id = coupon_id
                        } else if (type == 4) {
                            noVisit60IV.setImageResource(R.mipmap.on)
                            coupon_type4_id = coupon_id
                        } else if (type == 5) {
                            noVisit90IV.setImageResource(R.mipmap.on)
                            coupon_type5_id = coupon_id
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

                System.out.println(responseString);

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

    fun changeTempYn(id:Int) {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("coupon_id", id)

        CouponAction.change_temp_yn(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {

                        val coupon = response.getJSONObject("coupon")
                        val type = Utils.getInt(coupon, "type")
                        val temp_yn = Utils.getString(coupon, "temp_yn")

                        if(type == 1) {
                            changeImageOnOff(newMemberIV, temp_yn)
                        } else if (type == 2) {
                            changeImageOnOff(birthMemberIV, temp_yn)
                        } else if (type == 3) {
                            changeImageOnOff(noVisit30IV, temp_yn)
                        } else if (type == 4) {
                            changeImageOnOff(noVisit60IV, temp_yn)
                        } else if (type == 5) {
                            changeImageOnOff(noVisit90IV, temp_yn)
                        }

                    } else {
                        Toast.makeText(myContext,"저장된 쿠폰에 정보가 없습니다.정보저장후 시도해주세요.",Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(myContext,"저장된 쿠폰에 정보가 없습니다.정보저장후 시도해주세요.",Toast.LENGTH_SHORT).show()
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

    fun changeImageOnOff(imageView:ImageView, temp_yn:String) {
        if(temp_yn == "Y") {
            imageView.setImageResource(R.mipmap.off)
        } else {
            imageView.setImageResource(R.mipmap.on)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }


}
