package com.uadev.cryptomind.views.kuis

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityQuizBinding
import com.uadev.cryptomind.model.Quiz
import com.uadev.cryptomind.viewmodels.QuizViewModel
import io.github.muddz.styleabletoast.StyleableToast
import java.util.Locale

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 300000 // milliseconds (300 seconds)
    private val COUNTDOWN_INTERVAL: Long = 1000 // countdown interval in milliseconds (1 second)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.actionContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        // Initialize timer
        startTimer()

        // Show progress bar while fetching quizzes
        binding.loadingProgressBar.visibility = View.VISIBLE

        quizViewModel.fetchQuizzes(object : QuizViewModel.QuizFetchListener {
            override fun onSuccess(quizzes: List<Quiz>) {
                // Hide progress bar once data is fetched
                binding.loadingProgressBar.visibility = View.GONE
                showNextQuestion()
            }

            override fun onError(exception: Exception) {
                // Hide progress bar in case of error
                binding.loadingProgressBar.visibility = View.GONE
                Toast.makeText(this@QuizActivity, "Gagal memuat kuis", Toast.LENGTH_SHORT).show()
            }
        })

        binding.submitButton.setOnClickListener {
            checkAnswer()
        }

        // Enable action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Kuis"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))
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
            binding.numberTextView.text = "Soal ${quizViewModel.getCurrentQuestionIndex() + 1}"
            binding.questionTextView.text = currentQuiz.question
            val options = currentQuiz.options
            binding.optionA.text = options[0]
            binding.optionB.text = options[1]
            binding.optionC.text = options[2]
            binding.optionD.text = options[3]
        } else {
            finishQuiz()
        }
    }

    private fun checkAnswer() {
        val selectedOptionIndex = binding.optionsRadioGroup.indexOfChild(binding.optionsRadioGroup.findViewById<RadioButton>(binding.optionsRadioGroup.checkedRadioButtonId))

        if (selectedOptionIndex == -1) {
//            Toast.makeText(this, "Silakan pilih salah satu jawaban", Toast.LENGTH_SHORT).show()
            StyleableToast.makeText(this, "Anda belum memilih jawaban!", Toast.LENGTH_SHORT, R.style.toastNormal).show()
            return
        }

        val currentQuiz = quizViewModel.getCurrentQuestion()

        if (currentQuiz != null && selectedOptionIndex == currentQuiz.correctOptionIndex) {
            // Correct answer
            quizViewModel.increaseScore()
//            Toast.makeText(this, "Jawaban benar!", Toast.LENGTH_SHORT).show()
            StyleableToast.makeText(this, "Jawaban benar!", Toast.LENGTH_SHORT, R.style.toastTrue).show()
        } else {
            // Wrong answer
//            Toast.makeText(this, "Jawaban salah!", Toast.LENGTH_SHORT).show()
            StyleableToast.makeText(this, "Jawaban Salah!", Toast.LENGTH_SHORT, R.style.toastFalse).show()
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
