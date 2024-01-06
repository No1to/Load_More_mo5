package com.example.load_more_mo5

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DataAPI {

    @GET("data.php")
    fun getData(@Query("index") index: Int): Call<List<Data>>
}