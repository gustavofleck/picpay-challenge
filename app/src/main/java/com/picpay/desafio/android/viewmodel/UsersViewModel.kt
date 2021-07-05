package com.picpay.desafio.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.interactor.UsersInteractor
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.utils.SimpleResult
import com.picpay.desafio.android.utils.ThreadContextProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersViewModel(
    private val usersInteractor: UsersInteractor,
    private val threadContextProvider: ThreadContextProvider = ThreadContextProvider()
): ViewModel() {

    private val usersMutableViewState = MutableLiveData<UsersViewState>()
    fun usersViewState(): LiveData<UsersViewState> = usersMutableViewState

    fun getUsers() {
        usersMutableViewState.value = UsersViewState.Loading
        viewModelScope.launch(threadContextProvider.io) {
            val result = usersInteractor.getUsers()
            withContext(threadContextProvider.ui) { resultHandler(result) }
        }
    }

    private fun resultHandler(result: SimpleResult<List<User>>) {
        when (result) {
            is SimpleResult.Success -> successHandler(result.data)
            is SimpleResult.Error -> errorHandler(result.exception)
        }
    }

    private fun successHandler(data: List<User>) {
        usersMutableViewState.value = UsersViewState.Success(data)
    }

    private fun errorHandler(exception: Exception) {
        usersMutableViewState.value = UsersViewState.Error(exception)
    }

}
