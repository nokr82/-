package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import kotlinx.android.synthetic.main.fra_message_manage.*

//메시지관리  - 고객선택
class Message_Manage_Fragment : Fragment() {
    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null


    lateinit var messageStatisticsLL: LinearLayout
    lateinit var sendMessageLL: LinearLayout
    lateinit var autoCouponLL: LinearLayout

    lateinit var messageStatisticsTV: TextView
    lateinit var sendMessageTV: TextView
    lateinit var autoCouponTV: TextView



    lateinit var accumulateLL : LinearLayout
    lateinit var useLL : LinearLayout




    lateinit var messageFL: FrameLayout
    val MssgAnalysisFragment : MssgAnalysisFragment = MssgAnalysisFragment()
    val Message_write_Fragment : Message_write_Fragment = Message_write_Fragment()
    val AutoCouponSettingsFragment : AutoCouponSettingsFragment = AutoCouponSettingsFragment()

    var company_id = -1
    var member_id = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
            return inflater.inflate(R.layout.fra_message_manage,container,false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageStatisticsLL = view.findViewById(R.id.messageStatisticsLL)
        sendMessageLL = view.findViewById(R.id.sendMessageLL)
        autoCouponLL = view.findViewById(R.id.autoCouponLL)

        messageStatisticsTV = view.findViewById(R.id.messageStatisticsTV)
        sendMessageTV = view.findViewById(R.id.sendMessageTV)
        autoCouponTV = view.findViewById(R.id.autoCouponTV)

        accumulateLL = view.findViewById(R.id.accumulateLL)
        useLL = view.findViewById(R.id.useLL)



        messageFL = view.findViewById(R.id.messageFL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        company_id = PrefUtils.getIntPreference(context, "company_id")



        Utils.getViewHeight(messageFL,object : Utils.OnHeightSetListener {
            override fun sized(width: Int, height: Int) {
                val lps = messageFL.getLayoutParams()
                lps.height = height
                lps.width = width
                Log.d("너비",width.toString())
                messageFL.setLayoutParams(lps)
            }
        }
        )


       childFragmentManager.beginTransaction().replace(R.id.messageFL, MssgAnalysisFragment).commit()

        if (arguments != null) {
            member_id = getArguments()!!.getInt("member_id")
            if (member_id!=-1){
                setView()

                sendMessageTV.setTextColor(Color.parseColor("#FFFFFF"))
                var args:Bundle = Bundle()
                args.putInt("member_id", member_id)
                Message_write_Fragment.setArguments(args)
                childFragmentManager.beginTransaction().replace(R.id.messageFL, Message_write_Fragment).commit()
            }
            arguments = null
        }
        noLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("no_stack", 1)
            startActivity(intent)
        }
        useLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("step",4)
            startActivity(intent)
        }
        accumulateLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            startActivity(intent)
        }


        // 메세지 통계
        messageStatisticsLL.setOnClickListener {
            setView()

            messageStatisticsTV.setTextColor(Color.parseColor("#FFFFFF"))

            childFragmentManager.beginTransaction().replace(R.id.messageFL, MssgAnalysisFragment).commit()
        }

        // 메세지 작성
        sendMessageLL .setOnClickListener {
            setView()

            sendMessageTV.setTextColor(Color.parseColor("#FFFFFF"))

            childFragmentManager.beginTransaction().replace(R.id.messageFL, Message_write_Fragment).commit()
        }

        // 자동 쿠폰 설정
        autoCouponLL.setOnClickListener {
            setView()

            autoCouponTV.setTextColor(Color.parseColor("#FFFFFF"))

            childFragmentManager.beginTransaction().replace(R.id.messageFL, AutoCouponSettingsFragment).commit()
        }

    }

    fun setView(){
        messageStatisticsTV.setTextColor(Color.parseColor("#80FFFFFF"))
        sendMessageTV.setTextColor(Color.parseColor("#80FFFFFF"))
        autoCouponTV.setTextColor(Color.parseColor("#80FFFFFF"))

    }



    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


}
