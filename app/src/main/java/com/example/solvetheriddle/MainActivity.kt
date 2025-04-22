package com.example.solvetheriddle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.solvetheriddle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        var riddleText: String = ""
        var riddleAnswer: String = ""
        var userAnswer: String = ""
        var isCorrect: Boolean = false
        var totalRiddles: Int = 0
        var correctCount: Int = 0
        var incorrectCount: Int = 0
        var currentRiddleIndex: Int? = null
        var riddleNumber: Int = 1
        var correctAnswers: Int = 0
        var incorrectAnswers: Int = 0
        val answeredRiddleIndices = mutableSetOf<Int>()
    }

    private val riddles = listOf(
        "Что это такое: всегда идет, но никогда не приходит?",
        "Что может наполнить комнату, но не занимает места?",
        "Что имеет шею, но не имеет голову?",
        "Где есть реки, но нет воды, есть города, но нет зданий и есть леса, но нет деревьев?",
        "Чем больше из неё берешь, тем больше она становится. Что это?",
        "Оно всегда перед нами, но видеть его мы не можем. Что это?",
        "Есть зубы, да укусить не может. Что это?",
        "Это нечто настолько хрупкое, что достаточно лишь назвать это по имени, как оно сразу же разрушается. Что это?",
        "Я путешествую по всему миру, но всегда остаюсь в уголке. Что это?",
        "Стоит корова, орать здорова. Как по зубам дашь – вою не оберёшься. Что это?",
        "Что имеет лицо и две руки, но не имеет ног?",
        "Человек полностью здоров, не умер, не инвалид, но выносят его из больницы на руках. Кто он?",
        "У какой птицы глаза на хвосте?",
        "Запрыгнуть на ходу в него можно, а выпрыгнуть на ходу из него нельзя. Что это?",
        "Кого бьют по голове, чтобы ровно шел?",
        "Два кольца, два конца, в середине гвоздик. Что это?",
        "Без рук, без ног, лапшу крошит. Что это?",
        "Маленький, пузатенький, а весь дом бережёт. Что это?",
        "Никого не обижает, а её все толкают. Что это?",
        "Кто ходит сидя?"
    )

    private val answers = listOf(
        "Время",
        "Свет",
        "Бутылка",
        "Карта",
        "Яма",
        "Будущее",
        "Расческа",
        "Тишина",
        "Почтовая марка",
        "Пианино",
        "Часы",
        "Младенец",
        "Павлин",
        "Поезд",
        "Гвоздь",
        "Ножницы",
        "Нож",
        "Замок",
        "Дверь",
        "Шахматист"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            currentRiddleIndex = savedInstanceState.getInt("current_riddle_index").takeIf { it != -1 }
            riddleNumber = savedInstanceState.getInt("riddle_number")
            correctAnswers = savedInstanceState.getInt("correct_answers")
            incorrectAnswers = savedInstanceState.getInt("incorrect_answers")

            val savedIndices = savedInstanceState.getIntArray("answered_riddle_indices")
            if (savedIndices != null)
                answeredRiddleIndices.addAll(savedIndices.toSet())
        }

        binding.getRiddleButton.setOnClickListener {
            if (riddleNumber <= 10) {
                getRandomRiddle()
                binding.answerButton.isEnabled = true
            } else
                binding.getRiddleButton.isEnabled = false
        }

        binding.answerButton.setOnClickListener {
            currentRiddleIndex?.let { index ->
                riddleText = binding.riddleTextView.text.toString()
                riddleAnswer = answers[index]
                val intent = Intent(this, AnswerActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }

        binding.statisticsButton.setOnClickListener {
            totalRiddles = riddleNumber - 1
            correctCount = correctAnswers
            incorrectCount = incorrectAnswers
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getRandomRiddle() {
        val availableRiddleIndices = riddles.indices.filter { !answeredRiddleIndices.contains(it) }

        if (availableRiddleIndices.isEmpty()) {
            answeredRiddleIndices.clear()
            val allIndices = riddles.indices.toList()
            currentRiddleIndex = allIndices.random()
            binding.riddleTextView.text = riddles[currentRiddleIndex!!]
            updateUI()
            return
        }

        currentRiddleIndex = availableRiddleIndices.random()
        binding.riddleTextView.text = riddles[currentRiddleIndex!!]
        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val correctAnswer = answers[currentRiddleIndex!!]

            binding.resultTextView.text = "Ваш ответ: $userAnswer\n${if (!isCorrect) "Правильный ответ: $correctAnswer\n" else "" }"
            binding.resultTextView2.text = "${if (isCorrect) "Правильно!" else "Неправильно!"}"
            binding.answerButton.isEnabled = false

            if (isCorrect)
                correctAnswers++
            else
                incorrectAnswers++

            riddleNumber++
            answeredRiddleIndices.add(currentRiddleIndex!!)
            if (riddleNumber > 10) {
                binding.statisticsButton.isEnabled = true
                binding.getRiddleButton.isEnabled = false
                binding.answerButton.isEnabled = false
                binding.statisticTextView.text = "Все загадки прорешаны!\nПерейдите к статистике."
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("current_riddle_index", currentRiddleIndex ?: -1)
        outState.putInt("riddle_number", riddleNumber)
        outState.putInt("correct_answers", correctAnswers)
        outState.putInt("incorrect_answers", incorrectAnswers)

        outState.putIntArray("answered_riddle_indices", answeredRiddleIndices.toIntArray())
    }

    private fun updateUI() {
        binding.riddleNumberTextView.text = "Загадка ${riddleNumber}/10"
        binding.answerButton.isEnabled = currentRiddleIndex != null && riddleNumber <= 10
        binding.resultTextView.text = " "
        binding.resultTextView2.text = " "
        binding.statisticTextView.text = " "
    }
}