package com.example.gaenari.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.content.Intent
import android.widget.TextView
import android.widget.ImageView
import android.graphics.Color
import android.animation.ObjectAnimator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import com.example.gaenari.activity.dactivity.DActivity
import com.example.gaenari.activity.iactivity.IActivity
import com.example.gaenari.activity.tactivity.TActivity
import android.util.Log
import com.example.gaenari.R
import com.example.gaenari.activity.dactivity.DRunningService
import com.example.gaenari.activity.iactivity.IRunningService
import com.example.gaenari.activity.iactivity.IntervalService
import com.example.gaenari.activity.main.Program
import com.example.gaenari.activity.tactivity.TRunningService
import com.example.gaenari.dto.response.FavoriteResponseDto

class CountdownActivity : AppCompatActivity() {
    override fun onCreate(savedCreateState: Bundle?) {
        super.onCreate(savedCreateState)
        setContentView(R.layout.activity_countdown)

        val countdownTextView = findViewById<TextView>(R.id.countdownTextView)
        val imageView = findViewById<ImageView>(R.id.imageView)

        val programType = intent.getStringExtra("programType")

        val textSizeStart = 90f
        val textSizeEnd = 75f
        val textColorStart = Color.BLACK
        val textColorEnd = Color.DKGRAY
        val borderColorStart = Color.GREEN
        val borderColorEnd = Color.LTGRAY
        val animationDuration = 1000L

        object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                countdownTextView.text = secondsRemaining.toString()
                if(secondsRemaining==5 && programType=="D") {
                    Log.d("countdown", "onTick: DDDDDㅎㅇㅎㅇ")
                    startDRunningService()
                }
                if(secondsRemaining==5 && programType=="T") {
                    Log.d("countdown", "onTick: TTTTㅎㅇㅎㅇ")
                    startTRunningService()
                }
                if(secondsRemaining==5 && programType=="I") {
                    Log.d("countdown", "onTick: IIIIㅎㅇㅎㅇ")
                    startIRunningService()
                }
                // 텍스트 크기 애니메이션
                val textSizeAnimator = ObjectAnimator.ofFloat(
                    countdownTextView, "textSize", textSizeStart, textSizeEnd
                )
                textSizeAnimator.duration = animationDuration
                textSizeAnimator.repeatCount = 1
                textSizeAnimator.repeatMode = ObjectAnimator.REVERSE
                textSizeAnimator.start()

                // 텍스트 색상 애니메이션
                val textColorAnimator = ObjectAnimator.ofObject(
                    countdownTextView, "textColor", ArgbEvaluator(), textColorStart, textColorEnd
                )
                textColorAnimator.duration = animationDuration
                textColorAnimator.repeatCount = 1
                textColorAnimator.repeatMode = ObjectAnimator.REVERSE
                textColorAnimator.start()

                // 이미지뷰 배경색 애니메이션
                val imageColorAnimator = ValueAnimator.ofObject(
                    ArgbEvaluator(), borderColorStart, borderColorEnd
                )
                imageColorAnimator.duration = animationDuration
                imageColorAnimator.repeatCount = 1
                imageColorAnimator.addUpdateListener { animator ->
                    val color = animator.animatedValue as Int
                    imageView.setBackgroundColor(color)
                }
                imageColorAnimator.start()
            }

            override fun onFinish() {
                val intent = when (programType) {
                    "D" -> Intent(this@CountdownActivity, DActivity::class.java)
                    "T" -> Intent(this@CountdownActivity, TActivity::class.java)
                    "I" -> Intent(this@CountdownActivity, IActivity::class.java)
                    else -> null
                }

                if (intent != null) {
                    intent.putExtras(this@CountdownActivity.intent.extras ?: Bundle()) // 인텐트 데이터 복사
                    Log.d("countdown", "잘가니? : ${intent}")
                    startActivity(intent) // 다음 액티비티 시작
                }

                finish()
            }
        }.start()
    }

    private fun startDRunningService() {
        val serviceIntent = Intent(this, DRunningService::class.java)
        Log.d("countdown", "startDRunningService: DDDD시작?")
        startForegroundService(serviceIntent)
    }
    private fun startTRunningService() {
        val serviceIntent = Intent(this, TRunningService::class.java)
        Log.d("countdown", "startTRunningService: TTT시작?")
        startForegroundService(serviceIntent)
    }
    private fun startIRunningService() {
        val serviceIntent = Intent(this, IntervalService::class.java)
        Log.d("Intent Check", "Interval Service Intent : $serviceIntent")
        serviceIntent.putExtra("programData", intent.getParcelableExtra("programData", FavoriteResponseDto::class.java))
        startForegroundService(serviceIntent)
    }
}