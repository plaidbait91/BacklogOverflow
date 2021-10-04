package com.example.backlogoverflow

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import java.lang.Exception


const val LIST_ROUTE = "pending_list"
const val EDIT_ROUTE = "pending_edit/course={course}"


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
            .clickable {
                val json = jsonAdapter.toJson(course)

                navigation.navigate(
                    EDIT_ROUTE
                        .replace("{course}", json)
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
    val selectedIndex: Int by viewModel.selectedIndex.observeAsState(0)

    Column {

       Row(
           modifier = Modifier
               .height(80.dp)
               .padding(12.dp),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.spacedBy(8.dp)
       ) {
           Text(
               text = "Sort by ",
               fontSize = 18.sp,
               fontWeight = FontWeight.Bold
           )

           var expanded by remember { mutableStateOf(false) }
           val items = listOf("Recordings", "Deadline")


           Box(modifier = Modifier
               .wrapContentSize(Alignment.TopStart)) {

               Row(
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Text(
                       items[selectedIndex],
                       modifier = Modifier.clickable(onClick = { expanded = true }),
                       fontSize = 18.sp
                   )

                   Icon(
                       painter = painterResource(id = R.drawable.outline_arrow_drop_down_black_18),
                       contentDescription = "drop_down")
               }


               DropdownMenu(expanded = expanded, onDismissRequest = {
                   expanded = false
               }) {
                   items.forEachIndexed { index, s ->
                       DropdownMenuItem(onClick = {
                           expanded = false

                           if(index == 0) {
                               viewModel.recordingSort()
                           }

                           else {
                               viewModel.deadlineSort()
                           }
                       }) {
                           Text(text = s)
                       }
                   }
               }
           }

       }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.black))) {}

        if(list.isNotEmpty()) {
            LazyColumn {
                items(list) { item ->
                    pendingRow(course = item, navigation = navController)
                }
            }
        }

        else {
            emptyPendingScreen()
        }

    }


}

@Composable
fun editPending(course: Course, pendingViewModel: PendingViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)

    var count by rememberSaveable {
        mutableStateOf(course.count)
    }

    val displayName = course.courseName.replace('|', '/')

    val linkList = remember {
        mutableStateListOf(*course.links.toTypedArray())
    }

    for(i in 1..count) {
        linkList[i - 1] = linkList[i - 1].replace('|', '/')
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier
            .fillMaxHeight(0.05f))

        Row(Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column {
                Text(text = displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp)

                Spacer(modifier = Modifier.height(4.dp))

                var dead = "Not set"

                if(course.deadline != 0L) {
                    val date = Date(course.deadline)
                    val formatter = SimpleDateFormat("dd/MM hh:mma", Locale.getDefault())
                    dead = formatter.format(date)
                }

                Text(text = "Deadline: $dead",
                    color = Color.Gray)
            }


            Button(modifier = Modifier.fillMaxHeight(),
                onClick = {
                    for(i in 1..count) {
                        linkList[i - 1] = linkList[i - 1].replace('/', '|')
                    }

                    course.count = count
                    course.links = linkList

                    pendingViewModel.editCourse(course)

                    navController.navigate(LIST_ROUTE) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                    }

                }) {
                Text(text = "Save changes")
            }

        }

        Spacer(modifier = Modifier
            .fillMaxHeight(0.05f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Recordings left: $count",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp)

            Button(
                onClick = {
                count++
                linkList.add("")
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier
            .fillMaxHeight(0.05f))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(linkList) { i, link ->

                Row(modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(modifier = Modifier
                        .weight(3f),
                        value = link,
                        onValueChange = {
                            linkList[i] = it
                        },
                        label = { Text("Link " + (i + 1).toString()) })

                    Button(modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                        onClick = {
                            try {
                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(linkList[i]))
                                context.startActivity(browserIntent)
                            }

                            catch(e: Exception) {
                                val msg = "This URL seems to be invalid. Check for any typos and make sure https:// is included in the link"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }) {
                        Text(text = "Watch")
                    }

                    Button(modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                        onClick = {
                            count--
                            linkList.removeAt(i)

                            val total = prefs.getInt("count", 0) + 1
                            prefs.edit()
                                .putInt("count", total)
                                .apply()

                        }) {
                        Text(text = "Done")
                    }
                }

            }
        }



    }


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
