package com.example.learnnex.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
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

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginBody()

        }
    }
}

@Composable
fun LoginBody() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    val loginViewModel = remember { UserViewModel(UserRepoImpl()) }

    val context = LocalContext.current
    val activity = context as Activity


    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope ()

    var showDialog by remember { mutableStateOf(false) }

    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)

    val localEmail: String? = sharedPreferences.getString("email", "")
    val localPassword: String? = sharedPreferences.getString("password", "")


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
       LazyColumn(
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

           item {
               Spacer(modifier = Modifier.height(80.dp))

               Text(
                   text = "Login",
                   fontSize = 28.sp,
                   fontWeight = FontWeight.Bold,
                   color = Color.White
               )

               Spacer(modifier = Modifier.height(40.dp))

               OutlinedTextField(
                   value = email,
                   onValueChange = { data ->
                       email = data
                   },
                   keyboardOptions = KeyboardOptions(
                       keyboardType = KeyboardType.Email
                   ),
                   placeholder = {
                       Text("abc@gmail.com")
                   },
                   colors = TextFieldDefaults.colors(
                       unfocusedContainerColor = PurpleGrey80,
                       focusedContainerColor = PurpleGrey80,
                       focusedIndicatorColor = Blue,
                       unfocusedIndicatorColor = Color.Transparent
                   ),
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 15.dp),
                   shape = RoundedCornerShape(30.dp)
               )

               Spacer(modifier = Modifier.height(20.dp))

               OutlinedTextField(
                   value = password,
                   onValueChange = { data ->
                       password = data
                   },
                   placeholder = {
                       Text("********")

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
                   shape = RoundedCornerShape(30.dp)
               )

               Spacer(modifier = Modifier.height(10.dp))
               Text(
                   text = "Forgot password?", color = Color.Blue,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 15.dp)
                       .clickable {
                           val intent = Intent(context, ForgetPasswordActivity::class.java)
                           context.startActivity(intent)
                           activity.finish()
                       },
                   style = TextStyle(textAlign = TextAlign.End)
               )
               Spacer(modifier = Modifier.height(20.dp))
               Button(
                   onClick = {
                       loginViewModel.login(email, password){
                               success, message ->
                           if (success){
                               val user = UserModel(
                                   email= email
                               )
                               Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                               val intent = Intent(
                                   context,
                                   DashboardActivity::class.java
                               )
                               context.startActivity(intent)
                               activity.finish()
                           }else{
                               Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                           }
                       }

                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 20.dp)
                       .height(55.dp),
                   shape = RoundedCornerShape(30.dp),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = DarkBlue
                   )
               ) {
                   Text("Login", color = Color.White, fontSize = 16.sp)
               }

               Spacer(modifier = Modifier.height(16.dp))


               Text(
                   buildAnnotatedString {
                       append("Don't have account? ")

                       withStyle(SpanStyle(color = DarkBlue)) {
                           append("Sign up")
                       }
                   },
                   modifier = Modifier
                       .padding(start = 20.dp, top = 16.dp)
                       .fillMaxWidth()
                       .clickable {
                           val intent = Intent(context, RegistrationActivity::class.java)
                           context.startActivity(intent)
                       },
                   textAlign = TextAlign.Start,
                   color = Color.White
               )
           }
       }
    }
}




@Preview
@Composable
fun LoginPreview() {
    LoginBody()
}


