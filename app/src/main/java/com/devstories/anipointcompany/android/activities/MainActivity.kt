package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.context = this
        progressDialog = ProgressDialog(context)

        useLL.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)

        }

        couponLL.setOnClickListener {
            val intent = Intent(this, UseActivity::class.java)
            startActivity(intent)
        }


        logoLL.setOnClickListener {

        }


        }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


    }


