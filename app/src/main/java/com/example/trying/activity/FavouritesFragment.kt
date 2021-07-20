package com.example.trying.activity

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.trying.R
import com.example.trying.activity.database.BookDatabase
import com.example.trying.activity.database.BookEntity


class FavouritesFragment : Fragment() {

    lateinit var progressBar: ProgressBar
    lateinit var progressLayout : RelativeLayout

    lateinit var recyclerAdapter : FavouriteRecyclerAdapter

    lateinit var layoutManager : RecyclerView.LayoutManager

    lateinit var recyclerFavourite: RecyclerView

    var dbBookList = arrayListOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)

        layoutManager = GridLayoutManager(activity as Context,2)

        dbBookList = RetrieveFavourite(activity as Context).execute().get() as ArrayList<BookEntity>

        if(activity != null){

            progressLayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context,dbBookList)

            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }

        return view
    }

    class RetrieveFavourite(val context: Context) : AsyncTask<Void,Void,List<BookEntity>>(){

        override fun doInBackground(vararg params: Void?): List<BookEntity> {

            val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()

            return db.bookDao().getAllBooks()

        }


    }

}