package com.picpay.desafio.android.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.viewmodel.UsersViewModel
import com.picpay.desafio.android.viewmodel.UsersViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

const val USER_LIST = "USERS"

class MainActivity : AppCompatActivity() {

    private val viewModel: UsersViewModel by viewModel()
    private var usersAdapter = UserListAdapter()
    private var userList: ArrayList<User> = arrayListOf()
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUsers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        userList.addAll(usersAdapter.users)
        outState.putSerializable(USER_LIST, userList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userList = savedInstanceState.getSerializable(USER_LIST) as ArrayList<User>
    }

    private fun setupRecyclerView() {
        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = usersAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.usersViewState().observe(this, {
            viewHandler(it)
        })
    }

    private fun viewHandler(it: UsersViewState?) {
        when(it) {
            is UsersViewState.Loading -> viewBinding.userListProgressBar.visibility = View.VISIBLE
            is UsersViewState.Error -> errorViewState()
            is UsersViewState.Success -> successViewState(it.users)
        }
    }

    private fun errorViewState() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    private fun successViewState(users: List<User>) {
        viewBinding.userListProgressBar.visibility = View.GONE
        usersAdapter.users = users
    }
}
