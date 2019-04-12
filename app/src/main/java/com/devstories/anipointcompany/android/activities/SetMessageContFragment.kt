package com.devstories.anipointcompany.android.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstories.anipointcompany.android.R
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.devstories.anipointcompany.android.Actions.CouponAction
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException


class SetMessageContFragment : Fragment() {

    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null
    private val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
    lateinit var companyNameTV: TextView
    lateinit var memberNameTV: TextView
    lateinit var pointTV: TextView

    lateinit var textLengthTV: TextView
    lateinit var messageTV: TextView
    lateinit var titleTV: TextView
    lateinit var nextTV: TextView
    lateinit var companyTelTV: TextView
    lateinit var imgLL: LinearLayout
    lateinit var imgIV: ImageView

    lateinit var testTV: TextView


    lateinit var messageContentET: EditText
    lateinit var titleET: EditText

    var count: String? = null
    var coupon_id: String? = null
    var search_type = -1
    var visited_date: String? = null
    var from: String? = null
    var to: String? = null
    var gender = ArrayList<String>()
    var age = ArrayList<String>()
    var bitmap: BitmapDrawable? = null
    var thumbnail: Bitmap? = null
    var contentURI: Uri? = null
    private val GALLERY = 1

    var stack_visit = -1
    var mising_day = -1
    var use_money = -1
    var left_point = -1
    var missing_from: String? = null
    var missing_to: String? = null
    var use_from : String? = null
    var use_to: String? = null
    var left_from : String? = null
    var left_to : String? = null


