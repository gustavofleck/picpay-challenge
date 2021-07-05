package com.picpay.desafio.android.di

import com.picpay.desafio.android.data.interactor.UsersInteractor
import com.picpay.desafio.android.data.repository.UsersRepository
import com.picpay.desafio.android.data.service.PicPayServiceProvider
import com.picpay.desafio.android.viewmodel.UsersViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val picPayModule = module {

    single { PicPayServiceProvider(androidContext()).service() }
    factory { UsersRepository(picPayApi = get()) }
    factory { UsersInteractor(usersRepository = get()) }
    viewModel { UsersViewModel(usersInteractor = get()) }

}
