package com.example.employees.activity.add_employee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.POJOs.Skill;
import com.example.employees.R;
import com.example.employees.activity.add_employee.Adapter.SkillsRecyclerViewAdapter;
import com.example.employees.viewModel.GetEmployeesViewModel;
import com.example.employees.viewModel.SkillsViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddEmployeeActivity extends AppCompatActivity {
    private SkillsViewModel skillsViewModel;
    private List<Skill> skills;
    private String[] skillsStr;
    private AutoCompleteTextView autoCompleteTextView1;
    private RecyclerView skillsRecyclerView;
    private List<Skill> selectedSkills;
    private ImageView profileImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.skillsAutoCompleteTextView);
        profileImageView = findViewById(R.id.profileImageView);
        skillsRecyclerView = findViewById(R.id.skillsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        skillsRecyclerView.setLayoutManager(layoutManager);
        skillsViewModel = new ViewModelProvider(this).get(SkillsViewModel.class);
        selectedSkills = new ArrayList<>();
        skillsViewModel.getEmployeeWithSkills().observe(this, new Observer<List<Skill>>() {
            @Override
            public void onChanged(@Nullable final List<Skill> skills) {
                // Update the cached copy of the words in the adapter.
                AddEmployeeActivity.this.skills = skills;
                autoCompleteTextView1.setAdapter(renderSkills());
//                System.out.println(employeeWithSkills);
//                adapter.setWords(words);
            }
        });
    }
    private ArrayAdapter<String> renderSkills() {
        skillsStr = new String[skills.size()];
        for (int i = 0; i < skills.size(); i++) {
            skillsStr[i] = skills.get(i).getName();
        }
        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof String){
                    String skillName=(String) item;
                    if(selectedSkills.stream().filter(f-> f.getName() == skillName).count() == 0){
                        selectedSkills.add(skills.stream().filter(f-> f.getName() == skillName).findFirst().get()) ;
                        SkillsRecyclerViewAdapter skillsRecyclerViewAdapter = new SkillsRecyclerViewAdapter(AddEmployeeActivity.this,selectedSkills);
                        skillsRecyclerView.setAdapter(skillsRecyclerViewAdapter);
                    }

                }
            }
        });
        return new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, skillsStr);

    }
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profileImageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                profileImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }
                        Bitmap bitmap = null;
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        bitmap = BitmapFactory.decodeFile(picturePath);
                        profileImageView.setImageBitmap(bitmap);

//                        if (bitmap != null) {
//                            ImageView rotate = (ImageView) findViewById(R.id.rotate);
//
//                        }
                    }
                    break;
            }
        }
    }

    public void chooseImageBtnAction(View view) {
        selectImage(this);
    }
}
