package com.guerrerovillalobos.denunciasapp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarActivity : AppCompatActivity() {

    private lateinit var etDni: EditText
    private lateinit var spTipo: Spinner
    private lateinit var etDireccion: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnEnviar: Button
    private lateinit var progressBar: ProgressBar

    private val tiposDenuncia = arrayOf("ROBO", "ASALTO", "VANDALISMO", "RUIDOS MOLESTOS", "OTRO")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        initViews()
        setupSpinner()

        btnEnviar.setOnClickListener {
            registrarDenuncia()
        }
    }

    private fun initViews() {
        etDni = findViewById(R.id.etDni)
        spTipo = findViewById(R.id.spTipo)
        etDireccion = findViewById(R.id.etDireccion)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnEnviar = findViewById(R.id.btnEnviarRegistrar)
        progressBar = findViewById(R.id.progressBarRegistrar)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposDenuncia)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipo.adapter = adapter
    }

    private fun registrarDenuncia() {
        val dni = etDni.text.toString().trim()
        val tipo = spTipo.selectedItem.toString()
        val direccion = etDireccion.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        if (dni.length != 8) {
            etDni.error = "El DNI debe tener exactamente 8 dígitos"
            etDni.requestFocus()
            return
        }

        if (direccion.isEmpty()) {
            etDireccion.error = "Ingrese la dirección"
            etDireccion.requestFocus()
            return
        }

        if (descripcion.isEmpty()) {
            etDescripcion.error = "Ingrese la descripción"
            etDescripcion.requestFocus()
            return
        }

        val denuncia = Denuncia(
            dni = dni,
            tipo = tipo,
            direccion = direccion,
            descripcion = descripcion,
            estado = 1
        )

        progressBar.visibility = View.VISIBLE
        btnEnviar.isEnabled = false

        RetrofitClient.api.registrar(denuncia).enqueue(object : Callback<Denuncia> {
            override fun onResponse(call: Call<Denuncia>, response: Response<Denuncia>) {
                progressBar.visibility = View.GONE
                btnEnviar.isEnabled = true

                if (response.isSuccessful) {
                    Toast.makeText(this@RegistrarActivity, "Denuncia registrada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@RegistrarActivity, "Error al registrar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Denuncia>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnEnviar.isEnabled = true
                Toast.makeText(this@RegistrarActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}