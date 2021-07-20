package com.example.trying.activity.database

import androidx.room.Database
import androidx.room.RoomDatabase


//this is main connection point of our db

//it will not have default impleme


@Database(entities = [BookEntity::class],version = 1)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao() : BookDao

}