package com.example.pedidosApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.pedidosApp.modelos.User
import com.example.pedidosApp.vistas.loginVista.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PerfilActivity : AppCompatActivity() {
    lateinit var tvEmail: TextView
    lateinit var etName: EditText
    lateinit var etPhone: EditText
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("Users")

        tvEmail = findViewById(R.id.tvEmail)
        etName = findViewById(R.id.etName) as EditText
        etPhone = findViewById(R.id.etPhone) as EditText
        cargarPerfil()
    }

    fun cargarPerfil(){
        val currentUser = auth.currentUser
        if(currentUser != null){
            Log.d("user", currentUser.uid.toString())
            database.child(currentUser.uid.toString()).get().addOnSuccessListener {
                tvEmail.text = it.child("email").getValue().toString()
                etName.setText(it.child("name").getValue().toString())
                etPhone.setText(it.child("phone").getValue().toString())
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }
    }

    fun editar(view: View){
        val currentUser = auth.currentUser
        if(currentUser != null){
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            val mail = tvEmail.text.toString()

            if(name.isNotEmpty() && phone.isNotEmpty()){
                database = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid.toString())
                val user = User(name, phone, mail)
                database.setValue(user).addOnSuccessListener {
                    Toast.makeText(baseContext, "Modificado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

}