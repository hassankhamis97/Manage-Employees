package com.example.employees.POJOs;

import androidx.room.Entity;

@Entity(primaryKeys = {"employeeId", "skillId"})
public class EmployeeSkillCrossRef {
    public long employeeId;
    public long skillId;
}
