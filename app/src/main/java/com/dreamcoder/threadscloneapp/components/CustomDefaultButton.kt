package com.dreamcoder.threadscloneapp.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CustomDefaultButton(
    title: String,
    onclick: () -> Unit,
    modifier: Modifier,
    textColor: Color,
    fontFamily: FontFamily
) {
    ElevatedButton(
        onClick = {
            onclick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        modifier = modifier,
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = title, color = textColor, fontFamily = fontFamily)
    }
}