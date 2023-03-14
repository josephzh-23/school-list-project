package com.example.myapplication

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.Data.SchoolItem
import com.example.myapplication.Util.CustomError
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException

const val START_PAGE_INDEX = 0
class SchoolPagingSource(
    private val repository: SchoolRepository
) : PagingSource<Int, SchoolItem>() {

    // current page stored in params
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,SchoolItem> {
        return try {

            var apiResults = listOf<SchoolItem>()

            // Initially the key is null
            val currentPage = params.key ?: START_PAGE_INDEX
            withTimeout(5000L) {

                // Fetches 6 schools per request
                val response = repository.getPopularMoviesList(currentPage, 6)
                val data = response.body()!!
                val mapper = ObjectMapper()


                // Using jackson for mapping
                apiResults = data.map { school ->
                    mapper.readValue(school.toString(), SchoolItem::class.java)
                }
            }

            LoadResult.Page(
                data = apiResults,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = if (apiResults.isEmpty()) null else currentPage + 6
            )
        } catch (exception: HttpException) {
            LoadResult.Error(CustomError("Network request has failed"))
        } catch (exception: TimeoutCancellationException){
            LoadResult.Error(CustomError("Network request has timed out d"))
        } catch(exception: Exception){
            LoadResult.Error(CustomError("An exception has happened"))
        }
    }


    override fun getRefreshKey(state: PagingState<Int, SchoolItem>): Int? {
        return null
    }

}