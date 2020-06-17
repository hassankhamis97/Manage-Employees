package com.example.employees.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.employees.POJOs.Skill;
import com.example.employees.repository.EmployeeRepository;
import com.example.employees.repository.SkillRepository;

import java.util.List;

public class SkillsViewModel extends AndroidViewModel {
    private SkillRepository skillRepository;

    private LiveData<List<Skill>> skillsLiveData;

    public SkillsViewModel (Application application) {
        super(application);
        skillRepository = new SkillRepository(application);
        skillsLiveData = skillRepository.getSkills();
    }

    public LiveData<List<Skill>> getEmployeeWithSkills() { return skillsLiveData; }

}
