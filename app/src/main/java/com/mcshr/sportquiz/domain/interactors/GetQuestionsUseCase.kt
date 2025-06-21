package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor() {
    operator fun invoke(mode: QuizMode): List<QuizQuestion> {
        return when (mode) {
            QuizMode.EMOJI -> emojiQuestions()
            QuizMode.RIDDLE -> riddleQuestions()
            QuizMode.TEST -> testQuestions()
        }
    }

    private fun emojiQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion("⚽🐐", listOf("Мессі"), hint = "Футболіст"),
        QuizQuestion("🏀🐍", listOf("Кобі"), hint = "Грав за Lakers")
    )

    private fun riddleQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion("Не птах, а літає, в кільце мене кидають", listOf("М’яч"), hint = "Круглий предмет"),
        QuizQuestion("Коли мене кидають, я стрибаю — у сітку потрапляю", listOf("М'яч"), hint = "Знову ж таки")
    )

    private fun testQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            text = "Хто виграв Олімпіаду 2020 з футболу?",
            correctAnswer = listOf("Бразилія"),
            options = listOf("Аргентина", "Бразилія", "Франція", "Іспанія")
        )
    )

}