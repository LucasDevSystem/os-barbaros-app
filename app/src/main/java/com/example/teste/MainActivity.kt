package com.example.teste

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.teste.databinding.ActivityMainBinding
import com.example.teste.view.Home
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        println(currentUser.toString())

        if(auth.currentUser !== null){
            navegarPraHome()
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btLogin.setOnClickListener {

            val nome = binding.editNome.text.toString()
            val senha = binding.editSenha.text.toString()

            when{
                nome.isEmpty() -> {
                    mensagem(it, "Insira o seu Nome!!!")
                }
                senha.isEmpty() -> {
                    mensagem(it, "Preencha a senha!!!")
                } senha.length <= 5 -> {
                mensagem(it, "A senha precisa ter pelo menos 6 caracteres!")
                }else -> {
                auth.signInWithEmailAndPassword(nome, senha).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        navegarPraHome()
                    } else {
                        mensagem(it, "Usuario e Senha invalido!")
                    }
                }
                }
            }
        }

    }

    private fun mensagem(view: View, mensagem: String){
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#FF0000"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }
    private fun navegarPraHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }
}