package com.arifwidayana.themoviedb.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class BaseGenericViewModel(private val viewModel: ViewModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(viewModel::class.java) -> {
                return viewModel as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}