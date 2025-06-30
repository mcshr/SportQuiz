package com.mcshr.sportquiz.ui.multiplayerSetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcshr.sportquiz.domain.interactors.GetNicknameUseCase
import com.mcshr.sportquiz.domain.interactors.SaveNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MultiplayerSetupViewModel @Inject constructor(
    getNicknameUseCase: GetNicknameUseCase,
    private val saveNicknameUseCase: SaveNicknameUseCase
) : ViewModel() {

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname

    init{
        _nickname.value = getNicknameUseCase()
    }

    fun saveNickname(nickname: String) {
        saveNicknameUseCase(nickname)
    }
}
