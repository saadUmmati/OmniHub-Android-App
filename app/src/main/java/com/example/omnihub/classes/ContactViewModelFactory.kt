package com.example.omnihub.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.omnihub.ContactRepository

class ContactViewModelFactory(private val repository: ContactRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactViewModel(repository) as T
    }
}