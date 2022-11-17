package com.example.pedidosApp.config

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Conexion(var context:Context):SQLiteOpenHelper(context, "carrito.sql", null,1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        var tablaCarrito = "CREATE TABLE carrito (id not null primary key autoincrement, id_producto Integer, cantidad Double)"
        p0?.execSQL(tablaCarrito)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}