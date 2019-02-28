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
import com.devstories.anipointcompany.android.base.CustomProgressDialog

//고객방문분석메인
class User_visit_List_Fragment : Fragment() {
    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null
    lateinit var visitFL: FrameLayout
    lateinit var allLL: LinearLayout
    lateinit var ageLL: LinearLayout
    lateinit var daysLL: LinearLayout
    lateinit var timeLL: LinearLayout

    lateinit var allTV: TextView
    lateinit var ageTV: TextView
    lateinit var daysTV: TextView
    lateinit var timeTV: TextView
    lateinit var accumulateLL : LinearLayout
    lateinit var useLL : LinearLayout
    val User_visit_Select1_Fragment : User_visit_Select1_Fragment = User_visit_Select1_Fragment()
    val User_visit_Select2_Fragment : User_visit_Select2_Fragment = User_visit_Select2_Fragment()
    val User_visit_Select3_Fragment : User_visit_Select3_Fragment = User_visit_Select3_Fragment()
    val User_visit_Select4_Fragment : User_visit_Select4_Fragment = User_visit_Select4_Fragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fra_user_visit_analysis, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ageLL = view.findViewById(R.id.ageLL)
        daysLL= view.findViewById(R.id.daysLL)
        timeLL= view.findViewById(R.id.timeLL)
        allLL = view.findViewById(R.id.allLL)

        visitFL= view.findViewById(R.id.visitFL)
        ageTV = view.findViewById(R.id.ageTV)
        daysTV= view.findViewById(R.id.daysTV)
        timeTV= view.findViewById(R.id.timeTV)
        allTV= view.findViewById(R.id.allTV)

        accumulateLL = view.findViewById(R.id.accumulateLL)
        useLL = view.findViewById(R.id.useLL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        click()
        allLL.callOnClick()



    }



    fun click(){


        useLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            intent.putExtra("step",4)
            startActivity(intent)
        }
        accumulateLL.setOnClickListener {
            val intent = Intent(myContext, CalActivity::class.java)
            startActivity(intent)
        }

        allLL.setOnClickListener {
            setView()
            allTV.setTextColor(Color.parseColor("#FFFFFF"))
            childFragmentManager.beginTransaction().replace(R.id.visitFL, User_visit_Select1_Fragment).commit()
        }
        daysLL.setOnClickListener {
            setView()
            daysTV.setTextColor(Color.parseColor("#FFFFFF"))
            childFragmentManager.beginTransaction().replace(R.id.visitFL, User_visit_Select2_Fragment).commit()
        }
        timeLL.setOnClickListener {
            setView()
            timeTV.setTextColor(Color.parseColor("#FFFFFF"))
            childFragmentManager.beginTransaction().replace(R.id.visitFL, User_visit_Select3_Fragment).commit()
        }
        ageLL.setOnClickListener {
            setView()
            ageTV.setTextColor(Color.parseColor("#FFFFFF"))
            childFragmentManager.beginTransaction().replace(R.id.visitFL, User_visit_Select4_Fragment).commit()
        }
    }


    fun setView(){
        allTV.setTextColor(Color.parseColor("#80FFFFFF"))
        ageTV.setTextColor(Color.parseColor("#80FFFFFF"))
        daysTV.setTextColor(Color.parseColor("#80FFFFFF"))
        timeTV.setTextColor(Color.parseColor("#80FFFFFF"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}
