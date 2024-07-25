package com.dreamcoder.threadscloneapp.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun CustomImage(
     image:Int,
     description:String,
     modifier: Modifier
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = description,
        modifier = Modifier
    )

}