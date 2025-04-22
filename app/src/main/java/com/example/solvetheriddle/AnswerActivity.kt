package com.example.solvetheriddle

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solvetheriddle.databinding.ActivityAnswerBinding

class AnswerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnswerBinding
    private lateinit var riddleAnswer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.riddleTextView.text = MainActivity.riddleText
        riddleAnswer = MainActivity.riddleAnswer

        binding.checkAnswerButton.setOnClickListener {
            var userAnswer = binding.answerEditText.text.toString().trim()

            if (userAnswer.isEmpty())
                return@setOnClickListener
            else
                userAnswer = userAnswer.substring(0, 1).uppercase() + userAnswer.substring(1)

            userAnswer = userAnswer.replace('ё', 'е')
            riddleAnswer = riddleAnswer.replace('ё', 'е')
            MainActivity.isCorrect = userAnswer.equals(riddleAnswer, ignoreCase = true)
            MainActivity.userAnswer = userAnswer

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}