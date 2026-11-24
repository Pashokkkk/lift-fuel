package com.example.liftfuel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.liftfuel.data.local.model.TableCounts
import com.example.liftfuel.ui.theme.LiftFuelTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val counts = (application as LiftFuelApp).container.database.statsDao().observeCounts()
        setContent {
            LiftFuelTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DatabaseCheckScreen(
                        counts = counts,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

/** Temporary screen that proves the database and mock data work. Replaced by the real UI later. */
@Composable
fun DatabaseCheckScreen(counts: Flow<TableCounts>, modifier: Modifier = Modifier) {
    val current by counts.collectAsState(initial = null)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Lift & Fuel — database check", style = MaterialTheme.typography.titleLarge)
        val c = current
        if (c == null) {
            Text("Opening database…")
        } else {
            CountRows(c)
        }
    }
}

/** Emitted inside the parent Column, so the rows stack vertically. */
@Composable
private fun CountRows(c: TableCounts) {
    listOf(
        "Foods" to c.foods,
        "Exercises" to c.exercises,
        "Workout templates" to c.templates,
        "Reminders" to c.reminders,
        "Weight entries" to c.weightEntries,
        "Meal entries" to c.mealEntries,
        "Favourite meals" to c.favoriteMeals,
        "Meal photos" to c.photoAnalyses,
        "Workout sessions" to c.workoutSessions,
        "Workout sets" to c.workoutSets,
        "Tips" to c.recommendations,
    ).forEach { (label, count) ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label)
            Text(count.toString(), style = MaterialTheme.typography.titleMedium)
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
fun DatabaseCheckScreenPreview() {
    LiftFuelTheme {
        DatabaseCheckScreen(
            counts = flowOf(TableCounts(153, 48, 7, 30, 220, 3, 2, 13, 210, 3, 3)),
        )
    }
}
