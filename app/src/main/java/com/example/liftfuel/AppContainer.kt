package com.example.liftfuel

import android.content.Context
import com.example.liftfuel.data.local.LiftFuelDatabase
import com.example.liftfuel.data.seed.DatabaseSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Manual dependency injection: one instance per app, created in [LiftFuelApp].
 * Repositories, the AI client and the recommendation engine will be added here.
 */
class AppContainer(context: Context) {

    /** Lives as long as the process; for work that must not be cancelled with a screen. */
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val database: LiftFuelDatabase = LiftFuelDatabase.build(context)

    val seeder = DatabaseSeeder(database)
}
