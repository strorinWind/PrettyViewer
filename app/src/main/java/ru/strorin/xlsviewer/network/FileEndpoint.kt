package ru.strorin.xlsviewer.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url





interface FileEndpoint {

    @GET("ABITREPORTS/MAGREPORTS/FullTime/2646792482.xls")
    fun downloadFileWithFixedUrl(): Call<ResponseBody>

    @GET
    fun downloadFileWithDynamicUrlSync(@Url fileUrl: String): Call<ResponseBody>
}