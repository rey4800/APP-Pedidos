package com.example.pedidosApp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.pedidosApp.adapters.ProductoAdapter
import com.example.pedidosApp.config.Conexion
import com.example.pedidosApp.config.Config
import com.example.pedidosApp.modelos.Producto
import org.json.JSONArray
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class VerProducto : AppCompatActivity() {
    lateinit var btnMas:Button
    lateinit var btnMenos:Button
    lateinit var txtCantidad:TextView
    lateinit var btnAgregar:Button
    lateinit var nombreVer:TextView
    lateinit var descripcionVer:TextView
    lateinit var precioVer:TextView
    lateinit var imagenVer:ImageView
    lateinit var producto: Producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_producto)
        var toolbar =findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var id = intent.getStringExtra("Id")



        btnMas = findViewById(R.id.btnMas)
        btnMenos = findViewById(R.id.btnMenos)
        btnAgregar = findViewById(R.id.btnAgregar)
        txtCantidad = findViewById(R.id.txtCantidad)
        imagenVer = findViewById(R.id.imagenVer)
        nombreVer = findViewById(R.id.nombreVer)
        descripcionVer = findViewById(R.id.descripcionVer)
        precioVer = findViewById(R.id.precioVer)
        recargar(this,id)
        txtCantidad.isEnabled = false
        txtCantidad.text="1"

        btnMas.setOnClickListener{
            txtCantidad.text = (txtCantidad.text.toString().toInt()+1).toString()

        }
        btnMenos.setOnClickListener{
            if (txtCantidad.text.toString().toInt() > 1){
                txtCantidad.text = (txtCantidad.text.toString().toInt()-1).toString()
            }
        }


       // var conexion = Conexion(this)
        //var db = conexion.writableDatabase

       /*btnAgregar.setOnClickListener{
           db.execSQL("INSERT into carrito(id_producto, cantidad) values($producto.id, $txtCantidad.text.toString() )")
            startActivity(Intent(this, Carrito::class.java))
        }*/

    }


    fun recargar( contexto: Context, id:String){

        var config = Config()
        var url ="${config.ipServer}/productos/$id"
        var jsonObjectRequest =JsonObjectRequest(
            Request.Method.GET,url,null,
            {item: JSONObject ->

                producto = Producto(
                        item.getString("id"),
                        item.getString("name"),
                        item.getString("price"),
                        item.getString("image"),
                        item.getString("description"),
                        item.getString("id_category")
                )

                nombreVer.text = producto.nombre
                precioVer.text = "$ " + producto.precio
                descripcionVer.text = producto.descripcion
                Glide.with(this).load(producto.imagen).into(imagenVer)
                findViewById<TextView>(R.id.toolbarTitle).text = producto.nombre

            }, {


            });

        var queue = Volley.newRequestQueue(contexto)
        queue.add(jsonObjectRequest)

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}