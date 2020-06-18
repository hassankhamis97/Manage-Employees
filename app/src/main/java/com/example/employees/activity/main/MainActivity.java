package com.example.employees.activity.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.employees.POJOs.Employee;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.POJOs.Skill;
import com.example.employees.R;
import com.example.employees.activity.add_employee.AddEmployeeActivity;
import com.example.employees.viewModel.GetEmployeesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GetEmployeesViewModel employeesViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
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
//        byte[] x = "Any String you want".getBytes();
//        Employee emp = new Employee("hassan","hassankhamis97@hotmail.com",x);
//        Skill skill1 = new Skill(1,"PHP");
//        Skill skill2 = new Skill(2,"fsd");
//        EmployeeWithSkills employeeWithSkills = new EmployeeWithSkills();
//        employeeWithSkills.employee = emp;
//        employeeWithSkills.skills = new ArrayList<>();
//        employeeWithSkills.skills.add(skill1);
//        employeeWithSkills.skills.add(skill2);
//
//        employeesViewModel.insert(employeeWithSkills);
    }

    public void addEmployeeBtn(View view) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        startActivityForResult(intent,1);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
           Object emp = data.getSerializableExtra(AddEmployeeActivity.EXTRA_REPLY);
           if (emp instanceof EmployeeWithSkills) {
                EmployeeWithSkills employeeWithSkills = (EmployeeWithSkills) emp;
               employeesViewModel.insert(employeeWithSkills);

           }
        } else {
//            Toast.makeText(
//                    getApplicationContext(),
//                    R.string.empty_not_saved,
//                    Toast.LENGTH_LONG).show();
        }
    }
}
