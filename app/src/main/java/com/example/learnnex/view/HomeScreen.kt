package com.example.learnnex.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnnex.model.CourseModel
import com.example.learnnex.ui.theme.Blue
import com.example.learnnex.ui.theme.White
import com.example.learnnex.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: UserViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (CourseModel) -> Unit,
    onNavigateToContent: (CourseModel) -> Unit
) {
    val courses by viewModel.courses.observeAsState(initial = emptyList())
    val myEnrolledList by viewModel.myCourses.observeAsState(initial = emptyList())
    val currentUser = viewModel.getCurrentUser()
    val isAdmin = currentUser?.email == "admin@gmail.com"

    var selectedCourseForDetail by remember { mutableStateOf<CourseModel?>(null) }
    var courseToDelete by remember { mutableStateOf<CourseModel?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAllCourses()
        viewModel.getMyCourses()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Course", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    if (isAdmin) {
                        IconButton(
                            onClick = onNavigateToAdd,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(40.dp)
                                .background(Color(0xFFF1F3F9), RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = Blue)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(52.dp))
                    }
                },
                navigationIcon = { Spacer(modifier = Modifier.width(52.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFBFBFE))
        ) {
            if (courses.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No courses available", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(courses!!) { course ->
                        CourseCardHorizontal(
                            course = course,
                            isAdmin = isAdmin,
                            onDelete = {
                                courseToDelete = course
                                showDeleteConfirm = true
                            },
                            onEdit = { onNavigateToEdit(course) },
                            onClick = { selectedCourseForDetail = course }
                        )
                    }
                }
            }

            selectedCourseForDetail?.let { course ->
                val isEnrolled = myEnrolledList?.any { it.courseId == course.courseId } ?: false

                AlertDialog(
                    onDismissRequest = { selectedCourseForDetail = null },
                    title = { Text(course.courseName, fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text(course.description)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Category: ${course.category}", color = Blue, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text("Instructor: ${course.teacherName}", color = Color.Gray, fontSize = 13.sp)
                        }
                    },
                    confirmButton = {
                        if (isEnrolled || isAdmin) {
                            // If already enrolled or is admin, go straight to lessons
                            Button(
                                onClick = {
                                    onNavigateToContent(course)
                                    selectedCourseForDetail = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("Go to Lessons") }
                        } else {
                            // If NOT enrolled, show Enroll button
                            Button(
                                onClick = {
                                    viewModel.enrollInCourse(course) { success, _ ->
                                        if (success) {
                                            viewModel.getMyCourses() // Refresh enrollment list
                                            selectedCourseForDetail = null
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("Enroll Now") }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { selectedCourseForDetail = null }) {
                            Text("Cancel", color = Color.Gray)
                        }
                    }
                )
            }

            if (showDeleteConfirm) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false },
                    title = { Text("Delete Course", fontWeight = FontWeight.Bold) },
                    text = { Text("Are you sure you want to delete '${courseToDelete?.courseName}'? This cannot be undone.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                courseToDelete?.let { viewModel.deleteCourse(it.courseId) }
                                showDeleteConfirm = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(8.dp)
                        ) { Text("Delete", color = White) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@Composable
fun CourseCardHorizontal(
    course: CourseModel,
    isAdmin: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF7281D2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Book, null, tint = White, modifier = Modifier.size(36.dp))
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = course.courseName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text("By ${course.teacherName}", fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Surface(color = Color(0xFFE8EAF6), shape = RoundedCornerShape(4.dp)) {
                    Text(
                        text = course.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp, color = Color(0xFF5C6BC0), fontWeight = FontWeight.Medium
                    )
                }
            }

            if (isAdmin) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(50.dp)
                        .background(Color(0xFFF8F9FA)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, "Edit", tint = Blue, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color.Red, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}