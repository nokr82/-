package com.devstories.anipointcompany.android.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams


/**
 * Created by hooni
 */
object PointAction {

    // 회원 목록뽑기
    fun user_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/user_list.json", params, handler)
    }

    //방문 이력
    fun visit_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/visit_list.json", params, handler)
    }
    fun index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/index.json", params, handler)
    }
    fun user_visited(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/point/user_visited.json", params, handler)
    }
}