package com.mindddos.githubclient.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mindddos.githubclient.R
import com.mindddos.githubclient.adapters.RepoListAdapter
import com.mindddos.githubclient.repository.remote.models.UserWithRepos
import com.mindddos.githubclient.utils.Const
import com.mindddos.githubclient.utils.Status
import com.mindddos.githubclient.vm.UserDetailsVM
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.android.synthetic.main.activity_user_details.progress_bar
import org.koin.android.viewmodel.ext.android.viewModel

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class UserDetailsActivity : AppCompatActivity() {
    private val vm by viewModel<UserDetailsVM>()
    private val adapter by lazy { RepoListAdapter(listOf(), this) }

    companion object {
        fun startActivity(context: Context, userName: String) {
            val intent = Intent(context, UserDetailsActivity::class.java)
            intent.putExtra(Const.USERNAME_KEY, userName)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        vm.userLiveData.observe(this, Observer { data -> data?.let { renderData(it) } })
        vm.statusLiveData.observe(this, Observer {
            when (it) {
                Status.RUNNING -> progress_bar.visibility = View.VISIBLE
                Status.FINISHED -> progress_bar.visibility = View.INVISIBLE
                Status.ERROR -> progress_bar.visibility = View.INVISIBLE
                Status.NO_INTERNET -> TODO()
            }
        })

        rv_repos.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            rv_repos.context,
            layoutManager.orientation
        )
        rv_repos.layoutManager = layoutManager
        rv_repos.addItemDecoration(dividerItemDecoration)

        vm.loadUserDetails(intent.extras!!.getString(Const.USERNAME_KEY, ""))
    }

    private fun renderData(data: UserWithRepos) {
        Glide.with(this)
            .load(data.userInfo.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(RequestOptions.circleCropTransform())
            .into(iv_avatar)

        iv_location.visibility = View.VISIBLE
        tv_name.text = data.userInfo.name
        tv_username.text = data.userInfo.login
        tv_bio.text = data.userInfo.bio
        tv_location.text = data.userInfo.location?.capitalize()
        tv_followers.text = String.format(getString(R.string.followers), data.userInfo.followers)
        tv_following.text = String.format(getString(R.string.following), data.userInfo.following)


        // init recycle view
        adapter.setItems(data.repositories)
    }
}