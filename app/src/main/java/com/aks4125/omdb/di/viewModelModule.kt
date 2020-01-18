package com.aks4125.omdb.di

import com.aks4125.omdb.viewmodel.MovieViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieViewModel(service = get()) }
}