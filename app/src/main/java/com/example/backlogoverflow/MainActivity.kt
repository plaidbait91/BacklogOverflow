package com.example.backlogoverflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.backlogoverflow.database.CourseDatabase
import com.example.backlogoverflow.ui.theme.BacklogOverflowTheme
import com.example.backlogoverflow.viewmodel.CourseViewModel
import com.example.backlogoverflow.viewmodel.CourseViewModelFactory
import com.example.backlogoverflow.viewmodel.PendingViewModel
import com.example.backlogoverflow.viewmodel.PendingViewModelFactory
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this).application

        val dataSource = CourseDatabase.getInstance(application).courseDao

        val courseViewModel: CourseViewModel by viewModels { CourseViewModelFactory(dataSource = dataSource) }
        val pendingViewModel: PendingViewModel by viewModels { PendingViewModelFactory(dataSource = dataSource) }

        setContent {
            BacklogOverflowTheme {
                MainScreen(courseViewModel, pendingViewModel)
                }
            }
        }
    }


@Composable
fun MainScreen(viewModel: CourseViewModel, viewModel2: PendingViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    // If you want the drawer from the right side, uncomment the following
    // CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    val title = stringResource(id = R.string.app_name)
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState, title) },
        drawerBackgroundColor = colorResource(id = R.color.purple_700),
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        },
    )
    {
        Navigation(navController, viewModel, viewModel2)
    }
    // }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState, title: String) {
    TopAppBar(
        title = { Text(text = title, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = colorResource(id = R.color.purple_700),
        contentColor = Color.White
    )
}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val items = listOf(
        NavDrawerItem.Courses,
        NavDrawerItem.Pending,
        NavDrawerItem.Profile
    )
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.white))
    ) {
        // Header
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(colorResource(id = R.color.purple_500)),
            verticalArrangement = Arrangement.Center) {
            Text(
                text = "Menu",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        // Space between
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )
        // List of navigation items
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.nav, onItemClick = {
                navController.navigate(item.nav) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true

                }
                // Close drawer
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Ghot Lives Matter",
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DrawerItem(item: NavDrawerItem, selected: Boolean, onItemClick: (NavDrawerItem) -> Unit) {
    val background = if (selected) R.color.ripple else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(60.dp)
            .background(colorResource(id = background))
            .padding(15.dp)
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.Black),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(id = R.color.gray))) {}
}

@Preview(showBackground = false)
@Composable
fun DrawerItemPreview() {
    DrawerItem(item = NavDrawerItem.Profile, selected = false, onItemClick = {})
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
}

@Preview(showBackground = false)
@Composable
fun TopBarPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    TopBar(scope = scope, scaffoldState = scaffoldState, stringResource(R.string.app_name))
}

/*@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}*/

@Composable
fun Navigation(navController: NavHostController, courseViewModel: CourseViewModel, pendingViewModel: PendingViewModel) {
    NavHost(navController, startDestination = NavDrawerItem.Courses.nav) {
        composable(NavDrawerItem.Courses.nav) {
            mainCoursesScreen(courseViewModel)
        }
        composable(NavDrawerItem.Pending.nav) {
            mainPendingScreen(pendingViewModel)
        }
        composable(NavDrawerItem.Profile.nav) {
            profileScreen()
        }

    }
}