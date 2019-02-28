package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CouponAction
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_test_msg.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream


//회원수정 다이얼로그
class DlgTestMsgActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: CustomProgressDialog? = null

    var title = ""
    var message = ""
    var thumbnail: Bitmap? = null
    var contentURI:Uri? = null
    var company_id = -1

    var num = ""
    var num2 = ""
    var num3 = ""
    var h:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideNavigations(this)
        setContentView(R.layout.dlg_test_msg)

        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        intent = getIntent()
        title = intent.getStringExtra("title")
        message = intent.getStringExtra("message")
        h = intent.getStringExtra("imageUri")
        if(h != null) {
            contentURI = Uri.parse(h)
        }
        company_id = PrefUtils.getIntPreference(context, "company_id")
//        contentURI = intent.getParcelableExtra("contentURI")
//
//        thumbnail= MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
//        thumbnail = Utils.rotate(context.contentResolver, thumbnail, contentURI)




        checkTV.setOnClickListener {
            test_message()
        }

    }



    override fun onDestroy() {
        super.onDestroy()

        progressDialog = null

    }

    fun hideNavigations(context: Activity) {
        val decorView = context.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    // 쿠폰 만들기(step3) - 메세지 보내기
    fun test_message() {
        num = Utils.getString(phoneNum1ET)
        num2 = Utils.getString(phoneNum2ET)
        num3 = Utils.getString(phoneNum3ET)

        var phone = num+num2+num3

        val params = RequestParams()
        params.put("company_id",company_id)
        params.put("title",title)
        params.put("message",message)
        params.put("phone",phone)
        if (contentURI!=null){
            params.put("type",2)
            thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
            params.put("upload", ByteArrayInputStream(Utils.getByteArray(thumbnail)))
        }else{
            if (message.length<30){
                params.put("type",1)
            }else if (message.length>30){
                params.put("type",3)
            }
        }
        CouponAction.test_message(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        Toast.makeText(context,"전송성공", Toast.LENGTH_SHORT).show()
                        Utils.hideKeyboard(context)
                        finish()
                    }else{
                        Toast.makeText(context,"전송실패", Toast.LENGTH_SHORT).show()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }


            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
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
