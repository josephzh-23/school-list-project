package com.example.myapplication

import com.example.myapplication.Data.SchoolSATScore
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class SchoolRepository @Inject constructor(
    private val apiServices: EducationService,
) {
    suspend fun getPopularMoviesList(offset: Int, limit: Int) =
        apiServices.getSchoolList(offset, limit)

    suspend fun getSATScoreBySchool(dbn: String): Resource<SchoolSATScore> {
        try {

            val ans = withTimeout(5000L) {
                val response = apiServices.getSATByDBN(dbn)

                response.takeIf { it.isSuccessful }?.body()?.let {
                    if (it.isEmpty()) {
                        return@withTimeout Resource.Error("this school has no SAT score")
                    } else {
                        return@withTimeout Resource.Success(it.first())
                    }
                }
            }


            return ans ?: Resource.Error("Network request has timed out")
        } catch (e: Exception) {
            println(e.message.toString())
            return Resource.Error("An error has occured")
        }
    }
}