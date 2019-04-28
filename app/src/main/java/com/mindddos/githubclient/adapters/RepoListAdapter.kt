package com.mindddos.githubclient.adapters

import android.annotation.SuppressLint
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.mindddos.githubclient.R
import com.mindddos.githubclient.repository.remote.models.Repository
import com.mindddos.githubclient.utils.TextUtils
import kotlinx.android.synthetic.main.item_repo.view.*
import kotlinx.android.synthetic.main.item_search_result.view.*

class RepoListAdapter(
    private var items: List<Repository>,
    private val activity: AppCompatActivity
) :
    RecyclerView.Adapter<RepoListAdapter.RepositoryVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryVH {
        return RepositoryVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repo, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Repository>) {
        this.items = items
        notifyDataSetChanged()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RepositoryVH, position: Int) {
        holder.tvName.movementMethod = LinkMovementMethod()
        holder.tvName.text = TextUtils.getHyperlinkSpannable(items[position].name, items[position].html_url)

        holder.tvLang.text = items[position].language
        holder.tvForks.text = items[position].forks_count.toString()
        holder.tvStars.text = items[position].stargazers_count.toString()
    }

    class RepositoryVH(v: View) : RecyclerView.ViewHolder(v) {
        val tvName = v.tv_name
        val tvLang = v.tv_lang
        val tvForks = v.tv_fork_count
        val tvStars = v.tv_stars_count
    }
}