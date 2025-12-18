package com.example.learnnex.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.learnnex.R
import com.example.learnnex.ui.theme.Blue
import com.example.learnnex.ui.theme.White

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
fun DashboardBody(){

    val context= LocalContext.current
    val activity = context as Activity


    val email = activity.intent.getStringExtra("email")
    val password = activity.intent.getStringExtra("password")

    data class NavItem(val label: String,val icon: Int)
    var selectedIndex by remember { mutableStateOf(0) }

    var ListNav = listOf(
        NavItem(
            label = "Home",
            icon = R.drawable.outline_home_24,
        ),
        NavItem(
            label = "Search",
            icon = R.drawable.baseline_search_24,
        ),
        NavItem(
            label = "Profile",
            icon = R.drawable.outline_person_24,
        ),
        NavItem(
            label = "More",
            icon = R.drawable.baseline_more_horiz_24,
        )
    )
    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    actionIconContentColor = White,
                    titleContentColor = White,
                    navigationIconContentColor = White
                ),
                title = {Text("Dashboard")},
                navigationIcon = {
                    IconButton(onClick = {
                        activity.finish()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.outline_arrow_back_ios_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_settings_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                ListNav.forEachIndexed {index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(item.label)
                        },
                        onClick = {
                            selectedIndex = index
                        },
                        selected = selectedIndex == index
                    )
                }
            }
        }

    ){ padding->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ){
            item {
                when (selectedIndex) {
                    0 -> HomeScreen()
                    1 -> SearchScreen()
                    2 -> NotificationScreen()
                    3 -> MoreScreen()
                    else -> HomeScreen()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDashboard(){
    DashboardBody()
}