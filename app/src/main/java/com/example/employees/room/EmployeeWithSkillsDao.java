package com.example.employees.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.employees.POJOs.Employee;
import com.example.employees.POJOs.EmployeeSkillCrossRef;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.POJOs.Skill;

import java.util.List;

@Dao
public interface EmployeeWithSkillsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long insert(Employee employee);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long insert(Skill skill);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(EmployeeSkillCrossRef employeeSkillCrossRef);
    @Query("DELETE FROM skill_table")
    void deleteAllSkills();
    @Transaction
    @Query("SELECT * from EMPLOYEE_TABLE")
    public LiveData<List<EmployeeWithSkills>> getEmployeeWithSkills();
    @Query("SELECT * from skill_table")
    public LiveData<List<Skill>> getSkills();
}
