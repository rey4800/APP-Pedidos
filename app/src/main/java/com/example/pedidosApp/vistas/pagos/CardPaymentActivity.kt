package com.example.pedidosApp.vistas.pagos

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pedidosApp.Carrito
import com.example.pedidosApp.MainActivity
import com.example.pedidosApp.R
import com.example.pedidosApp.config.Conexion
import com.example.pedidosApp.config.Config
import com.google.firebase.auth.FirebaseAuth
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Math.abs
import kotlin.math.roundToInt


class CardPaymentActivity : AppCompatActivity() {

    lateinit var txtTotal:TextView
    lateinit var txtSubTotal:TextView
    lateinit var txtMunicipio:TextView
    lateinit var txtDireccion:TextView
    lateinit var txtCard:TextView
    lateinit var txtDate:TextView
    lateinit var txtCV:TextView
    private lateinit var firebaseAuth: FirebaseAuth
    var conexion = Conexion(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment)
        txtTotal = findViewById<TextView>(R.id.txtTotal)
        txtSubTotal = findViewById<TextView>(R.id.txtSubTotal)
        txtMunicipio = findViewById<TextView>(R.id.txtMunicipio)
        txtDireccion = findViewById<TextView>(R.id.txtDireccion)
        txtCard = findViewById<TextView>(R.id.txtCard)
        txtDate = findViewById<TextView>(R.id.txtDate)
        txtCV = findViewById<TextView>(R.id.txtCV)
        firebaseAuth = FirebaseAuth.getInstance()
        var db = conexion.writableDatabase


        var total = intent.getStringExtra("total")

        txtTotal.setText(total)
        txtSubTotal.setText(total)


    }


    fun realizarPedido(view: View){

    var municipio = txtMunicipio.text.toString()
    var direccion = txtDireccion.text.toString()
    var card = txtCard.text.toString()
    var date = txtDate.text.toString()
    var cv = txtCV.text.toString()
        var total = txtTotal.text.toString()
        var idUsuario = firebaseAuth.currentUser?.uid.toString()
        val idPedido1 = abs((0..999999).random())
        val idPedido =idPedido1.toString()




        if (municipio.isEmpty() || direccion.isEmpty() || card.isEmpty() || date.isEmpty() || cv.isEmpty()){

            MotionToast.darkToast(this,"Campos Vacios️",
                "Debe de llenar todos los campos!",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))



        }else{

            AlertDialog.Builder(this)
                .setTitle("Comprar el pedido")
                .setMessage("Desea realizar la compra?") // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, which ->


                        var direccionEnvio:String = direccion + "," + municipio
                        insertarPedido(idPedido,idUsuario,"0.00",total,direccionEnvio)
                        InsertarDetallesPedidos(idPedido)

                        MotionToast.darkToast(this,"Compra Realizada",
                            "Muchas gracias por su compra pedido en camino!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this,R.font.helvetica_regular))

                        var intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)
                        finish()





                    }) // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()

        }
    }


    fun InsertarDetallesPedidos(idPedido:String){

        var sql = "SELECT * FROM carrito"
        var db = conexion.writableDatabase
        var respuesta = db.rawQuery(sql,null)

        if(respuesta.moveToFirst()){

            do {

                //productos.add(ItemCarito(respuesta.getString(1),"","","","",respuesta.getString(2)))

                detallesPedido (idPedido,respuesta.getString(1) ,respuesta.getString(3),respuesta.getString(2))

            }while (respuesta.moveToNext())

            db.execSQL("DELETE FROM carrito")

        }

    }




    fun insertarPedido(id:String,usuarioid:String,costosEnvio:String,monto:String,direccion:String){
        var config = Config()
        var url ="${config.ipServer}/pedido"
        val queue = Volley.newRequestQueue(this)

        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response -> // response
                Log.d("Response", response!!)
            },
            Response.ErrorListener { error->// error
                Log.d("Error.Response", error.toString())
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                //params["id"] = id
                params["id_usuario"] = usuarioid
                params["costo_envio"] = costosEnvio
                params["monto"] = monto
                params["id_tipopago"] = "4"
               params["fecha"] = direccion
                params["status"] = "1"
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }
        }

        queue.add(postRequest)
    }


    fun detallesPedido(idpedido:String,comboid:String,precio:String,cantidad:String){

        var config = Config()
        var url ="${config.ipServer}/detallepedido"
        val queue = Volley.newRequestQueue(this)

        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response -> // response
                Log.d("Response", response!!)
            },
            Response.ErrorListener { error->// error
                Log.d("Error.Response", error.toString())
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["id_pedido"] = "84"//idpedido
                params["id_combo"] = comboid
                params["precio"] = precio
                params["cantidad"] = cantidad
                params["status"] = "1"

                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }
        }

        queue.add(postRequest)

    }





    fun regresarCarrito(view: View){

        finish()

    }
}