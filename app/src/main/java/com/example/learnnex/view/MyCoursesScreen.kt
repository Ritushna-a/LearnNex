package com.example.learnnex.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnnex.model.CourseModel
import com.example.learnnex.model.EnrollmentModel
import com.example.learnnex.ui.theme.Blue
import com.example.learnnex.viewmodel.UserViewModel

@Composable
fun MyCoursesScreen(viewModel: UserViewModel, onNavigateToContent: (CourseModel) -> Unit) {
    val myEnrolledList by viewModel.myCourses.observeAsState(initial = emptyList())
    var selectedEnrollment by remember { mutableStateOf<EnrollmentModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getMyCourses()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        if (myEnrolledList.isNullOrEmpty()) {
            Text(
                "No courses enrolled yet",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(myEnrolledList!!) { enrollment ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedEnrollment = enrollment },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Book, null, tint = Blue)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    enrollment.courseName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text("Tap to view lessons", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        selectedEnrollment?.let { enrollment ->
            AlertDialog(
                onDismissRequest = { selectedEnrollment = null },
                title = { Text(enrollment.courseName, fontWeight = FontWeight.Bold) },
                text = { Text(enrollment.description.ifEmpty { "No description available." }) },
                confirmButton = {
                    Button(
                        onClick = {
                            val course = CourseModel(
                                courseId = enrollment.courseId,
                                courseName = enrollment.courseName,
                                description = enrollment.description
                            )
                            onNavigateToContent(course)
                            selectedEnrollment = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Blue)
                    ) {
                        Text("Open Lessons")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedEnrollment = null }) {
                        Text("Close", color = Color.Gray)
                    }
                }
            )
        }
    }
}