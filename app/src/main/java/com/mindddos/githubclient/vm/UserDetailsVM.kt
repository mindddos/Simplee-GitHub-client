package com.mindddos.githubclient.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mindddos.githubclient.repository.remote.GitHubApi
import com.mindddos.githubclient.repository.remote.models.UserInfo
import com.mindddos.githubclient.utils.SingleLiveEvent
import com.mindddos.githubclient.utils.Status
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDetailsVM(private val api: GitHubApi) : ViewModel() {
    val userLiveData = MutableLiveData<UserInfo?>()
    val statusLiveData = SingleLiveEvent<Status>()

    fun loadUserDetails(userName: String) {
        GlobalScope.launch(CoroutineExceptionHandler { _, t ->
            run {
                t.printStackTrace()
                statusLiveData.postValue(Status.ERROR)
            }


        }) {

            userLiveData.postValue(api.getUserInfo(userName).await())
            statusLiveData.postValue(Status.FINISHED)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // workaround for preventing pushing old data to new fragments
        userLiveData.postValue(null)
    }
}