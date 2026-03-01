package com.example.learnnex.view

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<LoginActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testNavigateToRegistration() {
        // Click the "Create Account" button
        composeRule.onNodeWithTag("register").performClick()

        // Verify it navigates to RegistrationActivity
        Intents.intended(hasComponent(RegistrationActivity::class.java.name))
    }

    @Test
    fun testLoginUI_elements_displayed() {
        composeRule.onNodeWithTag("email").assertExists()
        composeRule.onNodeWithTag("password").assertExists()
        composeRule.onNodeWithTag("login").assertExists()
    }
}
