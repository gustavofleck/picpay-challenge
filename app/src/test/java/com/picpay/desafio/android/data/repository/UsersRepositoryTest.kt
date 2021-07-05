package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.utils.usersStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class UsersRepositoryTest {

    private val picPayApiMock = mockk<PicPayService>(relaxed = true)
    private val repository = UsersRepository(picPayApiMock)

    @Test
    fun `SHOULD request a list of users, WHEN 'getUsers' is called`() {
        coEvery { picPayApiMock.getUsers() } returns usersStub()

        runBlocking { repository.getUsers() }

        coVerify { picPayApiMock.getUsers() }
    }

}
