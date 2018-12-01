package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
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

        val MessageUserFragment : MessageUserFragment = MessageUserFragment()
        val SetCouponFragment : SetCouponFragment = SetCouponFragment()
        val MssgAnalysisFragment : MssgAnalysisFragment = MssgAnalysisFragment()


        setfilter()

        userRL.setOnClickListener {
            setfilter()
            userRL.setBackgroundColor(Color.parseColor("#0068df"))
            userTV.setTextColor(Color.parseColor("#ffffff"))
            childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MessageUserFragment).commit()
        }
        couponRL.setOnClickListener {
            setfilter()
            couponRL.setBackgroundColor(Color.parseColor("#0068df"))
            couponTV.setTextColor(Color.parseColor("#ffffff"))
            childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, SetCouponFragment).commit()
        }
        writeRL.setOnClickListener {
            setfilter()
            writeRL.setBackgroundColor(Color.parseColor("#0068df"))
            writeTV.setTextColor(Color.parseColor("#ffffff"))
            childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MssgAnalysisFragment).commit()
        }
        finalRL.setOnClickListener {
            setfilter()
            finalRL.setBackgroundColor(Color.parseColor("#0068df"))
            finalTV.setTextColor(Color.parseColor("#ffffff"))
            childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MessageUserFragment).commit()
        }

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
