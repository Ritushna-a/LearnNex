package com.example.learnnex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnnex.model.CourseModel
import com.example.learnnex.model.EnrollmentModel
import com.example.learnnex.model.LessonModel
import com.example.learnnex.repository.CourseRepo
import com.example.learnnex.repository.UserRepo

class CourseViewModel(
    private val courseRepo: CourseRepo,
    private val userRepo: UserRepo
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _courses = MutableLiveData<List<CourseModel>>()
    val courses: LiveData<List<CourseModel>> = _courses

    private val _myCourses = MutableLiveData<List<EnrollmentModel>>()
    val myCourses: LiveData<List<EnrollmentModel>> = _myCourses

    private val _lessons = MutableLiveData<List<LessonModel>>()
    val lessons: LiveData<List<LessonModel>> = _lessons

    fun isAdmin(): Boolean {
        val currentUser = userRepo.getCurrentUser()
        return currentUser?.email == "admin@gmail.com"
    }

    fun getCurrentUser() = userRepo.getCurrentUser()

    fun fetchAllCourses() {
        _isLoading.value = true
        courseRepo.getAllCourses { success, list, _ ->
            _isLoading.postValue(false)
            if (success) _courses.postValue(list ?: emptyList())
        }
    }

    /**
     * Admin specific: Fetches all enrollments across all users to display student lists.
     */
    fun fetchEnrollmentsForAdmin() {
        _isLoading.value = true
        courseRepo.getAllEnrollments { success, list, _ ->
            _isLoading.postValue(false)
            if (success) _myCourses.postValue(list ?: emptyList())
        }
    }

    fun addOrUpdateCourse(course: CourseModel, onResult: (Boolean, String) -> Unit) {
        _isLoading.value = true
        courseRepo.addCourse(course) { s, m ->
            _isLoading.value = false
            onResult(s, m)
        }
    }

    fun deleteCourse(courseId: String) {
        courseRepo.deleteCourse(courseId) { s, _ -> if (s) fetchAllCourses() }
    }

    fun getMyCourses() {
        val uid = userRepo.getCurrentUser()?.uid ?: return
        courseRepo.getMyCourses(uid) { s, list, _ ->
            if (s) _myCourses.postValue(list ?: emptyList())
        }
    }

    fun fetchLessons(courseId: String) {
        _isLoading.value = true
        courseRepo.getLessonsByCourse(courseId) { s, list, _ ->
            _isLoading.postValue(false)
            if (s) _lessons.postValue(list ?: emptyList())
        }
    }

    fun addLesson(lesson: LessonModel, onResult: (Boolean, String) -> Unit) {
        courseRepo.addLesson(lesson) { s, m -> onResult(s, m) }
    }

    fun deleteLesson(lessonId: String, courseId: String) {
        courseRepo.deleteLesson(lessonId) { s, _ -> if (s) fetchLessons(courseId) }
    }

    fun enrollInCourse(course: CourseModel, onResult: (Boolean, String) -> Unit) {
        val user = userRepo.getCurrentUser() ?: return
        val enrollment = EnrollmentModel(
            userId = user.uid,
            courseId = course.courseId,
            courseName = course.courseName,
            description = course.description
        )
        courseRepo.enrollInCourse(enrollment, onResult)
    }
}