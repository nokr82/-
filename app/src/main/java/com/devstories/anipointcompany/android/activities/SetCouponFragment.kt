package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.devstories.anipointcompany.android.R

//메시지쿠폰관리 - 쿠폰작성
class SetCouponFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var expirationLL: LinearLayout
    lateinit var expirationIV: ImageView
    lateinit var coupon_opSP: Spinner
    lateinit var coupon_exSP: Spinner
    lateinit var coupon_prdET: TextView
    lateinit var weekdayLL: LinearLayout
    lateinit var saturdayLL: LinearLayout
    lateinit var sundayLL: LinearLayout
    lateinit var weekdayIV: ImageView
    lateinit var saturdayIV: ImageView
    lateinit var sundayIV: ImageView
    lateinit var validityIV: ImageView
    lateinit var helpTV: TextView

    lateinit var adapter: ArrayAdapter<String>
    var op_coupon = arrayOf("상품증정","상품할인")
    var op_expiration = arrayOf("7일","30일","60일","90일")



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)
        return inflater.inflate(R.layout.fragment_set_coupon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expirationLL= view.findViewById(R.id.expirationLL)

        coupon_opSP= view.findViewById(R.id.coupon_opSP)
        coupon_exSP= view.findViewById(R.id.coupon_exSP)
        coupon_prdET= view.findViewById(R.id.coupon_prdET)

        weekdayLL= view.findViewById(R.id.weekdayLL)
        saturdayLL= view.findViewById(R.id.saturdayLL)
        sundayLL= view.findViewById(R.id.sundayLL)
        validityIV= view.findViewById(R.id.validityIV)
        helpTV= view.findViewById(R.id.helpTV)
        expirationIV= view.findViewById(R.id.expirationIV)
        weekdayIV= view.findViewById(R.id.weekdayIV)
        saturdayIV= view.findViewById(R.id.saturdayIV)
        sundayIV= view.findViewById(R.id.sundayIV)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setmenu2()
        setmenu()

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,op_coupon)
        coupon_opSP.adapter = adapter
        //스피너 선택이벤트
        coupon_opSP.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position==0){

                }else if (position==1){

                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        adapter = ArrayAdapter(myContext,R.layout.spiner_item,op_expiration)
        coupon_exSP.adapter = adapter
        //스피너 선택이벤트
        coupon_exSP.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position==0){

                }else if (position==1){

                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }




        expirationLL.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected) {
                expirationIV.setImageResource(R.mipmap.box_check_on)
            } else {
                expirationIV.setImageResource(R.mipmap.box_check_off)
            }

        }

        weekdayLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                weekdayIV.setImageResource(R.mipmap.box_check_on)
            } else {
                weekdayIV.setImageResource(R.mipmap.box_check_off)
            }

        }
        saturdayLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                saturdayIV.setImageResource(R.mipmap.box_check_on)
            } else {
                saturdayIV.setImageResource(R.mipmap.box_check_off)
            }

        }
        sundayLL.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                sundayIV.setImageResource(R.mipmap.box_check_on)
            } else {
                sundayIV.setImageResource(R.mipmap.box_check_off)
            }
        }
        validityIV.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) {
                validityIV.setImageResource(R.mipmap.switch_off)
            } else {
                validityIV.setImageResource(R.mipmap.switch_on)
            }
        }

    }



    fun setmenu(){
    expirationIV.setImageResource(R.mipmap.box_check_off)
    }

    fun setmenu2(){
        weekdayIV.setImageResource(R.mipmap.box_check_off)
        saturdayIV.setImageResource(R.mipmap.box_check_off)
        sundayIV.setImageResource(R.mipmap.box_check_off)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
