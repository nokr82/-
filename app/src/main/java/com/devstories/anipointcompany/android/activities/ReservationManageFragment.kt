package com.devstories.anipointcompany.android.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devstories.anipointcompany.android.R
import com.devstories.anipointcompany.android.base.CustomProgressDialog
import kotlinx.android.synthetic.main.fra_reservation_manage.*

class ReservationManageFragment : Fragment() {
    lateinit var myContext: Context

    private var progressDialog: CustomProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        return inflater.inflate(R.layout.fra_reservation_manage, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        reservTV.setOnClickListener {
            val intent = Intent(context, DlgReserveSaveActivity::class.java)
            startActivity(intent)
        }




    }


}
