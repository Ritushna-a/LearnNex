package com.example.learnnex.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.learnnex.R
import com.example.learnnex.model.CourseModel
import com.example.learnnex.repository.UserRepoImpl
import com.example.learnnex.ui.theme.Blue
import com.example.learnnex.ui.theme.White
import com.example.learnnex.viewmodel.UserViewModel

data class NavItem(val label: String, val icon: Int)
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody() {
    val context = LocalContext.current
    val activity = context as Activity

    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    var selectedIndex by remember { mutableStateOf(0) }

    var showAddCourse by remember { mutableStateOf(false) }
    var editingCourse by remember { mutableStateOf<CourseModel?>(null) }
    var viewingCourse by remember { mutableStateOf<CourseModel?>(null) }

    val currentUser = userViewModel.getCurrentUser()
    val isAdmin = currentUser?.email == "admin@gmail.com"

    if (showAddCourse || editingCourse != null) {
        AddCourseScreen(
            viewModel = userViewModel,
            onBack = {
                showAddCourse = false
                editingCourse = null
            },
            existingCourse = editingCourse
        )
    } else if (viewingCourse != null) {
        CourseContentScreen(
            course = viewingCourse!!,
            viewModel = userViewModel,
            onBack = { viewingCourse = null }
        )
    } else {
        val ListNav = listOf(
            NavItem("Home", R.drawable.outline_home_24),
            NavItem("My Courses", R.drawable.baseline_book_24),
            NavItem("Profile", R.drawable.outline_person_24),
            NavItem("Settings", R.drawable.baseline_settings_24)
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue,
                        titleContentColor = White,
                        navigationIconContentColor = White
                    ),
                    title = {
                        Text(when(selectedIndex) {
                            0 -> "LearnNex"
                            1 -> "Enrolled Courses"
                            2 -> "Profile"
                            else -> "Settings"
                        })
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            val intent = Intent(activity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            activity.startActivity(intent)
                        }) {
                            Icon(painterResource(R.drawable.outline_arrow_back_ios_24), null)
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(containerColor = White) {
                    ListNav.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(painterResource(item.icon), null) },
                            label = { Text(item.label) },
                            onClick = { selectedIndex = index },
                            selected = selectedIndex == index
                        )
                    }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when (selectedIndex) {
                    0 -> HomeScreen(
                        viewModel = userViewModel,
                        onNavigateToAdd = { showAddCourse = true },
                        onNavigateToEdit = { editingCourse = it },
                        onNavigateToContent = { viewingCourse = it }
                    )
                    1 -> MyCoursesScreen(userViewModel, onNavigateToContent = { viewingCourse = it })
                    2 -> ProfileScreen()
                    3 -> SettingsScreen()
                }
            }
        }
    }
}