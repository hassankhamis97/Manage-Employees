package com.example.employees.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.employees.POJOs.Employee;
import com.example.employees.POJOs.EmployeeSkillCrossRef;
import com.example.employees.POJOs.Skill;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Employee.class, Skill.class, EmployeeSkillCrossRef.class}, version = 1, exportSchema = false)
public abstract class EmployeeRoomDatabase extends RoomDatabase {
    public abstract EmployeeWithSkillsDao employeeWithSkillsDao();

    private static volatile EmployeeRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static EmployeeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EmployeeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EmployeeRoomDatabase.class, "employee_database")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more Skills, just add them.
                EmployeeWithSkillsDao dao = INSTANCE.employeeWithSkillsDao();
                dao.deleteAllSkills();

                Skill skillPhp = new Skill(1,"PHP");
                dao.insert(skillPhp);
                Skill skillAsp = new Skill(2,"ASP.NET");
                dao.insert(skillAsp);
                Skill skillIos = new Skill(3,"iOS");
                dao.insert(skillIos);
                Skill skillAndroid = new Skill(4,"Android");
                dao.insert(skillAndroid);
            });
        }
    };
}
