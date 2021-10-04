package com.example.backlogoverflow

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun profileScreen() {

    val context = LocalContext.current

    val record = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).getInt("count", 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .padding(16.dp)
    ) {
        Text(
            text = "Recordings watched this week: $record",
            color = Color.Black,
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun profileScreenPreview() {
    profileScreen()
}