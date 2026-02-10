package com.example.dogfoodscanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogfoodscanner.data.DogFood
import com.example.dogfoodscanner.data.DogFoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanViewModel(private val repository: DogFoodRepository) : ViewModel() {
    private val _result = MutableStateFlow<DogFood?>(null)
    val result: StateFlow<DogFood?> = _result

    fun onBarcodeScanned(value: String) {
        viewModelScope.launch {
            _result.value = repository.getByBarcode(value)
        }
    }
}
