package com.picpay.desafio.android.data.interactor

import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repository.UsersRepository
import com.picpay.desafio.android.utils.SimpleResult

class UsersInteractor(
    private val usersRepository: UsersRepository
) {

    suspend fun getUsers() : SimpleResult<List<User>> {
        return try {
            SimpleResult.Success(usersRepository.getUsers())
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }

}
