package com.picpay.desafio.android.viewmodel

import androidx.lifecycle.Observer
import com.picpay.desafio.android.data.interactor.UsersInteractor
import com.picpay.desafio.android.utils.InstantExecutorExtension
import com.picpay.desafio.android.utils.SimpleResult
import com.picpay.desafio.android.utils.TestThreadContextProvider
import com.picpay.desafio.android.utils.usersStub
import io.mockk.*
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class UsersViewModelTest {

    private val userList = usersStub()
    private val observerViewStateMock = mockk<Observer<UsersViewState>>(relaxed = true)
    private val loadingViewState = UsersViewState.Loading
    private val errorViewStateSlot = slot<UsersViewState.Error>()
    private val successViewStateSlot = slot<UsersViewState.Success>()
    private val usersInteractorMock = mockk<UsersInteractor>(relaxed = true)
    private lateinit var usersViewModel: UsersViewModel

    @BeforeEach
    fun setUp() {
        usersViewModel = UsersViewModel(
            usersInteractorMock,
            TestThreadContextProvider()
        )

        usersViewModel.usersViewState().observeForever(observerViewStateMock)
    }

    @Test
    fun `SHOULD change view state to Loading, WHEN 'getUser' is called`() {
        coEvery { usersInteractorMock.getUsers() } returns SimpleResult.Success(userList)

        runBlocking {
            usersViewModel.getUsers()
        }

        verify {
            observerViewStateMock.onChanged(loadingViewState)
        }
    }

    @Test
    fun `SHOULD change view state to Success, WHEN users are retrieved`() {
        coEvery { usersInteractorMock.getUsers() } returns SimpleResult.Success(userList)

        runBlocking {
            usersViewModel.getUsers()
        }

        verifySequence {
            observerViewStateMock.onChanged(loadingViewState)
            observerViewStateMock.onChanged(capture(successViewStateSlot))
        }

        assertEquals(userList, successViewStateSlot.captured.users)
    }

    @Test
    fun `SHOULD change view state to Error, WHEN cannot get users`() {
        val exception = Exception()
        coEvery { usersInteractorMock.getUsers() } returns SimpleResult.Error(exception)

        runBlocking {
            usersViewModel.getUsers()
        }

        verifySequence {
            observerViewStateMock.onChanged(loadingViewState)
            observerViewStateMock.onChanged(capture(errorViewStateSlot))
        }

        assertEquals(exception, errorViewStateSlot.captured.exception)
    }
}
