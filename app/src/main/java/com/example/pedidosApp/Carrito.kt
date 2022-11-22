package com.example.pedidosApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedidosApp.adapters.CarritoAdapter
import com.example.pedidosApp.config.Conexion
import com.example.pedidosApp.modelos.ItemCarito
import com.example.pedidosApp.vistas.pagos.CardPaymentActivity
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import kotlin.math.roundToInt


class Carrito : AppCompatActivity() {

    var productos=ArrayList<ItemCarito>()
    var conexion = Conexion(this)

    lateinit var txtTotal:TextView
    lateinit var txtSubTotal:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        var recyclerView =findViewById<RecyclerView>(R.id.listaCarrito)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var txtTotal = findViewById<TextView>(R.id.txtTotal)
        var txtSubTotal = findViewById<TextView>(R.id.txtSubTotal)


        var sql = "SELECT * FROM carrito"
        var db = conexion.writableDatabase
        var respuesta = db.rawQuery(sql,null)

        if(respuesta.moveToFirst()){

            do {

                productos.add(ItemCarito(respuesta.getString(1),"","","","",respuesta.getString(2)))


            }while (respuesta.moveToNext())

        }

        val adapter = CarritoAdapter(productos)
        recyclerView.adapter = adapter

        txtTotal.setText(calcularTotal().toString())
        txtSubTotal.setText(calcularTotal().toString())


    }



    fun calcularTotal():Double{

        var total:Double = 0.0
        var sql = "SELECT * FROM carrito"
        var db = conexion.writableDatabase
        var respuesta = db.rawQuery(sql,null)

        if(respuesta.moveToFirst()){

            do {

                //productos.add(ItemCarito(respuesta.getString(1),"","","","",respuesta.getString(2)))

                total += (respuesta.getString(2).toDouble() * respuesta.getString(3).toDouble())

            }while (respuesta.moveToNext())

        }

        return (total * 100.0).roundToInt() / 100.0
    }


    fun actualizarTotal(){

        var txtTotal2 = findViewById<TextView>(R.id.txtTotal)
        var txtSubTotal2 = findViewById<TextView>(R.id.txtSubTotal)
        txtTotal2.setText(calcularTotal().toString())
        txtSubTotal2.setText(calcularTotal().toString())

    }

    fun aplicarCompra(view: View){

        var txtTotal2 = findViewById<TextView>(R.id.txtTotal)


        if(validarProductosCarrito()){

            var intent = Intent(this,CardPaymentActivity::class.java)
            intent.putExtra("total",txtTotal2.text.toString())
            this.startActivity(intent)
            finish()

        }else{

            MotionToast.createToast(this,
                "Carrito Vacio !!!️",
                "No tiene ningun producto agregado para comprar!",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_CENTER,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))



        }



    }


    fun validarProductosCarrito():Boolean{

        var sql = "SELECT * FROM carrito"
        var db = conexion.writableDatabase
        var respuesta = db.rawQuery(sql,null)

        return respuesta.moveToFirst()

    }



    fun volverHome(view: View){

        finish()

    }

    fun refreshActivity() {
        recreate()
    }


}