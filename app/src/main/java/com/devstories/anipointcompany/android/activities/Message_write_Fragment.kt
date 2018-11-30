package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.devstories.anipointcompany.android.R

class Message_write_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var userRL: RelativeLayout
    lateinit var couponRL: RelativeLayout
    lateinit var writeRL: RelativeLayout
    lateinit var finalRL: RelativeLayout
    lateinit var userchoiceFL: FrameLayout



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        return inflater.inflate(R.layout.fra_message_userchoice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRL = view.findViewById(R.id.userRL)
        couponRL = view.findViewById(R.id.couponRL)
        writeRL = view.findViewById(R.id.writeRL)
        finalRL = view.findViewById(R.id.finalRL)
        userchoiceFL = view.findViewById(R.id.userchoiceFL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val MessageUserFragment : MessageUserFragment = MessageUserFragment()

        childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MessageUserFragment).commit()

        userRL.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MessageUserFragment).commit()
        }



    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}
