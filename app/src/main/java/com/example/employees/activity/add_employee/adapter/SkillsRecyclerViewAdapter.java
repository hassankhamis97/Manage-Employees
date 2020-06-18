package com.example.employees.activity.add_employee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.POJOs.Skill;
import com.example.employees.R;

import java.util.List;

public class SkillsRecyclerViewAdapter extends RecyclerView.Adapter<SkillsRecyclerViewAdapter.ViewHolder> {
    List<Skill> selectedSkills;
    Context context;
    public SkillsRecyclerViewAdapter(Context context, List<Skill> selectedSkills) {
        this.context = context;
        this.selectedSkills = selectedSkills;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.skills_layout,recyclerView,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameLbl.setText(selectedSkills.get(position).getName());
        holder.deleteSkill_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSkills.remove(position);
                notifyItemRemoved(position);
//                takeAction(tripName, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedSkills.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameLbl;
        public ImageButton deleteSkill_Btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameLbl = itemView.findViewById(R.id.skillName_txt);
            deleteSkill_Btn = itemView.findViewById(R.id.deleteSkill_Btn);

        }
    }
}
