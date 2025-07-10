package com.example.keygencetestapp.api

import com.example.keygencetestapp.constant.RetrofitConstant.FIXED_SAMPLE_LAYOUT_ROUTE
import com.example.keygencetestapp.constant.RetrofitConstant.FIXED_TEST_LAYOUT_ROUTE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface SmapriApi {
/*
[Request Before URL Encoding]
http://localhost:8080/Format/Print
?__format_archive_url=http://example.com/smapri.spfmtz
&__format_archive_update=update
&__format_id_number=1
&String1=Orange
&Barcode1=123456789012
&Price1=100
&Frame1.visible=false
&QTY=1

URL for print request: http://localhost:8080/Format/Print?__format_archive_url=http://example.com/smapri.spfmtz&__format_archive_update=update&__format_id_number=1&String1=Orange&Barcode1=123456789012&Price1=100&Frame1.visible=false&QTY=1 */

    @GET("Format/Print")
    suspend fun sendStockInPrintRequest (
        @Query("__format_archive_url") archiveUrl: String = FIXED_TEST_LAYOUT_ROUTE,
        @Query("__format_id_number") formatIdNumber: Int = 1,
        @Query("__format_archive_update") archiveUpdate: String = "update",
        @Query("String7") poId: String,
        @Query("String10") productName: String,
        @Query("String9") productId: String,
        @Query("String11") quantity: String,
        @Query("String15") productCode: String,
        @Query("String13") receivedDate: String,
        @Query("Barcode2") productIdForQR: String = productId,
        @Query("(Print quantity)") quantityCopy: Int = 1
    ): Response<Void>


    @GET("Printer/Status")
    suspend fun getPrinterStatus(): Response<ResponseBody>

    @GET("Format/Print?__format_archive_url=${FIXED_SAMPLE_LAYOUT_ROUTE}&__format_id_number=1")
    suspend fun sendSamplePrintRequest(): Response<ResponseBody>
}
