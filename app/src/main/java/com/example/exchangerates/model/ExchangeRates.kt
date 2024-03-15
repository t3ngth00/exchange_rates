package com.example.exchangerates.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.Date

data class Rates (
    val VND: Float,
    val EUR: Float,
    val GBP: Float,
    val USD: Float
)

data class ExchangeRate(
    var success: Boolean,
    var base: String,
    var date: Date,
    var rates: Rates
)

const val BASE_URL = "https://api.fxratesapi.com/"

interface ExchangeRatesApi {
    @GET("latest?base=EUR")
    suspend fun getRates(): ExchangeRate

    companion object {
        var exchangeRatesService: ExchangeRatesApi? = null

        fun getInstance(): ExchangeRatesApi {
            if (exchangeRatesService === null) {
                exchangeRatesService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ExchangeRatesApi::class.java)
            }
            return exchangeRatesService!!
        }
    }
}


