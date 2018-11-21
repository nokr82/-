package com.devstories.anipointcompany.android.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.devstories.aninuriandroid.adapter.UserListAdapter
import com.devstories.anipointcompany.android.R


class Point_List_Fragment : Fragment() {
    lateinit var myContext: Context
    internal lateinit var view: View
    lateinit var userLV: ListView

    lateinit var useradapter: UserListAdapter
    var data = arrayListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context


            return inflater.inflate(R.layout.fra_point_list,container,false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLV = view.findViewById(R.id.userLV)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        for (i in 0..5) {
            data.add(i)
        }

        useradapter = UserListAdapter(myContext,data)
        userLV.adapter = useradapter
    }

}
