package com.example.keygencetestapp.DI.retrofit

import com.example.keygencetestapp.constant.RetrofitConstant

class BaseUrlHolder {
    @Volatile
    var baseUrl: String = RetrofitConstant.SMAPRI_BASE_URL
}