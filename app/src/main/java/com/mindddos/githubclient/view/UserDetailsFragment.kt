package com.mindddos.githubclient.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mindddos.githubclient.R
import com.mindddos.githubclient.repository.remote.models.UserInfo
import com.mindddos.githubclient.utils.Const
import com.mindddos.githubclient.utils.Status
import com.mindddos.githubclient.vm.UserDetailsVM
import kotlinx.android.synthetic.main.fragment_user_details.*
import org.koin.android.viewmodel.ext.android.viewModel

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class UserDetailsFragment : Fragment() {
    private val vm by viewModel<UserDetailsVM>()

    companion object {
        fun newInstance(username: String): UserDetailsFragment {
            val args = Bundle()
            args.putString(Const.USERNAME_KEY, username)
            return UserDetailsFragment().apply { arguments = args }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_user_details, container, false)

        arguments?.let {
            vm.loadUserDetails(it.getString(Const.USERNAME_KEY, ""))
        }

        vm.userLiveData.observe(this, Observer { renderUserInfo(it) })
        vm.statusLiveData.observe(this, Observer {
            when (it) {
                Status.RUNNING -> progress_bar.visibility = View.VISIBLE
                Status.FINISHED -> progress_bar.visibility = View.INVISIBLE
                Status.ERROR -> progress_bar.visibility = View.INVISIBLE
            }
        })
        return v
    }

    private fun renderUserInfo(userInfo: UserInfo) {
        Glide.with(context!!)
            .load(userInfo.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(RequestOptions.circleCropTransform())
            .into(iv_avatar)

        tv_name.text = userInfo.name
        tv_username.text = userInfo.login
        tv_bio.text = userInfo.bio
        tv_location.text = userInfo.location?.capitalize()
        tv_repos_count.text = String.format(context!!.getString(R.string.repos_count), userInfo.publicRepos)
        tv_followers.text = String.format(context!!.getString(R.string.followers), userInfo.followers)
        tv_following.text = String.format(context!!.getString(R.string.following), userInfo.following)
    }
}