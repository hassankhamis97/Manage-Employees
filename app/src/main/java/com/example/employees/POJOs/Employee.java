package com.example.employees.POJOs;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "employee_table")
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long employeeId;
    public String name;
//    public
}
