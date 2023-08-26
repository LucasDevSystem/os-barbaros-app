package com.example.teste.view

import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.teste.R
import com.example.teste.databinding.ActivityAgendamentoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Agendamento : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentoBinding

    @RequiresApi(Build.VERSION_CODES.N)
    private val calendar: Calendar = Calendar.getInstance()
    private var data: String = ""
    private var hora: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var auth = Firebase.auth

        binding = ActivityAgendamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val datePicker = binding.datePicker
        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var dia = dayOfMonth.toString()
            var mes = monthOfYear.toString()

            if (dayOfMonth < 10){
                dia = "0$dayOfMonth"
            }
            if (monthOfYear < 10){
                mes = "" + (monthOfYear + 1)
            }else{
                mes = (monthOfYear +1).toString()
            }

            data = "$dia / $mes / $year"
        }

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val minuto: String

            if (minute < 10){
                minuto = "0$minute"
            }else{
                minuto = minute.toString()
            }

        hora = "$hourOfDay:$minuto"
        }
        binding.timePicker.setIs24HourView(true)

        binding.btAgendar.setOnClickListener{
            val barbeiro1 = binding.barbeiro1
            val barbeiro2 = binding.barbeiro2
            val barbeiro3 = binding.barbeiro3

            when{
                hora.isEmpty() -> {
                    mensagem(it, "Preencha o horário!!", "#FF0000")
                }
                hora < "8:00" && hora > "19:00" -> {
                    mensagem(it, "Barber Shop está fechado - horário de atendimento das 08:00 as 19:00!!", "#FF0000")
                }
                data.isEmpty() -> {
                    mensagem(it, "Coloque uma data!!", "#FF0000")
                }

                (barbeiro1.isChecked || barbeiro2.isChecked  || barbeiro3.isChecked) && data.isNotEmpty() && hora.isNotEmpty() -> {
                    val userId = auth.currentUser?.uid
                    val email  =  auth.currentUser?.email
                    val barbeiroSelecionado = getBarbeiro( barbeiro1.isChecked ,  barbeiro2.isChecked , barbeiro3.isChecked );

                    if(userId !== null && email !== null){
                        salvarAgendamento(email,userId,barbeiroSelecionado,data,hora)
                        mensagem(it, "Agendamento realizado com sucesso!!!!", "#FF03DAC5")
                    }

                }else ->{
                mensagem(it, "Escolha um barbeiro!!", "#FF0000")

            }
            }
        }

    }

    private fun mensagem(view: View, mensagem: String, cor: String){
        println(mensagem)
        val snackbar = Snackbar.make(view,mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun getBarbeiro(isBarbeiro1: Boolean,isBarbeiro2: Boolean,isBarbeiro3: Boolean): String {

        if(isBarbeiro1){
            return "Arthur F.Foureaux"
        }else if(isBarbeiro2){
            return "Daniel Bruno Fernandes Conrado"
        }else if(isBarbeiro3){
            return "Uclisses"
        }
        return ""
    }


    private fun salvarAgendamento(cliente: String, clienteId: String, barbeiro: String, data: String, hora: String){
        val database = FirebaseFirestore.getInstance()

        val agendamento = hashMapOf(
            "cliente" to cliente,
            "barbeiro" to barbeiro,
            "data" to data,
             "hora" to hora
        )
        println(clienteId)

        database.collection("agendamento").document(clienteId).set(agendamento).addOnCompleteListener(
            println("salvo")
        )
    }
}

private fun Any.addOnCompleteListener(println: Unit) {

}
