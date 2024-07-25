package com.dreamcoder.threadscloneapp.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun CustomText(
    title: String,
    fontWeight: FontWeight,
    fontFamily: FontFamily,
    textSize: TextUnit,
    color: Color,
    modifier: Modifier
) {
    Text(
        text = title,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        fontSize = textSize,
        color = color,
        modifier = modifier
    )
}