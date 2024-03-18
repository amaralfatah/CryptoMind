package com.uadev.cryptomind.views

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityQuizBinding
import com.uadev.cryptomind.model.Quiz
import com.uadev.cryptomind.viewmodels.QuizViewModel
import java.util.Locale

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 60000 // Waktu dalam milidetik (60 detik)
    private val COUNTDOWN_INTERVAL: Long = 1000 // Interval countdown dalam milidetik (1 detik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.actionContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        // Inisialisasi timer
        startTimer()

        quizViewModel.fetchQuizzes(object : QuizViewModel.QuizFetchListener {
            override fun onSuccess(quizzes: List<Quiz>) {
                showNextQuestion()
            }

            override fun onError(exception: Exception) {
                Toast.makeText(this@QuizActivity, "Failed to load quizzes", Toast.LENGTH_SHORT).show()
            }
        })

        binding.submitButton.setOnClickListener {
            checkAnswer()
        }

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Quiz"
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                updateCountDownText()
                finishQuiz()
            }
        }

        countDownTimer.start()
    }

    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.timerTextView.text = timeLeftFormatted
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle klik tombol kembali di action bar
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showNextQuestion() {
        binding.optionsRadioGroup.clearCheck()

        val currentQuiz = quizViewModel.getCurrentQuestion()
        if (currentQuiz != null) {
            binding.numberTextView.text = "Soal ke - ${quizViewModel.getCurrentQuestionIndex() + 1}"
            binding.questionTextView.text = currentQuiz.question
            binding.optionA.text = currentQuiz.options[0]
            binding.optionB.text = currentQuiz.options[1]
            binding.optionC.text = currentQuiz.options[2]
            binding.optionD.text = currentQuiz.options[3]
        } else {
            finishQuiz()
        }
    }

    private fun checkAnswer() {
        val selectedOptionIndex = binding.optionsRadioGroup.indexOfChild(binding.optionsRadioGroup.findViewById<RadioButton>(binding.optionsRadioGroup.checkedRadioButtonId))

        val currentQuiz = quizViewModel.getCurrentQuestion()

        if (currentQuiz != null && selectedOptionIndex == currentQuiz.correctOptionIndex) {
            // Correct answer
            quizViewModel.increaseScore()
            Toast.makeText(this, "Jawaban benar!", Toast.LENGTH_SHORT).show()
        } else {
            // Wrong answer
            Toast.makeText(this, "Jawaban salah!", Toast.LENGTH_SHORT).show()
        }

        quizViewModel.moveToNextQuestion()
        showNextQuestion()
    }

    private fun finishQuiz() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", quizViewModel.getScore())
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

}
