package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R


class Message_Manage_Fragment : Fragment() {
    lateinit var myContext: Context

    lateinit var adapter: ArrayAdapter<String>
    var option_visitday = arrayOf("전체", "5일", "2×4", "4×1", "4×2")

    lateinit var visitdaySP: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context


            return inflater.inflate(R.layout.fra_message_manage,container,false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        visitdaySP = view.findViewById(R.id.visitdaySP)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_visitday)
        visitdaySP.adapter = adapter

    }

}
