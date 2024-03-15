package com.example.exchangerates.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangerates.model.ExchangeRatesApi
import kotlinx.coroutines.launch

sealed interface ExchangeRatesUIState {
    data object Loading : ExchangeRatesUIState
    data object Error : ExchangeRatesUIState
    data object Success : ExchangeRatesUIState
}

class ExchangeRatesViewModel: ViewModel() {
    var eurInput by mutableStateOf("")
    var vndOutput by mutableStateOf(0.0)
        private set
    var vndRate by mutableStateOf(0.0f)
        private set
    var exchangeRatesUIState by mutableStateOf<ExchangeRatesUIState>(ExchangeRatesUIState.Loading)
        private set

    init {
        getExchangeRateForVnd()
    }
    fun changeEur(newValue : String) {
        eurInput = newValue
    }

    fun convert() {
        val euros = eurInput.toDoubleOrNull() ?: 0.0
        vndOutput = euros * vndRate
    }

    private fun getExchangeRateForVnd() {
        viewModelScope.launch {
            var exchangeRatesApi: ExchangeRatesApi? = null

            try {
                exchangeRatesApi = ExchangeRatesApi.getInstance()
                val exchangeRates = exchangeRatesApi!!.getRates()
                vndRate = exchangeRates.rates.VND
                if(exchangeRates.success) {
                    vndRate = exchangeRates.rates.VND
                    exchangeRatesUIState = ExchangeRatesUIState.Success
                }
                else {
                    exchangeRatesUIState = ExchangeRatesUIState.Error
                }
            } catch (e: Exception) {
                vndRate = 0.0f
                exchangeRatesUIState = ExchangeRatesUIState.Error
            }

        }
    }
}