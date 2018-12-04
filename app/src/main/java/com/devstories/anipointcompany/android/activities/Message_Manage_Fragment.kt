package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R

//메시지관리  - 고객선택
class Message_Manage_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null


    lateinit var messageStatisticsLL: LinearLayout
    lateinit var sendMessageLL: LinearLayout
    lateinit var autoCouponLL: LinearLayout

    lateinit var messageStatisticsTV: TextView
    lateinit var sendMessageTV: TextView
    lateinit var autoCouponTV: TextView

    lateinit var view1: View
    lateinit var view2: View
    lateinit var view3: View

    lateinit var accumulateLL : LinearLayout
    lateinit var useLL : LinearLayout

    lateinit var messageFL: FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

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

        view1 = view.findViewById(R.id.view1)
        view2 = view.findViewById(R.id.view2)
        view3 = view.findViewById(R.id.view3)

        messageFL = view.findViewById(R.id.messageFL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val MessageUserFragment : MessageUserFragment = MessageUserFragment()
        val Message_write_Fragment : Message_write_Fragment = Message_write_Fragment()
        val AutoCouponSettingsFragment : AutoCouponSettingsFragment = AutoCouponSettingsFragment()

        childFragmentManager.beginTransaction().replace(R.id.messageFL, Message_write_Fragment).commit()


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
            view1.visibility = View.VISIBLE
            messageStatisticsTV.setTextColor(Color.parseColor("#FFFFFF"))
        }

        // 메세지 작성
        sendMessageLL .setOnClickListener {
            setView()
            view2.visibility = View.VISIBLE
            sendMessageTV.setTextColor(Color.parseColor("#FFFFFF"))

            childFragmentManager.beginTransaction().replace(R.id.messageFL, Message_write_Fragment).commit()
        }

        // 자동 쿠폰 설정
        autoCouponLL.setOnClickListener {
            setView()
            view3.visibility = View.VISIBLE
            autoCouponTV.setTextColor(Color.parseColor("#FFFFFF"))

            childFragmentManager.beginTransaction().replace(R.id.messageFL, AutoCouponSettingsFragment).commit()
        }

    }

    fun setView(){
        view1.visibility = View.INVISIBLE
        view2.visibility = View.INVISIBLE
        view3.visibility = View.INVISIBLE

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
