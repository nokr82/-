package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.activities.CouponFragment
import kotlinx.android.synthetic.main.activity_coupon_use.*

class UseActivity :  FragmentActivity() {
    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_use)

        this.context = this
        progressDialog = ProgressDialog(context)

        val UseFragment : UseFragment = UseFragment()
        val CouponFragment : CouponFragment = CouponFragment()


        useLL.setBackgroundResource(R.drawable.background_strock_707070)
        supportFragmentManager.beginTransaction().replace(R.id.main_frame, UseFragment).commit()
        use_op_LL.visibility = View.VISIBLE



        useLL.setOnClickListener {
            setmenu()
            phonET.setHint("휴대전화번호를 눌러주세요")
            titleTV.text = "사용/조회"
            use_op_LL.visibility = View.VISIBLE
            useLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, UseFragment).commit()
        }

        couponLL.setOnClickListener {
            setmenu()
            phonET.setHint("사용할 포인트를 입력하세요.")
            titleTV.text = "쿠폰/포인트\n조회"
            use_op_LL.visibility = View.GONE
            couponLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, CouponFragment).commit()
        }


        }

    fun setmenu(){
        couponLL.setBackgroundResource(R.drawable.background_strock_null)
        useLL.setBackgroundResource(R.drawable.background_strock_null)
    }



    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


    }


