package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_edit_per.*
import android.content.Intent
import com.devstories.anipointcompany.android.base.Utils


//회원수정 다이얼로그
class DlgTestMsgActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_test_msg)

        this.context = this
        progressDialog = ProgressDialog(context)




    }



    override fun onDestroy() {
        super.onDestroy()

        progressDialog = null

    }


}
