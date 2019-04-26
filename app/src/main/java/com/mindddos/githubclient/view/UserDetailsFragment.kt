package com.mindddos.githubclient.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
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
import org.koin.android.viewmodel.ext.android.getViewModel


// Because there's no java lol)))
@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class UserDetailsFragment : Fragment() {
    private lateinit var vm: UserDetailsVM

    companion object {
        fun newInstance(username: String): UserDetailsFragment {
            val args = Bundle()
            args.putString(Const.USERNAME_KEY, username)
            return UserDetailsFragment().apply { arguments = args }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_user_details, container, false)

        arguments?.let {
            vm.loadUserDetails(it.getString(Const.USERNAME_KEY, ""))
        }

        v.findViewById<RelativeLayout>(R.id.root).setOnTouchListener { _, motionEvent ->
            if (isOnActiveArea(motionEvent)) {
                activity!!.supportFragmentManager.popBackStack()
            }
            return@setOnTouchListener false

        }
        return v
    }

    private fun isOnActiveArea(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {

            // create a rect for storing the fragment window rect
            val r = Rect(0, 0, 0, 0)
            // retrieve the fragment's windows rect
            content.getHitRect(r)
            // check if the event position is inside the window rect
            // if the event is not inside then we can close the fragment
            return r.contains(event.x.toInt(), event.y.toInt())
        }

        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        vm = getViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.userLiveData.observe(viewLifecycleOwner, Observer { userInfo -> userInfo?.let { renderUserInfo(it) } })
        vm.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.RUNNING -> progress_bar.visibility = View.VISIBLE
                Status.FINISHED -> progress_bar.visibility = View.INVISIBLE
                Status.ERROR -> progress_bar.visibility = View.INVISIBLE
            }
        })
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