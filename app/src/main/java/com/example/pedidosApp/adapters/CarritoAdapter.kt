package com.example.pedidosApp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.pedidosApp.Carrito
import com.example.pedidosApp.R
import com.example.pedidosApp.config.Conexion
import com.example.pedidosApp.config.Config
import com.example.pedidosApp.modelos.ItemCarito
import com.example.pedidosApp.modelos.Producto
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
        var conexion = Conexion(itemView.context)
        var db = conexion.writableDatabase

        fun bindItems(producto : ItemCarito){
            val itemNombre = itemView.findViewById<TextView>(R.id.itemNombreCarrito)
            val itemDescripcion = itemView.findViewById<TextView>(R.id.itemDescripcionCarrito)
            val itemPrecio  = itemView.findViewById<TextView>(R.id.itemPrecioCarrito)
            val itemImagen = itemView.findViewById<ImageView>(R.id.imagenCarrito)
            val txtCantidadCarrito = itemView.findViewById<TextView>(R.id.txtCantidadCarrito)
            val btnEliminar = itemView.findViewById<Button>(R.id.btnEliminar)
            val btnMas = itemView.findViewById<Button>(R.id.btnMas)
            val btnMenos = itemView.findViewById<Button>(R.id.btnMenos)

            txtCantidadCarrito.isEnabled = false
            txtCantidadCarrito.setText(producto.cantidad)
            recargar(itemView.context,producto.id, itemNombre, itemDescripcion,itemPrecio,itemImagen)

            btnEliminar.setOnClickListener {

                AlertDialog.Builder(itemView.context)
                    .setTitle("Eliminar Producto")
                    .setMessage("Desea eliminar este producto?") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->

                            db.execSQL("DELETE FROM carrito WHERE id_producto =" + producto.id)
                            (itemView.context as Carrito).refreshActivity()

                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()

            }



            btnMas.setOnClickListener{
                txtCantidadCarrito.text = (txtCantidadCarrito.text.toString().toInt()+1).toString()

                var cantidadTotal = txtCantidadCarrito.text.toString().toDouble()
                db.execSQL("UPDATE carrito SET cantidad ="+cantidadTotal+" WHERE id_producto = "+producto.id)
                //(itemView.context as Carrito).refreshActivity()
                (itemView.context as Carrito).actualizarTotal()




            }
            btnMenos.setOnClickListener{
                if (txtCantidadCarrito.text.toString().toInt() > 1){
                    txtCantidadCarrito.text = (txtCantidadCarrito.text.toString().toInt()-1).toString()
                    var cantidadTotal = txtCantidadCarrito.text.toString().toDouble()
                    db.execSQL("UPDATE carrito SET cantidad ="+cantidadTotal+" WHERE id_producto = "+producto.id)
                    //(itemView.context as Carrito).refreshActivity()
                    (itemView.context as Carrito).actualizarTotal()

                }
            }


        }



        fun recargar(contexto: Context, id:String, nombreVer:TextView, descripcionVer:TextView,precioVer:TextView, imagenVer:ImageView){

            var config = Config()
            var url ="${config.ipServer}/productos/$id"
            var jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,url,null,
                {item: JSONObject ->

                  var producto = Producto(
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
                    Glide.with(contexto).load(producto.imagen).into(imagenVer)
                    db.execSQL("UPDATE carrito SET precio ="+producto.precio+" WHERE id_producto = "+producto.id)


                }, {


                });

            var queue = Volley.newRequestQueue(contexto)
            queue.add(jsonObjectRequest)

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

            return total
        }






    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.position=position
        holder.bindItems(productos[position])
    }

}