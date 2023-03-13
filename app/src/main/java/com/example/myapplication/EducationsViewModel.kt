package com.example.myapplication

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.Data.SchoolItem
import com.example.myapplication.Data.SchoolSATScore
import com.example.myapplication.Util.CheckNetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EducationsViewModel @Inject constructor(
    application: Application,
    private val repo: SchoolRepository,
) : BaseViewModel(application) {

    var app: Application
    var checkConnection: CheckNetworkConnection ?=null
    val scores: MutableLiveData<Resource<SchoolSATScore>> = MutableLiveData()
    lateinit var schoolList: Flow<PagingData<SchoolItem>>


    init{
        app = application
    }
    // This could have been done in init{} block but to
    // prevent casting error for CheckNetWorkConnection
    // when running unit test, the following is used
    fun createConnectionMonitor(){
        checkConnection = CheckNetworkConnection(app)
    }



    fun getSATScoreBySchool(dbn: String) {
        scores.value = Resource.Loading()
        viewModelScope.launch {
            scores.postValue(repo.getSATScoreBySchool(dbn))
        }
    }

    // Launch the paging source flow to fetch data via paging source
    fun launchPagingSource() {
        schoolList = Pager(PagingConfig(8)) {
            SchoolPagingSource(repo)
        }.flow.cachedIn(viewModelScope)
    }
}


