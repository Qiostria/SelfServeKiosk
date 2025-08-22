package com.slimlite.selfservekiosk

import android.app.Application
import android.os.StrictMode

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

//        // Disable StrictMode to prevent ANR dialogs
//        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
//        StrictMode.setVmPolicy(StrictMode.VmPolicy.LAX)
    }
}