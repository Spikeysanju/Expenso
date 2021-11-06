package dev.spikeysanju.expensetracker.app

import android.app.Application
import dev.spikeysanju.expensetracker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ExpenseTracker : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // declare used Android context
            androidContext(this@ExpenseTracker)
            // declare modules
            modules(appModule)
        }
    }
}
