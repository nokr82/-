package com.devstories.anipointcompany.android.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

/**
 * Created by hooni
 */
object CouponAction {

    // 쿠폰만들기(step1) - (고객필터)
    fun member_filter(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/member_filter.json", params, handler)
    }

    // 쿠폰만들기(step2) -(쿠폰설정)
    fun  coupon_add(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/add.json", params, handler)
    }
    // 쿠폰 만들기(step3) - 메세지 보내기
    fun send_message(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/send_message.json", params, handler)
    }


}