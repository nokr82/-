package com.devstories.aninuriandroid.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams


/**
 * Created by hooni
 */
object MemberAction {

    // 회원 페이지
    fun my_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_info.json", params, handler)
    }
    // 회원 목록뽑기
    fun user_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/user_list.json", params, handler)
    }

}