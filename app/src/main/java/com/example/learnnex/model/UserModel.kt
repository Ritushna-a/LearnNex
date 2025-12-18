package com.example.learnnex.model

data class UserModel(
    val userId: String="",
    val email: String="" ,
    val name: String="",

    val dob: String="",
    val phoneNum: String=""

){
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "userId" to userId,
            "email" to email,
            "name" to name,
            "dob" to dob,
            "phoneNum" to phoneNum
        )
    }
}
