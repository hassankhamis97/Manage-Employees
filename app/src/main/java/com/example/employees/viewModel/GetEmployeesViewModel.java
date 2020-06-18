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
        employeeRepository = EmployeeRepository.getInstance(application);
        employeesLiveData = employeeRepository.getEmployeeWithSkills();
    }
    public void insert(EmployeeWithSkills employeeWithSkills) { employeeRepository.insert(employeeWithSkills); }

    public LiveData<List<EmployeeWithSkills>> getEmployeeWithSkills() { return employeesLiveData; }

    public void deleteEmployee(EmployeeWithSkills employeeWithSkills) {
        employeeRepository.deleteEmployee(employeeWithSkills);
    }
}
