package com.devstories.anipointcompany.android.activities

import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.RootActivity


class IntroActivity : RootActivity() {

    protected var _splashTime = 2000 // time to display the splash screen in ms
    private val _active = true
    private var splashThread: Thread? = null

    private var progressDialog: ProgressDialog? = null

    private var context: Context? = null

    private var is_push:Boolean = false

    val SHOW_DLG = 301

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        this.context = this
        progressDialog = ProgressDialog(context)

        // clear all notification
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancelAll()

        val buldle = intent.extras
        if (buldle != null) {
            try {
                is_push = buldle.getBoolean("FROM_PUSH")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        splashThread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    while (waited < _splashTime && _active) {
                        Thread.sleep(100)
                        waited += 100
                    }
                } catch (e: InterruptedException) {

                } finally {
                    stopIntro()
                }
            }
        }
        (splashThread as Thread).start()

    }

    private fun stopIntro() {

            handler.sendEmptyMessage(0)

    }

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            //versionInfo();

        }
    }



}
