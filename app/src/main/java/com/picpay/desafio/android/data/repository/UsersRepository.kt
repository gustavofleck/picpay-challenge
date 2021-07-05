package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.service.PicPayService

class UsersRepository(
    private val picPayApi: PicPayService
) {
    suspend fun getUsers() = picPayApi.getUsers()
}
