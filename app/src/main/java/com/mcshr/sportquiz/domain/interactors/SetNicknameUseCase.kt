package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizInfoRepository
import javax.inject.Inject

class SaveNicknameUseCase @Inject constructor(
    private val repository: QuizInfoRepository
) {
    operator fun invoke(nickname: String){
        repository.saveNickname(nickname)
    }
}