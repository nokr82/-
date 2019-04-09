package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.Config
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.RootActivity
import kotlinx.android.synthetic.main.activity_contract_view.*

class ContractViewActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog:  CustomProgressDialog? = null

    var cont_id = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigations(this)
        setContentView(R.layout.activity_contract_view)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        cont_id = intent.getIntExtra("cont_id",-1)


        val url = Config.url + "/company/contract_view?contract_id=" + cont_id

        webWV.settings.javaScriptEnabled = true
        webWV.loadUrl(url)


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
        progressDialog?.dismiss()
        progressDialog = null
    }
}
