package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity
import kotlinx.android.synthetic.main.activity_point.*

class PointActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)

        this.context = this
        progressDialog = ProgressDialog(context)

        titleTV.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)

        }


        }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


    }


