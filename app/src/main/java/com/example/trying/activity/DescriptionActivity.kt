package com.example.trying.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.trying.R
import android.view.View
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response
import com.squareup.picasso.Picasso
import org.json.JSONException
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.trying.activity.database.BookDatabase
import com.example.trying.activity.database.BookEntity

class DescriptionActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var txtBookName : TextView
    lateinit var txtBookAuthor : TextView
    lateinit var txtBookPrice : TextView
    lateinit var txtBookRating : TextView
    lateinit var imgBookImage : ImageView
    lateinit var txtBookDesc : TextView
    lateinit var btnAddToFav : Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout : RelativeLayout

    var bookId : String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        
        toolbar = findViewById(R.id.toolbar)
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE


        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Description"

        if(intent != null){
            bookId = intent.getStringExtra("book_id")
        }
        else{
            finish()
            Toast.makeText(this@DescriptionActivity,"Some Error Occured",Toast.LENGTH_LONG).show()
        }

        if (bookId == "100"){
            finish()
            Toast.makeText(this@DescriptionActivity,"Some Error Occured",Toast.LENGTH_LONG).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id",bookId)

        val jsonRequest = object : JsonObjectRequest(Request.Method.POST,url,jsonParams,

            Response.Listener {

                   try{
                       val success = it.getBoolean("success")

                       if (success){

                           val bookJsonObject = it.getJSONObject("book_data")
                           progressLayout.visibility = View.GONE


                           val bookUrl = bookJsonObject.getString("image")
                           Picasso.get().load(bookJsonObject.getString("image")).into(imgBookImage)
                           txtBookName.text = bookJsonObject.getString("name")
                           txtBookAuthor.text = bookJsonObject.getString("author")
                           txtBookPrice.text = bookJsonObject.getString("price")
                           txtBookRating.text = bookJsonObject.getString("rating")
                           txtBookDesc.text = bookJsonObject.getString("description")

                           val bookEntity = BookEntity(
                               bookId?.toInt() as Int,
                               txtBookName.text.toString(),
                               txtBookAuthor.text.toString(),
                               txtBookPrice.text.toString(),
                               txtBookRating.text.toString(),
                               txtBookDesc.text.toString(),
                               bookUrl
                           )

                           val checkFav = DBAsyncTask(applicationContext,bookEntity,1).execute()
                           val isFav = checkFav.get()

                           if(isFav){
                               btnAddToFav.text = "Remove From Favourites"
                               val favColor = ContextCompat.getColor(applicationContext,R.color.colorFav)
                               btnAddToFav.setBackgroundColor(favColor)
                           }
                           else{
                               btnAddToFav.text = "Add to Favourites"
                               val noFavColor = ContextCompat.getColor(applicationContext,R.color.purple_500)
                               btnAddToFav.setBackgroundColor(noFavColor)
                           }

                           btnAddToFav.setOnClickListener {

                               //this will eecute when book is not a favoruites
                               if (! DBAsyncTask(applicationContext,bookEntity,1).execute().get()){

                                   val sync = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                   val result = sync.get()
                                   
                                   if(result){
                                       Toast.makeText(this@DescriptionActivity,"book Added to Favourites",Toast.LENGTH_SHORT).show()
                                       btnAddToFav.text = "Remove From Favourites"
                                       val favColor = ContextCompat.getColor(applicationContext,R.color.colorFav)
                                       btnAddToFav.setBackgroundColor(favColor)
                                   }
                                   else{
                                       Toast.makeText(this@DescriptionActivity,"Some Error Occured",Toast.LENGTH_SHORT).show()
                                   }

                               }
                               else{
                                   val sync = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                   val result = sync.get()
                                   if(result){
                                       Toast.makeText(this@DescriptionActivity,"book remove from Favourites",Toast.LENGTH_SHORT).show()
                                       btnAddToFav.text = "Add to Favourites"
                                       val noFavColor = ContextCompat.getColor(applicationContext,R.color.purple_500)
                                       btnAddToFav.setBackgroundColor(noFavColor)
                                   }
                                   else{
                                       Toast.makeText(this@DescriptionActivity,"Some Error occured",Toast.LENGTH_SHORT).show()
                                   }
                               }
                           }

                       }
                       else{

                           Toast.makeText(this@DescriptionActivity,"Some Error Occures",Toast.LENGTH_LONG).show()
                       }

                   }catch (e:JSONException){

                       Toast.makeText(this@DescriptionActivity,"Some Error Occured",Toast.LENGTH_LONG).show()
                   }


            },

            Response.ErrorListener {

                 Toast.makeText(this@DescriptionActivity,"Volley Error",Toast.LENGTH_LONG).show()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "d0076af4b6ca9b"
                return headers
            }
        }

        queue.add(jsonRequest)

    }

    class DBAsyncTask(val context : Context, val bookEntity: BookEntity,val mode : Int) : AsyncTask<Void,Void,Boolean>() {

//        mode 1 -> check DB if the book is favorite or not
//          mode 2 -> Save the book in DB as favroites
//            mode 3 -> remove the favorites book

        val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1 ->{
                    val book : BookEntity? = db.bookDao().getBookById(bookEntity.book_Id.toString())
                    db.close()
                    return book != null

                }

                2 ->{
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }

                3 ->{
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }
            }

            return false

        }

    }

}