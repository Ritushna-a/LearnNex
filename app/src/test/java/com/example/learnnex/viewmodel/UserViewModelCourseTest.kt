package com.example.learnnex.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.learnnex.model.CourseModel
import com.example.learnnex.repository.UserRepo
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelCourseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun addCourse_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)
        val course = CourseModel(courseName = "Kotlin", description = "Learn Kotlin", category = "Coding")

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Course added successfully")
            null
        }.`when`(repo).addCourse(eq(course), any())

        var successResult = false
        viewModel.addCourse(course) { success, _ ->
            successResult = success
        }

        assertTrue(successResult)
        verify(repo).addCourse(eq(course), any())
    }
}
