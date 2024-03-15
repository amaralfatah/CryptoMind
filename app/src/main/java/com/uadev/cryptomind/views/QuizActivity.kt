package com.uadev.cryptomind.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.uadev.cryptomind.R
import com.uadev.cryptomind.model.Quiz
import com.uadev.cryptomind.viewmodels.QuizViewModel

class QuizActivity : AppCompatActivity() {

    private lateinit var quizViewModel: QuizViewModel

    private lateinit var timerTextView: TextView
    private lateinit var numberTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var submitButton: Button

    private lateinit var optionA: RadioButton
    private lateinit var optionB: RadioButton
    private lateinit var optionC: RadioButton
    private lateinit var optionD: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        // Initialize views
        initializeViews()

        quizViewModel.fetchQuizzes(object : QuizViewModel.QuizFetchListener {
            override fun onSuccess(quizzes: List<Quiz>) {
                showNextQuestion()
            }

            override fun onError(exception: Exception) {
                Toast.makeText(this@QuizActivity, "Failed to load quizzes", Toast.LENGTH_SHORT).show()
            }
        })

        submitButton.setOnClickListener {
            checkAnswer()
        }
    }

    private fun initializeViews() {
        timerTextView = findViewById(R.id.timerTextView)
        numberTextView = findViewById(R.id.numberTextView)
        questionTextView = findViewById(R.id.questionTextView)
        radioGroup = findViewById(R.id.optionsRadioGroup)
        submitButton = findViewById(R.id.submitButton)

        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
    }

    private fun showNextQuestion() {
        radioGroup.clearCheck()

        val currentQuiz = quizViewModel.getCurrentQuestion()
        if (currentQuiz != null) {
            numberTextView.text = "Soal ke - ${quizViewModel.getCurrentQuestionIndex() + 1}"
            questionTextView.text = currentQuiz.question
            optionA.text = currentQuiz.options[0]
            optionB.text = currentQuiz.options[1]
            optionC.text = currentQuiz.options[2]
            optionD.text = currentQuiz.options[3]
        } else {
            finishQuiz()
        }
    }

    private fun checkAnswer() {
        val selectedOptionIndex = radioGroup.indexOfChild(findViewById<RadioButton>(radioGroup.checkedRadioButtonId))

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
}
