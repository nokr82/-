package com.devstories.anipointcompany.android.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

/**
 * Created by hooni
 */
object CompanyAction {

    // 사업자정보
    fun company_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/info.json", params, handler)
    }

    // 사업자정보
    fun edit_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/edit_info.json", params, handler)
    }
    // 사업자정보
    fun edit_image(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/edit_images.json", params, handler)
    }
    fun company_login(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/login/index.json", params, handler)
    }
    fun sales_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/sales_list.json", params, handler)
    }

    fun membership_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/membership_list.json", params, handler)
    }
}