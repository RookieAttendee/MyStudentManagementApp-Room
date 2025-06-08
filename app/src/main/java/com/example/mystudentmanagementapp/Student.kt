package com.example.mystudentmanagementapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "students")
data class Student(
    var name: String,
    @PrimaryKey val mssv: String,
    var email: String,
    var phone: String
) : Serializable
