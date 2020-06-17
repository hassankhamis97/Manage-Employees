package com.example.employees.POJOs;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "skill_table")
public class Skill {
    @PrimaryKey
    private int skillId;
    private String name;

    public Skill(int skillId, String name) {
        this.skillId = skillId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getSkillId() {
        return skillId;
    }
}
