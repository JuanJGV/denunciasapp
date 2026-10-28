package com.guerrerovillalobos.denunciasapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarActivity : AppCompatActivity() {

    private lateinit var etDni: EditText
    private lateinit var btnBuscar: Button
    private lateinit var rvResultados: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var adapter: DenunciaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)

        initViews()
        setupRecyclerView()

        btnBuscar.setOnClickListener {
            buscarPorDni()
        }
    }

    private fun initViews() {
        etDni = findViewById(R.id.etBuscarDni)
        btnBuscar = findViewById(R.id.btnBuscar)
        rvResultados = findViewById(R.id.rvResultados)
        progressBar = findViewById(R.id.progressBarBuscar)
    }

    private fun setupRecyclerView() {
        adapter = DenunciaAdapter { denuncia ->
            if (denuncia.id != null) {
                val intent = Intent(this, DetalleActivity::class.java)
                intent.putExtra("DENUNCIA_ID", denuncia.id)
                startActivity(intent)
            } else {
                Toast.makeText(this, "ID de denuncia no válido", Toast.LENGTH_SHORT).show()
            }
        }
        rvResultados.layoutManager = LinearLayoutManager(this)
        rvResultados.adapter = adapter
    }

    private fun buscarPorDni() {
        val dni = etDni.text.toString().trim()

        if (dni.length != 8) {
            etDni.error = "Ingrese un DNI válido de 8 dígitos"
            etDni.requestFocus()
            return
        }

        adapter.limpiarList()
        progressBar.visibility = View.VISIBLE
        btnBuscar.isEnabled = false

        RetrofitClient.api.listarPorCiudadano(dni).enqueue(object : Callback<List<Denuncia>> {
            override fun onResponse(call: Call<List<Denuncia>>, response: Response<List<Denuncia>>) {
                progressBar.visibility = View.GONE
                btnBuscar.isEnabled = true

                if (response.isSuccessful) {
                    val lista = response.body()
                    if (lista != null && lista.size > 0) {
                        adapter.addList(lista)
                    } else {
                        Toast.makeText(this@BuscarActivity, "No se encontraron denuncias para el DNI ingresado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@BuscarActivity, "Error en el servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Denuncia>>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnBuscar.isEnabled = true
                Toast.makeText(this@BuscarActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}