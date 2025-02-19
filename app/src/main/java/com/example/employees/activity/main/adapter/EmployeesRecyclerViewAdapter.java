package com.example.employees.activity.main.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.POJOs.EmployeeWithSkills;
import com.example.employees.R;
import com.example.employees.activity.add_employee.AddOrEditEmployeeActivity;
import com.example.employees.viewModel.GetEmployeesViewModel;

import java.util.List;

public class EmployeesRecyclerViewAdapter extends RecyclerView.Adapter<EmployeesRecyclerViewAdapter.ViewHolder> {
    private static EmployeesRecyclerViewAdapter instance = null;
    List<EmployeeWithSkills> employeeWithSkillsList;
    Context context;
    GetEmployeesViewModel employeesViewModel;
    int deleteItemPosition;
    public static final int EDIT_EMPLOYEE = 2;
    public static EmployeesRecyclerViewAdapter getInstance(Context context,GetEmployeesViewModel employeesViewModel)
    {
        if (instance == null) {
            instance = new EmployeesRecyclerViewAdapter(context,employeesViewModel);
        }

        return instance;
    }
    public EmployeesRecyclerViewAdapter(Context context,GetEmployeesViewModel employeesViewModel) {
        this.context = context;
        this.employeeWithSkillsList = employeeWithSkillsList;
        this.employeesViewModel = employeesViewModel;
    }
    public void fillEmployeeArray(List<EmployeeWithSkills> employeeWithSkillsList){
        this.employeeWithSkillsList = employeeWithSkillsList;
    }
    @NonNull
    @Override
    public EmployeesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.employee_item_layout,recyclerView,false);
        EmployeesRecyclerViewAdapter.ViewHolder vh = new EmployeesRecyclerViewAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.nameLbl.setText(employeeWithSkillsList.get(position).employee.getName());
        holder.emailLbl.setText(employeeWithSkillsList.get(position).employee.getEmail());

        if (employeeWithSkillsList.get(position).employee.getImage() != null) {
            byte[] bitmapdata = employeeWithSkillsList.get(position).employee.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            holder.employeeImage.setImageBitmap(getRoundedTopLeftCornerBitmap(bitmap,120));
        }
        else {
            holder.employeeImage.setImageResource(R.drawable.default_profile_image);
        }
        holder.deleteEmployee_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemPosition = position;
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        holder.editEmployee_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemPosition = position;
                Intent intent = new Intent(context, AddOrEditEmployeeActivity.class);
                intent.putExtra("isEdit",true);
                intent.putExtra("employeeWithSkills",employeeWithSkillsList.get(position));
                ((Activity) context).startActivityForResult(intent,EDIT_EMPLOYEE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeWithSkillsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameLbl;
        public TextView emailLbl;
        public Button deleteEmployee_Btn;
        public Button editEmployee_Btn;
        public ImageView employeeImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameLbl = itemView.findViewById(R.id.employeeNameTxt_main);
            emailLbl = itemView.findViewById(R.id.employeeEmailTxt_main);
            editEmployee_Btn = itemView.findViewById(R.id.editEmployeeBtn_main);
            deleteEmployee_Btn = itemView.findViewById(R.id.deleteEmployeeBtn_main);
            employeeImage = itemView.findViewById(R.id.employeeImageView_main);

        }
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    employeesViewModel.deleteEmployee(employeeWithSkillsList.get(deleteItemPosition));
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    public static Bitmap getRoundedTopLeftCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        final Rect topRightRect = new Rect(bitmap.getWidth()/2, 0, bitmap.getWidth(), bitmap.getHeight()/2);
        final Rect bottomRightRect = new Rect(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // Fill in upper right corner
        canvas.drawRect(topRightRect, paint);
        // Fill in bottom right corners
        canvas.drawRect(bottomRightRect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
