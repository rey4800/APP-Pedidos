package com.example.pedidosApp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pedidosApp.R
import com.example.pedidosApp.VerProducto
import com.example.pedidosApp.modelos.Producto


class ProductoAdapter (val productos:ArrayList<Producto>) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>(){
    var position:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoAdapter.ViewHolder {
        var vista = LayoutInflater.from(parent.context).inflate(R.layout.item_producto,parent,false )
        return  ViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return  productos.size
    }

    override fun onBindViewHolder(holder: ProductoAdapter.ViewHolder, position: Int) {
        this.position=position
        holder.bindItems(productos[position])
    }
    class  ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){



        val itemNombre = itemView.findViewById<TextView>(R.id.item_nombre)
        val itemDescripcion = itemView.findViewById<TextView>(R.id.item_descripcion)
        val itemPrecio = itemView.findViewById<TextView>(R.id.item_precio)
        val itemImagen = itemView.findViewById<ImageView>(R.id.itemImage)


        fun bindItems(producto :Producto){

            itemNombre.text =producto.nombre
            itemDescripcion.text =producto.descripcion
            itemPrecio.text ="$ "  + producto.precio
            var url = producto.imagen
            itemImagen.loadUrl(url)

            itemView.setOnClickListener {

                var intent =Intent(itemView.context,VerProducto::class.java)
                intent.putExtra("Id",producto.id)
                itemView.context.startActivity(intent)

            }
        }


        fun ImageView.loadUrl(url: String) {

            Glide.with(itemView.context).load(url).into(itemImagen)
        }




    }

}