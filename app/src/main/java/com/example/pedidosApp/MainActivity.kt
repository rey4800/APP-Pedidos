package com.example.pedidosApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pedidosApp.vistas.ComprasFragment
import com.example.pedidosApp.vistas.HomeFragment
import com.example.pedidosApp.vistas.loginVista.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer_layout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nav_view: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         toolbar =findViewById(R.id.toolbar)
         drawer_layout = findViewById(R.id.drawer_layout)
         nav_view = findViewById(R.id.nav_view)
        firebaseAuth = FirebaseAuth.getInstance()


         val toggle = ActionBarDrawerToggle(
             this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
         drawer_layout.addDrawerListener(toggle)
         toggle.syncState()

         nav_view.setNavigationItemSelectedListener(this)
        findViewById<TextView>(R.id.toolbarIndicator).text="20"
        supportFragmentManager.beginTransaction().replace(R.id.mainContent,HomeFragment()).commit()

        cargarDatosdeUsuario()



}

    fun cargarDatosdeUsuario(){

        if (firebaseAuth.currentUser != null) {
            var id = firebaseAuth.currentUser?.email
            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
            val headerview = navigationView.getHeaderView(0)

            headerview.findViewById<TextView>(R.id.txtMail).setText(id.toString())

        }


    }

    fun verCarrito(view:View){

        var intent = Intent(this,Carrito::class.java)
        this.startActivity(intent)


    }

override fun onBackPressed() {
 if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
     drawer_layout.closeDrawer(GravityCompat.START)
 } else {
     super.onBackPressed()
 }
}

override fun onCreateOptionsMenu(menu: Menu): Boolean {
 // Inflate the menu; this adds items to the action bar if it is present.
 //menuInflater.inflate(R.menu.main, menu)
 return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
 // Handle action bar item clicks here. The action bar will
 // automatically handle clicks on the Home/Up button, so long
 // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
        R.id.not -> {

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}

override fun onNavigationItemSelected(item: MenuItem): Boolean {
 // Handle navigation view item clicks here.
 when (item.itemId) {
     R.id.nav_first_fragment -> {
         supportFragmentManager.beginTransaction().replace(R.id.mainContent,HomeFragment()).commit()
         setTitle("Import")
     }
     R.id.nav_second_fragment -> {
        // supportFragmentManager.beginTransaction().replace(R.id.mainContent,FavsFragment()).commit()
         var intent = Intent(this,Carrito::class.java)
         this.startActivity(intent)
         setTitle("Gallery")
     }
     R.id.nav_third_fragment -> {
         supportFragmentManager.beginTransaction().replace(R.id.mainContent,ComprasFragment()).commit()
         setTitle("Carrito")
     }

     R.id.cerrarSesion ->{

         firebaseAuth.signOut()
         var intent = Intent(this,LoginActivity::class.java)
         this.startActivity(intent)
         finish()

     }

 }

 drawer_layout.closeDrawer(GravityCompat.START)
 return true
}


}