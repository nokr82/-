package com.devstories.anipointcompany.android.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import android.view.View
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.devstories.anipointcompany.android.Actions.CompanyAction
import com.devstories.anipointcompany.android.base.*
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_sign.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream


//회원수정 다이얼로그
class DlgSignActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: CustomProgressDialog? = null

    private val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
    var ShareUri: Uri? = null
    private var mBitmap: Bitmap? = null
    private var mPaint: Paint? = null
    private var signDV: DrawingView? = null
    private var mNewBitmap: Bitmap? = null
    private var bitmap:Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigations(this)
        setContentView(R.layout.dlg_sign)

        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)



        mPaint = Paint()
        mPaint!!.setAntiAlias(true)
        mPaint!!.setDither(true)
        mPaint!!.setColor(Color.BLACK)
        mPaint!!.setStyle(Paint.Style.STROKE)
        mPaint!!.setStrokeJoin(Paint.Join.ROUND)
        mPaint!!.setStrokeCap(Paint.Cap.ROUND)
        mPaint!!.setStrokeWidth(10f)


        signDV = DrawingView(context)
        signRL.addView(signDV)
        resignTV.setOnClickListener(View.OnClickListener {
            signDV!!.clearCanvas()
            hintSignTV.visibility = View.VISIBLE
        })
        writeTV.setOnClickListener {
            checkPermissions()
        }

    }

    fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            loadPermissions(perms, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)
        } else {
            confirm()
        }
    }

    fun confirm() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("서명을 하시겠습니까?")
        builder.setCancelable(true)
        builder.setNegativeButton("취소") { dialog, id -> dialog.cancel() }
        builder.setPositiveButton("전송") { dialog, id ->
            dialog.cancel()
            save()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun loadPermissions(perms: Array<String>, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, perms[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, perms, requestCode)
        } else {
            confirm()
        }
    }
    inner class DrawingView(internal var context: Context) : View(context) {

        private var mCanvas: Canvas? = null
        private val mPath: Path
        private val mBitmapPaint: Paint
        private val circlePaint: Paint
        private val circlePath: Path

        private var mX: Float = 0.toFloat()
        private var mY: Float = 0.toFloat()

        init {
            mPath = Path()

            mBitmapPaint = Paint()
            // mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            // mBitmapPaint.setColor(Color.TRANSPARENT);

            circlePaint = Paint()
            circlePath = Path()
            circlePaint.isAntiAlias = true
            circlePaint.color = Color.BLUE
            circlePaint.style = Paint.Style.STROKE
            circlePaint.strokeJoin = Paint.Join.MITER
            circlePaint.strokeWidth = 4f
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            // mBitmap.eraseColor(Color.WHITE);
            // mBitmap.eraseColor(Color.TRANSPARENT);

            mNewBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            mCanvas = Canvas(mBitmap)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
            canvas.drawPath(mPath, mPaint)
            canvas.drawPath(circlePath, circlePaint)
        }

        private fun touch_start(x: Float, y: Float) {
            mPath.reset()
            mPath.moveTo(x, y)
            mX = x
            mY = y
        }

        private fun touch_move(x: Float, y: Float) {
            val dx = Math.abs(x - mX)
            val dy = Math.abs(y - mY)
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
                mX = x
                mY = y

                circlePath.reset()
                circlePath.addCircle(mX, mY, 30f, Path.Direction.CW)
            }
        }

        private fun touch_up() {
            mPath.lineTo(mX, mY)
            circlePath.reset()
            // commit the path to our offscreen
            mCanvas!!.drawPath(mPath, mPaint)
            // kill this so we don't double draw
            mPath.reset()
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touch_start(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    hintSignTV.visibility = View.GONE
                    touch_move(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    touch_up()
                    invalidate()
                }
            }
            return true
        }

        fun clearCanvas() {
            mCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            mCanvas!!.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
            mCanvas!!.drawPath(mPath, mPaint)
            mCanvas!!.drawPath(circlePath, circlePaint)

            invalidate()
            System.gc()
        }
        private val TOUCH_TOLERANCE = 4f

    }

    // 서명저장
    private fun save() {
        signRL.setBackgroundColor(Color.TRANSPARENT)
        signRL.drawingCacheBackgroundColor = Color.TRANSPARENT
        signRL.isDrawingCacheEnabled = true
        signRL.buildDrawingCache(true)

        bitmap = signRL.drawingCache
        bitmap!!.setHasAlpha(true)
        val params = RequestParams()
        params.put("company_id",PrefUtils.getIntPreference(context,"company_id"))

        Log.d("결과",bitmap.toString())

        if (bitmap != null) {
            params.put("upfile", ByteArrayInputStream(Utils.getByteArray(bitmap)))

            CompanyAction.sign_save(params, object : JsonHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }
                    try {
                        val result = response!!.getString("result")

                        Log.d("결과",response.toString())

                        if ("ok" == result) {
                            var intent = Intent()
                            intent.putExtra("contract_id", response!!.getInt("contract_id"))
                            intent.action = "SIGNUP"
                            sendBroadcast(intent)
                            finish()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                }

                override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                    // System.out.println.println.println.println(responseString);
                }

                private fun error() {
                    Utils.alert(context, "조회중 장애가 발생하였습니다.")
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }

                    // System.out.println.println.println.println(responseString);

                    throwable.printStackTrace()
                    error()
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }
                    throwable.printStackTrace()
                    error()
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                    if (progressDialog != null) {
                        progressDialog!!.dismiss()
                    }
                    throwable.printStackTrace()
                    error()
                }

                override fun onStart() {
                    // show dialog
                    if (progressDialog != null) {
                        progressDialog!!.setMessage("처리중...")
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
    fun hideNavigations(context: Activity) {
        val decorView = context.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }


    override fun onDestroy() {
        super.onDestroy()

        progressDialog = null

    }


}
