package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.devstories.anipointcompany.android.R

class MessageUserFragment : Fragment() {

    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    var option_visitday = arrayOf("전체", "5일", "2×4", "4×1", "4×2")




    lateinit var visitdaySP: Spinner


    lateinit var tenLL: LinearLayout
    lateinit var twoLL: LinearLayout
    lateinit var threeLL: LinearLayout
    lateinit var fourLL: LinearLayout
    lateinit var fiveLL: LinearLayout
    lateinit var sixLL: LinearLayout

    lateinit var girlLL: LinearLayout
    lateinit var menLL: LinearLayout
    lateinit var genderLL: LinearLayout

    lateinit var citizenLL: LinearLayout
    lateinit var workerLL: LinearLayout
    lateinit var studentLL: LinearLayout
    lateinit var cautionLL: LinearLayout

    lateinit var tenTV: TextView
    lateinit var twoTV: TextView
    lateinit var threeTV: TextView
    lateinit var fourTV: TextView
    lateinit var fiveTV: TextView
    lateinit var sixTV: TextView

    lateinit var girlTV: TextView
    lateinit var menTV: TextView
    lateinit var genderTV: TextView

    lateinit var citizenTV: TextView
    lateinit var workerTV: TextView
    lateinit var studentTV: TextView
    lateinit var cautionTV: TextView

    lateinit var allRL: RelativeLayout
    lateinit var acc_countRL: RelativeLayout
    lateinit var use_moneyRL: RelativeLayout
    lateinit var novisitRL: RelativeLayout
    lateinit var pointRL: RelativeLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        return inflater.inflate(R.layout.fra_message_wirte_step1, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        visitdaySP = view.findViewById(R.id.visitdaySP)
        tenLL = view.findViewById(R.id.tenLL)
        twoLL = view.findViewById(R.id.twoLL)
        threeLL = view.findViewById(R.id.threeLL)
        fourLL = view.findViewById(R.id.fourLL)
        fiveLL = view.findViewById(R.id.fiveLL)
        sixLL = view.findViewById(R.id.sixLL)
        studentLL = view.findViewById(R.id.studentLL)
        workerLL = view.findViewById(R.id.workerLL)
        cautionLL = view.findViewById(R.id.cautionLL)
        genderLL = view.findViewById(R.id.genderLL)
        menLL = view.findViewById(R.id.menLL)
        girlLL = view.findViewById(R.id.girlLL)
        citizenLL = view.findViewById(R.id.citizenLL)
        tenTV = view.findViewById(R.id.tenTV)
        twoTV = view.findViewById(R.id.twoTV)
        threeTV = view.findViewById(R.id.threeTV)
        fourTV = view.findViewById(R.id.fourTV)
        fiveTV = view.findViewById(R.id.fiveTV)
        sixTV = view.findViewById(R.id.sixTV)
        studentTV = view.findViewById(R.id.studentTV)
        workerTV = view.findViewById(R.id.workerTV)
        cautionTV = view.findViewById(R.id.cautionTV)
        genderTV = view.findViewById(R.id.genderTV)
        menTV = view.findViewById(R.id.menTV)
        girlTV = view.findViewById(R.id.girlTV)
        citizenTV = view.findViewById(R.id.citizenTV)
        cautionTV = view.findViewById(R.id.cautionTV)
        allRL = view.findViewById(R.id.allRL)
        use_moneyRL =view.findViewById(R.id.use_moneyRL)
        acc_countRL = view.findViewById(R.id.acc_countRL)
        novisitRL = view.findViewById(R.id.novisitRL)
        pointRL = view.findViewById(R.id.pointRL)



    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        adapter = ArrayAdapter(myContext,R.layout.spiner_item,option_visitday)
        visitdaySP.adapter = adapter
        setmenu1()
        setmenu2()
        setmenu3()
        setfilter()

        genderLL.setOnClickListener {
            setmenu1()
            genderLL.setBackgroundResource(R.drawable.background_00d1ce)
            genderTV.setTextColor(Color.parseColor("#ffffff"))
        }
        menLL.setOnClickListener {
            setmenu1()
            menLL.setBackgroundResource(R.drawable.background_00d1ce)
            menTV.setTextColor(Color.parseColor("#ffffff"))
        }
        girlLL.setOnClickListener {
            setmenu1()
            girlLL.setBackgroundResource(R.drawable.background_00d1ce)
            girlTV.setTextColor(Color.parseColor("#ffffff"))
        }



        tenLL.setOnClickListener {
            setmenu2()
            tenLL.setBackgroundResource(R.drawable.background_00d1ce)
            tenTV.setTextColor(Color.parseColor("#ffffff"))
        }
        twoLL.setOnClickListener {
            setmenu2()
            twoLL.setBackgroundResource(R.drawable.background_00d1ce)
            twoTV.setTextColor(Color.parseColor("#ffffff"))


        }
        threeLL.setOnClickListener {
            setmenu2()
            threeLL.setBackgroundResource(R.drawable.background_00d1ce)
            threeTV.setTextColor(Color.parseColor("#ffffff"))


        }
        fourLL.setOnClickListener {
            setmenu2()
            fourLL.setBackgroundResource(R.drawable.background_00d1ce)
            fourTV.setTextColor(Color.parseColor("#ffffff"))


        }
        fiveLL.setOnClickListener {
            setmenu2()
            fiveLL.setBackgroundResource(R.drawable.background_00d1ce)
            fiveTV.setTextColor(Color.parseColor("#ffffff"))


        }
        sixLL.setOnClickListener {
            setmenu2()
            sixLL.setBackgroundResource(R.drawable.background_00d1ce)
            sixTV.setTextColor(Color.parseColor("#ffffff"))

        }


        citizenLL.setOnClickListener {
            setmenu3()
            citizenLL.setBackgroundResource(R.drawable.background_00d1ce)
            citizenTV.setTextColor(Color.parseColor("#ffffff"))
        }
        studentLL.setOnClickListener {
            setmenu3()
            studentLL.setBackgroundResource(R.drawable.background_00d1ce)
            studentTV.setTextColor(Color.parseColor("#ffffff"))
        }

        workerLL.setOnClickListener {
            setmenu3()
            workerLL.setBackgroundResource(R.drawable.background_00d1ce)
            workerTV.setTextColor(Color.parseColor("#ffffff"))

        }

        cautionLL.setOnClickListener {
            setmenu3()
            cautionLL.setBackgroundResource(R.drawable.background_00d1ce)
            cautionTV.setTextColor(Color.parseColor("#ffffff"))
        }
        allRL.setOnClickListener {
            setfilter()
            allRL.setBackgroundColor(Color.parseColor("#0068df"))
        }
        acc_countRL.setOnClickListener {
            setfilter()
            acc_countRL.setBackgroundColor(Color.parseColor("#0068df"))
        }
        novisitRL.setOnClickListener {
            setfilter()
            novisitRL.setBackgroundColor(Color.parseColor("#0068df"))
        }
        use_moneyRL.setOnClickListener {
            setfilter()
            use_moneyRL.setBackgroundColor(Color.parseColor("#0068df"))
        }
        pointRL.setOnClickListener {
            setfilter()
            pointRL.setBackgroundColor(Color.parseColor("#0068df"))
        }


    }

