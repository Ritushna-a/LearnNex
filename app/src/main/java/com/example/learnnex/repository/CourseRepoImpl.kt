package com.example.learnnex.repository

import com.example.learnnex.model.CourseModel
import com.example.learnnex.model.EnrollmentModel
import com.example.learnnex.model.LessonModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CourseRepoImpl : CourseRepo {
    private val database = FirebaseDatabase.getInstance()
    private val courseRef = database.getReference("Courses")
    private val enrollRef = database.getReference("Enrollments")
    private val lessonRef = database.getReference("Lessons")

    override fun getAllCourses(callback: (Boolean, List<CourseModel>?, String) -> Unit) {
        courseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val courses = snapshot.children.mapNotNull { it.getValue(CourseModel::class.java) }
                callback(true, courses, "Success")
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false, null, error.message)
            }
        })
    }

    override fun addCourse(course: CourseModel, callback: (Boolean, String) -> Unit) {
        val id = course.courseId.ifEmpty { courseRef.push().key ?: "" }
        val finalCourse = course.copy(courseId = id)
        courseRef.child(id).setValue(finalCourse).addOnCompleteListener {
            callback(it.isSuccessful, if (it.isSuccessful) "Course Saved" else it.exception?.message ?: "Error")
        }
    }

    override fun deleteCourse(courseId: String, callback: (Boolean, String) -> Unit) {
        courseRef.child(courseId).removeValue().addOnCompleteListener {
            callback(it.isSuccessful, if (it.isSuccessful) "Deleted" else "Error")
        }
    }

    override fun enrollInCourse(enrollment: EnrollmentModel, callback: (Boolean, String) -> Unit) {
        val id = enrollRef.push().key ?: ""
        enrollRef.child(id).setValue(enrollment.copy(enrollmentId = id)).addOnCompleteListener {
            callback(it.isSuccessful, if (it.isSuccessful) "Enrolled" else "Error")
        }
    }

    override fun getMyCourses(userId: String, callback: (Boolean, List<EnrollmentModel>?, String) -> Unit) {
        enrollRef.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(EnrollmentModel::class.java) }
                callback(true, list, "Success")
            }
            override fun onCancelled(error: DatabaseError) = callback(false, null, error.message)
        })
    }

    override fun getAllEnrollments(callback: (Boolean, List<EnrollmentModel>?, String) -> Unit) {
        enrollRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(EnrollmentModel::class.java) }
                callback(true, list, "Success")
            }
            override fun onCancelled(error: DatabaseError) = callback(false, null, error.message)
        })
    }

    override fun addLesson(lesson: LessonModel, callback: (Boolean, String) -> Unit) {
        val id = lesson.lessonId.ifEmpty { lessonRef.push().key ?: "" }
        lessonRef.child(id).setValue(lesson.copy(lessonId = id)).addOnCompleteListener {
            callback(it.isSuccessful, if (it.isSuccessful) "Lesson Saved" else "Error")
        }
    }

    override fun getLessonsByCourse(courseId: String, callback: (Boolean, List<LessonModel>?, String) -> Unit) {
        lessonRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lessons = snapshot.children.mapNotNull { it.getValue(LessonModel::class.java) }
                callback(true, lessons.sortedBy { it.order }, "Success")
            }
            override fun onCancelled(error: DatabaseError) = callback(false, null, error.message)
        })
    }

    override fun deleteLesson(lessonId: String, callback: (Boolean, String) -> Unit) {
        lessonRef.child(lessonId).removeValue().addOnCompleteListener {
            callback(it.isSuccessful, if (it.isSuccessful) "Deleted" else "Error")
        }
    }
}
