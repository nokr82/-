package com.devstories.anipointcompany.android.Actions

import com.devstories.anipointcompany.android.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

/**
 * Created by hooni
 */
object CompanyAction {

    // 스텝 체크
    fun company_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/info.json", params, handler)
    }

    fun company_login(params: RequestParams, handler: JsonHttpResponseHandler) {
        //HttpClient.post("/login/admin_login.json", params, handler)
        HttpClient.post("/login/index.json", params, handler)
    }

}