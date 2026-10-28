package com.guerrerovillalobos.denunciasapp

import java.io.Serializable

data class Denuncia(
    val id: Int? = null,
    val dni: String,
    val fecha: String? = null,
    val tipo: String,
    val direccion: String,
    val descripcion: String,
    var estado: Int? = 1
) : Serializable