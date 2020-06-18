package com.example.employees.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.employees.POJOs.EmployeeSkillCrossRef;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.POJOs.Skill;
import com.example.employees.room.EmployeeRoomDatabase;
import com.example.employees.room.EmployeeWithSkillsDao;

import java.util.List;

public class SkillRepository {
    private EmployeeWithSkillsDao employeeWithSkillsDao;
    private LiveData<List<Skill>> skillsLiveData;
    public SkillRepository(Application application) {
        EmployeeRoomDatabase db = EmployeeRoomDatabase.getDatabase(application);
        employeeWithSkillsDao = db.employeeWithSkillsDao();
        skillsLiveData = employeeWithSkillsDao.getSkills();
    }

    public LiveData<List<Skill>> getSkills() {
        return skillsLiveData;
    }
}
