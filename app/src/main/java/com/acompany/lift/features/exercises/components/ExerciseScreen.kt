package com.acompany.lift.features.exercises.components

import android.text.format.DateUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DoubleArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acompany.lift.common.components.elapsedTimeMillis
import com.acompany.lift.data.model.Routine
import com.acompany.lift.data.model.RoutineExercise
import com.acompany.lift.features.exercises.components.ExerciseScreenViewModel.SessionState
import com.acompany.lift.features.exercises.data.ExerciseHelper.initialWeight
import java.util.*

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
fun ExerciseScreen(
    viewModel: ExerciseScreenViewModel,
    routineId: Long
) {
    val routine by viewModel.getRoutine(routineId).collectAsState(initial = null)
    val selectedExercise by viewModel.selectedRoutineExercise.collectAsState()
    val sessionState by viewModel.sessionState.collectAsState()

    routine?.let { routine ->
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = routine.name) })
            },
            content = {
                ExerciseModalSheet(
                    sheetContent = {
                        selectedExercise?.let { selectedExercise ->
                            SheetContent(
                                routine = routine,
                                routineExercise = selectedExercise,
                                onOneRepMaxChanged = { oneRepMax ->
                                    viewModel.updateOneRepMax(
                                        selectedExercise.id,
                                        oneRepMax
                                    )
                                },
                                onWeightChanged = { weight ->
                                    viewModel.updateRoutineExerciseWeight(
                                        selectedExercise.id,
                                        weight
                                    )
                                },
                                onPercentOneRepMaxChanged = { percentOneRepMax ->
                                    viewModel.updateRoutineExercisePercentOneRepMax(
                                        selectedExercise.id,
                                        percentOneRepMax
                                    )
                                },
                                onDoneClick = {
                                    hide()
                                }
                            )
                        }
                    },
                    content = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            ExerciseList(
                                routineExercises = routine.exercises,
                                sessionState = sessionState,
                                onExerciseClick = { routineExercise ->
                                    viewModel.setSelectedRoutineExercise(routineExercise)
                                    show()
                                },
                                onActionClick = { routineExercise ->
                                    viewModel.moveToNextState(routine.exercises)
                                }
                            )
                            SessionProgressFloatingActionButton(sessionState) {
                                viewModel.moveToNextState(routine.exercises)
                            }
                        }
                    }
                )
            }
        )
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun SheetContent(
    routine: Routine,
    routineExercise: RoutineExercise,
    onOneRepMaxChanged: (Float?) -> Unit,
    onWeightChanged: (Float?) -> Unit,
    onPercentOneRepMaxChanged: (Float?) -> Unit,
    onDoneClick: () -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier.weight(1f), text = "Edit ${routineExercise.exercise.name}")
        OutlinedButton(onClick = {
            keyboardController?.hideSoftwareKeyboard()
            onDoneClick()
        }) {
            Text(text = "Done")
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
    Text(fontSize = 12.sp, text = "All ${routineExercise.exercise.name}(s)")
    Spacer(modifier = Modifier.size(4.dp))
    FloatTextField(
        key = routineExercise.id.toString(),
        label = "One rep max",
        initialValue = routineExercise.exercise.oneRepMax,
        onValueChanged = { value -> onOneRepMaxChanged(value) },
        onDone = { keyboardController?.hideSoftwareKeyboard() }
    )
    Spacer(modifier = Modifier.size(24.dp))
    Text(fontSize = 12.sp, text = "Routine:  ${routine.name}")
    Spacer(modifier = Modifier.size(4.dp))
    FloatTextField(
        key = routineExercise.id.toString(),
        label = "% One rep max",
        initialValue = routineExercise.percentOneRepMax?.let { (it * 100) },
        onValueChanged = { value -> onPercentOneRepMaxChanged(value?.let { it / 100f }) },
        onDone = { keyboardController?.hideSoftwareKeyboard() }
    )
    Spacer(modifier = Modifier.size(8.dp))
    FloatTextField(
        key = routineExercise.id.toString(),
        label = "Weight",
        initialValue = routineExercise.weight ?: routineExercise.initialWeight(),
        onValueChanged = { value -> onWeightChanged(value) },
        onDone = { keyboardController?.hideSoftwareKeyboard() }
    )
}

@Composable
private fun FloatTextField(
    key: String,
    label: String,
    initialValue: Float?,
    onValueChanged: (Float?) -> Unit,
    onDone: () -> Unit
) {
    val textState = remember(key) { mutableStateOf(initialValue?.toString() ?: "") }
    OutlinedTextField(
        value = textState.value,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        onValueChange = { textFieldValue ->
            textState.value = textFieldValue
            onValueChanged(textFieldValue.toFloatOrNull())
        },
        keyboardActions = KeyboardActions(onDone = { onDone() })
    )
}

@Composable
private fun ExerciseList(
    routineExercises: List<RoutineExercise>,
    sessionState: SessionState,
    onExerciseClick: (RoutineExercise) -> Unit,
    onActionClick: (RoutineExercise) -> Unit
) {
    ExerciseList(
        routineExercises = routineExercises,
        sessionState = sessionState,
        modifier = Modifier.padding(8.dp),
        onExerciseClick = { routineExercise ->
            onExerciseClick(routineExercise)
        },
        onActionClick = { routineExercise ->
            onActionClick(routineExercise)
        }
    )
}

@Composable
fun SessionProgressFloatingActionButton(
    sessionProgress: SessionState,
    onClick: () -> Unit
) {
    val (sessionIcon, sessionIconDescription) = when (sessionProgress) {
        SessionState.None -> Icons.Rounded.Timer to "start session"
        is SessionState.Resting,
        is SessionState.InProgress -> Icons.Rounded.DoubleArrow to "next session"
        is SessionState.Complete -> Icons.Rounded.CheckCircle to "completed"
    }
    AnimatedFloatingActionButton(
        text = DateUtils.formatElapsedTime(
            elapsedTimeMillis(sessionProgress.startDate ?: Date())
        ),
        icon = sessionIcon,
        contentDescription = sessionIconDescription,
        expanded = sessionProgress is SessionState.InProgress || sessionProgress is SessionState.Resting,
        onClick = onClick,
        modifier = Modifier.bottomEndFabPlacement()
    )
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun AnimatedFloatingActionButton(
    text: String,
    icon: ImageVector,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibility(visible = expanded) {
                Box(Modifier.padding(start = 16.dp, end = 8.dp)) {
                    Text(text = text)
                }
            }
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
            AnimatedVisibility(visible = expanded) {
                Spacer(Modifier.width(20.dp))
            }
        }
    }
}

fun Modifier.bottomEndFabPlacement(): Modifier {
    return this.then(
        fillMaxSize()
            .wrapContentSize(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 16.dp)
    )
}