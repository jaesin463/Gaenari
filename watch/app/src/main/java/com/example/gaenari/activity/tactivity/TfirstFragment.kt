// TFirstFragment.kt

package com.example.gaenari.activity.tactivity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.gaenari.R
import com.example.gaenari.activity.result.ResultActivity
import com.example.gaenari.dto.request.SaveDataRequestDto

class TFirstFragment : Fragment() {
    private lateinit var distanceView: TextView
    private lateinit var timeView: TextView
    private lateinit var heartRateView: TextView
    private lateinit var speedView: TextView
    private lateinit var circleProgress: TCircleProgress
    private lateinit var updateReceiver: BroadcastReceiver
    private lateinit var gifImageView : pl.droidsonroids.gif.GifImageView
    private lateinit var requestDto: SaveDataRequestDto

    private var totalHeartRateAvg: Int = 0
    private var totalSpeedAvg: Double = 0.0
    private var totalDistance: Double = 0.0
    private var totalHeartRate: Float = 0f
    private var heartRateCount: Int = 0
    private var totalTime: Long = 0

    companion object {
        fun newInstance(
            programTarget: Int,
            programType: String,
            programTitle: String,
            programId: Long
        ): TFirstFragment {
            val args = Bundle()
            args.putInt("programTarget", programTarget)
            args.putString("programType", programType)
            args.putString("programTitle", programTitle)
            args.putLong("programId", programId)

            val fragment = TFirstFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tfirst, container, false)
        distanceView = view.findViewById(R.id.달린거리)
        timeView = view.findViewById(R.id.달린시간)
        heartRateView = view.findViewById(R.id.심박수)
        speedView = view.findViewById(R.id.속력)
        circleProgress = view.findViewById(R.id.circleProgress)
        gifImageView = view.findViewById<pl.droidsonroids.gif.GifImageView>(R.id.gifImageView)
//        context?.let {
//            Glide.with(it)
//                .asGif()
//                .load(R.raw.run5)
//                .dontTransform()
//                .into(gifImageView)
//        }

        val programTarget = arguments?.getInt("programTarget") ?: 0
        Log.d("first", "onCreateView: ${programTarget}")
        setupUpdateReceiver(programTarget)
        return view
    }

    private fun setupUpdateReceiver(programTarget: Int) {
        updateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    "com.example.sibal.UPDATE_INFO" -> {
                        val distance = intent.getDoubleExtra("distance", 0.0)
                        val speed = intent.getFloatExtra("speed", 0f)
                        val time = intent.getLongExtra("time", 0)
                        if(speed>0.6){
                            updateGifForActivity(true)
                        }else{
                            updateGifForActivity(false)
                        }

                        totalDistance = distance
                        totalTime = time
                        Log.d("티액티비티", "onReceive: ${time}")

                        val remainingTime = (programTarget * 1000) - totalTime
                        if (remainingTime > 0) {
                            updateUI(distance, programTarget, remainingTime, speed)
                        }
                    }
                    "com.example.sibal.UPDATE_TIMER" -> {
                        val time = intent.getLongExtra("time", 0)
                        val remainingTime = (programTarget * 1000) - time
                        updateTimerUI(remainingTime , programTarget,)
                    }
                    "com.example.sibal.UPDATE_ONE_MINUTE" -> {
                        val checkspeed = intent.getDoubleExtra("(averageSpeed",0.0)
                        val checkheart = intent.getIntExtra("averageHeartRate",0)
                        val checkdistance = intent.getDoubleExtra("distance",0.0)
                        Log.d("checkcheck", "${checkspeed} , ${checkheart}, ${checkdistance} ")
                        updateUIcheck(checkspeed,checkheart,checkdistance)
                    }
                    "com.example.sibal.UPDATE_HEART_RATE" -> {
                        val heartRate = intent.getFloatExtra("heartRate", 0f)
                        totalHeartRate += heartRate
                        if (heartRate > 40) {
                            heartRateCount++
                        }
                        updateheartUI(heartRate)
                    }
                    "com.example.sibal.EXIT_PROGRAM" -> {
                        requestDto = intent.getParcelableExtra("requestDto", SaveDataRequestDto::class.java)!!
                        totalHeartRateAvg = requestDto.heartrates.average
                        totalSpeedAvg = requestDto.speeds.average
                        sendResultsAndFinish(context)
                    }
                }
            }
        }

        val intentFilter = IntentFilter().apply {
            addAction("com.example.sibal.UPDATE_INFO")
            addAction("com.example.sibal.UPDATE_TIMER")
            addAction("com.example.sibal.UPDATE_ONE_MINUTE")
            addAction("com.example.sibal.UPDATE_HEART_RATE")
            addAction("com.example.sibal.EXIT_PROGRAM")
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(updateReceiver, intentFilter)
    }

    private fun updateUIcheck(checkspeed : Double,checkheart:Int,checkdistance:Double ){
    }
    private fun updateUI(distance: Double, programTarget: Int, remainingTime: Long, speed: Float) {
        val totalMillis = programTarget * 1000
        val progress = 100 * (1 - (remainingTime.toFloat() / totalMillis))
        circleProgress.setProgress(progress)

//        distanceView.text = formatTime(remainingTime)
        timeView.text = String.format("%.2f", distance / 1000)
        speedView.text = String.format("%.2f", speed * 3.6)
    }

    @SuppressLint("ResourceType")
    fun updateGifForActivity(isRunning: Boolean) {
        val resourceId = if (isRunning) {
            R.raw.run4  // 사용자가 달리기를 시작한 경우
        } else {
            R.raw.stop4 // 사용자가 달리기를 멈춘 경우
        }
        gifImageView.setImageResource(resourceId)
    }



    private fun updateheartUI(heartRate: Float) {
        heartRateView.text = String.format("%d", heartRate.toInt())
    }

    private fun updateTimerUI(remainingTime: Long , programTarget: Int) {
        val totalMillis = programTarget * 1000
        val progress = 100 * (1 - (remainingTime.toFloat() / totalMillis))
        circleProgress.setProgress(progress)
        distanceView.text = formatTime(remainingTime)
    }

    private fun formatTime(millis: Long): String {
        val hours = (millis / 3600000) % 24
        val minutes = (millis / 60000) % 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun sendResultsAndFinish(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(500)
        }

        val programTarget = arguments?.getInt("programTarget") ?: 0
        val programType = arguments?.getString("programType") ?: ""
        val programTitle = arguments?.getString("programTitle") ?: ""
        val programId = arguments?.getLong("programId") ?: 0L

        val intent = Intent(context, ResultActivity::class.java).apply {
            putExtra("requestDto", requestDto)
            putExtra("programTarget", programTarget)
            putExtra("programType", programType)
            putExtra("programTitle", programTitle)
            putExtra("programId", programId)
            putExtra("totalDistance", totalDistance)
            putExtra("totalTime", totalTime)
        }

        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(updateReceiver)
    }
}
