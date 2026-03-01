package com.example.learnnex.model

data class CourseModel(
                       val courseId: String = "",
                       val courseName: String = "",
                       val description: String = "",
                       val teacherId: String = "",
                       val teacherName: String = "",
                       val category: String = "",
                       val imageUrl: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "courseId" to courseId,
            "courseName" to courseName,
            "description" to description,
            "teacherId" to teacherId,
            "teacherName" to teacherName,
            "category" to category,
            "imageUrl" to imageUrl
        )
    }
}
