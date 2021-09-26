package com.example.backlogoverflow

sealed class NavDrawerItem(var nav: String, var image: Int, var title: String) {
    object Courses: NavDrawerItem("courses", R.drawable.outline_library_books_black_24, "Courses")
    object Pending: NavDrawerItem("pending", R.drawable.outline_watch_later_black_24, "Pending")
    object Profile: NavDrawerItem("profile", R.drawable.outline_person_black_24, "My Profile")
}
