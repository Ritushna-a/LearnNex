package com.example.learnnex.view

import android.app.Activity
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnnex.model.UserModel
import com.example.learnnex.repository.UserRepoImpl
import com.example.learnnex.ui.theme.Blue
import com.example.learnnex.ui.theme.DarkBlue
import com.example.learnnex.ui.theme.Purple
import com.example.learnnex.ui.theme.PurpleGrey80
import com.example.learnnex.viewmodel.UserViewModel

class ForgetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForgetBody()

        }
    }
}

@Composable
fun ForgetBody(){
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val activity= context as Activity
    val forgotpassViewModel = remember { UserViewModel(UserRepoImpl()) }

    Scaffold { padding ->
        LazyColumn (
            modifier = Modifier.fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Purple,
                            Blue
                        )
                    )
                )
                .padding(padding)
                .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

        ) {
            item {
                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = "Forgot Password",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

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
                    shape = RoundedCornerShape(15.dp)
                )


                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        forgotpassViewModel.forgetPassword(email){
                                success, message ->
                            if (success){
                                val forget = UserModel(
                                    email = email
                                )
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                            }else{
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlue
                    )
                ) {
                    Text("Send Reset Code", color = Color.White)
                }
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Back to Login",
                    color = DarkBlue,
                    modifier = Modifier
                        .clickable{
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            activity.finish()
                        }
                )

            }
        }
    }
}