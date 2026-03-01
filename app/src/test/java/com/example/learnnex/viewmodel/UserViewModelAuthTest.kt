package com.example.learnnex.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.learnnex.repository.UserRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelAuthTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun login_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        var successResult = false
        var messageResult = ""

        viewModel.login("test@gmail.com", "123456") { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(successResult)
        assertEquals("Login success", messageResult)
        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }

    @Test
    fun login_failure_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(false, "Invalid credentials")
            null
        }.`when`(repo).login(eq("wrong@gmail.com"), eq("wrong"), any())

        var successResult = true
        var messageResult = ""

        viewModel.login("wrong@gmail.com", "wrong") { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(!successResult)
        assertEquals("Invalid credentials", messageResult)
        verify(repo).login(eq("wrong@gmail.com"), eq("wrong"), any())
    }
}
