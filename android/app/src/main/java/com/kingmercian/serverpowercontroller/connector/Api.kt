package com.kingmercian.serverpowercontroller.connector

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {
    @POST("/switch/pc_power_toggle/turn_on")
    fun togglePower(@Header("Authorization") authHeader: String): Call<ResponseBody>

    @POST("/switch/pc_force_shutdown/turn_on")
    fun forcePower(@Header("Authorization") authHeader: String): Call<ResponseBody>

    @GET("/binary_sensor/pc_power_state")
    fun getPower(@Header("Authorization") authHeader: String): Call<ResponseBody>

}