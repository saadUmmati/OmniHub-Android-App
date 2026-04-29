package com.example.omnihub.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omnihub.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    fun loadContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = repository.fetchContacts()
                _contacts.postValue(list)
            } catch (e: Exception) {
            }
        }
    }
}