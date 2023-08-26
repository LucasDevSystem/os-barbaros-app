package com.example.teste.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teste.MainActivity
import com.example.teste.R
import com.example.teste.adapter.ServicosAdapater
import com.example.teste.databinding.ActivityHomeBinding
import com.example.teste.databinding.ActivityMainBinding
import com.example.teste.model.Servicos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    private lateinit var servicosAdapater: ServicosAdapater
    private val listaServicos: MutableList<Servicos> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        val email = auth.currentUser?.email

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.txtNomeUsuario.text = "Bem vindo, $email"
        val recyclerViewServicos = binding.recyclerViewServicos
        recyclerViewServicos.layoutManager = GridLayoutManager(this,2)
        servicosAdapater = ServicosAdapater(this, listaServicos)
        recyclerViewServicos.setHasFixedSize(true)
        recyclerViewServicos.adapter = servicosAdapater
        getServicos()

        binding.btAgendar.setOnClickListener{
            if (email != null) {
                navegarParaAgendamento(email)
            }
        }

        binding.btSair.setOnClickListener{
            auth.signOut()
            navegarParaLogin()
        }

    }

    private fun navegarParaAgendamento(nome: String){
        val intent = Intent(this, Agendamento::class.java)
        intent.putExtra("nome", nome)
        startActivity(intent)
    }

    private fun navegarParaLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun getServicos(){
        val servico1 = Servicos(R.drawable.img1,"Corte de cabelo")
        listaServicos.add(servico1)

        val servico2 = Servicos(R.drawable.img2,"Corte de barba")
        listaServicos.add(servico2)

        val servico3 = Servicos(R.drawable.img3,"Lavagem de cabelo")
        listaServicos.add(servico3)

        val servico4 = Servicos(R.drawable.img4,"Tratamento de cabelo")
        listaServicos.add(servico4)
    }

}