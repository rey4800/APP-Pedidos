package com.example.pedidosApp.vistas.pagos

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.pedidosApp.Carrito
import com.example.pedidosApp.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

lateinit var txtTotal:TextView
lateinit var txtSubTotal:TextView
lateinit var txtMunicipio:TextView
lateinit var txtDireccion:TextView
lateinit var txtCard:TextView
lateinit var txtDate:TextView
lateinit var txtCV:TextView


class CardPaymentActivity : AppCompatActivity() {
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

                        //si api

                    }) // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()

        }
    }



    fun regresarCarrito(view: View){

        finish()

    }
}