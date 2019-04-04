package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity
import kotlinx.android.synthetic.main.dlg_edit_per.*
import android.content.Intent
import android.view.View
import android.view.WindowManager
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import com.devstories.anipointcompany.android.base.Utils


//회원수정 다이얼로그
class DlgEditPerActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: CustomProgressDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigations(this)
        setContentView(R.layout.dlg_edit_per)

        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)


        cal()

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)



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
    //계산클릭이벤트
    fun cal(){

        oneLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 1)
        }
        twoLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 2)
        }
        threeLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 3)
        }
        fourLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 4)
        }
        fiveLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 5)
        }
        sixLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 6)
        }
        sevenLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 7)
        }
        eightLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 8)
        }
        nineLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 9)
        }
        zeroLL.setOnClickListener {
            moneyTV.setText(moneyTV.getText().toString() + 0)
        }
        delLL.setOnClickListener {
            val text = moneyTV.getText().toString()
            if (text.length > 0){
                moneyTV.setText(text.substring(0, text.length - 1))
            }else{

            }
        }

        useLL.setOnClickListener {
            setpoint()
        }


    }

fun setpoint(){

    var money = Utils.getInt(moneyTV)
    if (money==-1){
        money=0
    }


    val resultIntent = Intent()
    resultIntent.putExtra("point",money)
    setResult(RESULT_OK, resultIntent)
    finish()
}


    override fun onDestroy() {
        super.onDestroy()

        progressDialog = null

    }


}
