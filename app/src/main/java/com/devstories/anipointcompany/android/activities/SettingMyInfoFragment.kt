package com.devstories.anipointcompany.android.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Config
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageSize
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_setting_my_info.*
import kotlinx.android.synthetic.main.fragment_setting_my_info.view.*
import kotlinx.android.synthetic.main.item_company_img.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException
//설정 -내정보
//이미지 용량줄일 필요 !!
class SettingMyInfoFragment : Fragment() {

    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null

    lateinit var compNameTV: TextView
    lateinit var phoneNum1ET: EditText
    lateinit var phoneNum2ET: EditText
    lateinit var phoneNum3ET: EditText
    lateinit var termET: TextView
    lateinit var compIdET: TextView
    lateinit var addImage1RL: RelativeLayout
    lateinit var tempPasswordET: EditText
    lateinit var newPasswordET: EditText
    lateinit var newPassCheckET: EditText
    lateinit var checkTV: TextView
    lateinit var infocheckTV: TextView
    lateinit var imgcheckTV: TextView
    lateinit var termTV: TextView
    lateinit var userLL: LinearLayout

    private val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
    private val GALLERY = 1


    //비트맵 이미지 배열
    //이걸로 api배열에 이미지를 넣는다.
    var addImages = ArrayList<Bitmap>()
    var delids = ArrayList<Int>()
    var passwd = ""

    var company_id = -1

