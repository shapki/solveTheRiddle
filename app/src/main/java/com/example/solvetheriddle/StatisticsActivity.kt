package com.example.solvetheriddle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.solvetheriddle.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.totalRiddlesTextView.text = "Вариантов загадок: 20\nПрорешано: ${MainActivity.totalRiddles}"
        binding.correctAnswersTextView.text = "Правильных ответов: ${MainActivity.correctCount}"
        binding.incorrectAnswersTextView.text = "Неправильных ответов: ${MainActivity.incorrectCount}"

        binding.restartButton.setOnClickListener {
            MainActivity.totalRiddles = 0
            MainActivity.correctCount = 0
            MainActivity.incorrectCount = 0
            MainActivity.currentRiddleIndex = null
            MainActivity.riddleNumber = 1
            MainActivity.correctAnswers = 0
            MainActivity.incorrectAnswers = 0
            MainActivity.answeredRiddleIndices.clear()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.exitButton.setOnClickListener {
            finishAffinity()
        }
    }
}