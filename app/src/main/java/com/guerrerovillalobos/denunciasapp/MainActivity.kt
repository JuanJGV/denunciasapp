package com.guerrerovillalobos.denunciasapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rvDenuncias: RecyclerView
    private lateinit var btnRegistrar: Button
    private lateinit var btnBuscarDni: Button
    private lateinit var btnCargarMas: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var adapter: DenunciaAdapter
    private var offset = 0
    private val limit = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        cargarDenuncias(reset = true)
    }

    private fun initViews() {
        rvDenuncias = findViewById(R.id.rvDenuncias)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnBuscarDni = findViewById(R.id.btnBuscarDni)
        btnCargarMas = findViewById(R.id.btnCargarMas)
        progressBar = findViewById(R.id.progressBar)
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
        rvDenuncias.layoutManager = LinearLayoutManager(this)
        rvDenuncias.adapter = adapter
    }

    private fun setupListeners() {
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }

        btnBuscarDni.setOnClickListener {
            val intent = Intent(this, BuscarActivity::class.java)
            startActivity(intent)
        }

        btnCargarMas.setOnClickListener {
            cargarDenuncias(reset = false)
        }
    }

    private fun cargarDenuncias(reset: Boolean) {
        if (reset) {
            offset = 0
            adapter.limpiarList()
        }

        progressBar.visibility = View.VISIBLE
        btnCargarMas.isEnabled = false

        RetrofitClient.api.listar(offset = offset, limit = limit).enqueue(object : Callback<List<Denuncia>> {
            override fun onResponse(call: Call<List<Denuncia>>, response: Response<List<Denuncia>>) {
                progressBar.visibility = View.GONE
                btnCargarMas.isEnabled = true

                if (response.isSuccessful) {
                    val lista = response.body()
                    if (lista != null && lista.size > 0) {
                        adapter.addList(lista)
                        offset += lista.size
                    } else {
                        Toast.makeText(this@MainActivity, "No hay más denuncias por cargar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error en el servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Denuncia>>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnCargarMas.isEnabled = true
                Toast.makeText(this@MainActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}