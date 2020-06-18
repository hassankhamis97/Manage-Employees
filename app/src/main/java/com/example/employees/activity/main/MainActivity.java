package com.example.employees.activity.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.employees.POJOs.EmployeeSkillCrossRef;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.R;
import com.example.employees.activity.add_employee.AddOrEditEmployeeActivity;
import com.example.employees.activity.main.adapter.EmployeesRecyclerViewAdapter;
import com.example.employees.room.EmployeeRoomDatabase;
import com.example.employees.room.EmployeeWithSkillsDao;
import com.example.employees.viewModel.GetEmployeesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GetEmployeesViewModel employeesViewModel;
    private RecyclerView employeesRecyclerView;
    public final int ADD_NEW_EMPLOYEE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        employeesRecyclerView = findViewById(R.id.employeeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        employeesRecyclerView.setLayoutManager(layoutManager);
        employeesViewModel = new ViewModelProvider(this).get(GetEmployeesViewModel.class);
        employeesViewModel.getEmployeeWithSkills().observe(this, new Observer<List<EmployeeWithSkills>>() {

            @Override
            public void onChanged(@Nullable final List<EmployeeWithSkills> employeeWithSkills) {
                EmployeesRecyclerViewAdapter employeesRecyclerViewAdapter = EmployeesRecyclerViewAdapter.getInstance(MainActivity.this,employeesViewModel);
                employeesRecyclerViewAdapter.fillEmployeeArray(employeeWithSkills);
                employeesRecyclerView.setAdapter(employeesRecyclerViewAdapter);
            }
        });
    }

    public void addEmployeeBtn(View view) {
        Intent intent = new Intent(this, AddOrEditEmployeeActivity.class);
        intent.putExtra("isEdit",false);
        startActivityForResult(intent,ADD_NEW_EMPLOYEE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_EMPLOYEE && resultCode == RESULT_OK) {
            EmployeeWithSkills employeeWithSkills = (EmployeeWithSkills) data.getSerializableExtra(AddOrEditEmployeeActivity.EXTRA_REPLY);
            employeesViewModel.insert(employeeWithSkills);
        } else if (requestCode == EmployeesRecyclerViewAdapter.EDIT_EMPLOYEE && resultCode == RESULT_OK){
            EmployeeWithSkills employeeWithSkills = (EmployeeWithSkills) data.getSerializableExtra(AddOrEditEmployeeActivity.EXTRA_REPLY);
            employeesViewModel.updateEmployee(employeeWithSkills);
        }
    }
}
