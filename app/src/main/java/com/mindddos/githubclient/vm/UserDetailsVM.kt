package com.mindddos.githubclient.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mindddos.githubclient.repository.remote.GitHubApi
import com.mindddos.githubclient.repository.remote.models.UserWithRepos
import com.mindddos.githubclient.utils.NetworkUtils
import com.mindddos.githubclient.utils.SingleLiveEvent
import com.mindddos.githubclient.utils.Status
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDetailsVM(private val api: GitHubApi) : ViewModel() {
    val userLiveData = MutableLiveData<UserWithRepos?>()
    val statusLiveEvent = SingleLiveEvent<Status>()

    fun loadUserDetails(userName: String) {
        statusLiveEvent.postValue(Status.RUNNING)
        GlobalScope.launch(CoroutineExceptionHandler { _, t ->
            run {
                t.printStackTrace()
                statusLiveEvent.postValue(Status.ERROR)
            }


        }) {
            if (!NetworkUtils.isInternetAvailable) {
                statusLiveEvent.postValue(Status.NO_INTERNET)
                return@launch
            }
            val userInfo = api.getUserInfo(userName).await()
            val repositories = api.getUserRepos(userName).await()
            userLiveData.postValue(UserWithRepos(userInfo, repositories))
            statusLiveEvent.postValue(Status.FINISHED)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // workaround for preventing pushing old data to new fragments
        userLiveData.postValue(null)
    }
}