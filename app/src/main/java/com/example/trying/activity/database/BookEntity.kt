package com.example.trying.activity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//It is store the data in form of table hence it is data class


//this help to say compiler that it is entity class

@Entity(tableName = "books")
data class BookEntity (

    @PrimaryKey val book_Id : Int,
    @ColumnInfo(name = "book_name") val bookName : String,
    @ColumnInfo(name = "book_author") val bookAuthor : String,
    @ColumnInfo(name = "book_price") val bookPrice : String,
    @ColumnInfo(name = "book_rating") val bookRating : String,
    @ColumnInfo(name = "book_desc") val bookDesc : String,
    @ColumnInfo(name = "book_image") val bookImage : String

        )