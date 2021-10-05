package com.example.backlogoverflow

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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

    val record = context
        .getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        .getInt("count", 0)
    val prefs = context
        .getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)

    val dark = prefs.getBoolean("dark_theme", false)

    var checked by remember { mutableStateOf(dark) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(colorResource(id = R.color.white))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Recordings watched this week: $record",
            //color = Color.Black,
            fontSize = 20.sp
        )

        Row(modifier = Modifier
            .fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Text(text = "Enable Dark Theme",
            fontSize = 20.sp)

            Switch(checked = checked,
                onCheckedChange = {
                    prefs.edit()
                        .putBoolean("dark_theme", it)
                        .apply()

                    checked = it

                    if(it) profileViewModel.darkTheme()
                    else profileViewModel.lightTheme()

                })
        }


    }
}

@Preview(showBackground = true)
@Composable
fun profileScreenPreview() {
    profileScreen()
}