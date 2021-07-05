package com.picpay.desafio.android.data.interactor

import com.picpay.desafio.android.data.repository.UsersRepository
import com.picpay.desafio.android.utils.SimpleResult
import com.picpay.desafio.android.utils.usersStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


internal class UsersInteractorTest {

    private val usersRepositoryMock = mockk<UsersRepository>(relaxed = true)
    private val interactor = UsersInteractor(usersRepositoryMock)

    @Nested()
    @DisplayName("GIVEN a successful request")
    inner class SuccessResult{

        private val userList = usersStub()

        @BeforeEach
        fun setUp() {
            coEvery { usersRepositoryMock.getUsers() } returns userList
        }

        @Test
        fun `SHOULD return a success result`() {
            val result = runBlocking { interactor.getUsers() }

            assertTrue(result is SimpleResult.Success)
        }

        @Test
        fun `SHOULD return a user list`() {
            val result = runBlocking { interactor.getUsers() as SimpleResult.Success }

            assertEquals(userList, result.data)
        }
    }

    @Nested()
    @DisplayName("GIVEN a failure request")
    inner class ErrorResult {

        private val exception = Exception()

        @BeforeEach
        fun setUp() {
            coEvery { usersRepositoryMock.getUsers() } throws exception
        }

        @Test
        fun `SHOULD return a error result`() {
            val result = runBlocking { interactor.getUsers() }

            assertTrue(result is SimpleResult.Error)
        }

        @Test
        fun `SHOULD return an exception`() {
            val result = runBlocking { interactor.getUsers() as SimpleResult.Error }

            assertEquals(exception, result.exception)
        }
    }

}
