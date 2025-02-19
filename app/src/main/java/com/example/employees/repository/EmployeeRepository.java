package com.example.employees.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.employees.POJOs.EmployeeSkillCrossRef;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.room.EmployeeRoomDatabase;
import com.example.employees.room.EmployeeWithSkillsDao;

import java.util.List;

public class EmployeeRepository {
    private static EmployeeRepository instance = null;
    private EmployeeWithSkillsDao employeeWithSkillsDao;
    private LiveData<List<EmployeeWithSkills>> employeesLiveData;
    public static EmployeeRepository getInstance(Application application)
    {
        if (instance == null) {
            instance = new EmployeeRepository(application);
        }
        return instance;
    }
    private EmployeeRepository(Application application) {
        EmployeeRoomDatabase db = EmployeeRoomDatabase.getDatabase(application);
        employeeWithSkillsDao = db.employeeWithSkillsDao();
        employeesLiveData = employeeWithSkillsDao.getEmployeeWithSkills();
    }
   public LiveData<List<EmployeeWithSkills>> getEmployeeWithSkills() {
        return employeesLiveData;
    }
    public void insert(EmployeeWithSkills employeeWithSkills) {
           long employeeId = employeeWithSkillsDao.insert(employeeWithSkills.employee);

           for (int i = 0 ; i < employeeWithSkills.skills.size() ; i++){
               employeeWithSkillsDao.insert(new EmployeeSkillCrossRef(employeeId,employeeWithSkills.skills.get(i).getSkillId()));
           }
        employeesLiveData = employeeWithSkillsDao.getEmployeeWithSkills();
    }

    public void deleteEmployee(EmployeeWithSkills employeeWithSkills) {
        employeeWithSkillsDao.deleteEmployeeSkillCross(employeeWithSkills.employee.getEmployeeId());
        employeeWithSkillsDao.deleteEmployee(employeeWithSkills.employee);

    }

    public void updateEmployee(EmployeeWithSkills employeeWithSkills) {
        employeeWithSkillsDao.deleteEmployeeSkillCross(employeeWithSkills.employee.getEmployeeId());

        for (int i = 0 ; i < employeeWithSkills.skills.size() ; i++){
            employeeWithSkillsDao.insert(new EmployeeSkillCrossRef(employeeWithSkills.employee.getEmployeeId(),employeeWithSkills.skills.get(i).getSkillId()));
        }
        employeeWithSkillsDao.updateEmployee(employeeWithSkills.employee);
    }
}
