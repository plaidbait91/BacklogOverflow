package com.example.backlogoverflow

import android.text.style.ClickableSpan
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.backlogoverflow.database.Course
import com.example.backlogoverflow.viewmodel.CourseViewModel
import com.squareup.moshi.Moshi
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import java.text.SimpleDateFormat
import java.time.*
import java.util.*
import java.util.Collections.addAll

val moshi = Moshi.Builder().build()
val jsonAdapter = moshi.adapter(Course::class.java).lenient()

const val ADD_ROUTE = "add_or_edit_course/course={course}/{editMode}"
var onClick = {}
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
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                
                val delete = rememberMaterialDialogState()
                val empty = rememberMaterialDialogState()

                MaterialDialog(
                    dialogState = delete,
                    buttons = {
                        positiveButton("Yes, Delete all courses", onClick = {
                            viewModel.clearCourses()
                        })
                        negativeButton("No, Go back")
                    }
                ) {
                    title(text = "Delete all courses?")
                    message(text = "This action will clear all your courses.")
                }

                MaterialDialog(
                    dialogState = empty,
                    buttons = {
                        positiveButton("OK")
                    }
                ) {
                    title("No courses")
                    message("You haven't added any courses yet.")
                }

                FloatingActionButton(
                    backgroundColor = colorResource(id = R.color.purple_700),
                    onClick = {
                        val course = Course(
                            courseName = "",
                            timings = mutableListOf(null, null, null, null, null, null, null),
                            links = mutableListOf(),
                            deadline = 0L
                        )
                        val json = jsonAdapter.toJson(course)
                        navigation.navigate(ADD_ROUTE.replace("{course}", json).replace("{editMode}", "false")) {
                            navigation.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                        }
                    }
                ) {
                    Icon(Icons.Filled.Add,"")
                }

                FloatingActionButton(onClick = {
                    if(list.isNotEmpty()) delete.show()
                    else empty.show()
                },
                    backgroundColor = colorResource(id = R.color.purple_500)) {
                    Icon(Icons.Filled.Delete, "")
                }
            }

        }
    ) {
        if(list.isNotEmpty()) {
            LazyColumn {
                items(list) { item ->
                    courseRow(course = item, navigation = navigation)
                }
            }
        }

        else {
            emptyCoursesScreen()
        }

    }

}

