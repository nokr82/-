package com.devstories.anipointcompany.android.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.devstories.anipointcompany.android.R
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.devstories.anipointcompany.android.Actions.CouponAction
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.SerializableEntity
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.Serializable


class SetMessageContFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var companyNameTV: TextView
    lateinit var memberNameTV: TextView
    lateinit var pointTV: TextView

    lateinit var textLengthTV: TextView
    lateinit var messageTV: TextView
    lateinit var titleTV: TextView
    lateinit var nextTV: TextView
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

    var member_id = -1
    var company_id = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.frag_set_message_cont, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            } else {
                coupon_id = getArguments()!!.getString("coupon_id")
                search_type = getArguments()!!.getInt("search_type", -1)
                gender = getArguments()!!.getStringArrayList("gender")
                age = getArguments()!!.getStringArrayList("age")
                visited_date = getArguments()!!.getString("visited_date")
                from = getArguments()!!.getString("from")
                to = getArguments()!!.getString("to")
                count = getArguments()!!.getString("count")
                Log.d("쿠폰", search_type.toString())
                Log.d("쿠폰", coupon_id)
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

        pointTV.setOnClickListener {

            var message = messageContentET.text.toString() + "{_포인트_}"

            messageContentET.setText(message)

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

        nextTV.setOnClickListener {
            dlgView()


        }

    }

    fun dlgView() {
        var mPopupDlg: DialogInterface? = null

        val builder = AlertDialog.Builder(myContext)
        val dialogView = layoutInflater.inflate(R.layout.dlg_send_message, null)
        val cancelTV = dialogView.findViewById<TextView>(R.id.cancelTV)
        val msgWriteTV = dialogView.findViewById<TextView>(R.id.msgWriteTV)
        val sendCntTV = dialogView.findViewById<TextView>(R.id.sendCntTV)
        sendCntTV.text = count
        mPopupDlg = builder.setView(dialogView).show()
        cancelTV.setOnClickListener {
            mPopupDlg.dismiss()
        }
        msgWriteTV.setOnClickListener {
            send_message()
        }
    }

    // 쿠폰 만들기(step3) - 메세지 보내기
    fun send_message() {
        var message = Utils.getString(messageContentET)
        var title = Utils.getString(titleET)

        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("coupon_id", coupon_id)
        if (member_id != -1) {
            params.put("member_id", member_id)
        }
        params.put("message", message)
        params.put("7days_yn", "N")
        params.put("title", title)
        if (imgIV.drawable != null) {
            bitmap = imgIV.drawable as BitmapDrawable
            params.put("upload", ByteArrayInputStream(Utils.getByteArray(bitmap!!.bitmap)))
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
        if (search_type == 2) {
            params.put("from", from)
            params.put("to", to)
        } else if (search_type == 3) {
            params.put("visited_date", visited_date)
        } else if (search_type == 4) {
            params.put("from", from)
            params.put("to", to)
        } else if (search_type == 5) {
            params.put("from", from)
            params.put("to", to)
        }
        params.put("search_type", search_type)



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
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data!!.data
                Log.d("uri", contentURI.toString())
                //content://media/external/images/media/1200

                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(myContext.contentResolver, contentURI)
                    thumbnail = Utils.rotate(myContext.contentResolver, thumbnail, contentURI)
                    Log.d("thumbnail", thumbnail.toString())
                    imgIV.visibility = View.VISIBLE
                    imgIV.setImageBitmap(thumbnail)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(myContext, "바꾸기실패", Toast.LENGTH_SHORT).show()
                }

            }
        }


    }

}
