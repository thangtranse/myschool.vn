package com.iuh.tranthang.myshool


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.ActivityFragment
import com.iuh.tranthang.myshool.ViewApdater.PageAdapter
import com.iuh.tranthang.myshool.ViewApdater.ProfileFragment
import com.iuh.tranthang.myshool.model.Parameter


class ProfileActivity : AppCompatActivity() {

    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    var pageAdapter: PageAdapter? = null
    private var drawerLayout: DrawerLayout? = null
    private var abdt: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mAuth = FirebaseAuth.getInstance()
        val intent = Intent(this, InsideActivity::class.java)
        val intent_profile = Intent(this, ProfileActivity::class.java)
        var token = getSharedPreferences("usename", Context.MODE_PRIVATE)
//        Log.e("tmt check", mAuth!!.uid)

        var db = dbConnect()
        if (db.isAuthentication()) {
            db.getDocument(Parameter().root_User, mAuth!!.uid!!)
        } else {
            Log.e("tmt", "false")
        }


        pageAdapter = PageAdapter(supportFragmentManager)
        pageAdapter!!.addFragment(ProfileFragment(), "PROFILE")
        pageAdapter!!.addFragment(ActivityFragment(), "ACTIVITY")
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager!!.adapter = pageAdapter
        tabLayout!!.setupWithViewPager(viewPager)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.menuNavigation)

        abdt = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)
        val mUser = mAuth!!.currentUser
        val drawerIndicatorEnabled = abdt!!.isDrawerIndicatorEnabled
        drawerLayout!!.addDrawerListener(abdt!!)
        abdt!!.syncState()
        navigationView!!.setNavigationItemSelectedListener(
                object : NavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        var boolean: Boolean?
                        if (item!!.itemId == R.id.DangXuat) {
                            var editor = token.edit()
                            editor.putString("loginusername", " ")
                            editor.commit()
                            startActivity(intent)
                            finish()
                            boolean = true
                        } else if (item!!.itemId == R.id.itemTrangCaNhan) {
                            startActivity(intent_profile)
                            boolean = true
                        } else {
                            boolean = false
                        }
                        return boolean!!
                    }
                }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var boolean: Boolean?
        if (item!!.itemId == R.id.DangXuat) {
            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
            var editor = token.edit()
            editor.putString("loginusername", " ")
            editor.commit()
            var intent = Intent(this, InsideActivity::class.java)
            startActivity(intent)
            finish()
            boolean = true
        } else if (item!!.itemId == R.id.itemTrangCaNhan) {
            var intent_profile = Intent(this, ProfileActivity::class.java)
            startActivity(intent_profile)
            boolean = true
        } else {
            boolean = super.onOptionsItemSelected(item)
        }
        return boolean!!
    }
}
