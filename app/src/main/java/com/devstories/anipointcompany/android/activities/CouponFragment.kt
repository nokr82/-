package com.devstories.anipointcompany.android.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.anipointcompany.android.R


class CouponFragment : Fragment() {

    internal lateinit var view: View
    lateinit var oneLL: LinearLayout
    lateinit var twoLL: LinearLayout
    lateinit var threeLL: LinearLayout
    lateinit var fourLL: LinearLayout
    lateinit var fiveLL: LinearLayout
    lateinit var sixLL: LinearLayout
    lateinit var sevenLL: LinearLayout
    lateinit var eightLL: LinearLayout
    lateinit var nineLL: LinearLayout
    lateinit var zeroLL: LinearLayout
    lateinit var backLL: LinearLayout

    lateinit var pointTV: TextView




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            return inflater.inflate(R.layout.fra_coupon,container,false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backLL = view.findViewById(R.id.backLL)
        oneLL = view.findViewById(R.id.oneLL)
        twoLL = view.findViewById(R.id.twoLL)
        threeLL = view.findViewById(R.id.threeLL)
        fourLL = view.findViewById(R.id.fourLL)
        fiveLL = view.findViewById(R.id.fiveLL)
        sixLL = view.findViewById(R.id.sixLL)
        sevenLL = view.findViewById(R.id.sevenLL)
        eightLL = view.findViewById(R.id.eightLL)
        zeroLL = view.findViewById(R.id.zeroLL)
        nineLL = view.findViewById(R.id.nineLL)

        pointTV = view.findViewById(R.id.pointTV)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        oneLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 1)
        }
        twoLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 2)
        }
        threeLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 3)
        }
        fourLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 4)
        }
        fiveLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 5)
        }
        sixLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 6)
        }
        sevenLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 7)
        }
        eightLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 8)
        }
        nineLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 9)
        }
        zeroLL.setOnClickListener {
            pointTV.setText(pointTV.getText().toString() + 0)
        }
        backLL.setOnClickListener {
            val text = pointTV.getText().toString()
            if (text.length > 0){
                pointTV.setText(text.substring(0, text.length - 1))
            }else{
            }
        }

    }

}
