package com.example.learnnex.repository

import com.example.learnnex.model.CourseModel
import com.example.learnnex.model.EnrollmentModel
import com.example.learnnex.model.LessonModel
import com.example.learnnex.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepo {
    fun login(
        email: String,password: String,
        callback: (Boolean, String)-> Unit
    )

    fun register(
        email: String,password: String,
        callback: (Boolean, String, String) -> Unit
    )

    fun  addUserToDatabase(
        userId: String,
        model: UserModel,callback: (Boolean, String) -> Unit
    )

    fun getUserById(
        userId: String,
        callback: (Boolean, UserModel?) -> Unit
    )

    fun getAllUser(
        callback: (Boolean, List<UserModel>?) -> Unit
    )

    fun getCurrentUser() : FirebaseUser?

    fun deleteUser(
        userId: String,
        callback: (Boolean, String) -> Unit
    )

    fun updateProfile(
        userId: String,
        model: UserModel,callback: (Boolean, String) -> Unit
    )
    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    )

    fun logout()

    fun getAllCourses(
        callback: (Boolean, List<CourseModel>?, String) -> Unit
    )

    fun addCourse(
        course: CourseModel,
        callback: (Boolean, String) -> Unit
    )

    fun deleteCourse(
        courseId: String,
        callback: (Boolean, String) -> Unit
    )
    fun enrollInCourse(
        enrollment: EnrollmentModel,
        callback: (Boolean, String) -> Unit
    )

    fun getMyCourses(
        userId: String,
        callback: (Boolean, List<EnrollmentModel>?, String) -> Unit
    )

    fun getAllEnrollments(
        callback: (Boolean, List<EnrollmentModel>?, String) -> Unit
    )

    fun addLesson(
        lesson: LessonModel,
        callback: (Boolean, String) -> Unit
    )

    fun getLessonsByCourse(
        courseId: String,
        callback: (Boolean, List<LessonModel>?, String) -> Unit
    )

    fun deleteLesson(
        lessonId: String,
        callback: (Boolean, String) -> Unit
    )
}