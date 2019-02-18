package com.devstories.anipointcompany.android.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

/**
 * Created by hooni
 */
object RequestStepAction {

    // 스텝 체크
    fun checkStep(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/request_step/check_step.json", params, handler)
    }

    // 스텝
    fun changeStep(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/request_step/change_step.json", params, handler)
    }

    // 스텝 삭제
    fun endStep(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/request_step/end_step.json", params, handler)
    }
    //알람
    fun send_alram(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/send_alram.json", params, handler)
    }
}