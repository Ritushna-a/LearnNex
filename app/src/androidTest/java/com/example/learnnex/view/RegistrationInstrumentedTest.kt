package com.example.learnnex.view

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<RegistrationActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testNavigateToLogin() {
        // Click the "Sign in" link
        composeRule.onNodeWithTag("signInLink").performClick()

        // Verify it navigates to LoginActivity
        Intents.intended(hasComponent(LoginActivity::class.java.name))
    }

    @Test
    fun testRegistrationUI_elements_displayed() {
        composeRule.onNodeWithTag("name").assertExists()
        composeRule.onNodeWithTag("email").assertExists()
        composeRule.onNodeWithTag("dob").assertExists()
        composeRule.onNodeWithTag("phone").assertExists()
        composeRule.onNodeWithTag("password").assertExists()
        composeRule.onNodeWithTag("confirmPassword").assertExists()
        composeRule.onNodeWithTag("terms").assertExists()
        composeRule.onNodeWithTag("signUp").assertExists()
    }
}
