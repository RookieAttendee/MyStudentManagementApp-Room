package com.example.mystudentmanagementapp

import androidx.room.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAll(): List<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(student: Student)

    @Update
    fun update(student: Student)

    @Delete
    fun delete(student: Student)

    @Query("SELECT * FROM students WHERE mssv = :mssv LIMIT 1")
    fun getById(mssv: String): Student?
}
