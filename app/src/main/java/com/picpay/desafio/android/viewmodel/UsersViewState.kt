package com.picpay.desafio.android.viewmodel

import com.picpay.desafio.android.data.model.User
import java.lang.Exception

sealed class UsersViewState {
    object Loading : UsersViewState()
    class Error(val exception: Exception) : UsersViewState()
    class Success(val users: List<User>) : UsersViewState()
}
