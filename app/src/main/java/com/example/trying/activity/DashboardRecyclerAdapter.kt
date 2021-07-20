package com.example.trying.activity

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.Toast
import com.example.trying.R
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context,val itemList : ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view:View): RecyclerView.ViewHolder(view) {

        val imgBookImage : ImageView = view.findViewById(R.id.imgBookImage)
        val txtBookName : TextView = view.findViewById(R.id.txtBookName)
        val txtBookAuthor : TextView = view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice : TextView = view.findViewById(R.id.txtBookPrice)
        val txtBookRating : TextView = view.findViewById(R.id.txtBookRating)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {

        val text = itemList[position]
        holder.txtBookName.text = text.bookName
        holder.txtBookAuthor.text = text.bookAuthor
        holder.txtBookRating.text = text.bookRating
        holder.txtBookPrice.text = text.bookPrice
//        holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(text.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)


//          yaha pe activity k badle mai context likte hai
        holder.llContent.setOnClickListener {
//            Toast.makeText(context,"Clicked on ${holder.txtBookName.text}",Toast.LENGTH_LONG).show()

            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",text.bookId)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {

        return itemList.size
    }

}