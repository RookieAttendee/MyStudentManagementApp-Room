package com.example.mystudentmanagementapp

import java.io.Serializable

data class Student(
    var name: String,
    var mssv: String,
    var email: String,
    var phone: String
) : Serializable

