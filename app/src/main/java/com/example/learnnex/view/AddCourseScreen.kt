package com.example.learnnex.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnnex.model.CourseModel
import com.example.learnnex.ui.theme.Blue
import com.example.learnnex.ui.theme.White
import com.example.learnnex.viewmodel.UserViewModel

@Composable
fun AddCourseScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit,
    existingCourse: CourseModel? = null
) {
    var name by remember { mutableStateOf(existingCourse?.courseName ?: "") }
    var desc by remember { mutableStateOf(existingCourse?.description ?: "") }
    var category by remember { mutableStateOf(existingCourse?.category ?: "") }
    val isLoading by viewModel.isLoading.observeAsState(false)
    val currentUser = viewModel.getCurrentUser()

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF1F3F6)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.9f).padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    if (existingCourse == null) "Create New Course" else "Update Course",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Course Title") },
                    modifier = Modifier.fillMaxWidth().testTag("courseTitle"),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = desc, onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().testTag("courseDesc"),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = category, onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth().testTag("courseCategory"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        val courseToSave = CourseModel(
                            courseId = existingCourse?.courseId ?: "",
                            courseName = name,
                            description = desc,
                            category = category,
                            teacherId = existingCourse?.teacherId ?: currentUser?.uid ?: "",
                            teacherName = existingCourse?.teacherName ?: "Admin",
                            imageUrl = existingCourse?.imageUrl ?: ""
                        )
                        viewModel.addCourse(courseToSave) { success, _ ->
                            if (success) onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp).testTag("publishCourse"),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                    else Text(if (existingCourse == null) "Publish Course" else "Save Changes",
                        fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                TextButton(onClick = onBack, modifier = Modifier.testTag("cancelButton")) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        }
    }
}