@Composable
fun courseRow(course: Course, navigation: NavHostController) {
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
            .background(Color.White)
            .clickable {
                val json = jsonAdapter.toJson(course)

                navigation.navigate(
                    ADD_ROUTE
                        .replace("{course}", json)
                        .replace("{editMode}", "true")
                ) {
                    navigation.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                }
            }
    ) {
        Text(
            text = course.courseName.replace('|', '/'),
            fontSize = 24.sp,
            color = Color.Black
        )

        Text(
            text = title,
            color = Color.Gray
        )

    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(id = R.color.gray))) {}
}

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
fun addCourseScreen(
    course: Course,
    viewModel: CourseViewModel,
    navigation: NavHostController,
    editMode: Boolean) {

   val displayName = course.courseName.replace('|', '/')

   var name by rememberSaveable { mutableStateOf(displayName) }


   var count by rememberSaveable {
       mutableStateOf(course.count)
   }
    
   var deadline by rememberSaveable { mutableStateOf(course.deadline) }

   var deadDate by rememberSaveable{ mutableStateOf(LocalDate.now()) }
   var deadTime by rememberSaveable{ mutableStateOf(LocalTime.now()) }

    val daySelectedList: MutableList<String?> = remember {
       mutableStateListOf(*course.timings.toTypedArray())
   }

   val linkList = remember {
       mutableStateListOf(*course.links.toTypedArray())
   }

    for(i in linkList.indices) {
        linkList[i] = linkList[i].replace('|', '/')
    }

   val daysList = DayOfWeek.values()
   var timingTime: LocalTime by rememberSaveable { mutableStateOf(LocalTime.now())} 
   val dialogState = rememberMaterialDialogState() 
   val dialogState2 = rememberMaterialDialogState() 


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Course Name") },
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {

                        for(i in linkList.indices) {
                            linkList[i] = linkList[i].replace('/', '|')
                        }

                        name = name.replace('/', '|')

                        if (editMode) {
                            val edited = Course(
                                id = course.id,
                                courseName = name,
                                timings = daySelectedList,
                                links = linkList,
                                count = count,
                                deadline = deadline
                            )

                            viewModel.editCourse(edited)

                        } else {
                            val new = Course(
                                courseName = name,
                                timings = daySelectedList,
                                links = linkList,
                                count = count,
                                deadline = deadline
                            )

                            viewModel.addCourse(new)

                        }

                        navigation.navigate("courses_list") {
                            navigation.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                        }


                    }) {

                    Text(text = if (editMode) "Edit course" else "Add course")

                }

                if(editMode) {
                    Button(onClick = {
                        val delete = Course(
                            id = course.id,
                            courseName = name,
                            timings = daySelectedList,
                            links = linkList,
                            count = count,
                            deadline = deadline
                        )

                        viewModel.deleteCourse(delete)
                        navigation.navigate("courses_list") {
                            navigation.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                        }
                    }) {
                        Text(text = "Delete course")
                    }
                }
                
            }

            
        }

        Text(text = "Lecture Timings",
        fontWeight = FontWeight.Bold
        )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(daysList) { i, day ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Checkbox(
                            checked = daySelectedList[i] != null,
                            onCheckedChange = { checked ->
                                if(checked) {
                                    dialogState.show()
                                    onClick = {
                                        val instant: Instant =
                                            timingTime.atDate(LocalDate.of(1990, 1, 1))
                                                .atZone(ZoneId.systemDefault()).toInstant()
                                        val time = Date.from(instant)
                                        val format = SimpleDateFormat("HH:mm:ss")
                                        daySelectedList[i] = format.format(time)

                                    }
                                }

                                else {
                                    daySelectedList[i] = null
                                }


                            }
                        )

                        Text(
                            text = day.name.lowercase().capitalize() + " " + (if(daySelectedList[i] != null) daySelectedList[i] else ""),
                            fontSize = 18.sp)

                        MaterialDialog(
                            dialogState = dialogState,
                            buttons = {
                                positiveButton("Ok", onClick = onClick)
                                negativeButton("Cancel")
                            }
                        ) {


                            timepicker{
                                timingTime = it
                            }

                        }

                    }

                }
            }

        /*LazyColumn {
            items(daySelectedList) {
                if (it != null) {
                    Text(text = it)
                }

                else {
                    Text(text = "null")
                }
            }
        }*/

        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        Text(text = "Current deadline: " + (if(deadline == 0L) "Not set" else format.format(Date(deadline))),
            fontWeight = FontWeight.Bold
        )

        Button(onClick = {
            dialogState2.show()
        }) {
            Text(text = "Change/set deadline")
        }

        Text(text = "Number of recordings to watch",
        fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                if(count > 0) {
                    count--
                    linkList.removeLast()
                }
                }) {
                Text(text = "-")
            }

            Text(text = count.toString(),
            fontSize = 18.sp)

            Button(
                onClick = {
                    count++
                    linkList.add("")
                }) {
                Text(text = "+")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(linkList) { i, link ->
                TextField(value = link,
                    onValueChange = {
                        linkList[i] = it
                    },
                label = { Text("Link " + (i + 1).toString()) })
            }
        }


        MaterialDialog(
            dialogState = dialogState2,
            buttons = {
                positiveButton("Ok", onClick = {

                    val temp = deadDate.atTime(deadTime)

                    val out = Date.from(temp.atZone(ZoneId.systemDefault()).toInstant())
                    deadline = out.time
                })
                negativeButton("Cancel")
            }
        ) {
            datepicker {
                deadDate = it
            }

            timepicker {
                deadTime = it
            }
        }




    }
}


@Composable
fun AddCourseNavigation(navController: NavHostController, viewModel: CourseViewModel) {
    NavHost(navController, startDestination = "courses_list") {
        composable("courses_list") {
            coursesScreen(viewModel, navController)
        }
        composable(ADD_ROUTE,
            arguments = listOf(navArgument("course") {
                type = NavType.StringType
            },
            navArgument("editMode") {
                type = NavType.BoolType
            })) {
            val json = it.arguments?.getString("course")
            val course = jsonAdapter.fromJson(json)

            val bool = it.arguments?.getBoolean("editMode")
            if (course != null && bool != null) {
                addCourseScreen(course, viewModel, navController, bool)
            }
        }

    }
}

