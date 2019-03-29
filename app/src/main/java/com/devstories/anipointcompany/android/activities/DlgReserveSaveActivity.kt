package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.ArrayAdapter
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import kotlinx.android.synthetic.main.dlg_reserve_save.*


//회원수정 다이얼로그
class DlgReserveSaveActivity : FragmentActivity() {

    lateinit var context: Context
    private var progressDialog: CustomProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>


    var member_id = -1
    var company_id = -1
    var age = ""

    val ReserveMemberListFragment : ReserveMemberListFragment = ReserveMemberListFragment()
    val ReserveNewMemberFragment : ReserveNewMemberFragment = ReserveNewMemberFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigations(this)
        setContentView(R.layout.dlg_reserve_save)

        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)



        memberLL.setBackgroundResource(R.drawable.background_strock_707070)
        supportFragmentManager.beginTransaction().replace(R.id.reserveFL, ReserveMemberListFragment).commit()

        backIV.setOnClickListener {
            finish()
        }


        newmemberLL.setOnClickListener {
            setmenu()
            newmemberTV.setTextColor(Color.parseColor("#000000"))
            newmemberLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.reserveFL, ReserveNewMemberFragment).commit()
        }
        memberLL.setOnClickListener {
            setmenu()
            memberTV.setTextColor(Color.parseColor("#000000"))
            memberLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.reserveFL, ReserveMemberListFragment).commit()
        }

    }

    fun setmenu(){
        newmemberTV.setTextColor(Color.parseColor("#c9c9c9"))
        memberTV.setTextColor(Color.parseColor("#c9c9c9"))
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