    var imageUrlToIVs = HashMap<String, ImageView>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        return inflater.inflate(R.layout.fragment_setting_my_info, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compNameTV = view.findViewById(R.id.compNameTV)
        phoneNum1ET = view.findViewById(R.id.phoneNum1ET)
        phoneNum2ET = view.findViewById(R.id.phoneNum2ET)
        phoneNum3ET = view.findViewById(R.id.phoneNum3ET)
        compIdET = view.findViewById(R.id.compIdET)
        addImage1RL = view.findViewById(R.id.addImage1RL)
        termTV = view.findViewById(R.id.termTV)
        tempPasswordET = view.findViewById(R.id.tempPasswordET)
        newPasswordET = view.findViewById(R.id.newPasswordET)
        newPassCheckET = view.findViewById(R.id.newPassCheckET)
        checkTV = view.findViewById(R.id.checkTV)
        infocheckTV= view.findViewById(R.id.infocheckTV)
        imgcheckTV= view.findViewById(R.id.imgcheckTV)
        userLL =  view.findViewById(R.id.userLL)
        termTV=  view.findViewById(R.id.termTV)



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        company_id = PrefUtils.getIntPreference(context, "company_id")


        company_info(company_id)


        logoutTV.setOnClickListener {
            PrefUtils.clear(context)
            val intent = Intent(context, LoginActivity::class.java)
            PrefUtils.setPreference(context,"autoLogin", false)
            intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        //정보수정
        infocheckTV.setOnClickListener {
            edit_info()
            company_info(company_id)
        }
        addImage1RL.setOnClickListener {

            if (userLL.getChildCount() > 9) {
                Toast.makeText(myContext, "10개만등록불가.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            choosePhotoFromGallary()
        }

        addmanageTV.setOnClickListener {
            val intent = Intent(myContext,DlgAddManageActivity::class.java)
            startActivity(intent)
        }


        imgcheckTV.setOnClickListener {

            if (userLL.getChildCount() > 9) {
                Toast.makeText(myContext, "10개만등록불가.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            edit_image()
        }
        checkTV.setOnClickListener {
            var t_pass = Utils.getString(tempPasswordET)
            if (passwd==t_pass){
                var n_pass = Utils.getString(newPasswordET)
                var n_pass2 = Utils.getString(newPassCheckET)
                if (n_pass==n_pass2){
                    edit_pass()
                }else{
                    Toast.makeText(myContext,"새로운 비밀번호가 같지않습니다",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(myContext,"현재 비밀번호가 같지않습니다",Toast.LENGTH_SHORT).show()
            }
            tempPasswordET.setText("")
            newPasswordET.setText("")
            newPassCheckET.setText("")
        }

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

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                Log.d("uri",contentURI.toString())
                //content://media/external/images/media/1200

                try
                {
                    var thumbnail = MediaStore.Images.Media.getBitmap(myContext.contentResolver, contentURI)
                    thumbnail = Utils.rotate(myContext.contentResolver, thumbnail, contentURI)
                    Log.d("넓이1",thumbnail.width.toString())
                    Log.d("높이",thumbnail.height.toString())
                    //비트맵배열에 비트맵추가
                    addImages.add(thumbnail)

                    Log.d("이미지 추가",addImages.toString())
                    val userView = View.inflate(myContext, R.layout.item_company_img, null)
                    val c_imgIV :ImageView = userView.findViewById(R.id.c_imgIV)
                    val delIV :ImageView = userView.findViewById(R.id.delIV)
                    c_imgIV.setImageBitmap(thumbnail)
                    userLL.addView(userView)
                    //배열사이즈값 -해줘서
                    userView.tag = addImages.size -1

                    delIV.setOnClickListener {
                        Log.d("태그",userView.tag.toString())
                            userLL.removeView(userView)
                    }

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(myContext, "바꾸기실패", Toast.LENGTH_SHORT).show()
                }

            }
        }


    }

    override fun onResume() {
        super.onResume()
//        company_info(company_id)
    }

    //사업체 정보뽑기
    fun company_info(company_id: Int) {
        val params = RequestParams()
        params.put("company_id",company_id)

        CompanyAction.company_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        val company = response.getJSONObject("company")
                        val company_name = Utils.getString(company,"company_name")
                        val phone1 = Utils.getString(company,"phone1")
                        val phone2 = Utils.getString(company,"phone2")
                        val phone3 = Utils.getString(company,"phone3")
                        val login_id = Utils.getString(company,"login_id")
                        var s_contract_term = Utils.getString(company,"s_contract_term")
                        var e_contract_term = Utils.getString(company,"e_contract_term")

                        passwd=Utils.getString(company,"passwd")

                        compNameTV.setText(company_name)
                        phoneNum1ET.setText(phone1)
                        phoneNum2ET.setText(phone2)
                        phoneNum3ET.setText(phone3)
                        compIdET.setText(login_id)
                        termTV.text = s_contract_term+"~"+e_contract_term

                        val images = response.getJSONArray("images")
                        Log.d("이미지",images.toString())

                        userLL.removeAllViews()

                        for (i in 0..images.length()-1){
                            //새로운뷰를 이미지의 길이만큼생성
                            val userView = View.inflate(myContext, R.layout.item_company_img, null)
                            var json=images[i] as JSONObject
                            Log.d("제이슨",json.toString())
                            val CompanyImage = json.getJSONObject("CompanyImage")
                            Log.d("제이슨이미지",json.toString())
                            val image_uri = Utils.getString(CompanyImage,"image_uri")
                            val c_imgIV :ImageView = userView.findViewById(R.id.c_imgIV)
                            val delIV :ImageView = userView.findViewById(R.id.delIV)
                            var image = Config.url + image_uri
                            Log.d("이미지1",image)
                            userView.tag = Utils.getInt(CompanyImage, "id")
                            delIV.setOnClickListener {
                                userLL.removeView(userView)
                                delids.add(userView.tag as Int)
                                Log.d("아이디값",delids.toString())
                            }

                            imageUrlToIVs.put(image, c_imgIV)

                            ImageLoader.getInstance().loadImage(image, object : ImageLoadingListener {
                                override fun onLoadingCancelled(imageUri: String?, view: View?) {

                                }

                                override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                                }

                                override fun onLoadingStarted(imageUri: String?, view: View?) {
                                }

                                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                                    val iv = imageUrlToIVs.get(imageUri!!)
                                    iv!!.setImageBitmap(loadedImage)
                                    Log.d("높이23",loadedImage!!.height.toString())
                                }
                            })

                            userLL.addView(userView)

                        }


                        val customers = response.getJSONArray("customer")
                        manageLL.removeAllViews()
                        for (i in 0..customers.length()-1){
                            //새로운뷰를 이미지의 길이만큼생성
                            val userView = View.inflate(myContext, R.layout.item_manage, null)
                            val delLL :LinearLayout = userView.findViewById(R.id.delLL)
                            val nameTV :TextView = userView.findViewById(R.id.nameTV)
                            var json=customers[i] as JSONObject
                            val CompanyCustomer = json.getJSONObject("CompanyCustomer")
                            val name = Utils.getString(CompanyCustomer,"name")
                            nameTV.text = name
                            userView.tag = Utils.getInt(CompanyCustomer, "id")
                            delLL.setOnClickListener {
                                manageLL.removeView(userView)
                                del_manage(Utils.getInt(CompanyCustomer, "id"))
                            }
                            manageLL.addView(userView)

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

    //사업체 비밀번호변경
    fun del_manage(customer_id:Int) {
        val params = RequestParams()
        params.put("customer_id", customer_id)
        CompanyAction.del_manage(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        Toast.makeText(myContext,"삭제되었습니다.", Toast.LENGTH_SHORT).show()

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
    //사업체 정보수정
    fun edit_info() {
        val company_name = Utils.getString(compNameTV)
        var phone1:String =  Utils.getString(phoneNum1ET)
        var phone2:String =  Utils.getString(phoneNum2ET)
        var phone3:String =  Utils.getString(phoneNum3ET)
        var login_id:String =  Utils.getString(compIdET)


        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("company_name",company_name)
        params.put("phone1",phone1)
        params.put("phone2",phone2)
        params.put("phone3",phone3)
        params.put("login_id",login_id)

//        var t_pass = Utils.getString(tempPasswordET)
//        var n_pass = Utils.getString(newPasswordET)
//        var n_pass2 = Utils.getString(newPassCheckET)
//
//        params.put("passwd",passwd)


        CompanyAction.edit_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        Toast.makeText(myContext,"수정완료", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(myContext,"수정실패", Toast.LENGTH_SHORT).show()

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
    //사업체 비밀번호변경
    fun edit_pass() {
        val params = RequestParams()
        params.put("company_id", company_id)
        var n_pass2 = Utils.getString(newPassCheckET)
        params.put("passwd",n_pass2)
        CompanyAction.edit_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        Toast.makeText(myContext,"변경완료", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(myContext,"변경실패", Toast.LENGTH_SHORT).show()

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
    //사업체 이미지 업데이트및 삭제
    fun edit_image() {

        val params = RequestParams()
        params.put("company_id", company_id)
        if (delids.size>0){

            for (i in 0..(delids.size -1)){
                val delimg = delids[i]
                //배열로 입력저장은 [] 이걸 넣어준다
                params.put("del_ids["+i+"]",delimg)
                Log.d("삭제번호",delimg.toString())

            }

        }

            //비트맵배열의 크기만큼
//        if (addImages.size > 0){
//            for(i in 0..(addImages.size - 1)) {
//                val byteArrayInputStream = ByteArrayInputStream(Utils.getByteArray(addImages[i]))
//                params.put("upload[" + i + "]", byteArrayInputStream)
//                Log.d("바이트썸네",byteArrayInputStream.toString())
//            }
//        }

        var seq = 0;

        for (i in 0 until userLL.childCount) {
            val v = userLL.getChildAt(i)
            Log.d("브이",v.toString())
            val imagV = v.findViewById<ImageView>(R.id.c_imgIV)
            if (imagV is ImageView) {
                val bitmap = imagV.drawable as BitmapDrawable
                params.put("upload[$i]", ByteArrayInputStream(Utils.getByteArray(bitmap.bitmap)))
                Log.d("넓이",bitmap.bitmap.width.toString())
                Log.d("높이",bitmap.bitmap.height.toString())
                Log.d("브이",i.toString())
                seq++
            }
        }


        CompanyAction.edit_image(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        addImages.clear()
                        company_info(company_id)

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


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }



}
