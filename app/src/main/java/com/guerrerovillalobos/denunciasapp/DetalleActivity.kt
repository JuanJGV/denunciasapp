package com.guerrerovillalobos.denunciasapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleActivity : AppCompatActivity() {

    private lateinit var tvId: TextView
    private lateinit var tvDni: TextView
    private lateinit var tvFecha: TextView
    private lateinit var tvTipo: TextView
    private lateinit var tvDireccion: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvEstado: TextView
    private lateinit var btnAtender: Button
    private lateinit var progressBar: ProgressBar

    private var denunciaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        denunciaId = intent.getIntExtra("DENUNCIA_ID", -1)

        initViews()

        if (denunciaId != -1) {
            cargarDetalleDenuncia(denunciaId)
        } else {
            Toast.makeText(this, "ID de denuncia inválido", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnAtender.setOnClickListener {
            if (denunciaId != -1) {
                atenderDenuncia(denunciaId)
            }
        }
    }

    private fun initViews() {
        tvId = findViewById(R.id.tvDetalleId)
        tvDni = findViewById(R.id.tvDetalleDni)
        tvFecha = findViewById(R.id.tvDetalleFecha)
        tvTipo = findViewById(R.id.tvDetalleTipo)
        tvDireccion = findViewById(R.id.tvDetalleDireccion)
        tvDescripcion = findViewById(R.id.tvDetalleDescripcion)
        tvEstado = findViewById(R.id.tvDetalleEstado)
        btnAtender = findViewById(R.id.btnAtender)
        progressBar = findViewById(R.id.progressBarDetalle)
    }

    private fun cargarDetalleDenuncia(id: Int) {
        progressBar.visibility = View.VISIBLE

        RetrofitClient.api.obtenerPorId(id).enqueue(object : Callback<Denuncia> {
            override fun onResponse(call: Call<Denuncia>, response: Response<Denuncia>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val denuncia = response.body()!!
                    mostrarDetalle(denuncia)
                } else {
                    Toast.makeText(this@DetalleActivity, "Error al obtener datos: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Denuncia>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@DetalleActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun mostrarDetalle(denuncia: Denuncia) {
        tvId.text = "ID: ${denuncia.id ?: "N/A"}"
        tvDni.text = "DNI: ${denuncia.dni}"
        tvFecha.text = "Fecha: ${denuncia.fecha ?: "Sin fecha"}"
        tvTipo.text = "Tipo: ${denuncia.tipo}"
        tvDireccion.text = "Dirección: ${denuncia.direccion}"
        tvDescripcion.text = "Descripción: ${denuncia.descripcion}"

        actualizarEstadoUI(denuncia.estado)
    }

    private fun actualizarEstadoUI(estado: Int?) {
        if (estado == 0) {
            tvEstado.text = "Estado: Atendida"
            tvEstado.setTextColor(ContextCompat.getColor(this, R.color.color_atendida))
            btnAtender.isEnabled = false
            btnAtender.text = "Atendida por Serenazgo"
        } else {
            tvEstado.text = "Estado: Pendiente"
            tvEstado.setTextColor(ContextCompat.getColor(this, R.color.color_pendiente))
            btnAtender.isEnabled = true
            btnAtender.text = "Marcar como Atendida por Serenazgo"
        }
    }

    private fun atenderDenuncia(id: Int) {
        progressBar.visibility = View.VISIBLE
        btnAtender.isEnabled = false

        RetrofitClient.api.atender(id).enqueue(object : Callback<Denuncia> {
            override fun onResponse(call: Call<Denuncia>, response: Response<Denuncia>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    Toast.makeText(this@DetalleActivity, "Denuncia marcada como atendida", Toast.LENGTH_SHORT).show()
                    actualizarEstadoUI(0)
                } else {
                    btnAtender.isEnabled = true
                    Toast.makeText(this@DetalleActivity, "Error al atender denuncia: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Denuncia>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnAtender.isEnabled = true
                Toast.makeText(this@DetalleActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}