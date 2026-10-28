package com.guerrerovillalobos.denunciasapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class DenunciaAdapter(
    private val onItemClick: (Denuncia) -> Unit
) : RecyclerView.Adapter<DenunciaAdapter.DenunciaViewHolder>() {

    private val listaDenuncias = ArrayList<Denuncia>()

    inner class DenunciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        val tvDni: TextView = itemView.findViewById(R.id.tvDni)
        val tvDireccion: TextView = itemView.findViewById(R.id.tvDireccion)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)

        fun bind(denuncia: Denuncia) {
            tvTipo.text = denuncia.tipo
            tvDni.text = "DNI: ${denuncia.dni}"
            tvDireccion.text = "Dirección: ${denuncia.direccion}"

            if (denuncia.estado == 0) {
                tvEstado.text = "Atendida"
                tvEstado.setTextColor(ContextCompat.getColor(itemView.context, R.color.color_atendida))
            } else {
                tvEstado.text = "Pendiente"
                tvEstado.setTextColor(ContextCompat.getColor(itemView.context, R.color.color_pendiente))
            }

            itemView.setOnClickListener {
                onItemClick(denuncia)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenunciaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_denuncia, parent, false)
        return DenunciaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DenunciaViewHolder, position: Int) {
        holder.bind(listaDenuncias[position])
    }

    override fun getItemCount(): Int = listaDenuncias.size

    fun addList(nuevaLista: List<Denuncia>) {
        val startPos = listaDenuncias.size
        listaDenuncias.addAll(nuevaLista)
        notifyItemRangeInserted(startPos, nuevaLista.size)
    }

    fun limpiarList() {
        listaDenuncias.clear()
        notifyDataSetChanged()
    }
}