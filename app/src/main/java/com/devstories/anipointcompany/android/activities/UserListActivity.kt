package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.devstories.aninuriandroid.adapter.UserVisitAdapter
import com.devstories.anipointcompany.android.R
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : FragmentActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null
    var data = arrayListOf<Int>()


    lateinit var uservisitadapter: UserVisitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        this.context = this
        progressDialog = ProgressDialog(context)
        val User_List_Fragment : User_List_Fragment = User_List_Fragment()
        val User_visit_List_Fragment : User_visit_List_Fragment = User_visit_List_Fragment()
        val Message_Manage_Fragment : Message_Manage_Fragment = Message_Manage_Fragment()
        val Point_List_Fragment : Point_List_Fragment = Point_List_Fragment()

        userLL.setBackgroundResource(R.drawable.background_strock_707070)
        useroptionLL.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().replace(R.id.userFL, User_List_Fragment).commit()


        accumulateLL.setOnClickListener {
            val intent = Intent(this, PointActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        userLL.setOnClickListener {
            setmenu()
            titleTV.text = "고객목록"
            useroptionLL.visibility = View.VISIBLE
            userLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.userFL, User_List_Fragment).commit()
        }
        pointLL.setOnClickListener {

            setmenu()
            titleTV.text = "포인트 내역"
            pointLL.setBackgroundResource(R.drawable.background_strock_707070)
            point_op_LL.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.userFL, Point_List_Fragment).commit()
        }
        uservisitLL.setOnClickListener {
            setmenu()
            titleTV.text = "고객 방문\n분석"
            uservisitLL.setBackgroundResource(R.drawable.background_strock_707070)
            uservisit_op_LL.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.userFL, User_visit_List_Fragment).commit()
        }
        messageLL.setOnClickListener {
            setmenu()
            titleTV.text = "메세지/\n쿠폰 관리"
            messageLL.setBackgroundResource(R.drawable.background_strock_707070)
            message_op_LL.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.userFL, Message_Manage_Fragment).commit()
        }

        }


    fun setmenu(){
        pointLL.setBackgroundResource(R.drawable.background_strock_null)
        userLL.setBackgroundResource(R.drawable.background_strock_null)
        messageLL.setBackgroundResource(R.drawable.background_strock_null)
        uservisitLL.setBackgroundResource(R.drawable.background_strock_null)
        useroptionLL.visibility = View.GONE
        message_op_LL.visibility = View.GONE
        point_op_LL.visibility = View.GONE
        uservisit_op_LL.visibility = View.GONE
    }





    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


    }


