package com.example.task.data.models

data class User (
    val id : Int? = null,
    val name : String,
    val email : String,
    val gender : String = "male",
    val status : String = "inactive",
    val created_at : String? = null,
    val updated_at : String? = null
)