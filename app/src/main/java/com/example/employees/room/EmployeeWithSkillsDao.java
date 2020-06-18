package com.example.employees.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.employees.POJOs.Employee;
import com.example.employees.POJOs.EmployeeSkillCrossRef;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.POJOs.Skill;

import java.util.List;

@Dao
public interface EmployeeWithSkillsDao {
    //     Employee Table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long insert(Employee employee);
    @Transaction
    @Query("SELECT * from EMPLOYEE_TABLE")
    public LiveData<List<EmployeeWithSkills>> getEmployeeWithSkills();
    @Delete
    void deleteEmployee(Employee employee);
    @Update
    void updateEmployee(Employee employee);

    //     Skills Table

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long insert(Skill skill);

    @Query("DELETE FROM skill_table")
    void deleteAllSkills();

    @Query("SELECT * from skill_table")
    public LiveData<List<Skill>> getSkills();


    //     EmployeeSkillCrossRef Table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(EmployeeSkillCrossRef employeeSkillCrossRef);

    @Query("SELECT * from EmployeeSkillCrossRef")
    public List<EmployeeSkillCrossRef> getEmployeeSkillCrossRef();

    @Query("DELETE FROM EmployeeSkillCrossRef where employeeId = :employeeId")
    void deleteEmployeeSkillCross(long employeeId);


}
