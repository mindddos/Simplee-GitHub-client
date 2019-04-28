package com.mindddos.githubclient.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mindddos.githubclient.repository.remote.GitHubApi
import com.mindddos.githubclient.repository.remote.models.UsersSearchResult
import com.mindddos.githubclient.utils.NetworkUtils
import com.mindddos.githubclient.utils.SingleLiveEvent
import com.mindddos.githubclient.utils.Status
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchScreenVM(private val api: GitHubApi) : ViewModel() {
    val resultsLiveEvent = MutableLiveData<UsersSearchResult>()
    val statusLiveEvent = SingleLiveEvent<Status>()
    var currentJob: Job? = null

    fun searchForQuery(query: String) {
        statusLiveEvent.postValue(Status.RUNNING)
        if (query == "") {
            statusLiveEvent.postValue(Status.FINISHED)
            return
        }
        currentJob = GlobalScope.launch(CoroutineExceptionHandler { _, t ->
            run {
                t.printStackTrace()
                statusLiveEvent.postValue(Status.ERROR)
            }
        }) {
            if (!NetworkUtils.isInternetAvailable) {
                statusLiveEvent.postValue(Status.NO_INTERNET)
                return@launch
            }
            resultsLiveEvent.postValue(api.searchForUser(query).await())
            statusLiveEvent.postValue(Status.FINISHED)
        }
    }

    override fun onCleared() {
        currentJob?.cancel()
        super.onCleared()
    }
}