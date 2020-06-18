package com.example.employees.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "skill_table")
public class Skill implements Serializable , Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(skillId);
        dest.writeString(name);
    }
}
