package com.avanzz.phonebook.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.avanzz.phonebook.R
import com.google.android.material.navigation.NavigationView

class Home : AppCompatActivity(), View.OnClickListener {

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var txtTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Set up the navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val layoutContact: LinearLayout = findViewById(R.id.layoutContact)
        val layoutSMS: LinearLayout = findViewById(R.id.layoutSMS)
        val layoutCall: LinearLayout = findViewById(R.id.layoutCall)
        txtTitle = findViewById(R.id.txtTitle)

        // Set up the app bar
        val toolbar: Toolbar = findViewById(R.id.toolbarHome)


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, ContactsFragment.newInstance())
        transaction.commit()

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        toggle.setHomeAsUpIndicator(R.drawable.menu_home)
        toggle.syncState()


        /*actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        actionBarDrawerToggle.setToolbarNavigationClickListener(View.OnClickListener {
            drawerLayout.openDrawer(
                GravityCompat.START
            )
        })
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu)
        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)*/

        layoutContact.setOnClickListener(this)
        layoutSMS.setOnClickListener(this)
        layoutCall.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layoutContact -> {
                txtTitle.text = "Contacts"
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, ContactsFragment.newInstance())
                transaction.commit()
                if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    this.drawerLayout.closeDrawer(GravityCompat.START)
                    return
                }
            }
            R.id.layoutCall -> {
                txtTitle.text = "Call Log"
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, CallLogFragment.newInstance())
                transaction.commit()
                if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    this.drawerLayout.closeDrawer(GravityCompat.START)
                    return
                }
            }
            R.id.layoutSMS -> {
                txtTitle.text = "SMS"
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, SmsFragment.newInstance())
                transaction.commit()

                if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    this.drawerLayout.closeDrawer(GravityCompat.START)
                    return
                }


            }
            // Add more cases for other views as needed
            else -> {
                // Handle click on other views
            }
        }
    }
}