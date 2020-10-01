package fragment

import adapter.DashboardRecyclerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sunnyboohhub.bookhub.R
import model.Book
import org.json.JSONException
import util.ConnectionManager
import java.util.*
import kotlin.collections.HashMap

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DashboardFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var RecyclerDashboard:RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar


    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1 , book2 ->

        if(book1.bookRating.compareTo(book2.bookRating , true) == 0)
        {
            //sort according to name if the rating is same
            book1.bookName.compareTo(book2.bookName , true)
        }
        else
        {
            book1.bookRating.compareTo(book2.bookRating , true)
        }
    }

    lateinit var recyclerAdapter:DashboardRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)

        RecyclerDashboard = view.findViewById(R.id.RecyclerDashboard)

        layoutManager=LinearLayoutManager(activity)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url ="http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context))  //to check if internet is connected or not we put the entire code inside if block
        {
            val jsonObjectRequest = object : JsonObjectRequest(Method.GET , url ,null , Response.Listener{

                try{                                                    //to check the corrupted responses and JSON exception due to server issues
                    progressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")

                    if(success)
                    {
                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length())
                        {
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
                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)
                            RecyclerDashboard.layoutManager = layoutManager
                            RecyclerDashboard.adapter = recyclerAdapter
                        }
                    }
                    else
                    {
                        Toast.makeText(activity as Context , "Error occurred !!" , Toast.LENGTH_SHORT).show()
                    }
                } catch (e :JSONException)
                {
                    Toast.makeText(activity as Context , "Some Unexpected Error Occured!!" , Toast.LENGTH_SHORT).show()
                }

            } , Response.ErrorListener{

                if(activity !=null) {
                    Toast.makeText(
                        activity as Context,
                        "Volley error occurred !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String , String>()
                    headers["content-type"] = "application/json"
                    headers["token"] = "6170c7c16c01e7"
                    return headers
                }

            }
            queue.add(jsonObjectRequest)
        }

        else
        {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Network  not Found")
            dialog.setPositiveButton("Open Settings"){text , listener->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text , listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard , menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item?.itemId
        if(id == R.id.action_sort)
        {
            Collections.sort(bookInfoList , ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}