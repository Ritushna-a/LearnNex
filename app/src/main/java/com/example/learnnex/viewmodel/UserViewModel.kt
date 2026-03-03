package com.example.learnnex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnnex.model.CourseModel
import com.example.learnnex.model.EnrollmentModel
import com.example.learnnex.model.LessonModel
import com.example.learnnex.model.UserModel
import com.example.learnnex.repository.UserRepo
import com.google.firebase.auth.FirebaseUser

class UserViewModel(val repo: UserRepo) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _users = MutableLiveData<UserModel?>()
    val users: LiveData<UserModel?> get() = _users

    private val _courses = MutableLiveData<List<CourseModel>?>()
    val courses: LiveData<List<CourseModel>?> get() = _courses
    private val _myCourses = MutableLiveData<List<EnrollmentModel>?>()
    val myCourses: LiveData<List<EnrollmentModel>?> get() = _myCourses

    private val _lessons = MutableLiveData<List<LessonModel>>()
    val lessons: LiveData<List<LessonModel>> = _lessons
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        _isLoading.value = true
        repo.login(email, password) { success, msg ->
            _isLoading.value = false
            callback(success, msg)
        }
    }

    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        _isLoading.value = true
        repo.register(email, password) { success, msg, uid ->
            _isLoading.value = false
            callback(success, msg, uid)
        }
    }

    fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        _isLoading.value = true
        repo.addUserToDatabase(userId, model) { success, msg ->
            _isLoading.value = false
            callback(success, msg)
        }
    }

    fun getUserById(userId: String) {
        repo.getUserById(userId) { success, user ->
            if (success) _users.postValue(user)
        }
    }

    fun updateProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        _isLoading.value = true
        repo.updateProfile(userId, model) { success, msg ->
            _isLoading.value = false
            callback(success, msg)
        }
    }

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        _isLoading.value = true
        repo.forgetPassword(email) { success, msg ->
            _isLoading.value = false
            callback(success, msg)
        }
    }

    fun getCurrentUser(): FirebaseUser? = repo.getCurrentUser()
    fun logout() = repo.logout()

}