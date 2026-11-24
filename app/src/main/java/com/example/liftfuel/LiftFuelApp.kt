package com.example.liftfuel

import android.app.Application
import kotlinx.coroutines.launch

class LiftFuelApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        container.applicationScope.launch {
            // Catalog on every install; demo history only in debug builds.
            container.seeder.seedIfEmpty(includeDemoData = BuildConfig.SEED_DEMO_DATA)
        }
    }
}