    var member_id = -1
    var company_id = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        return inflater.inflate(R.layout.frag_set_message_cont, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companyTelTV = view.findViewById(R.id.companyTelTV)
        companyNameTV = view.findViewById(R.id.companyNameTV)
        memberNameTV = view.findViewById(R.id.memberNameTV)
        pointTV = view.findViewById(R.id.pointTV)
        textLengthTV = view.findViewById(R.id.textLengthTV)
        messageTV = view.findViewById(R.id.messageTV)
        titleTV = view.findViewById(R.id.titleTV)
        nextTV = view.findViewById(R.id.nextTV)
        imgLL = view.findViewById(R.id.imgLL)
        imgIV = view.findViewById(R.id.imgIV)
        messageContentET = view.findViewById(R.id.messageContentET)
        titleET = view.findViewById(R.id.titleET)
        testTV = view.findViewById(R.id.testTV)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(context, "company_id")




        if (getArguments() != null) {
            member_id = getArguments()!!.getInt("member_id", -1)
            Log.d("멤버디", member_id.toString())
            if (member_id != -1) {
                coupon_id = getArguments()!!.getString("coupon_id")
            } else {
                coupon_id = getArguments()!!.getString("coupon_id")
                stack_visit  = getArguments()!!.getInt("stack_visit")
                mising_day  = getArguments()!!.getInt("mising_day")
                use_money  = getArguments()!!.getInt("use_money")
                left_point  = getArguments()!!.getInt("left_point")
                gender = getArguments()!!.getStringArrayList("gender")
                age = getArguments()!!.getStringArrayList("age")
                from = getArguments()!!.getString("from")
                to = getArguments()!!.getString("to")
                missing_from = getArguments()!!.getString("missing_from")
                missing_to = getArguments()!!.getString("missing_to")
                use_from = getArguments()!!.getString("use_from")
                use_to = getArguments()!!.getString("use_to")
                left_from = getArguments()!!.getString("left_from")
                left_to = getArguments()!!.getString("left_to")
                count = getArguments()!!.getString("count")
                Log.d("쿠폰", gender.toString())
                Log.d("쿠폰", age.toString())
            }

        }



        imgLL.setOnClickListener {
            choosePhotoFromGallary()
        }


        testTV.setOnClickListener {
            var intent = Intent(myContext, DlgTestMsgActivity::class.java)
            var message = Utils.getString(messageContentET)
            var title = Utils.getString(titleET)
            intent.putExtra("title", title)
            intent.putExtra("message", message)

            if (contentURI != null) {
                intent.putExtra("imageUri", contentURI.toString())
            } else {

            }

            startActivity(intent)
        }


        companyNameTV.setOnClickListener {
            var message = messageContentET.text.toString() + "{_매장이름_}"
            messageContentET.setText(message)
        }

        memberNameTV.setOnClickListener {

            var message = messageContentET.text.toString() + "{_고객이름_}"

            messageContentET.setText(message)

        }

        companyTelTV.setOnClickListener {

            var message = messageContentET.text.toString() + "{_매장번호_}"

            messageContentET.setText(message)

        }

        pointTV.setOnClickListener {

            var message = messageContentET.text.toString() + "{_포인트_}"

            messageContentET.setText(message)

        }
        nextTV.setOnClickListener {
            dlgView()
        }
        messageContentET.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textLengthTV.text = s.length.toString()
                messageTV.text = s

            }
        })

        titleET.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                titleTV.text = "[Web발신]\n" + s + "\n"
            }
        })



    }

    override fun onPause() {
        super.onPause()
        member_id = -1
        setedit()
    }

    fun setedit(){
        titleET.setText("")
        messageContentET.setText("")
    }

    fun dlgView() {
        var mPopupDlg: DialogInterface? = null

        val builder = AlertDialog.Builder(myContext)
        val dialogView = layoutInflater.inflate(R.layout.dlg_send_message, null)
        val cancelTV = dialogView.findViewById<TextView>(R.id.cancelTV)
        val msgWriteTV = dialogView.findViewById<TextView>(R.id.msgWriteTV)
        val sendCntTV = dialogView.findViewById<TextView>(R.id.sendCntTV)
        if (member_id != -1){
            sendCntTV.text = "1"
        }else{
            sendCntTV.text = count
        }
        mPopupDlg = builder.setView(dialogView).show()
        cancelTV.setOnClickListener {
            mPopupDlg.dismiss()
        }
        msgWriteTV.setOnClickListener {
            send_message()
            mPopupDlg.dismiss()
            var intent = Intent()
            intent.action = "FINAL_NEXT"
            myContext.sendBroadcast(intent)

        }
    }

    // 쿠폰 만들기(step3) - 메세지 보내기
    fun send_message() {



        var message = Utils.getString(messageContentET)
        var title = Utils.getString(titleET)

        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("coupon_id", coupon_id)
        if (member_id!=-1){
            params.put("member_id", member_id)
        }

        params.put("message",message+"\n무료거부:080-450-5601")


        params.put("7days_yn", "N")
        params.put("title", title)
        if (imgIV.drawable != null) {
            bitmap = imgIV.drawable as BitmapDrawable
            params.put("upload", ByteArrayInputStream(Utils.getByteArray(bitmap!!.bitmap)))
            params.put("type",2)
        }else{
            if (message.length<30){
                params.put("type",1)
            }else if (message.length>30){
                params.put("type",3)
            }
        }
        if (age.size > 0) {
            for (i in 0..(age.size - 1)) {
                val agestr = age[i]
                //배열로 입력저장은 [] 이걸 넣어준다
                params.put("age[" + i + "]", agestr)
                Log.d("나이", agestr)
            }
        }

        if (gender.size > 0) {
            for (i in 0..(gender.size - 1)) {
                val genderstr = gender[i]
                //배열로 입력저장은 [] 이걸 넣어준다
                params.put("gender[" + i + "]", genderstr)
                Log.d("성별", genderstr)
            }
        }
        if (stack_visit==2){
            params.put("stack_visit",stack_visit)
            params.put("from",from)
            params.put("to",to)
        }
        if (mising_day==3){
            params.put("mising_day",mising_day)
            params.put("missing_from ",missing_from)
            params.put("missing_to ",missing_to)
        }
        if (use_money==4){
            params.put("use_money",use_money)
            params.put("use_from",use_from)
            params.put("use_to",use_to)
        }
        if (left_point==5){
            params.put("left_point",left_point)
            params.put("left_from",left_from)
            params.put("left_to",left_to)
        }



        CouponAction.send_message(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        Toast.makeText(myContext, "전송성공", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(myContext, "전송실패", Toast.LENGTH_SHORT).show()

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

    private fun choosePhotoFromGallary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            loadPermissions(perms, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)
        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY)
        }

    }
    private fun loadPermissions(perms: Array<String>, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(myContext, perms[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity, perms, requestCode)
        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null)
            {
                val contentURI = data.data
                //Log.d("uri", contentURI.toString())

                try {
                    val selectedImageUri = data.data
                    var bt: Bitmap? = null

                    val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)

                    val cursor = context!!.contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)
                    if (cursor!!.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        bt = Utils.getImage(context!!.contentResolver, picturePath)
                        cursor.close()
                        imgIV.setImageBitmap(bt)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "바꾸기실패", Toast.LENGTH_SHORT).show()
                }

            }
        }


    }

}
