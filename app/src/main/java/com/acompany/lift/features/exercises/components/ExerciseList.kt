package com.acompany.lift.features.exercises.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Colors
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.acompany.lift.features.exercises.data.RoutineExerciseListPreviewProvider
import com.acompany.lift.data.model.RoutineExercise

@Composable
fun ExerciseList(
    routineExercises: List<RoutineExercise>,
    modifier: Modifier = Modifier,
    onExerciseClick: (RoutineExercise) -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        items(routineExercises) { routineExercise ->
            ExerciseListItem(
                routineExercise = routineExercise
            ) { onExerciseClick(routineExercise) }
            Divider()
        }
    }
}

@Preview
@Composable
private fun ExerciseListPreview(
    @PreviewParameter(RoutineExerciseListPreviewProvider::class) preview: Pair<Colors, List<RoutineExercise>>
) {
    MaterialTheme(colors = preview.first) {
        ExerciseList(routineExercises = preview.second)
    }
}