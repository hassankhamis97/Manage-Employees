package com.example.employees.POJOs;

import androidx.room.Entity;

@Entity(primaryKeys = {"employeeId", "skillId"})
public class EmployeeSkillCrossRef {
    private long employeeId;
    private int skillId;

    public EmployeeSkillCrossRef(long employeeId, int skillId) {
        this.employeeId = employeeId;
        this.skillId = skillId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public int getSkillId() {
        return skillId;
    }
}
