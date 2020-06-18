package com.example.employees.activity.add_employee;

import androidx.annotation.NonNull;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.employees.POJOs.Employee;
import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.POJOs.Skill;
import com.example.employees.R;
import com.example.employees.activity.add_employee.adapter.SkillsRecyclerViewAdapter;
import com.example.employees.viewModel.SkillsViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class AddOrEditEmployeeActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "employeeWithSkills";
    private SkillsViewModel skillsViewModel;
    private List<Skill> skills;
    private String[] skillsStr;
    private AutoCompleteTextView autoCompleteTextView1;
    private RecyclerView skillsRecyclerView;
    private List<Skill> selectedSkills;
    private ImageView profileImageView;
    private EmployeeWithSkills employeeWithSkills;
    private TextView employeeName;
    private TextView employeeEmail;
    private Button save_edit_Btn;
    byte[] imageArray;
    boolean isEdit;
    SkillsRecyclerViewAdapter skillsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.skillsAutoCompleteTextView);
        employeeName = findViewById(R.id.name_txt);
        employeeEmail = findViewById(R.id.email_txt);
        profileImageView = findViewById(R.id.profileImageView);
        skillsRecyclerView = findViewById(R.id.skillsRecyclerView);
        save_edit_Btn = findViewById(R.id.save_Edit_Employee_Btn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        skillsRecyclerView.setLayoutManager(layoutManager);
        skillsViewModel = new ViewModelProvider(this).get(SkillsViewModel.class);
        selectedSkills = new ArrayList<>();
        employeeWithSkills = new EmployeeWithSkills();

        skillsRecyclerViewAdapter = SkillsRecyclerViewAdapter.getInstance(AddOrEditEmployeeActivity.this);
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        if (isEdit){
            save_edit_Btn.setText("Edit");
            fillData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        skillsViewModel.getEmployeeWithSkills().observe(this, new Observer<List<Skill>>() {
            @Override
            public void onChanged(@Nullable final List<Skill> skills) {
                AddOrEditEmployeeActivity.this.skills = skills;
                autoCompleteTextView1.setAdapter(renderSkills());
            }
        });
    }

    private void fillData() {
        employeeWithSkills = (EmployeeWithSkills) getIntent().getSerializableExtra("employeeWithSkills");
        employeeName.setText(employeeWithSkills.employee.getName());
        employeeEmail.setText(employeeWithSkills.employee.getEmail());

        if (employeeWithSkills.employee.getImage() != null) {
            imageArray = employeeWithSkills.employee.getImage();
            setImageArrayToImageView();
        }
        else {
            profileImageView.setImageResource(R.drawable.default_profile_image);
        }
        selectedSkills = employeeWithSkills.skills;
        skillsRecyclerViewAdapter.fillSkillsArray(selectedSkills);
        skillsRecyclerView.setAdapter(skillsRecyclerViewAdapter);
    }

    private void setImageArrayToImageView() {

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
        profileImageView.setImageBitmap(bitmap);
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
                        skillsRecyclerViewAdapter.fillSkillsArray(selectedSkills);
                        skillsRecyclerView.setAdapter(skillsRecyclerViewAdapter);
                    }
                    autoCompleteTextView1.setText("");
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
                    startActivityForResult(pickPhoto, 1);

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
                        resizeImageAndConvertToArray(selectedImage);

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri imageUri = data.getData();
                        try {
                            Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            resizeImageAndConvertToArray(selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void resizeImageAndConvertToArray(Bitmap selectedImage) {
        Bitmap resizedImageBitmap = getResizedBitmap(selectedImage, 500);
        profileImageView.setImageBitmap(resizedImageBitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageArray = stream.toByteArray();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public void chooseImageBtnAction(View view) {
        selectImage(this);
    }

    public void saveEmployee_BtnAction(View view) {
        if (employeeName.getText().toString().trim().isEmpty() == true) {
            employeeName.setError("This field is required");
        }
        else {
            Intent replyIntent = new Intent();

            if (!isEdit) {
                employeeWithSkills.employee = new Employee(employeeName.getText().toString(), employeeEmail.getText().toString(), imageArray);
            }
            else {
                employeeWithSkills.employee.setName(employeeName.getText().toString());
                employeeWithSkills.employee.setEmail(employeeEmail.getText().toString());
                employeeWithSkills.employee.setImage(imageArray);
            }
            employeeWithSkills.skills = selectedSkills;
            replyIntent.putExtra(EXTRA_REPLY, employeeWithSkills);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        employeeName.setText(savedInstanceState.getString("employeeName"));
        employeeEmail.setText(savedInstanceState.getString("employeeEmail"));
        imageArray = savedInstanceState.getByteArray("imageArray");
        selectedSkills = savedInstanceState.getParcelableArrayList("selectedSkills");
        skillsRecyclerView.setAdapter(skillsRecyclerViewAdapter);
        if (imageArray != null)
            setImageArrayToImageView();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("employeeName", employeeName.getText().toString());
        outState.putString("employeeEmail", employeeEmail.getText().toString());
        outState.putByteArray("imageArray", imageArray);
        outState.putParcelableArrayList("selectedSkills", (ArrayList<? extends Parcelable>) selectedSkills);
//        outState.putParcelableArrayList("skillsList", (ArrayList<? extends Parcelable>) selectedSkills.to);
    }

}
