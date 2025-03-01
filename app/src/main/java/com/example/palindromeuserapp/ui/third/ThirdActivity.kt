package com.example.palindromeuserapp.ui.third

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.palindromeuserapp.R
import com.example.palindromeuserapp.model.User
import com.example.palindromeuserapp.model.UserResponse
import com.example.palindromeuserapp.network.ApiClient
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class ThirdActivity : AppCompatActivity() {

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvUsers: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var userAdapter: UserAdapter

    private var currentPage = 1
    private val perPage = 6
    private var totalPages = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        swipeRefresh = findViewById(R.id.swipeRefresh)
        rvUsers = findViewById(R.id.rvUsers)
        tvEmpty = findViewById(R.id.tvEmpty)

        userAdapter = UserAdapter { user ->
            val intent = Intent().apply {
                putExtra("EXTRA_SELECTED_USER", "${user.first_name} ${user.last_name}")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        rvUsers.layoutManager = LinearLayoutManager(this@ThirdActivity)
        rvUsers.adapter = userAdapter

        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.custom_divider)
        val dividerItemDecoration = DividerItemDecoration(
            rvUsers.context,
            (rvUsers.layoutManager as LinearLayoutManager).orientation
        )
        dividerDrawable?.let {
            dividerItemDecoration.setDrawable(it)
            rvUsers.addItemDecoration(dividerItemDecoration)
        }

        swipeRefresh.setOnRefreshListener {
            currentPage = 1
            loadUsers(isRefresh = true)
        }

        rvUsers.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore() {
                if (!isLoading && currentPage < totalPages) {
                    currentPage++
                    loadUsers(isRefresh = false)
                }
            }
        })

        loadUsers(isRefresh = false)
    }

    private fun loadUsers(isRefresh: Boolean) {
        isLoading = true
        lifecycleScope.launch {
            try {
                val response = ApiClient.service.getUsers(page = currentPage, perPage = perPage)
                if (response.isSuccessful) {
                    val body: UserResponse? = response.body()
                    body?.let {
                        totalPages = it.total_pages
                        val listUser: List<User> = it.data

                        if (listUser.isEmpty()) {
                            rvUsers.visibility = View.GONE
                            tvEmpty.visibility = View.VISIBLE
                        } else {
                            rvUsers.visibility = View.VISIBLE
                            tvEmpty.visibility = View.GONE

                            if (isRefresh) {
                                userAdapter.setData(listUser)
                                swipeRefresh.isRefreshing = false
                            } else {
                                if (currentPage == 1) {
                                    userAdapter.setData(listUser)
                                } else {
                                    userAdapter.addData(listUser)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        if (lastVisibleItem == totalItemCount - 1) {
            onLoadMore()
        }
    }

    abstract fun onLoadMore()
}
