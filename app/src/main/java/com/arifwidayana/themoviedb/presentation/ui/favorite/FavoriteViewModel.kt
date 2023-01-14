package com.arifwidayana.themoviedb.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arifwidayana.themoviedb.common.wrapper.Resource
import com.arifwidayana.themoviedb.data.local.model.entity.FavoriteEntity
import com.arifwidayana.themoviedb.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
): FavoriteContract, ViewModel(){
    private val _getFavoriteResult = MutableStateFlow<Resource<List<FavoriteEntity>>>(Resource.Empty())
    override val getFavoriteResult: StateFlow<Resource<List<FavoriteEntity>>> = _getFavoriteResult

    override fun getFavorite() {
        viewModelScope.launch {
            favoriteRepository.getFavorite().collect {
                try {
                    if (!it.data.isNullOrEmpty()) {
                        _getFavoriteResult.value = Resource.Success(it.data)
                    } else {
                        _getFavoriteResult.value = Resource.Error(message = "You don't have favorite movies")

                    }
                } catch (e: Exception) {
                    _getFavoriteResult.value = Resource.Error(message = e.message.orEmpty())
                }
            }
        }
    }
}