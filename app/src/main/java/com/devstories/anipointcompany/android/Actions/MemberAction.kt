package com.devstories.anipointcompany.android.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams


/**
 * Created by hooni
 */
object MemberAction {

    // 회원 정보뽑기
    fun my_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_info.json", params, handler)
    }
    // 회원 목록뽑기
    fun user_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/user_list.json", params, handler)
    }

    //방문 이력
    fun visit_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/visit_list.json", params, handler)
    }

    //손님등록
    fun member_join(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/member_join.json", params, handler)
    }

    //생일인 유저
    fun today_birth_user(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/user_birth.json", params, handler)
    }
}