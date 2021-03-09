package com.acompany.weightr.features.exercises.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.acompany.data.model.Exercise
import com.acompany.weightr.features.Screen
import timber.log.Timber

class ExerciseScreen(exercises: (sessionId: Int) -> List<Exercise>) : Screen(
    route = "sessions/{sessionId}/exercises",
    arguments = listOf(navArgument(ARG_SESSION_ID) { type = NavType.IntType }),
    name = "Exercises",
    content = { paddingValues, navController, navBackStackEntry ->
        val editingExercise = remember { mutableStateOf(null as Exercise?) }
        LazyExerciseList(
            exercises = exercises(navBackStackEntry.arguments!!.getInt(ARG_SESSION_ID)),
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            onExerciseClick = { exercise ->
                Timber.d("Click $exercise")
            },
            onExerciseLongClick = { exercise ->
                editingExercise.value = exercise
                Timber.d("Long click $exercise")
            }
        )
        editingExercise.value?.let { exercise ->
            EditExercisesAlert(exercise) {
                editingExercise.value = null
            }
        }
    }
) {
    companion object {
        const val ARG_SESSION_ID = "sessionId"
    }
}