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
        QuizQuestion("⚽🥅👟", listOf("Футбол"), hint = "Грають ногами"),
        QuizQuestion("🏀⛹️‍♂️🏆", listOf("Баскетбол"), hint = "М’яч в кільце"),
        QuizQuestion("🎾🏃‍♂️🏸", listOf("Теніс"), hint = "Ракетка і м’яч"),
        QuizQuestion("🏀🐍", listOf("Кобі"), hint = "Грав за Lakers"),
        QuizQuestion("🥊👊🧤", listOf("Бокс"), hint = "Удар руками"),
        QuizQuestion("⛷️❄️🏁", listOf("Лижі", "Лижний спорт"), hint = "Зимовий спорт"),
        QuizQuestion("🏃‍♂️⏱️🏁", listOf("Біг", "Легкоатлетика"), hint = "Час і швидкість"),
        QuizQuestion("🚴‍♂️⛰️🚵‍♀️", listOf("Велоспорт", "Велосипед"), hint = "Крутиш педалі"),
        QuizQuestion("🏐🙌🌐", listOf("Волейбол"), hint = "Через сітку руками"),
        QuizQuestion("🥋🇯🇵🥋", listOf("Дзюдо"), hint = "Японське бойове мистецтво"),
        QuizQuestion("🏆⚾️🇺🇸", listOf("МЛБ"), hint = "Вища бейсбольна ліга США"),
        QuizQuestion("⚽🐐", listOf("Мессі"), hint = "Футболіст"),
    )

    private fun riddleQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            "Один кричить, двоє мовчать, четверо працюють.",
            listOf("Бейсбол"),
            hint = "Біта і м'яч"
        ),
        QuizQuestion("Літає, як птах, та падає, як камінь.", listOf("Волан"), hint = "Бадмінтон"),
        QuizQuestion(
            "Що більше міцності додасть, той довше стояти зможе.",
            listOf("Штанга"),
            hint = "Жим важкого"
        ),
        QuizQuestion(
            "Через сітку літає, руками відбивають.",
            listOf("Волейбол"),
            hint = "Командна гра"
        ),
        QuizQuestion(
            "На доріжці не спіткнись, на фініші не запізнись.",
            listOf("Біг"),
            hint = "Швидкість і час"
        ),
        QuizQuestion(
            "Тиснуть силою — залізо газують, м'яз заливає, рекорд досягають.",
            listOf("Важка атлетика", "Пауерліфтинг"),
            hint = "Жим лежачи/станової тяги"
        ),
        QuizQuestion(
            "Хто його кине, той і виграє, хто зловить — той втратить.",
            listOf("М’яч"),
            hint = "Спортзал"
        ),
        QuizQuestion(
            "Стрибає, крутиться, на льоду малює.",
            listOf("Фігурне катання"),
            hint = "Краса і точність"
        ),
        QuizQuestion(
            "Довгий стрибок, пісок у вічі, а перемога — у руках.",
            listOf("Стрибки у довжину"),
            hint = "Легка атлетика"
        ),
        QuizQuestion(
            "У воді не тоне, у повітрі не літає, а рекорд встановлює.",
            listOf("Плавець"),
            hint = "Спорт у басейні"
        )
    )

    private fun testQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            text = "Скільки гравців в одній футбольній команді на полі?",
            correctAnswer = listOf("11"),
            options = listOf("9", "10", "11", "12")
        ),
        QuizQuestion(
            text = "Хто з цих осіб є футбольним тренером?",
            correctAnswer = listOf("Зідан", "Шевченко"),
            options = listOf("Лопес", "Зідан", "Бондаренко", "Шевченко")
        ),
        QuizQuestion(
            text = "У якому виді спорту використовують ракетку та волан?",
            correctAnswer = listOf("Бадмінтон"),
            options = listOf("Теніс", "Бадмінтон", "Сквош", "Настільний теніс")
        ),
        QuizQuestion(
            text = "Скільки хвилин триває один тайм у футболі?",
            correctAnswer = listOf("45"),
            options = listOf("30", "40", "45", "60")
        ),
        QuizQuestion(
            text = "Який спорт асоціюється з олімпійськими кільцями?",
            correctAnswer = listOf("Всі види спорту"),
            options = listOf("Футбол", "Легка атлетика", "Плавання", "Всі види спорту")
        ),
        QuizQuestion(
            text = "Хто з українців відомий як гімнаст?",
            correctAnswer = listOf("Лілія Подкопєєва"),
            options = listOf(
                "Лілія Подкопєєва",
                "Олександр Усик",
                "Сергій Бубка",
                "Ярослава Магучіх"
            )
        ),
        QuizQuestion(
            text = "Який вид спорту називають 'королем спорту'?",
            correctAnswer = listOf("Легка атлетика"),
            options = listOf("Футбол", "Бокс", "Легка атлетика", "Баскетбол")
        ),
        QuizQuestion(
            text = "Яке покриття найчастіше використовують у тенісі Вімблдону?",
            correctAnswer = listOf("Трава"),
            options = listOf("Глина", "Трава", "Хард", "Пісок")
        ),
        QuizQuestion(
            text = "Хто з українців вигравав чемпіонат світу з боксу?",
            correctAnswer = listOf("Олександр Усик"),
            options = listOf(
                "Андрій Шевченко",
                "Сергій Бубка",
                "Олександр Усик",
                "Ярослава Магучіх"
            )
        ),
        QuizQuestion(
            text = "У якому спорті є 'дабл-дабл' та 'трипл-дабл'?",
            correctAnswer = listOf("Баскетбол"),
            options = listOf("Волейбол", "Футбол", "Баскетбол", "Теніс")
        ),
        QuizQuestion(
            text = "Яка тривалість марафонської дистанції?",
            correctAnswer = listOf("42 км 195 м"),
            options = listOf("21 км", "35 км", "42 км", "42 км 195 м")
        ),
        QuizQuestion(
            text = "Скільки очок дається за триочковий кидок у баскетболі?",
            correctAnswer = listOf("3"),
            options = listOf("1", "2", "3", "4")
        )
    )

}