package com.example.learnnex.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learnnex.R
import com.example.learnnex.model.UserModel
import com.example.learnnex.repository.UserRepoImpl
import com.example.learnnex.viewmodel.UserViewModel

@Composable
fun ProfileScreen() {
    val viewModel: UserViewModel = remember { UserViewModel(UserRepoImpl()) }
    val firebaseUser = viewModel.getCurrentUser()
    val userState = viewModel.users.observeAsState()

    LaunchedEffect(firebaseUser?.uid) {
        firebaseUser?.uid?.let { viewModel.getUserById(it) }
    }

    val user = userState.value

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (user == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            ProfileUI(user = user, viewModel = viewModel)
        }
    }
}

@Composable
fun ProfileUI(user: UserModel, viewModel: UserViewModel) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }

    // --- Image Picker Logic ---
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        if (uri != null) {
            Toast.makeText(context, "Image selected!", Toast.LENGTH_SHORT).show()
            // Note: In a real app, you'd call viewModel.uploadToFirebase(uri) here
        }
    }

    // Form States
    var name by remember { mutableStateOf(user.name) }
    var phone by remember { mutableStateOf(user.phoneNum) }
    var dob by remember { mutableStateOf(user.dob) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // --- Header Section ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primaryContainer, Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Profile Picture with Edit Overlay
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            .clickable { galleryLauncher.launch("image/*") }
                    ) {
                        // If you have Coil library, use AsyncImage(model = selectedImageUri...)
                        // Using standard Image for now:
                        Image(
                            painter = painterResource(id = R.drawable.outline_person_24),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().padding(12.dp)
                        )
                    }

                    // Edit Icon Badge
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(36.dp)
                            .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            .clickable { galleryLauncher.launch("image/*") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        // --- Content Section ---
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {

            AnimatedContent(targetState = isEditing, label = "toggle_edit") { editing ->
                if (editing) {
                    Column {
                        ProfileTextField(value = name, onValueChange = { name = it }, label = "Name", icon = Icons.Default.Person)
                        ProfileTextField(value = phone, onValueChange = { phone = it }, label = "Phone", icon = Icons.Default.Phone)
                        ProfileTextField(value = dob, onValueChange = { dob = it }, label = "Birthday", icon = Icons.Default.Cake)
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            InfoRow(Icons.Default.Person, "Full Name", user.name)
                            InfoRow(Icons.Default.Email, "Email", user.email)
                            InfoRow(Icons.Default.Phone, "Phone", user.phoneNum)
                            InfoRow(Icons.Default.Cake, "Birthday", user.dob)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Button
            Button(
                onClick = {
                    if (isEditing) {
                        val updatedUser = user.copy(name = name, phoneNum = phone, dob = dob)
                        viewModel.updateProfile(user.userId, updatedUser) { _, msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                    isEditing = !isEditing
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(if (isEditing) Icons.Default.Save else Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isEditing) "Save Changes" else "Edit Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout/Delete Button
            OutlinedButton(
                onClick = {
                    viewModel.deleteUser(user.userId) { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        if (success) {
                            viewModel.logout()
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete Account")
            }
        }
    }
}

@Composable
fun ProfileTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(10.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}