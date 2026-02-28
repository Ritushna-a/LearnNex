package com.example.learnnex.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnnex.R
import com.example.learnnex.model.UserModel
import com.example.learnnex.repository.UserRepoImpl
import com.example.learnnex.ui.theme.Blue
import com.example.learnnex.ui.theme.DarkBlue
import com.example.learnnex.ui.theme.Purple
import com.example.learnnex.ui.theme.PurpleGrey80
import com.example.learnnex.viewmodel.UserViewModel

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrationBody()
        }
    }
}

@Composable
fun RegistrationBody() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var terms by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val isLoading by userViewModel.isLoading.observeAsState(false)

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context, { _, y, m, d -> selectedDate = "$y/${m + 1}/$d" },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Purple, Blue)))
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(30.dp))

            RegistrationTextField(name, { name = it }, "Username")
            RegistrationTextField(email, { email = it }, "Email", KeyboardType.Email)

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Date of Birth (dd/mm/yyyy)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 8.dp)
                    .clickable { datePicker.show() },
                enabled = false,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = PurpleGrey80,
                    disabledTextColor = Color.Black,
                    disabledPlaceholderColor = Color.Gray,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(15.dp)
            )

            RegistrationTextField(phone, { phone = it }, "Phone Number", KeyboardType.Phone)

            PasswordTextField(password, { password = it }, "Password", visibility) { visibility = !visibility }
            PasswordTextField(confirmPassword, { confirmPassword = it }, "Confirm Password", visibility) { visibility = !visibility }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = terms,
                    onCheckedChange = { terms = it },
                    colors = CheckboxDefaults.colors(checkedColor = DarkBlue, checkmarkColor = Color.White)
                )
                Text("I agree to terms & Conditions", color = Color.White, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    when {
                        name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedDate.isEmpty() -> {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                        password != confirmPassword -> {
                            Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                        }
                        !terms -> {
                            Toast.makeText(context, "Please accept terms", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            userViewModel.register(email, password) { success, msg, uid ->
                                if (success) {
                                    val user = UserModel(uid, email, name, selectedDate, phone)
                                    userViewModel.addUserToDatabase(uid, user) { dbSuccess, dbMsg ->
                                        if (dbSuccess) {
                                            context.startActivity(Intent(context, LoginActivity::class.java))
                                            activity?.finish()
                                        }
                                        Toast.makeText(context, dbMsg, Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp).height(55.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                shape = RoundedCornerShape(15.dp)
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Sign Up", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(color = DarkBlue, fontWeight = FontWeight.Bold)) {
                        append("Sign in")
                    }
                },
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                },
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RegistrationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = PurpleGrey80,
            focusedContainerColor = PurpleGrey80,
            focusedIndicatorColor = Blue,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visibility: Boolean,
    onVisibilityToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label) },
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    painter = painterResource(if (visibility) R.drawable.outline_visibility_24 else R.drawable.outline_visibility_off_24),
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = PurpleGrey80,
            focusedContainerColor = PurpleGrey80,
            focusedIndicatorColor = Blue,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}