package com.example.employees.POJOs;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class EmployeeWithSkills {
    @Embedded
    public Employee employee;
    @Relation(
            parentColumn = "employeeId",
            entityColumn = "skillId",
            associateBy = @Junction(EmployeeSkillCrossRef.class)
    )
    public List<Skill> skills;
}
