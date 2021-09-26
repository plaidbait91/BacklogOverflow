package com.example.backlogoverflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backlogoverflow.database.CourseDao
import com.example.backlogoverflow.viewmodel.CourseViewModel

@Composable
fun coursesScreen(viewModel: CourseViewModel) {
    val list: List<String> by viewModel.list.observeAsState(listOf())
    LazyColumn {
        items(list) { item ->
            courseRow(text = item)
        }
    }
}

@Composable
fun courseRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(15.dp)
            .background(Color.White)
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            color = Color.Black
        )

    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(id = R.color.gray))) {}
}

@Preview(showBackground = true)
@Composable
fun courseRowPreview() {
    courseRow("sometext")
}

@Preview(showBackground = true)
@Composable
fun coursesScreenPreview() {
    val x : CourseDao = CourseDao()
    val test : CourseViewModel = CourseViewModel(x)
    coursesScreen(test)
}

@Composable
fun pendingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Pending View",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun pendingScreenReview() {
    pendingScreen()
}

@Composable
fun profileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Profile View",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun profileScreenPreview() {
    profileScreen()
}