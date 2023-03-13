package com.example.myapplication

import com.example.myapplication.Data.SchoolSATScore
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface EducationService {

    @GET("s3k6-pzi2")
    suspend fun getSchoolList(
        @Query("\$offset") offset: Int,
        @Query("\$limit") limit: Int,
    ): Response<List<JsonObject>>

    @GET("f9bf-2cp4")
    suspend fun getSATByDBN(
        @Query("dbn") dbn: String
    ): Response<List<SchoolSATScore>>

}
