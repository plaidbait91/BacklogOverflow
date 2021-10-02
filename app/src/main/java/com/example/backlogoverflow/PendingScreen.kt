package com.example.backlogoverflow

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.backlogoverflow.database.Course
import com.example.backlogoverflow.viewmodel.PendingViewModel
import java.text.SimpleDateFormat
import java.util.*

val LIST_ROUTE = "pending_list"
val EDIT_ROUTE = "pending_edit/course={course}"


@Composable
fun mainPendingScreen(pendingViewModel: PendingViewModel) {
    val navController = rememberNavController()

    AddPendingNavigation(navController, pendingViewModel)
}


@Composable
fun pendingRow(course: Course, navigation: NavHostController) {
    var title = "No deadline"

    if(course.deadline != 0L) {
        val date = Date(course.deadline)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        title = formatter.format(date)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(15.dp)
    ) {

        Text(text = course.courseName,
        fontSize = 24.sp,
        color = Color.Black)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${course.count} recordings",
                fontSize = 18.sp,
                color = Color.Gray
            )

            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(id = R.color.gray))) {}
}

@Composable
fun pendingScreen(viewModel: PendingViewModel, navController: NavHostController) {
    val list: List<Course> by viewModel.list.observeAsState(listOf())

    if(list.isNotEmpty()) {
        LazyColumn {
            items(list) { item ->
                pendingRow(course = item, navigation = navController)
                Log.v("TESTING MODE", item.courseName)
            }
        }
    }

    else {
        emptyPendingScreen()
    }
}

@Composable
fun editPending(course: Course, pendingViewModel: PendingViewModel, navController: NavHostController) {

}

@Composable
fun emptyPendingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "You have no pending work...yet.",
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}


@Composable
fun AddPendingNavigation(navController: NavHostController, pendingViewModel: PendingViewModel) {
    NavHost(navController, startDestination = LIST_ROUTE) {
        composable(LIST_ROUTE) {
            pendingScreen(pendingViewModel, navController)
        }
        composable(
            EDIT_ROUTE,
            arguments = listOf(navArgument("course") {
                type = NavType.StringType
            })) {
            val json = it.arguments?.getString("course")
            val course = jsonAdapter.fromJson(json)

            if (course != null) {
                editPending(course, pendingViewModel, navController)
            }
        }

    }
}
