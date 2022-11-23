package com.example.pedidosApp.vistas.loginVista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.pedidosApp.R
import com.example.pedidosApp.modelos.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var txtIngresar: TextView
    lateinit var txtEmail: TextView
    lateinit var txtPassword: TextView
    lateinit var txtConfirmPassword: TextView
    lateinit var txtName: TextView
    lateinit var txtPhone: TextView
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        txtIngresar = findViewById(R.id.txtIngresar)
        txtEmail = findViewById(R.id.emailEt)
        txtPassword = findViewById(R.id.passET)
        txtConfirmPassword = findViewById(R.id.confirmPassEt)
        txtEmail = findViewById(R.id.emailEt)

    }


    fun Registrarse(view: View) {

        val name = txtName.text.toString()
        val phone = txtPhone.text.toString()
        val email = txtEmail.text.toString()
        val pass = txtPassword.text.toString()
        val confirmPass = txtConfirmPassword.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && name.isNotEmpty() &&
                phone.isNotEmpty()) {
            if (pass == confirmPass) {

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        var id = firebaseAuth.currentUser?.uid

                        database = FirebaseDatabase.getInstance().getReference("Users")
                        val user = User(name, phone, email)
                        database.child(id.toString()).setValue(user).addOnSuccessListener {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

        }
    }


}