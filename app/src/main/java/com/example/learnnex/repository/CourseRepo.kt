package com.example.learnnex.repository

import com.example.learnnex.model.CourseModel
import com.example.learnnex.model.EnrollmentModel
import com.example.learnnex.model.LessonModel

interface CourseRepo {
    fun getAllCourses(callback: (Boolean, List<CourseModel>?, String) -> Unit)
    fun addCourse(course: CourseModel, callback: (Boolean, String) -> Unit)
    fun deleteCourse(courseId: String, callback: (Boolean, String) -> Unit)

    fun enrollInCourse(enrollment: EnrollmentModel, callback: (Boolean, String) -> Unit)
    fun getMyCourses(userId: String, callback: (Boolean, List<EnrollmentModel>?, String) -> Unit)
    fun getAllEnrollments(callback: (Boolean, List<EnrollmentModel>?, String) -> Unit)

    fun addLesson(lesson: LessonModel, callback: (Boolean, String) -> Unit)
    fun getLessonsByCourse(courseId: String, callback: (Boolean, List<LessonModel>?, String) -> Unit)
    fun deleteLesson(lessonId: String, callback: (Boolean, String) -> Unit)
}