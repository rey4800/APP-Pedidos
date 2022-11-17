package com.example.pedidosApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedidosApp.adapters.CarritoAdapter
import com.example.pedidosApp.config.Conexion
import com.example.pedidosApp.modelos.ItemCarito


class Carrito : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        var recyclerView =findViewById<RecyclerView>(R.id.listaCarrito)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var productos=ArrayList<ItemCarito>()


        var conexion = Conexion(this)
        var db = conexion.writableDatabase
        var sql = "SELECT * FROM carrito"
        var respuesta = db.rawQuery(sql,null)

        if(respuesta.moveToFirst()){

            do {

                productos.add(ItemCarito(respuesta.getString(1),"","","","",respuesta.getString(2)))


            }while (respuesta.moveToNext())

        }

        val adapter = CarritoAdapter(productos)
        recyclerView.adapter = adapter
    }
}