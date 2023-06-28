/**
 * Copyright (C) 2020 SINTEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.sintef.fiskinfo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import no.sintef.fiskinfo.databinding.MainActivityBinding
import java.util.*


class MainActivity : AppCompatActivity() { //, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var controller : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_launcher_background)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        controller = host!!.navController


        drawerLayout = findViewById(R.id.drawer_layout)

        val topLevelDests = HashSet<Int>()
        topLevelDests.add(R.id.fragment_map)
        topLevelDests.add(R.id.fragment_overview)
        topLevelDests.add(R.id.fragment_tools)

        appBarConfiguration = AppBarConfiguration.Builder(controller.graph)
            .setDrawerLayout(drawerLayout)
            .build()

        setupActionBarWithNavController(controller, drawerLayout)
        //setupActionBarWithNavController(this, controller, drawerLayout)

        setupNavigationMenu(controller)
/*
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)*/
    }


    protected fun setupNavigationMenu(navController: NavController) {
        val view = findViewById<NavigationView>(R.id.navigation_view)
        NavigationUI.setupWithNavController(view, navController)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val snapItem = view.menu.findItem(R.id.fragment_snap)
        snapItem.isVisible = prefs.getBoolean(getString(R.string.pref_snap_enable_service), false)

        val spriceItem = view.menu.findItem(R.id.fragment_sprice)
        spriceItem.isVisible = prefs.getBoolean(getString(R.string.pref_sprice_enable_service_key), false)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(controller, drawerLayout)
        //return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    override fun onBackPressed() {
        //val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return NavigationUI.onNavDestinationSelected(item, controller) || super.onOptionsItemSelected(
            item
        )
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x001
        const val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0x002
    }
}