    fun setfilter(){
        allRL.setBackgroundResource(R.drawable.background_strock_null)
        acc_countRL.setBackgroundResource(R.drawable.background_strock_null)
        novisitRL.setBackgroundResource(R.drawable.background_strock_null)
        use_moneyRL.setBackgroundResource(R.drawable.background_strock_null)
        pointRL.setBackgroundResource(R.drawable.background_strock_null)
    }

    fun setmenu1(){
        genderLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        menLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        girlLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)

        genderTV.setTextColor(Color.parseColor("#9a9a99"))
        menTV.setTextColor(Color.parseColor("#9a9a99"))
        girlTV.setTextColor(Color.parseColor("#9a9a99"))
    }
    fun setmenu2(){
        tenLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        twoLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        threeLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        fourLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        fiveLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        sixLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)

        tenTV.setTextColor(Color.parseColor("#9a9a99"))
        twoTV.setTextColor(Color.parseColor("#9a9a99"))
        threeTV.setTextColor(Color.parseColor("#9a9a99"))
        fourTV.setTextColor(Color.parseColor("#9a9a99"))
        fiveTV.setTextColor(Color.parseColor("#9a9a99"))
        sixTV.setTextColor(Color.parseColor("#9a9a99"))
    }
    fun setmenu3(){
        citizenLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        workerLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        studentLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)
        cautionLL.setBackgroundResource(R.drawable.background_strock_c1c1c1)

        citizenTV.setTextColor(Color.parseColor("#9a9a99"))
        workerTV.setTextColor(Color.parseColor("#9a9a99"))
        studentTV.setTextColor(Color.parseColor("#9a9a99"))
        cautionTV.setTextColor(Color.parseColor("#9a9a99"))
    }
}
