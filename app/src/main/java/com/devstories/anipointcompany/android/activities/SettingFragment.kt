package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.PrefUtils
import com.devstories.anipointcompany.android.base.Utils
import kotlinx.android.synthetic.main.fragment_setting.*

//메인설정
class SettingFragment : android.support.v4.app.Fragment() {

    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null
    lateinit var myInfoTV: TextView
    lateinit var operPolicyTV: TextView




    lateinit var accumulateLL: LinearLayout
    lateinit var useLL: LinearLayout
    

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myInfoTV = view.findViewById(R.id.myInfoTV)
        operPolicyTV = view.findViewById(R.id.operPolicyTV)

        accumulateLL = view.findViewById(R.id.accumulateLL)
        useLL = view.findViewById(R.id.useLL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val SettingMyInfoFragment: SettingMyInfoFragment = SettingMyInfoFragment()
        val OperPolicyFragment: OperPolicyFragment = OperPolicyFragment()

        Utils.getViewHeight(msLL,object : Utils.OnHeightSetListener {
            override fun sized(width: Int, height: Int) {
                val lps = msLL.getLayoutParams()
                lps.width = width
                lps.height = height
                msLL.setLayoutParams(lps)
            }
        }

        )




        useLL.setOnClickListener {
            if ( PrefUtils.getStringPreference(myContext, "only_yn")=="Y"){
                val intent = Intent(myContext, OnlyCalActivity::class.java)
                if (PrefUtils.getStringPreference(myContext, "self_yn")=="Y"){
                    intent.putExtra("self_yn", 1)
                }
                intent.putExtra("step", 4)
                startActivity(intent)
            }else{
                val intent = Intent(myContext, CalActivity::class.java)
                if (PrefUtils.getStringPreference(myContext, "self_yn")=="Y"){
                    intent.putExtra("self_yn", 1)
                }
                intent.putExtra("step", 4)
                startActivity(intent)
            }
        }
        accumulateLL.setOnClickListener {
            if ( PrefUtils.getStringPreference(myContext, "only_yn")=="Y"){
                val intent = Intent(myContext, OnlyCalActivity::class.java)
                if (PrefUtils.getStringPreference(myContext, "self_yn")=="Y"){
                    intent.putExtra("self_yn", 1)
                }
                startActivity(intent)
            }else{
                val intent = Intent(myContext, CalActivity::class.java)
                if (PrefUtils.getStringPreference(myContext, "self_yn")=="Y"){
                    intent.putExtra("self_yn", 1)
                }
                startActivity(intent)
            }
        }
        noLL.setOnClickListener {
            if ( PrefUtils.getStringPreference(myContext, "only_yn")=="Y"){
                val intent = Intent(myContext, OnlyCalActivity::class.java)
                intent.putExtra("no_stack", 1)
                startActivity(intent)
            }else{
                val intent = Intent(myContext, CalActivity::class.java)
                intent.putExtra("no_stack", 1)
                startActivity(intent)
            }
        }


        myInfoTV.setOnClickListener {
            //프레그먼트에서 프래그먼트선언하기
            setmenu()
            myInfoTV.setTextColor(Color.parseColor("#ffffff"))
            childFragmentManager.beginTransaction().replace(R.id.settingFL, SettingMyInfoFragment).commit()
        }
        myInfoTV.callOnClick()

        operPolicyTV.setOnClickListener {
            //프레그먼트에서 프래그먼트선언하기
            setmenu()
            operPolicyTV.setTextColor(Color.parseColor("#ffffff"))
            childFragmentManager.beginTransaction().replace(R.id.settingFL, OperPolicyFragment).commit()
        }


    }

    fun setmenu() {
        myInfoTV.setTextColor(Color.parseColor("#80ffffff"))
        operPolicyTV.setTextColor(Color.parseColor("#80ffffff"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}
