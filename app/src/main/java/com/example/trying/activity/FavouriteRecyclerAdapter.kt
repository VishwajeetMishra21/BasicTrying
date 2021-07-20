package com.example.trying.activity

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.LayoutInflater
import com.example.trying.R
import com.example.trying.activity.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context,val bookList : ArrayList<BookEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouritesViewHolder>() {

    class FavouritesViewHolder(view:View) : RecyclerView.ViewHolder(view){

        val llFavContent : LinearLayout = view.findViewById(R.id.llFavContent)
        val imgFavBookImage : ImageView = view.findViewById(R.id.imgFavBookImage)
        val txtFavBookTitle : TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtFavBookAuthor : TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtFavBookPrice : TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtFavBookRating : TextView = view.findViewById(R.id.txtFavBookRating)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_favouties_row,parent,false)
        return FavouritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {

        val text = bookList[position]
        holder.txtFavBookTitle.text = text.bookName
        holder.txtFavBookAuthor.text = text.bookAuthor
        holder.txtFavBookRating.text = text.bookRating
        holder.txtFavBookPrice.text = text.bookPrice
        Picasso.get().load(text.bookImage).into(holder.imgFavBookImage)

    }

    override fun getItemCount(): Int {

        return bookList.size
    }

}