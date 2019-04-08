package com.devstories.anipointcompany.android.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

/**
 * Created by hooni
 */
object CompanyAction {


    fun contract_view_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/contract_view_list.json", params, handler)
    }
    fun contract_write(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/contract_write.json", params, handler)
    }

    fun contract_confirm(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/contract_confirm.json", params, handler)
    }


    fun del_manage(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/del_manage.json", params, handler)
    }
    fun contract_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/contract_list.json", params, handler)
    }


    fun reserve_confirm(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/reserve_del.json", params, handler)
    }

    fun reservation_days(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/reservation_days.json", params, handler)
    }
    fun reserve_del(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/reserve_del.json", params, handler)
    }
    fun reserve_list(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/reserve_list.json", params, handler)
    }
    fun reserve(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/reserve.json", params, handler)
    }
    fun addmanage(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/addmanage.json", params, handler)
    }

    // 사업자정보
    fun company_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/info.json", params, handler)
    }

    // 사업자정보
    fun edit_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/edit_info.json", params, handler)
    }
    // 예약정보
    fun reserve_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/reserve_info.json", params, handler)
    }
    // 예약정보
    fun edit_reserve(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/edit_reserve.json", params, handler)
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