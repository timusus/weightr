package com.acompany.lift.features.exercises.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acompany.lift.common.components.CircleIcon

@Composable
fun RowScope.ExerciseMenuItem(elapsedTime: Long?, onStartClick: () -> Unit) {
    elapsedTime?.let {
        IconButton(
            onClick = {

            }) {
            Text(text = DateUtils.formatElapsedTime(elapsedTime / 1000))
        }
        Spacer(modifier = Modifier.size(8.dp))
    } ?: run {
        IconButton(
            onClick = {
                onStartClick()
            }) {
            CircleIcon(
                icon = Icons.Rounded.PlayCircleOutline,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}