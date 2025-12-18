package com.example.learnnex.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.learnnex.ui.theme.White
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
    var visibility by remember { mutableStateOf(false) }
    var terms by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate by remember { mutableStateOf("") }

    val datePicker = DatePickerDialog(
        context,{_,y,m,d-> selectedDate="$y/${m+1}/$d" },year,month,day
        )
    val activity = context as? Activity

    val sharedPreference = context.getSharedPreferences(
        "User",
        Context.MODE_PRIVATE)

    val editor = sharedPreference.edit()

    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope ()

    val userViewModel =  remember { UserViewModel(UserRepoImpl()) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Purple,
                            Blue
                        )
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(value = name,
                onValueChange = { data ->
                    name=data
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                placeholder = {
                    Text("Username")
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = PurpleGrey80,
                    focusedContainerColor  = PurpleGrey80,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = email,
                onValueChange = { data ->
                    email=data
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                placeholder = {
                    Text("abc@gmail.com")
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = PurpleGrey80,
                    focusedContainerColor  = PurpleGrey80,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {
                    selectedDate = it
                },
                enabled = false,
                placeholder = {
                    Text("dd/mm/yyyy")

                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = PurpleGrey80,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = PurpleGrey80,
                    focusedContainerColor = PurpleGrey80,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable() {
                        datePicker.show()
                    }
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(15.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = {
                    Text("Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        visibility = !visibility
                    }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.outline_visibility_24)
                            else
                                painterResource(R.drawable.outline_visibility_off_24),
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = PurpleGrey80,
                    focusedContainerColor = PurpleGrey80,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                },
                placeholder = {
                    Text("Confirm Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        visibility = !visibility
                    }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.outline_visibility_24)
                            else
                                painterResource(R.drawable.outline_visibility_off_24),
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = PurpleGrey80,
                    focusedContainerColor = PurpleGrey80,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = terms,
                    onCheckedChange = {
                        terms = it
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Blue,
                        checkmarkColor = White
                    )
                )
                Text("I agree to terms & Condition", color = Color.White)
            }

            Button(
                onClick = {
                    if (!terms){
                        Toast.makeText(context,
                            "Please agree to the Terms & Conditions!!", Toast.LENGTH_LONG
                        ).show()
                    }else{
                        userViewModel.register(email, password){
                                success, message, userId ->
                            if (success){
                                val user = UserModel(
                                    userId = userId,
                                    name = name,
                                    email = email,
                                    phoneNum = phone
                                )
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                val intent = Intent(
                                    context,
                                    LoginActivity::class.java
                                )
                                context.startActivity(intent)
                                userViewModel.addUserToDatabase(userId, user){
                                        success, message ->
                                    if (success){
                                        Toast.makeText(context, message,
                                            Toast.LENGTH_LONG).show()
                                    }else{
                                        Toast.makeText(context, message,
                                            Toast.LENGTH_LONG).show()
                                    }
                                }
                            }else{
                                Toast.makeText(context,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue
                )
            ) {
                Text("Sign Up", color = White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(buildAnnotatedString {
                append("Already have an account? ")

                withStyle(SpanStyle(color = DarkBlue)) {
                    append("Sign in")
                }
            },
                modifier = Modifier
                    .padding(start = 20.dp, top = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        activity?.finish()
                    },
                textAlign = TextAlign.Start,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PreviewRegistration(){
    RegistrationBody()
}



