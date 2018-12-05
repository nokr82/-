package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.devstories.aninuriandroid.adapter.UserVisitAdapter
import com.devstories.anipointcompany.android.R
import kotlinx.android.synthetic.main.activity_user_list.*
//업체 메인화면 액티비티
class UserListActivity : FragmentActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null
    var data = arrayListOf<Int>()


    lateinit var uservisitadapter: UserVisitAdapter


    val User_List_Fragment : User_List_Fragment = User_List_Fragment()
    val User_visit_List_Fragment : User_visit_List_Fragment = User_visit_List_Fragment()
    val Message_Manage_Fragment : Message_Manage_Fragment = Message_Manage_Fragment()
    val Point_List_Fragment : Point_List_Fragment = Point_List_Fragment()
    val SettingFragment : SettingFragment = SettingFragment()
    val Sales_Analysis_List_Fragment : Sales_Analysis_List_Fragment = Sales_Analysis_List_Fragment()

    //고객리스트 =>메시지보내기
    internal var MsgReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setmenu()
                var id =   intent.getStringExtra("member_id")
                messageLL.setBackgroundResource(R.drawable.background_strock_707070)
                supportFragmentManager.beginTransaction().replace(R.id.userFL, Message_Manage_Fragment).commit()
                //브로드캐스트생성
                var intent = Intent()
                intent.putExtra("member_id", id)
                intent.action = "MSG_NEXT"
                context.sendBroadcast(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        this.context = this
        progressDialog = ProgressDialog(context)


        //메시지보내기
        var filter = IntentFilter("MSG_NEXT")
        context.registerReceiver(MsgReceiver, filter)



        userLL.setBackgroundResource(R.drawable.background_strock_707070)
        supportFragmentManager.beginTransaction().replace(R.id.userFL, User_List_Fragment).commit()
        userLL.setOnClickListener {
            setmenu()
            userLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, User_List_Fragment).commit()
            destroy()
        }
        pointLL.setOnClickListener {
            setmenu()
            pointLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, Point_List_Fragment).commit()
            destroy()
        }
        uservisitLL.setOnClickListener {
            setmenu()
            uservisitLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, User_visit_List_Fragment).commit()
            destroy()
        }
        messageLL.setOnClickListener {
            setmenu()
            messageLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, Message_Manage_Fragment).commit()
        }
        analysisRevenueLL.setOnClickListener {
            setmenu()
            analysisRevenueLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, Sales_Analysis_List_Fragment).commit()
            destroy()
        }
        settingLL.setOnClickListener {
            setmenu()
            settingLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, SettingFragment).commit()
            destroy()
        }

    }

    fun destroy(){
        try {
            if(null != MsgReceiver) {
                unregisterReceiver(MsgReceiver)
            }

        } catch (e: IllegalArgumentException) {
        }

    }



    fun setmenu(){
        pointLL.setBackgroundResource(R.drawable.background_strock_null)
        userLL.setBackgroundResource(R.drawable.background_strock_null)
        messageLL.setBackgroundResource(R.drawable.background_strock_null)
        uservisitLL.setBackgroundResource(R.drawable.background_strock_null)
        analysisRevenueLL.setBackgroundResource(R.drawable.background_strock_null)
        settingLL.setBackgroundResource(R.drawable.background_strock_null)
    }





    override fun onDestroy() {
            super.onDestroy()
            if (progressDialog != null) {
                progressDialog!!.dismiss()
            }
            try {
            if(null != MsgReceiver) {
                unregisterReceiver(MsgReceiver)
            }

        } catch (e: IllegalArgumentException) {
        }


    }


    }


