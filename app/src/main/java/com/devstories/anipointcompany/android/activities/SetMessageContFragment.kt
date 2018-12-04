package com.devstories.anipointcompany.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.devstories.anipointcompany.android.R
import android.text.Editable
import android.text.TextWatcher
import com.devstories.anipointcompany.android.base.Utils


class SetMessageContFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null

    lateinit var companyNameTV:TextView
    lateinit var memberNameTV:TextView
    lateinit var pointTV:TextView

    lateinit var textLengthTV:TextView
    lateinit var messageTV:TextView
    lateinit var titleTV:TextView
    lateinit var nextTV:TextView

    lateinit var messageContentET:EditText
    lateinit var titleET:EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.frag_set_message_cont, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companyNameTV = view.findViewById(R.id.companyNameTV)
        memberNameTV = view.findViewById(R.id.memberNameTV)
        pointTV = view.findViewById(R.id.pointTV)
        textLengthTV = view.findViewById(R.id.textLengthTV)
        messageTV = view.findViewById(R.id.messageTV)
        titleTV = view.findViewById(R.id.titleTV)
        nextTV = view.findViewById(R.id.nextTV)

        messageContentET = view.findViewById(R.id.messageContentET)
        titleET = view.findViewById(R.id.titleET)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        companyNameTV.setOnClickListener {

            var message = messageContentET.text.toString() + "{_매장이름_}"

            messageContentET.setText(message)

        }

        memberNameTV.setOnClickListener {

            var message = messageContentET.text.toString() + "{_고객이름_}"

            messageContentET.setText(message)

        }

        pointTV.setOnClickListener {

            var message = messageContentET.text.toString() + "{_포인트_}"

            messageContentET.setText(message)

        }

        messageContentET.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textLengthTV.text = s.length.toString()
                messageTV.text = s
            }
        })

        titleET.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                titleTV.text = "[Web발신]\n" + s + "\n"
            }
        })

        nextTV.setOnClickListener {
            // title
        }

    }

}
