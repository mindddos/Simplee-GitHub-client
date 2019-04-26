package com.mindddos.githubclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mindddos.githubclient.R
import com.mindddos.githubclient.repository.remote.models.UserItem
import com.mindddos.githubclient.utils.initDebouncedClickListener
import com.mindddos.githubclient.view.UserDetailsFragment
import kotlinx.android.synthetic.main.item_search_result.view.*

class SearchResultsAdapter(
    private var items: List<UserItem>,
    private val activity: AppCompatActivity
) :
    RecyclerView.Adapter<SearchResultsAdapter.SearchResultVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultVH {
        return SearchResultVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<UserItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SearchResultVH, position: Int) {

        holder.tvUserName.rootView.initDebouncedClickListener {
            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, UserDetailsFragment.newInstance(items[position].login))
                .addToBackStack(null)
                .commit()
        }

        val requestOptions = RequestOptions().placeholder(R.drawable.ic_photo_placeholder)

        Glide.with(activity)
            .setDefaultRequestOptions(requestOptions)
            .load(items[position].avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.ivIcon)
        holder.tvUserName.text = items[position].login
        holder.tvScore.text = Math.floor(items[position].score).toInt().toString()
    }

    class SearchResultVH(v: View) : RecyclerView.ViewHolder(v) {
        val ivIcon = v.iv_icon
        val tvUserName = v.tv_username
        val tvScore = v.tv_score

    }
}