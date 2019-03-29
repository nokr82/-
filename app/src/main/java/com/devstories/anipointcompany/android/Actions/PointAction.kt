package com.devstories.anipointcompany.android.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams


/**
 * Created by hooni
 */
object PointAction {

    fun index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/index.json", params, handler)
    }
    //방문분석
    fun user_visited(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/user_visited.json", params, handler)
    }
    //포인트분석
    fun user_points(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/user_points.json", params, handler)
    }
    //포인트환불
    fun pay_back(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/pay_back.json", params, handler)
    }
    //시간대별 분석
    fun time_detail(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/time_detail.json", params, handler)
    }
    //방문분석4
    fun user_detail(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/user_detail.json", params, handler)
    }
}