package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.devstories.aninuriandroid.adapter.UserVisitAdapter
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.Utils
import kotlinx.android.synthetic.main.activity_user_list.*
//업체 메인화면 액티비티
class UserListActivity : FragmentActivity() {

    lateinit var context:Context

    private var progressDialog: CustomProgressDialog? = null
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
                var id =   intent.getIntExtra("member_id",-1)
                var args:Bundle = Bundle()
                args.putInt("member_id", id)
                Log.d("멤버아이디", id.toString())
                Message_Manage_Fragment.setArguments(args)
                messageLL.setBackgroundResource(R.drawable.background_strock_707070)
                supportFragmentManager.beginTransaction().replace(R.id.userFL, Message_Manage_Fragment).commit()

//                //브로드캐스트생성
//                var intent = Intent()
//                intent.putExtra("member_id", id)
//                intent.action = "MSG_NEXT"
//                context.sendBroadcast(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        hideNavigations(this)
        setContentView(R.layout.activity_user_list)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        // showSystemUI()
        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        //메시지보내기
        var filter = IntentFilter("MSG_NEXT")
        context.registerReceiver(MsgReceiver, filter)



        userLL.setBackgroundResource(R.drawable.background_strock_707070)
        supportFragmentManager.beginTransaction().replace(R.id.userFL, User_List_Fragment).commit()
        userLL.setOnClickListener {
            setmenu()
            Utils.hideKeyboard(this)
            userLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, User_List_Fragment).commit()

        }
        pointLL.setOnClickListener {
            setmenu()
            Utils.hideKeyboard(this)
            pointLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, Point_List_Fragment).commit()

        }
        uservisitLL.setOnClickListener {
            setmenu()
            Utils.hideKeyboard(this)
            uservisitLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, User_visit_List_Fragment).commit()

        }
        messageLL.setOnClickListener {
            setmenu()
            Utils.hideKeyboard(this)
            messageLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, Message_Manage_Fragment).commit()
        }
        analysisRevenueLL.setOnClickListener {
            setmenu()
            Utils.hideKeyboard(this)
            analysisRevenueLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, Sales_Analysis_List_Fragment).commit()

        }
        settingLL.setOnClickListener {
            setmenu()
            Utils.hideKeyboard(this)
            settingLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, SettingFragment).commit()

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


    override fun onResume() {
        super.onResume()
        hideNavigations(this)
    }



    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
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


