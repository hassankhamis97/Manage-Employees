package com.example.employees.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.employees.POJOs.Employee;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.POJOs.Skill;
import com.example.employees.R;
import com.example.employees.viewModel.GetEmployeesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GetEmployeesViewModel employeesViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        employeesViewModel = new ViewModelProvider(this).get(GetEmployeesViewModel.class);
        employeesViewModel.getEmployeeWithSkills().observe(this, new Observer<List<EmployeeWithSkills>>() {
            @Override
            public void onChanged(@Nullable final List<EmployeeWithSkills> employeeWithSkills) {
                // Update the cached copy of the words in the adapter.
                System.out.println(employeeWithSkills);
//                adapter.setWords(words);
            }
        });
        // test
        byte[] x = "Any String you want".getBytes();
        Employee emp = new Employee("hassan","hassankhamis97@hotmail.com",x);
        Skill skill1 = new Skill(1,"PHP");
        Skill skill2 = new Skill(2,"fsd");
        EmployeeWithSkills employeeWithSkills = new EmployeeWithSkills();
        employeeWithSkills.employee = emp;
        employeeWithSkills.skills = new ArrayList<>();
        employeeWithSkills.skills.add(skill1);
        employeeWithSkills.skills.add(skill2);

        employeesViewModel.insert(employeeWithSkills);
    }
}
