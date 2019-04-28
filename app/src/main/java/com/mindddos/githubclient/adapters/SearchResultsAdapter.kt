package com.mindddos.githubclient.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mindddos.githubclient.R
import com.mindddos.githubclient.repository.remote.models.UserItem
import com.mindddos.githubclient.utils.initDebouncedClickListener
import com.mindddos.githubclient.view.UserDetailsActivity
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: SearchResultVH, position: Int) {

        holder.tvUserName.rootView.initDebouncedClickListener {
            UserDetailsActivity.startActivity(activity, items[position].login)
        }


        val requestOptions = RequestOptions().placeholder(R.drawable.ic_gh_logo)

        Glide.with(activity)
            .setDefaultRequestOptions(requestOptions)
            .load(items[position].avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.ivIcon)
        holder.tvUserName.text = items[position].login
    }

    class SearchResultVH(v: View) : RecyclerView.ViewHolder(v) {
        val ivIcon = v.iv_icon
        val tvUserName = v.tv_username
    }
}