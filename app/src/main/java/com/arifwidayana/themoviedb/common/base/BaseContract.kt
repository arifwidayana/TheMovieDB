package com.arifwidayana.themoviedb.common.base

import androidx.navigation.NavDirections

interface BaseContract {
    fun showContent(isVisible: Boolean)
    fun showEmptyContent(isVisible: Boolean)
    fun showErrorContent(isVisible: Boolean)
    fun showLoading(isVisible: Boolean)
    fun showMessageToast(isEnabled: Boolean, message: String? = null)
    fun showMessageSnackBar(isEnabled: Boolean, message: String? = null)
    fun moveNav()
    fun moveNav(navUp: Int)
    fun moveNav(direction: NavDirections)
}