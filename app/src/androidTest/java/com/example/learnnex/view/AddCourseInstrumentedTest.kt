package com.example.learnnex.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.learnnex.repository.UserRepo
import com.example.learnnex.viewmodel.UserViewModel
import org.mockito.kotlin.mock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddCourseInstrumentedTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testAddCourseUI_elements_displayed() {
        val mockRepo = mock<UserRepo>()
        val viewModel = UserViewModel(mockRepo)
        
        composeRule.setContent {
            AddCourseScreen(
                viewModel = viewModel,
                onBack = {}
            )
        }

        // Check if all fields with testTags exist
        composeRule.onNodeWithTag("courseTitle").assertExists()
        composeRule.onNodeWithTag("courseDesc").assertExists()
        composeRule.onNodeWithTag("courseCategory").assertExists()
        composeRule.onNodeWithTag("publishCourse").assertExists()
        composeRule.onNodeWithTag("cancelButton").assertExists()
    }
}
