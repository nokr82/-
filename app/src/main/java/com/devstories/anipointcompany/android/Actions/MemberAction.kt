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

    // 회원 정보 변경
    fun edit_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/edit_info.json", params, handler)
    }

    //멤버체크
    fun is_member(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/is_member.json", params, handler)
    }

    // 회원 목록뽑기
    fun user_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/user_list.json", params, handler)
    }

    //사용자 쿠폰 조회
    fun inquiry_point(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_point.json", params, handler)
    }
    //손님등록
    fun member_join(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/member_join.json", params, handler)
    }

    //포인트적립/사용 새거
    fun point(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/point.json", params, handler)
    }

    // 번호로 회원 검색
    fun search(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/search.json", params, handler)
    }

    // 번호로 회원 검색
    fun edit_membership(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/edit_membership.json", params, handler)
    }
}