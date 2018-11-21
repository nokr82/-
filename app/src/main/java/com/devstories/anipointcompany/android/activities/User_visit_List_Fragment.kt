package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R


class User_visit_List_Fragment : Fragment() {
    lateinit var myContext: Context

    lateinit var adapter: ArrayAdapter<String>
    var option_amount = arrayOf("5개씩 보기","10개씩 보기")


    lateinit var amountSP: Spinner


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
            return inflater.inflate(R.layout.fra_user_visit_analysis,container,false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amountSP = view.findViewById(R.id.amountSP)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_amount)
        amountSP.adapter = adapter
    }

}
