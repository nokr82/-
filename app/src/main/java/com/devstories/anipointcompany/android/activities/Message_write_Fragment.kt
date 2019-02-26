package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.devstories.anipointcompany.android.R
import kotlinx.android.synthetic.main.fra_message_wirte_step1.*
import java.util.ArrayList

//메인메시지관리
class Message_write_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var userRL: RelativeLayout
    lateinit var couponRL: RelativeLayout
    lateinit var writeRL: RelativeLayout
    lateinit var finalRL: RelativeLayout

    lateinit var userTV: TextView
    lateinit var couponTV: TextView
    lateinit var writeTV: TextView
    lateinit var finalTV: TextView

    lateinit var userchoiceFL: FrameLayout
    val MessageUserFragment: MessageUserFragment = MessageUserFragment()
    val SetCouponFragment: SetCouponFragment = SetCouponFragment()
    val SetMessageContFragment: SetMessageContFragment = SetMessageContFragment()
    var member_id = -1
    //고객선택
    internal var step1NextReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                //브로드캐스트로 프래그먼트이동리시버

                var gender = intent.getSerializableExtra("gender")
                var age = intent.getSerializableExtra("age")
                var count = intent.getStringExtra("count")
                var missing_from = intent.getStringExtra("missing_from")
                var missing_to = intent.getStringExtra("missing_to")
                var use_from = intent.getStringExtra("use_from")
                var use_to = intent.getStringExtra("use_to")
                var left_from = intent.getStringExtra("left_from")
                var left_to = intent.getStringExtra("left_to")
                var from = intent.getStringExtra("from")
                var to = intent.getStringExtra("to")



                var stack_visit = intent.getIntExtra("stack_visit", -1)
                var mising_day = intent.getIntExtra("mising_day", -1)
                var use_money = intent.getIntExtra("use_money", -1)
                var left_point = intent.getIntExtra("left_point", -1)


                setfilter()
                userRL.setBackgroundColor(Color.parseColor("#0068df"))
                userTV.setTextColor(Color.parseColor("#ffffff"))
                couponRL.setBackgroundColor(Color.parseColor("#0068df"))
                couponTV.setTextColor(Color.parseColor("#ffffff"))

                //쿠폰으로보내기
                var args: Bundle = Bundle()
                args.putString("count", count)
                args.putInt("stack_visit", stack_visit)
                args.putInt("mising_day", mising_day)
                args.putInt("use_money", use_money)
                args.putInt("left_point", left_point)
                args.putSerializable("gender", gender as ArrayList<String>?)
                args.putSerializable("age", age as ArrayList<String>?)
                args.putString("missing_from", missing_from)
                args.putString("missing_to", missing_to)
                args.putString("use_from", use_from)
                args.putString("use_to", use_to)
                args.putString("left_from", left_from)
                args.putString("left_to", left_to)
                args.putString("from", from)
                args.putString("to", to)


                SetCouponFragment.setArguments(args)
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, SetCouponFragment).commit()
            }
        }
    }

    // 쿠폰설정
    internal var step2NextReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                var coupon_id = intent.getStringExtra("coupon_id")
                var gender = intent.getSerializableExtra("gender")
                var age = intent.getSerializableExtra("age")
                var count = intent.getStringExtra("count")
                var missing_from = intent.getStringExtra("missing_from")
                var missing_to = intent.getStringExtra("missing_to")
                var use_from = intent.getStringExtra("use_from")
                var use_to = intent.getStringExtra("use_to")
                var left_from = intent.getStringExtra("left_from")
                var left_to = intent.getStringExtra("left_to")
                var from = intent.getStringExtra("from")
                var to = intent.getStringExtra("to")



                var stack_visit = intent.getIntExtra("stack_visit", -1)
                var mising_day = intent.getIntExtra("mising_day", -1)
                var use_money = intent.getIntExtra("use_money", -1)
                var left_point = intent.getIntExtra("left_point", -1)
                var member_id = intent.getIntExtra("member_id", -1)

                Log.d("멤버아뒤",member_id.toString())

                couponRL.setBackgroundColor(Color.parseColor("#0068df"))
                couponTV.setTextColor(Color.parseColor("#ffffff"))
                writeRL.setBackgroundColor(Color.parseColor("#0068df"))
                writeTV.setTextColor(Color.parseColor("#ffffff"))
                //메시지작성으로
                var args: Bundle = Bundle()
                if (member_id != -1){
                    args.putInt("member_id", member_id)
                }
                args.putString("coupon_id", coupon_id)
                args.putString("count", count)
                args.putInt("stack_visit", stack_visit)
                args.putInt("mising_day", mising_day)
                args.putInt("use_money", use_money)
                args.putInt("left_point", left_point)
                args.putSerializable("gender", gender as ArrayList<String>?)
                args.putSerializable("age", age as ArrayList<String>?)
                args.putString("missing_from", missing_from)
                args.putString("missing_to", missing_to)
                args.putString("use_from", use_from)
                args.putString("use_to", use_to)
                args.putString("left_from", left_from)
                args.putString("left_to", left_to)
                args.putString("from", from)
                args.putString("to", to)
                SetMessageContFragment.setArguments(args)
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, SetMessageContFragment).commit()

            }
        }
    }

    //메시지작성
    internal var step3NextReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                println("intent")
                couponRL.setBackgroundColor(Color.parseColor("#0068df"))
                couponTV.setTextColor(Color.parseColor("#ffffff"))
                writeRL.setBackgroundColor(Color.parseColor("#0068df"))
                writeTV.setTextColor(Color.parseColor("#ffffff"))
                finalRL.setBackgroundColor(Color.parseColor("#0068df"))
                finalTV.setTextColor(Color.parseColor("#ffffff"))
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MessageUserFragment).commit()

            }
        }
    }
    //최종 =>메시지보내기
    internal var FinalReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setfilter()
                userRL.setBackgroundColor(Color.parseColor("#0068df"))
                userTV.setTextColor(Color.parseColor("#ffffff"))
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MessageUserFragment).commit()
            }
        }
    }
    //건너뛰기
    internal var SkipReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setfilter()
                writeRL.setBackgroundColor(Color.parseColor("#0068df"))
                writeTV.setTextColor(Color.parseColor("#ffffff"))



                var gender = intent.getSerializableExtra("gender")
                var age = intent.getSerializableExtra("age")
                var visited_date = intent.getStringExtra("visited_date")
                var count = intent.getStringExtra("count")
                var from = intent.getStringExtra("from")
                var to = intent.getStringExtra("to")
                var search_type = intent.getIntExtra("search_type", -1)
                var member_id = intent.getIntExtra("member_id", -1)

                Log.d("멤버아뒤",member_id.toString())

                var args: Bundle = Bundle()
                if (member_id != -1){
                    args.putInt("member_id", member_id)
                }else{
                    args.putString("count", count)
                    args.putInt("search_type", search_type)
                    args.putStringArrayList("gender", gender as ArrayList<String>?)
                    args.putStringArrayList("age", age as ArrayList<String>?)
                    args.putString("visited_date", visited_date)
                    args.putString("from", from)
                    args.putString("to", to)
                }

                SetMessageContFragment.setArguments(args)
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, SetMessageContFragment).commit()
            }
        }
    }

  /*  //고객리스트 =>메시지보내기
    internal var MsgReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                setfilter()
//                member_id =   intent!!.getStringExtra("member_id")
//                Toast.makeText(myContext,member_id,Toast.LENGTH_SHORT).show()
                writeRL.setBackgroundColor(Color.parseColor("#0068df"))
                writeTV.setTextColor(Color.parseColor("#ffffff"))
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, SetMessageContFragment).commit()
            }
        }
    }*/
  //고객리스트 =>쿠폰설정
  internal var MsgReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent?) {
          if (intent != null) {
              setfilter()
//                member_id =   intent!!.getStringExtra("member_id")
//                Toast.makeText(myContext,member_id,Toast.LENGTH_SHORT).show()
              couponRL.setBackgroundColor(Color.parseColor("#0068df"))
              couponTV.setTextColor(Color.parseColor("#ffffff"))
              childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, SetCouponFragment).commit()
          }
      }
  }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        return inflater.inflate(R.layout.fra_message_userchoice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRL = view.findViewById(R.id.userRL)
        couponRL = view.findViewById(R.id.couponRL)
        writeRL = view.findViewById(R.id.writeRL)
        finalRL = view.findViewById(R.id.finalRL)

        userTV = view.findViewById(R.id.userTV)
        couponTV = view.findViewById(R.id.couponTV)
        writeTV = view.findViewById(R.id.writeTV)
        finalTV = view.findViewById(R.id.finalTV)

        userchoiceFL = view.findViewById(R.id.userchoiceFL)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        var filter = IntentFilter("MSG_NEXT")
        myContext.registerReceiver(MsgReceiver, filter)

        var filter5 = IntentFilter("FINAL_NEXT")
        myContext.registerReceiver(FinalReceiver, filter5)

        //고객선택
        var filter1 = IntentFilter("STEP1_NEXT")
        myContext.registerReceiver(step1NextReceiver, filter1)
        //쿠폰설정
        var filter2 = IntentFilter("STEP2_NEXT")
        myContext.registerReceiver(step2NextReceiver, filter2)
        //쿠폰설정
        var skipfilter = IntentFilter("SKIP_NEXT")
        myContext.registerReceiver(SkipReceiver, skipfilter)

        //메시지작성
        var filter3 = IntentFilter("STEP3_NEXT")
        myContext.registerReceiver(step3NextReceiver, filter3)

        if (arguments != null) {
            member_id = getArguments()!!.getInt("member_id", -1)
            if (member_id != -1) {
                setfilter()
//                member_id =   intent!!.getStringExtra("member_id")
//                Toast.makeText(myContext,member_id,Toast.LENGTH_SHORT).show()
                var args: Bundle = Bundle()
                args.putInt("member_id", member_id)
                SetCouponFragment.setArguments(args)
                couponRL.setBackgroundColor(Color.parseColor("#0068df"))
                couponTV.setTextColor(Color.parseColor("#ffffff"))
                childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, SetCouponFragment).commit()
                member_id = -1
            }
            arguments = null
        } else {
            setfilter()
            userRL.setBackgroundColor(Color.parseColor("#0068df"))
            userTV.setTextColor(Color.parseColor("#ffffff"))
            childFragmentManager.beginTransaction().replace(R.id.userchoiceFL, MessageUserFragment).commit()
        }


    }

    fun setfilter() {
        userRL.setBackgroundResource(R.drawable.background_strock_null)
        couponRL.setBackgroundResource(R.drawable.background_strock_null)
        writeRL.setBackgroundResource(R.drawable.background_strock_null)
        finalRL.setBackgroundResource(R.drawable.background_strock_null)

        userTV.setTextColor(Color.parseColor("#c5c5c5"))
        couponTV.setTextColor(Color.parseColor("#c5c5c5"))
        writeTV.setTextColor(Color.parseColor("#c5c5c5"))
        finalTV.setTextColor(Color.parseColor("#c5c5c5"))
    }
    override fun onPause() {
        super.onPause()

    }
    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }

        try {
            if (null != step1NextReceiver) {
                myContext.unregisterReceiver(step1NextReceiver)
            }

        } catch (e: IllegalArgumentException) {
        }

        try {
            if (null != SkipReceiver) {
                myContext.unregisterReceiver(SkipReceiver)
            }

        } catch (e: IllegalArgumentException) {
        }
        try {
            if (null != step3NextReceiver) {
                myContext.unregisterReceiver(step3NextReceiver)
            }

        } catch (e: IllegalArgumentException) {
        }

        try {
            if (null != step2NextReceiver) {
                myContext.unregisterReceiver(step2NextReceiver)
            }

        } catch (e: IllegalArgumentException) {
        }
        try {
            if (null != MsgReceiver) {
                myContext.unregisterReceiver(MsgReceiver)
            }

        } catch (e: IllegalArgumentException) {
        }


    }

}
