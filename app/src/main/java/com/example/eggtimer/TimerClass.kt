package com.example.eggtimer

import android.os.CountDownTimer

class TimerClass(timeIn: Int) {
    var isCounting = false
    lateinit var timer: CountDownTimer
    var time = 60

    init {
        this.time = timeIn
    }


}