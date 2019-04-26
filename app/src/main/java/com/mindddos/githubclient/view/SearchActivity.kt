package com.mindddos.githubclient.view

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mindddos.githubclient.R
import com.mindddos.githubclient.adapters.SearchResultsAdapter
import com.mindddos.githubclient.repository.remote.models.UserItem
import com.mindddos.githubclient.utils.Status
import com.mindddos.githubclient.vm.SearchScreenVM
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    private var snackBar: Snackbar? = null
    private val vm by viewModel<SearchScreenVM>()
    private lateinit var rvAdapter: SearchResultsAdapter
    private var lastQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        vm.resultsLiveEvent.observe(this, Observer {
            if (it.userItems.isNotEmpty())
                placeholder.visibility = View.INVISIBLE
            else placeholder.visibility = View.VISIBLE

            setupRvItems(it.userItems)
        })

        vm.statusLiveEvent.observe(this, Observer {
            when (it) {
                Status.RUNNING -> {
                    findViewById<RelativeLayout>(R.id.placeholder).visibility = View.INVISIBLE
                    progress_bar.visibility = View.VISIBLE
                }
                Status.FINISHED -> {
                    snackBar?.dismiss()
                    progress_bar.visibility = View.INVISIBLE
                }
                Status.ERROR -> {
                    showRetrySnackBar(getString(R.string.error_text))
                    progress_bar.visibility = View.INVISIBLE
                }
                Status.NO_INTERNET -> {
                    showRetrySnackBar(getString(R.string.no_internet_alert))
                    progress_bar.visibility = View.INVISIBLE
                }
            }
        })


        setupSearchView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        rvAdapter = SearchResultsAdapter(listOf(), this)
        val layoutManager = LinearLayoutManager(this)
        rv_results.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            rv_results.context,
            layoutManager.orientation
        )
        rv_results.addItemDecoration(dividerItemDecoration)
        rv_results.adapter = rvAdapter
    }


    private fun setupSearchView() {
        sv_username.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // clearing screen from fragment if present
                supportFragmentManager.popBackStackImmediate()
                // clearing focus from search view
                sv_username.clearFocus()

                query?.let { vm.searchForQuery(query) }

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
    }

    private fun setupRvItems(items: List<UserItem>) {
        rvAdapter.setItems(items)
        rv_results.smoothScrollToPosition(0)
    }

    private fun showRetrySnackBar(text: String) {
        snackBar = Snackbar.make(search_results, text, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) { vm.searchForQuery(lastQuery) }
        snackBar?.show()
    }


}
