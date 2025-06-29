package com.mcshr.sportquiz.data.firestore
import com.google.firebase.firestore.FirebaseFirestore
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizDataUploader @Inject constructor(
    private val firestore: FirebaseFirestore
){
    fun uploadAll() {
        CoroutineScope(Dispatchers.IO).launch {
            uploadCategory("EMOJI", emojiQuestions())
            uploadCategory("RIDDLE", riddleQuestions())
            uploadCategory("TEST", testQuestions())
        }
    }

    private fun uploadCategory(mode: String, questions: List<QuizQuestion>) {
        val collectionRef = firestore.collection("questions")
        for (question in questions) {
            val data = hashMapOf<String, Any>(
                "text" to question.text,
                "correctAnswers" to question.correctAnswer,
                "mode" to mode
            )

            question.hint?.let { data["hint"] = it }
            question.options?.let { data["options"] = it }

            collectionRef.add(data)
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
            "Один кричить, двоє мовчать, четверо працюють.", listOf("Бейсбол"), hint = "Біта і м'яч"
        ),
        QuizQuestion("Літає, як птах, та падає, як камінь.", listOf("Волан"), hint = "Бадмінтон"),
        QuizQuestion(
            "Що більше міцності додасть, той довше стояти зможе.",
            listOf("Штанга"),
            hint = "Жим важкого"
        ),
        QuizQuestion(
            "Через сітку літає, руками відбивають.", listOf("Волейбол"), hint = "Командна гра"
        ),
        QuizQuestion(
            "На доріжці не спіткнись, на фініші не запізнись.",
            listOf("Біг"),
            hint = "Швидкість і час"
        ),
        QuizQuestion(
            "Тиснуть силою — залізо газують...",
            listOf("Важка атлетика", "Пауерліфтинг"),
            hint = "Жим лежачи/станової тяги"
        ),
        QuizQuestion("Хто його кине, той і виграє...", listOf("М’яч"), hint = "Спортзал"),
        QuizQuestion(
            "Стрибає, крутиться, на льоду малює.",
            listOf("Фігурне катання"),
            hint = "Краса і точність"
        ),
        QuizQuestion(
            "Довгий стрибок, пісок у вічі...", listOf("Стрибки у довжину"), hint = "Легка атлетика"
        ),
        QuizQuestion("У воді не тоне...", listOf("Плавець"), hint = "Спорт у басейні")
    )

    private fun testQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            "Скільки гравців в одній футбольній команді на полі?",
            listOf("11"),
            options = listOf("9", "10", "11", "12")
        ), QuizQuestion(
            "Хто з цих осіб є футбольним тренером?",
            listOf("Зідан", "Шевченко"),
            options = listOf("Лопес", "Зідан", "Бондаренко", "Шевченко")
        ), QuizQuestion(
            "У якому виді спорту використовують ракетку та волан?",
            listOf("Бадмінтон"),
            options = listOf("Теніс", "Бадмінтон", "Сквош", "Настільний теніс")
        ), QuizQuestion(
            "Скільки хвилин триває один тайм у футболі?",
            listOf("45"),
            options = listOf("30", "40", "45", "60")
        ), QuizQuestion(
            "Який спорт асоціюється з олімпійськими кільцями?",
            listOf("Всі види спорту"),
            options = listOf("Футбол", "Легка атлетика", "Плавання", "Всі види спорту")
        ), QuizQuestion(
            "Хто з українців відомий як гімнаст?", listOf("Лілія Подкопєєва"), options = listOf(
                "Лілія Подкопєєва", "Олександр Усик", "Сергій Бубка", "Ярослава Магучіх"
            )
        ), QuizQuestion(
            "Який вид спорту називають 'королем спорту'?",
            listOf("Легка атлетика"),
            options = listOf("Футбол", "Бокс", "Легка атлетика", "Баскетбол")
        ), QuizQuestion(
            "Яке покриття найчастіше використовують у тенісі Вімблдону?",
            listOf("Трава"),
            options = listOf("Глина", "Трава", "Хард", "Пісок")
        ), QuizQuestion(
            "Хто з українців вигравав чемпіонат світу з боксу?",
            listOf("Олександр Усик"),
            options = listOf(
                "Андрій Шевченко", "Сергій Бубка", "Олександр Усик", "Ярослава Магучіх"
            )
        ), QuizQuestion(
            "У якому спорті є 'дабл-дабл' та 'трипл-дабл'?",
            listOf("Баскетбол"),
            options = listOf("Волейбол", "Футбол", "Баскетбол", "Теніс")
        ), QuizQuestion(
            "Яка тривалість марафонської дистанції?",
            listOf("42 км 195 м"),
            options = listOf("21 км", "35 км", "42 км", "42 км 195 м")
        ), QuizQuestion(
            "Скільки очок дається за триочковий кидок у баскетболі?",
            listOf("3"),
            options = listOf("1", "2", "3", "4")
        )
    )

}
