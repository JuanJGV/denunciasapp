package com.guerrerovillalobos.denunciasapp

import retrofit2.Call
import retrofit2.http.*

interface DenunciaApi {

    // 1. Registrar denuncia[cite: 1]
    @POST("denuncias/")
    fun registrar(@Body denuncia: Denuncia): Call<Denuncia>

    // 2. Obtener lista paginada[cite: 1]
    @GET("denuncias/")
    fun listar(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 5
    ): Call<List<Denuncia>>

    // 3. Obtener por ID[cite: 1]
    @GET("denuncias/{id}")
    fun obtenerPorId(@Path("id") id: Int): Call<Denuncia>

    // 4. Obtener denuncias de un ciudadano[cite: 1]
    @GET("denuncias/ciudadano/{dni}")
    fun listarPorCiudadano(@Path("dni") dni: String): Call<List<Denuncia>>

    // 5. Atender denuncia[cite: 2]
    @PUT("denuncias/{id}")
    fun atender(@Path("id") id: Int): Call<Denuncia>
}