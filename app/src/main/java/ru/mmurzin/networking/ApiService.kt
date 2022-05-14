package ru.mmurzin.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.mmurzin.networking.api.blockchair.responce.Repo

interface ApiService {
    @GET("{blockchain}/stats")
    fun getBlockchainStats(@Path("blockchain") blockchain: String): Call<Repo>
}