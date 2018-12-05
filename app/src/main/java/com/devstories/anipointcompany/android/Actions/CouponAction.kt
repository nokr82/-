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
    // 쿠폰 테스트메시지(step3)
    fun test_message(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/test_message.json", params, handler)
    }

    // 자동 쿠폰 - 리스트 정보
    fun auto_coupon(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/auto_coupon.json", params, handler)
    }

    // 자동 쿠폰 - 저장
    fun edit_coupon(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/edit_coupon.json", params, handler)
    }

    // 쿠폰 정보
    fun coupon(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/coupon.json", params, handler)
    }

    // 자동 쿠폰 on/off
    fun change_temp_yn(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/change_temp_yn.json", params, handler)
    }

    //고객 쿠폰리스트 조회
    fun member_coupon_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_point.json", params, handler)
    }

    // 메세지 통계
    fun message_analysis(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/message_analysis.json", params, handler)
    }

    // 메세지 통계 상세
    fun message_detail(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/coupon/message_detail.json", params, handler)
    }


}