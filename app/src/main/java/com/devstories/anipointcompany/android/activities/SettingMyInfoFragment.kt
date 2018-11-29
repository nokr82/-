package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.devstories.anipointcompany.android.R

class SettingMyInfoFragment : Fragment() {

    lateinit var myContext: Context

    lateinit var compNameET: EditText
    lateinit var phoneNum1ET: EditText
    lateinit var termET: EditText
    lateinit var compIdET: EditText
    lateinit var addImage1RL: EditText
    lateinit var addImage2RL: EditText
    lateinit var addImage3RL: EditText
    lateinit var tempPasswordET: EditText
    lateinit var newPasswordET: EditText
    lateinit var newPassCheckET: EditText
    lateinit var checkTV: EditText
 


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context

        return inflater.inflate(R.layout.fragment_setting_my_info, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compNameET = view.findViewById(R.id.compNameET)
        phoneNum1ET = view.findViewById(R.id.phoneNum1ET)
        compIdET = view.findViewById(R.id.compIdET)
        addImage1RL = view.findViewById(R.id.addImage1RL)
        addImage2RL = view.findViewById(R.id.addImage2RL)
        termET = view.findViewById(R.id.termET)
        addImage1RL = view.findViewById(R.id.addImage1RL)
        addImage2RL = view.findViewById(R.id.addImage2RL)
        addImage3RL = view.findViewById(R.id.addImage3RL)
        tempPasswordET = view.findViewById(R.id.tempPasswordET)
        newPasswordET = view.findViewById(R.id.newPasswordET)
        newPassCheckET = view.findViewById(R.id.newPassCheckET)
        checkTV = view.findViewById(R.id.checkTV)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }




}
