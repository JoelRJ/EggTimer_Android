// TODO: Convert timer to class, fix audio alarm
package com.example.eggtimer


import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    /*
     Timer resource:
     medium.com/@olajhidey/working-with-countdown-timer-in-android-studio-using-kotlin-39fd7826e205
    */
    var isCounting = false
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add a listener to the SeekBar for user input, update timesList as changed
        var seek = findViewById<SeekBar>(R.id.timeBar)
        seek?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                updateText(progress * 1000)
            }

            // If user begins to change time of timer, stop timer to wait to restart it
            override fun onStartTrackingTouch(p0: SeekBar?) {
                if (isCounting) {
                    timer.cancel()
                }
                background.setBackgroundColor(Color.parseColor("#ffffff"))
            }

            // If stopped while timer is on, will automatically restart timer with SeekBar progress
            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (isCounting) {
                    startTimer()
                }
                else {
                    updateText()
                }
            }
        })

        // Need to stop timer when selecting new time, update button to "stop timer", fix 5:0
        timerButton.setOnClickListener {
            if (!isCounting) {
                startTimer()
                background.setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                pauseTimer()
            }
            isCounting = !isCounting
        }

    }

    // Used to update the text view to the current time
    fun updateText(currentTime: Int = timeBar.progress * 1000) {
        val minutes = floor((currentTime / 1000) / 60.0).toInt()
        val seconds = (currentTime / 1000) % 60

        var secondsString = ""
        if (seconds < 10) {
            secondsString = String.format("%02d",seconds)
        }
        else {
            secondsString = seconds.toString()
        }

        var minutesString = ""
        if (minutes < 10) {
            minutesString = String.format("%02d",minutes)
        }
        else {
            minutesString = minutes.toString()
        }


        timerText.text = minutesString + ":" + secondsString
    }

    // Begin timer from wherever the SeekBar currently is
    // SeekBar is set to automatically update progress as time passes
    private fun startTimer() {
        val timerLength = timeBar.progress * 1000

        timer = object : CountDownTimer(timerLength.toLong(), 1000) {
            override fun onTick(currentTime: Long) {
                updateText(currentTime.toInt())
                timeBar.progress = (currentTime / 1000).toInt()
            }

            override fun onFinish() {
                // Make noise here or change background color
                background.setBackgroundColor(Color.parseColor("#00aaff"))

                // Add media player to make noise after alarm finishes
                var alarm = MediaPlayer.create(this@MainActivity, R.raw.balloon)
                alarm.start()

                pauseTimer()
            }
        }

        timer.start()
        timerButton.text = "Pause Countdown"
    }

    // Pause the timer (by canceling it) and set button text to start
    private fun pauseTimer() {
        timerButton.text = "Start Countdown"
        timer.cancel()
    }
}