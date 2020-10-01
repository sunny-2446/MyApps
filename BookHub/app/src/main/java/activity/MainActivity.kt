package activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.sunnyboohhub.bookhub.*
import fragment.AboutApp
import fragment.DashboardFragment
import fragment.FavouritesFragment
import fragment.Profile

class MainActivity : AppCompatActivity() {

    lateinit var DrawerLayout:DrawerLayout
    lateinit var CoordinatorLayout:CoordinatorLayout
    lateinit var Toolbar:androidx.appcompat.widget.Toolbar
    lateinit var FrameLayout:FrameLayout
    lateinit var NavigationView:NavigationView

    var previousMenuitem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DrawerLayout = findViewById(R.id.DrawerLayout)
        CoordinatorLayout = findViewById(R.id.CoordinatorLayout)
        Toolbar = findViewById(R.id.Toolbar)
        FrameLayout = findViewById(R.id.FrameLayout)
        NavigationView = findViewById(R.id.NavigationView)
        setUpToolbar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity , DrawerLayout ,
            R.string.open_drawer,
            R.string.close_drawer
        )
        //above code sets the icon in the specified activity and we can see the icon
        DrawerLayout.addDrawerListener(actionBarDrawerToggle)   //sets a click listener to the icon
        actionBarDrawerToggle.syncState()   //changes the hamburger icon to back arrow when clicked navigation drawer is synced with toggle

        opendashboard()

        NavigationView.setNavigationItemSelectedListener {


            if(previousMenuitem!=null)
            {
                previousMenuitem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuitem = it
            when(it.itemId)
            {
                R.id.Dashboard -> {
                   opendashboard()
                    DrawerLayout.closeDrawers()
                }

                R.id.Profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.FrameLayout,
                            Profile()
                        )
                        .commit()

                    supportActionBar?.title = "Profile"
                    DrawerLayout.closeDrawers()
                }

                R.id.Favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.FrameLayout,
                            FavouritesFragment()
                        )
                        .commit()

                    supportActionBar?.title = "Favourites"
                    DrawerLayout.closeDrawers()
                }

                R.id.About -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.FrameLayout,
                            AboutApp()
                        )
                        .commit()

                    supportActionBar?.title="About"
                    DrawerLayout.closeDrawers()
                }
                R.id.Exit ->{
                    finishAffinity()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() //sets the toolbar
    {
        setSupportActionBar(Toolbar)
        supportActionBar?.title = "Book Hub"
        supportActionBar?.setHomeButtonEnabled(true)      //sets the home bar icon in the tolbar but not functional
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
 //this function is used to add clicks to the the action bar clicking on the hamburger icon opens the navigatin drawer the home button(Hamburger) is a type of menu item
        val id=item.itemId
        if(id == android.R.id.home)  //id.home is used to get the id of the hamburger icon
        {
            DrawerLayout.openDrawer(GravityCompat.START)  //so that the navigation bar opens from the left
        }
        return super.onOptionsItemSelected(item)
    }

    fun opendashboard()
    {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.FrameLayout,
            DashboardFragment()
        )
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        NavigationView.setCheckedItem(R.id.Dashboard)
    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.FrameLayout)
        when(frag)
        {
            !is DashboardFragment -> opendashboard()

            else -> super.onBackPressed()
        }
    }
}