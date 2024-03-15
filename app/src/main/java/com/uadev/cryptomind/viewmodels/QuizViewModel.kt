package com.uadev.cryptomind.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.uadev.cryptomind.model.Quiz
import java.lang.Exception

class QuizViewModel : ViewModel() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var quizzesRef: DatabaseReference

    private var questions: MutableList<Quiz> = mutableListOf()
    private var currentQuestionIndex = 0
    private var score = 0

    fun fetchQuizzes(listener: QuizFetchListener) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        quizzesRef = firebaseDatabase.getReference("quizzes")

        quizzesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questions.clear()
                for (quizSnapshot in snapshot.children) {
                    val quiz = quizSnapshot.getValue(Quiz::class.java)
                    quiz?.let { questions.add(it) }
                }
                listener.onSuccess(questions)
            }

            override fun onCancelled(error: DatabaseError) {
                listener.onError(Exception("Failed to load quizzes"))
            }
        })
    }

    fun getCurrentQuestion(): Quiz? {
        return if (currentQuestionIndex < questions.size) {
            questions[currentQuestionIndex]
        } else {
            null
        }
    }

    fun moveToNextQuestion() {
        currentQuestionIndex++
    }

    fun increaseScore() {
        score++
    }

    fun getCurrentQuestionIndex(): Int {
        return currentQuestionIndex
    }

    fun getScore(): Int {
        return score
    }

    interface QuizFetchListener {
        fun onSuccess(quizzes: List<Quiz>)
        fun onError(exception: Exception)
    }
}
