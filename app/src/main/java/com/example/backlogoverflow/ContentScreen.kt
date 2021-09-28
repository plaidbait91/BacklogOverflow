package com.example.backlogoverflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.backlogoverflow.database.Course
import com.example.backlogoverflow.database.CourseDao
import com.example.backlogoverflow.database.CourseDatabase
import com.example.backlogoverflow.viewmodel.CourseViewModel
import com.example.backlogoverflow.viewmodel.CourseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun mainCoursesScreen(viewModel: CourseViewModel) {
    val navController = rememberNavController()
    AddCourseNavigation(navController = navController, viewModel = viewModel)
}


@Composable
fun coursesScreen(viewModel: CourseViewModel, navigation: NavHostController) {
    val list: List<Course> by viewModel.list.observeAsState(listOf())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigation.navigate("add_or_edit_course")
                }
            ) {
                Icon(Icons.Filled.Add,"")
            }
        }
    ) {
        if(list.isNotEmpty()) {
            LazyColumn {
                items(list) { item ->
                    courseRow(course = item)
                }
            }
        }

        else {
            emptyCoursesScreen()
        }
    }

}

@Composable
fun courseRow(course: Course) {

    val date = Date(course.deadline)
    val formatter = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(15.dp)
            .background(Color.White)
    ) {
        Text(
            text = formatter.format(date),
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
    courseRow(Course(
        courseName = "testing",
        timings = listOf(
            (System.currentTimeMillis()),
            (System.currentTimeMillis() + 84600000L),
            (System.currentTimeMillis()+ 42300000L)
        ),
        deadline = (System.currentTimeMillis()),
        links = listOf()))
}

/*@Preview(showBackground = true)
@Composable
fun coursesScreenPreview() {
    coursesScreen(CourseViewModel())
}*/

@Composable
fun emptyCoursesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "No courses added yet. Click + to add a course",
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun emptyPreview() {
    emptyCoursesScreen()
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

/*@Preview(showBackground = true)
@Composable
fun pendingScreenReview() {
    pendingScreen()
}*/

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

@Composable
fun addCourseScreen(
    course: Course = Course(
        courseName = "some_name",
        timings = listOf(),
        deadline = 0L,
        links = listOf())
) {
   var text by rememberSaveable { mutableStateOf(course.courseName) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .padding(16.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Course Name") },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun addCourseScreenPreview() {
    addCourseScreen()
}

@Composable
fun AddCourseNavigation(navController: NavHostController, viewModel: CourseViewModel) {
    NavHost(navController, startDestination = "courses_list") {
        composable("courses_list") {
            coursesScreen(viewModel, navController)
        }
        composable("add_or_edit_course") {
            addCourseScreen()
        }

    }
}

/*
@Preview(showBackground = true)
@Composable
fun profileScreenPreview() {
    profileScreen()
}*/
