package com.example.employees.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.employees.POJOs.Employee;
import com.example.employees.POJOs.EmployeeSkillCrossRef;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.POJOs.Skill;
import com.example.employees.room.EmployeeRoomDatabase;
import com.example.employees.room.EmployeeWithSkillsDao;

import java.util.List;

public class EmployeeRepository {
    private EmployeeWithSkillsDao employeeWithSkillsDao;
    private LiveData<List<EmployeeWithSkills>> employeesLiveData;

    public EmployeeRepository(Application application) {
        EmployeeRoomDatabase db = EmployeeRoomDatabase.getDatabase(application);
        employeeWithSkillsDao = db.employeeWithSkillsDao();
        employeesLiveData = employeeWithSkillsDao.getEmployeeWithSkills();
    }
   public LiveData<List<EmployeeWithSkills>> getEmployeeWithSkills() {
        return employeesLiveData;
    }
    public void insert(EmployeeWithSkills employeeWithSkills) {
//        EmployeeRoomDatabase.databaseWriteExecutor.execute(() -> {
           long employeeId = employeeWithSkillsDao.insert(employeeWithSkills.employee);
           for (int i = 0 ; i < employeeWithSkills.skills.size() ; i++){
               employeeWithSkillsDao.insert(new EmployeeSkillCrossRef(employeeId,employeeWithSkills.skills.get(i).getSkillId()));
           }
//        });
    }

}
