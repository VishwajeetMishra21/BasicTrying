package com.example.trying.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookclub.util.ConnectionManager
import com.example.trying.R
import java.util.*
import kotlin.collections.HashMap
import com.android.volley.Request
import org.json.JSONException
import kotlin.Comparator

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard : RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progressBar: ProgressBar

    lateinit var progressLayout: RelativeLayout

//    val bookList = arrayListOf(
//        "Vishwajeet","Muskan","Anju","Ranjit","Sanon","Kiran","Abhijett","Kriti","Indrajeet","Yo Boi"
//    )

//    val bookInfoList = arrayListOf<Book>(
//        Book("1","P.S. I love You", "Cecelia Ahern", "4.5", "Rs. 299", R.drawable.ps_ily),
//        Book("2","The Great Gatsby", "F. Scott Fitzgerald", "4.1", "Rs. 399", R.drawable.great_gatsby),
//        Book("3","Anna Karenina", "Leo Tolstoy", "4.3", "Rs. 199", R.drawable.anna_kare),
//        Book("4","Madame Bovary", "Gustave Flaubert", "4.0", "Rs. 500", R.drawable.madame),
//        Book("5","War and Peace", "Leo Tolstoy", "4.8", "Rs. 249", R.drawable.war_and_peace),
//        Book("6","Lolita", "Vladimir Nabokov", "3.9", "Rs. 349", R.drawable.lolita),
//        Book("7","Middlemarch", "George Eliot", "4.2", "Rs. 599", R.drawable.middlemarch),
//        Book("8","The Adventures of Huckleberry Finn", "Mark Twain", "4.5", "Rs. 699", R.drawable.adventures_finn),
//        Book("9","Moby-Dick", "Herman Melville", "4.5", "Rs. 499", R.drawable.moby_dick),
//        Book("10","The Lord of the Rings", "J.R.R Tolkien", "5.0", "Rs. 749", R.drawable.lord_of_rings)
//    )

    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1, book2 ->

        if(book1.bookRating.compareTo(book2.bookRating,true) == 0) {
            book1.bookName.compareTo(book2.bookName,true)
        }
        else {
            book1.bookRating.compareTo(book2.bookRating,true)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)


        progressBar = view.findViewById(R.id.progressBar)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books"

        if (ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,url,null,

                Response.Listener {

//              println("Response is $it")
                  try {

                       progressLayout.visibility = View.GONE

                      println("Response $it")

                      val success = it.getBoolean("success")
                      if(success){

                          val data = it.getJSONArray("data")
                          for (i in 0 until data.length()){

                              val bookJsonObject = data.getJSONObject(i)
                              val bookObject = Book(

                                  bookJsonObject.getString("book_id"),
                                  bookJsonObject.getString("name"),
                                  bookJsonObject.getString("author"),
                                  bookJsonObject.getString("rating"),
                                  bookJsonObject.getString("price"),
                                  bookJsonObject.getString("image")
                              )
                              bookInfoList.add(bookObject)

                              recyclerAdapter = DashboardRecyclerAdapter(activity as Context,bookInfoList)

                              recyclerDashboard.adapter = recyclerAdapter

                              recyclerDashboard.layoutManager = layoutManager

//                              recyclerDashboard.addItemDecoration(DividerItemDecoration(recyclerDashboard.context,(layoutManager as LinearLayoutManager).orientation))

                          }
                      }
                      else{
//                     yaha pe activity k jaga activity as Context likhte hai
                          Toast.makeText(activity as Context,"Some Error Occured",Toast.LENGTH_LONG).show()
                      }
                  }catch (e:JSONException){
                      Toast.makeText(activity as Context,"Some UnExpected Error Occured",Toast.LENGTH_LONG).show()
                  }


                },

                Response.ErrorListener {

                    println("Error is $it")
                    if(activity != null){
                        Toast.makeText(activity as Context,"Volley Error Occured",Toast.LENGTH_LONG).show()
                    }

                })
            {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "d0076af4b6ca9b"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }
        else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings"){text,listerner ->

               val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listerner ->

            ActivityCompat.finishAffinity(activity as Activity)

            }
            dialog.create()
            dialog.show()
        }



        return view
    }

// add menu item on toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item?.itemId

        if(id == R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

}