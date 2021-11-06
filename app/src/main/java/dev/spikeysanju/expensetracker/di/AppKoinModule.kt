package dev.spikeysanju.expensetracker.di

import androidx.room.Room
import dev.spikeysanju.expensetracker.data.local.AppDatabase
import dev.spikeysanju.expensetracker.data.local.datastore.UIModeDataStore
import dev.spikeysanju.expensetracker.repo.TransactionRepo
import dev.spikeysanju.expensetracker.services.exportcsv.ExportCsvService
import dev.spikeysanju.expensetracker.view.about.AboutViewModel
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        UIModeDataStore(
            context = androidContext()
        )
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "transaction.db"
        ).fallbackToDestructiveMigration().build()
    }

    single {
        ExportCsvService(appContext = androidContext())
    }

    factory {
        TransactionRepo(
            db = get()
        )
    }

    viewModel {
        AboutViewModel()
    }

    viewModel {
        TransactionViewModel(
            transactionRepo = get(),
            exportService = get(),
            uiModeDataStore = get()
        )
    }
}
