package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizInfoRepository
import javax.inject.Inject

class GetNicknameUseCase @Inject constructor(
    private val repository: QuizInfoRepository
) {
    operator fun invoke(): String = repository.getNickname()
}