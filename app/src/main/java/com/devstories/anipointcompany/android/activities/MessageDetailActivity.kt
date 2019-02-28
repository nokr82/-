package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.devstories.anipointcompany.android.Actions.CouponAction
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.RootActivity
import com.devstories.anipointcompany.android.base.Utils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_message_detail.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.data.PieData

// 메세지 통계 상세
class MessageDetailActivity : RootActivity() {

    lateinit var context: Context

    private var progressDialog: CustomProgressDialog? = null

    var company_id = -1

    var message_id = -1

    var type = -1
    var search_type = -1

    var totalMemberCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions
        val isImmersiveModeEnabled = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ")
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.")
        }
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        setContentView(R.layout.activity_message_detail)

        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        hideNavigations(this)

        company_id = PrefUtils.getIntPreference(context, "company_id")

        message_id = intent.getIntExtra("message_id", -1)
        // 1-자동쿠폰 / 2-맞춤메세지
        type = intent.getIntExtra("type", 1)
        search_type = intent.getIntExtra("search_type", 1)

        finishLL.setOnClickListener {
            finish()
        }

        ageBarChart.setDrawBarShadow(false)
        ageBarChart.setDrawValueAboveBar(true)
        ageBarChart.description.isEnabled = false
        ageBarChart.legend.isEnabled = false
        ageBarChart.setMaxVisibleValueCount(100)
        ageBarChart.setPinchZoom(false)
        ageBarChart.setDrawGridBackground(false)
        ageBarChart.setScaleEnabled(false)
        ageBarChart.setTouchEnabled(false)
        ageBarChart.getAxisLeft().setAxisMinimum(0f)
        ageBarChart.getAxisRight().setEnabled(false)
        ageBarChart.getAxisLeft().setValueFormatter(IAxisValueFormatter { value, axis ->
            //                return Double.parseDouble(String.format("%.2f", value)) + "h";
            var val_ = (value / totalMemberCnt) * 100f
            String.format("%.1f", val_)+ "%"
        })


        genderPieChart.setUsePercentValues(true)
        genderPieChart.description.isEnabled = false
        genderPieChart.setExtraOffsets(5f, 10f, 5f, 5f);


        genderPieChart.setDragDecelerationFrictionCoef(0.95f);

//        genderPieChart.setCenterTextTypeface(tfLight);
//        genderPieChart.setCenterText(generateCenterSpannableText());

        genderPieChart.setDrawHoleEnabled(true);
        genderPieChart.setHoleColor(Color.WHITE);

        genderPieChart.setTransparentCircleColor(Color.WHITE);
        genderPieChart.setTransparentCircleAlpha(110);

        genderPieChart.setHoleRadius(58f);
        genderPieChart.setTransparentCircleRadius(61f);

        genderPieChart.setDrawCenterText(true);

        genderPieChart.setRotationAngle(0f);
        // enable rotation of the chart by touch
        genderPieChart.setRotationEnabled(true);
        genderPieChart.setHighlightPerTapEnabled(true);
        genderPieChart.setEntryLabelColor(Color.WHITE);

        loadData()

    }

    override fun onResume() {
        super.onResume()
        hideNavigations(this)
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


    fun loadData() {

        val params = RequestParams()
        params.put("message_id", message_id)
        params.put("company_id", company_id)
        params.put("type", type)
        params.put("search_type", search_type)

        CouponAction.message_detail(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val message_obj = response.getJSONObject("message")
                        val memberMessages = message_obj.getJSONArray("MemberMessages")

                        totalMemberCnt = memberMessages.length()

                        val message = message_obj.getJSONObject("Message")
                        val coupon = message_obj.getJSONObject("Coupon")
                        val search_member = Utils.getString(message, "search_member")
                        val title = Utils.getString(message, "title")
                        val msg = Utils.getString(message, "message")
                        val coupon_name = Utils.getString(coupon, "name")

                        if(search_member == "") {
                            customerTV.text = "고객 " + totalMemberCnt + "명"
                        } else {
                            customerTV.text = search_member + totalMemberCnt + "명"
                        }

                        if(coupon_name == "") {
                            couponTV.text = "-"
                        } else {
                            couponTV.text = coupon_name
                        }

                        createdTV.text = Utils.getString(message, "created")

                        val age = response.getJSONArray("age")
                        val gender = response.getJSONObject("gender")
                        val ages = arrayOf("10대", "20대", "30대", "40대", "50대", "60대+")

                        var xAxis = ageBarChart.getXAxis()
                        xAxis.setTextColor(Color.parseColor("#a0a0a0"))
                        xAxis.setDrawLabels(true)
                        xAxis.setDrawGridLines(false)
                        xAxis.setGranularity(1f) // minimum axis-step (interval) is 1
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                        xAxis.setAvoidFirstLastClipping(true)
                        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
                            // System.out.println("va 0 : " + value);
                            if (value < 0) {
                                return@IAxisValueFormatter ""
                            }
                            if (ages.size > value) {
                                ages[value.toInt()]
                            } else {
                                ""
                            }
                        }


                        var ageData: MutableList<BarEntry> = ArrayList()

                        for (i in 0 until age.length()) {
                            var data = age[i] as JSONObject

                            ageData.add(BarEntry(i.toFloat(), Utils.getInt(data, "count").toFloat()))

                        }

                        var barDataSet = BarDataSet(ageData, "12")
                        barDataSet.setColors(*intArrayOf(Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8"), Color.parseColor("#4b8bc8")))
                        barDataSet.setDrawValues(false)

                        var barData = BarData(barDataSet)
                        barData.setBarWidth(0.1f)


                        ageBarChart.setData(barData)
                        ageBarChart.invalidate() // refresh


                        val entries = ArrayList<PieEntry>()
                        entries.add(PieEntry(Utils.getInt(gender, "Male").toFloat(), 0))
                        entries.add(PieEntry( Utils.getInt(gender, "Female").toFloat(), 1))

                         val dataSet: PieDataSet = PieDataSet(entries, "성 비율");
                        dataSet.setDrawIcons(false);

                        dataSet.sliceSpace = 3f
                        dataSet.iconsOffset = MPPointF(0f, 40f)
                        dataSet.selectionShift = 5f

                        val colors = ArrayList<Int>()
                        colors.add(Color.parseColor("#6799FF"))
                        colors.add(Color.parseColor("#FF00DD"))
                        dataSet.setColors(colors);

                        val data = PieData(dataSet)
                        data.setValueTextSize(11f);
                        data.setValueTextColor(Color.WHITE);

                        genderPieChart.setData(data);
                        genderPieChart.highlightValues(null);
                        genderPieChart.invalidate()

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
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

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


