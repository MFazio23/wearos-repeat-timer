package dev.mfazio.repeattimer

import android.app.Application
import timber.log.Timber

class FazioRepeatTimerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

    }
}