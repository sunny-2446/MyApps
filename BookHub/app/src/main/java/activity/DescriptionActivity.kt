package activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sunnyboohhub.bookhub.R
import database.BookDatabase
import database.BookEntity
import model.Book
import util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    lateinit var imgBookImage : ImageView
    lateinit var txtBookName : TextView
    lateinit var txtBookAuthor : TextView
    lateinit var txtBookPrice : TextView
    lateinit var txtBookRating : TextView
    lateinit var txtBookDesc : TextView
    lateinit var btnAddToFav : Button
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var toolbar: Toolbar

    var bookId :String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        txtBookName = findViewById(R.id.txtBookName)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressLayout = findViewById(R.id.progressLayout) //jhgkukunknlinhill
        progressLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if(intent!=null)
        {
            bookId = intent.getStringExtra("book_id")
        }
        else
        {
            finish()
            Toast.makeText(this@DescriptionActivity , "some unexpected Error Occurred" , Toast.LENGTH_SHORT).show()
        }
        if(bookId == "100")
        {
            finish()
            Toast.makeText(this@DescriptionActivity , "some  unexpected Error Occurred" , Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        var url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id" , bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity))
        {
            val jsonRequest = object : JsonObjectRequest(Request.Method.POST , url , jsonParams , Response.Listener{

                try{
                    val success = it.getBoolean("success")
                    if(success)
                    {
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE

                        val bookImageUrl = bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(imgBookImage)
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
                            bookImageUrl
                        )

                        val checkFav = DBAsyncTask(applicationContext , bookEntity , 1).execute()
                        val isFav = checkFav.get()

                        if(isFav)
                        {
                            btnAddToFav.text = "Remove From Favourites"
                            val favColor = ContextCompat.getColor(applicationContext , R.color.Favourites)
                            btnAddToFav.setBackgroundColor(favColor)
                        }
                        else{
                            btnAddToFav.text = "Add to Favourites"
                            val nofavColor = ContextCompat.getColor(applicationContext , R.color.colorPrimary)
                            btnAddToFav.setBackgroundColor(nofavColor)
                        }

                        btnAddToFav.setOnClickListener {

                            if(!DBAsyncTask(applicationContext , bookEntity , 1).execute().get())
                            {
                                val async = DBAsyncTask(applicationContext , bookEntity , 2).execute()
                                val result = async.get()
                                if(result)
                                {
                                    Toast.makeText(this@DescriptionActivity ,
                                        "Book Added to Favourites" ,
                                        Toast.LENGTH_SHORT).show()
                                    btnAddToFav.text = "Remove from Favourites"
                                    val favColor = ContextCompat.getColor(applicationContext , R.color.Favourites)
                                    btnAddToFav.setBackgroundColor(favColor)
                                }
                                else
                                {
                                    Toast.makeText(this@DescriptionActivity ,
                                        "Some Error Occurred" ,
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                val async = DBAsyncTask(applicationContext , bookEntity , 3).execute()
                                val result = async.get()
                                if(result)
                                {
                                    Toast.makeText(this@DescriptionActivity ,
                                        "Book Removed From Favourites" ,
                                        Toast.LENGTH_SHORT).show()

                                    btnAddToFav.text = "Add to Favourites"
                                    val nofavColor = ContextCompat.getColor(applicationContext , R.color.colorPrimary)
                                    btnAddToFav.setBackgroundColor(nofavColor)
                                }
                                else{
                                    Toast.makeText(this@DescriptionActivity ,
                                        "Some Error Occurred" ,
                                        Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(this@DescriptionActivity, "some Error occurred !!" , Toast.LENGTH_SHORT).show()
                    }
                } catch (e : JSONException)
                {
                    Toast.makeText(this@DescriptionActivity, "some Unexpected Error Occured!!" , Toast.LENGTH_SHORT).show()
                }

            } , Response.ErrorListener{

                Toast.makeText(this@DescriptionActivity , "Volley error occurred $it" , Toast.LENGTH_SHORT).show()
            }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String , String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "6170c7c16c01e7"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
        else
        {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Network  not Found")
            dialog.setPositiveButton("Open Settings"){text , listener->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit"){text , listener->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context:Context , val bookEntity:BookEntity , val mode:Int) : AsyncTask<Void, Void, Boolean>()
    {
        /*Mode1 ->Check database if the book is favourite or not
        * Mode2 ->Save the book into database as favourite
        * Mode3 ->Remove the Favoutite book*/

        val db = Room.databaseBuilder(context , BookDatabase::class.java , "books-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
                when(mode)
                {
                    1->{
                        //Check DB if the book is favourite or not
                        val book:BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                        db.close()
                        return book!=null
                    }
                    2->{
                        //Save the book into the database as favourite
                        db.bookDao().insertbook(bookEntity)
                        db.close()
                        return true
                    }
                    3->{
                        //Remove the Favourite book
                        db.bookDao().deletebook(bookEntity)
                        db.close()
                        return true
                    }
                }
            return false
        }
    }
}