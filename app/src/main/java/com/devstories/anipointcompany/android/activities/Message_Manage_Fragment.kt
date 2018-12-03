package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R

//메시지관리  - 고객선택
class Message_Manage_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null


    lateinit var selCustomTV: TextView
    lateinit var setCouponTV: TextView
    lateinit var wrMsgTV: TextView
    lateinit var finalCheckTV: TextView
    lateinit var messageFL: FrameLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

            return inflater.inflate(R.layout.fra_message_manage,container,false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selCustomTV = view.findViewById(R.id.selCustomTV)
        setCouponTV = view.findViewById(R.id.setCouponTV)
        wrMsgTV = view.findViewById(R.id.wrMsgTV)
        finalCheckTV = view.findViewById(R.id.finalCheckTV)
        messageFL = view.findViewById(R.id.messageFL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val MessageUserFragment : MessageUserFragment = MessageUserFragment()
        val Message_write_Fragment : Message_write_Fragment = Message_write_Fragment()

        childFragmentManager.beginTransaction().replace(R.id.messageFL, Message_write_Fragment).commit()

        //고객선텍
        selCustomTV.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.messageFL, Message_write_Fragment).commit()
        }

        //쿠폰설정
        setCouponTV .setOnClickListener {

        }
        //메세지설정
        wrMsgTV.setOnClickListener {

        }
        //최종확인
        finalCheckTV.setOnClickListener {

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }


}
