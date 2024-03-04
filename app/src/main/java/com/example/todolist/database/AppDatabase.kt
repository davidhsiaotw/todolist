package com.example.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.model.Task

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * access task DAO object
     */
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        // reference from https://developer.android.com/training/data-storage/room/migrating-db-versions#manual
                        val migration1To2 = object : Migration(1, 2) {
                            override fun migrate(database: SupportSQLiteDatabase) {
                                database.execSQL("ALTER TABLE Task ADD COLUMN start_time TEXT NOT NULL DEFAULT ''")
                                database.execSQL("ALTER TABLE Task ADD COLUMN due_time TEXT NOT NULL DEFAULT ''")
                            }
                        }

                        val instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_database"
                        ).fallbackToDestructiveMigration()
                            .addMigrations(migration1To2)
//                  .createFromAsset("database/todo_list.db")
                            .build()
                        INSTANCE = instance
                    }
                }
            }
            return INSTANCE!!
        }
    }
}