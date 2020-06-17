package com.example.employees.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.repository.EmployeeRepository;

import java.util.List;

public class GetEmployeesViewModel extends AndroidViewModel {
    private EmployeeRepository employeeRepository;

    private LiveData<List<EmployeeWithSkills>> employeesLiveData;

    public GetEmployeesViewModel (Application application) {
        super(application);
        employeeRepository = new EmployeeRepository(application);
        employeesLiveData = employeeRepository.getEmployeeWithSkills();
    }

    public LiveData<List<EmployeeWithSkills>> getEmployeeWithSkills() { return employeesLiveData; }

    public void insert(EmployeeWithSkills employeeWithSkills) { employeeRepository.insert(employeeWithSkills); }
}