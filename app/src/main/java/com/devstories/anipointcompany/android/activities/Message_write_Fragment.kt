package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.devstories.anipointcompany.android.R
//메인메시지관리
class Message_write_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var userRL: RelativeLayout
    lateinit var couponRL: RelativeLayout
    lateinit var writeRL: RelativeLayout
    lateinit var finalRL: RelativeLayout

    lateinit var userTV: TextView
    lateinit var couponTV: TextView
    lateinit var writeTV: TextView
    lateinit var finalTV: TextView

    lateinit var userchoiceFL: FrameLayout
    val MessageUserFragment : MessageUserFragment = MessageUserFragment()
    val SetCouponFragment : SetCouponFragment = SetCouponFragment()
    val MssgAnalysisFragment : MssgAnalysisFragment = MssgAnalysisFragment()
    //고객선택
    internal var step1NextReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                //브로드캐스트로 프래그먼트이동리시버
                println("intent")
                println("intent" + intent.getStringExtra("gender"))
                println("intent" + intent.getStringExtra("age"))
                setfilter()
                userRL.setBackgroundColor(Color.parseColor("#0068df"))
                userTV.setTextColor(Color.parseColor("#ffffff"))
                couponRL.setBackgroundColor(Color.parseColor("#0068df"))
                couponTV.setTextColor(Color.parseColor("#ffffff"))
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, SetCouponFragment).commit()
            }
        }
    }
    //쿠폰설정
    internal var step2NextReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                println("intent")
                couponRL.setBackgroundColor(Color.parseColor("#0068df"))
                couponTV.setTextColor(Color.parseColor("#ffffff"))
                writeRL.setBackgroundColor(Color.parseColor("#0068df"))
                writeTV.setTextColor(Color.parseColor("#ffffff"))
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL,MssgAnalysisFragment).commit()

            }
        }
    }
    //메시지작성
    internal var step3NextReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                println("intent")
                couponRL.setBackgroundColor(Color.parseColor("#0068df"))
                couponTV.setTextColor(Color.parseColor("#ffffff"))
                writeRL.setBackgroundColor(Color.parseColor("#0068df"))
                writeTV.setTextColor(Color.parseColor("#ffffff"))
                finalRL.setBackgroundColor(Color.parseColor("#0068df"))
                finalTV.setTextColor(Color.parseColor("#ffffff"))
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL,MessageUserFragment).commit()

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        return inflater.inflate(R.layout.fra_message_userchoice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRL = view.findViewById(R.id.userRL)
        couponRL = view.findViewById(R.id.couponRL)
        writeRL = view.findViewById(R.id.writeRL)
        finalRL = view.findViewById(R.id.finalRL)

        userTV = view.findViewById(R.id.userTV)
        couponTV = view.findViewById(R.id.couponTV)
        writeTV = view.findViewById(R.id.writeTV)
        finalTV = view.findViewById(R.id.finalTV)

        userchoiceFL = view.findViewById(R.id.userchoiceFL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //고객선택
        var filter1 = IntentFilter("STEP1_NEXT")
        myContext.registerReceiver(step1NextReceiver, filter1)
        //쿠폰설정
        var filter2 = IntentFilter("STEP2_NEXT")
        myContext.registerReceiver(step2NextReceiver, filter2)
        //메시지작성
        var filter3 = IntentFilter("STEP3_NEXT")
        myContext.registerReceiver(step3NextReceiver, filter3)


        setfilter()

        userRL.setBackgroundColor(Color.parseColor("#0068df"))
        userTV.setTextColor(Color.parseColor("#ffffff"))
        childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MessageUserFragment).commit()


    }
    fun setfilter(){
        userRL.setBackgroundResource(R.drawable.background_strock_null)
        couponRL.setBackgroundResource(R.drawable.background_strock_null)
        writeRL.setBackgroundResource(R.drawable.background_strock_null)
        finalRL.setBackgroundResource(R.drawable.background_strock_null)

        userTV.setTextColor(Color.parseColor("#c5c5c5"))
        couponTV.setTextColor(Color.parseColor("#c5c5c5"))
        writeTV.setTextColor(Color.parseColor("#c5c5c5"))
        finalTV.setTextColor(Color.parseColor("#c5c5c5"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}
