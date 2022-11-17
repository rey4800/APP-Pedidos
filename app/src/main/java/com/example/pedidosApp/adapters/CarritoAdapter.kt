package com.example.pedidosApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.pedidosApp.R
import com.example.pedidosApp.config.Config
import com.example.pedidosApp.modelos.ItemCarito
import org.json.JSONObject


class CarritoAdapter (val productos:ArrayList<ItemCarito>) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>(){
    var position:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoAdapter.ViewHolder {
        var vista = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito,parent,false )
        return  ViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return  productos.size
    }

    class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {

        }
        fun bindItems(producto : ItemCarito){
            val itemNombre = itemView.findViewById<TextView>(R.id.itemNombreCarrito)
            val itemDescripcion = itemView.findViewById<TextView>(R.id.itemDescripcionCarrito)
            val itemPrecio  = itemView.findViewById<TextView>(R.id.itemPrecioCarrito)
            val itemImagen = itemView.findViewById<ImageView>(R.id.imagenCarrito)

            itemNombre.text =producto.nombre
            itemDescripcion.text =producto.descripcion
            itemPrecio.text ="$ "  + producto.precio

            var url = producto.imagen
            Glide.with(itemView.context).load(url).into(itemImagen)
            itemNombre.text =producto.nombre

        }




        fun recargar(contexto: Context, id:String){

            var config = Config()
            var url ="${config.ipServer}/productos/$id"
            var jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,url,null,
                {item: JSONObject ->

                  /*  producto = Producto(
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
                    findViewById<TextView>(R.id.toolbarTitle).text = producto.nombre*/

                }, {


                });

            var queue = Volley.newRequestQueue(contexto)
            queue.add(jsonObjectRequest)

        }





    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.position=position
        holder.bindItems(productos[position])
    }

}